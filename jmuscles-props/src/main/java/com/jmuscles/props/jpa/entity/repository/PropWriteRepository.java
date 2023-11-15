package com.jmuscles.props.jpa.entity.repository;

import java.sql.Timestamp;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.jmuscles.props.converter.JmusclesConfigUtil;
import com.jmuscles.props.dto.PropVersionDto;
import com.jmuscles.props.dto.RequestTo;
import com.jmuscles.props.dto.TenantDto;
import com.jmuscles.props.jpa.entity.PropEntity;
import com.jmuscles.props.jpa.entity.PropVersionEntity;
import com.jmuscles.props.jpa.entity.PropVersionKey;
import com.jmuscles.props.jpa.entity.TenantEntity;
import com.jmuscles.props.util.Constants;
import com.jmuscles.props.util.Util;

/**
 * @author manish goel
 *
 */
public class PropWriteRepository {
	private static final Logger logger = LoggerFactory.getLogger(PropWriteRepository.class);

	private String applicationName;
	private RepositorySetup repositorySetup;
	private PropReadRepository propReadRepository;
	private PropVersionCrudRepository propVersionCrudRepository;

	public PropWriteRepository(String applicationName, RepositorySetup repositorySetup,
			PropVersionCrudRepository propVersionCrudRepository, PropReadRepository propReadRepository) {
		this.applicationName = applicationName;
		this.repositorySetup = repositorySetup;
		this.propVersionCrudRepository = propVersionCrudRepository;
		this.propReadRepository = propReadRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		repositorySetup.executeInTransaction(action);
	}

	public void create(RequestTo requestTo) {
		Map<String, Object> jmusclesMap = JmusclesConfigUtil.jmusclesConfigToMap(requestTo.getJmusclesConfig());
		TenantEntity tenantEntity = requestTo.getTenantDto().toTenantEntity();
		PropVersionEntity propVersionEntity = requestTo.getPropVersionDto().toPropVersionEntity();
		executeInTransaction(entityManager -> create(entityManager, jmusclesMap, tenantEntity, propVersionEntity,
				Util.currentTimeStamp(), this.applicationName));
	}

	public void create(EntityManager entityManager, Map<String, Object> jmusclesMap, TenantEntity tenantEntity,
			PropVersionEntity propVersionEntity, Timestamp createdAt, String createdBy) {
		propVersionEntity.resetCreateUpdate(createdAt, createdBy, null, null);
		this.propVersionCrudRepository.createWithNewMajorVersion(entityManager, propVersionEntity);
		this.savePropertiesToDatabase(entityManager, jmusclesMap, null, null,
				propVersionEntity.getPropVersionKey().getMajorVersion(),
				propVersionEntity.getPropVersionKey().getMinorVersion(), tenantEntity, createdAt, createdBy);
	}

	public void update(RequestTo requestTo) {
		Object mapObject = JmusclesConfigUtil.jmusclesConfigToMap(requestTo.getJmusclesConfig(),
				requestTo.getRequestPath());
		executeInTransaction(
				entityManager -> update(entityManager, requestTo.getRequestPath(), mapObject, requestTo.getTenantDto(),
						requestTo.getPropVersionDto(), Util.currentTimeStamp(), this.applicationName));
	}

	public void update(EntityManager entityManager, String requestPath, Object mapObject, TenantDto tenantDto,
			PropVersionDto propVersionDto, Timestamp createdAt, String createdBy) {

		PropEntity parentProp = this.propReadRepository.findByKeyPath(entityManager, requestPath, tenantDto.getId(),
				propVersionDto.getMajorVersion(), null);
		PropVersionEntity propVersionEntity = PropVersionEntity.of(
				PropVersionKey.of(propVersionDto.getMajorVersion(), null, tenantDto.getId()), propVersionDto.getName(),
				propVersionDto.getDescription(), requestPath, parentProp, createdAt, createdBy, null, null);

		this.propVersionCrudRepository.createWithNewMinorVersion(entityManager, propVersionEntity);

		Long majorVersion = propVersionEntity.getPropVersionKey().getMajorVersion();
		Long minorVersion = propVersionEntity.getPropVersionKey().getMinorVersion();

		if (mapObject instanceof Map) {
			this.savePropertiesToDatabase(entityManager, (Map<String, Object>) mapObject, parentProp, requestPath,
					majorVersion, minorVersion, tenantDto.toTenantEntity(), createdAt, createdBy);
		} else {
			saveEntity(entityManager,
					PropEntity.of(null, parentProp.getProp_key(), mapObject != null ? mapObject.toString() : null, null,
							parentProp.getParent(), majorVersion, minorVersion, null, tenantDto.toTenantEntity(),
							parentProp.getProp_full_key(), createdAt, createdBy, null, null));
		}

	}

	public void saveEntity(EntityManager entityManager, PropEntity propEntity) {
		try {
			entityManager.persist(propEntity);
		} catch (Exception ex1) {
			String keyToLogError = propEntity.getProp_full_key();
			if (propEntity.getProp_value() != null
					&& propEntity.getProp_value().length() > Constants.PROP_VALUE_LENGTH) {
				logger.info("First attempt falied and trying second attempt to save data for key: " + keyToLogError);
				propEntity.setProp_value("BLOB");
				propEntity.setProp_value_blob(propEntity.getProp_value().getBytes());
				try {
					entityManager.merge(propEntity);
					return;
				} catch (Exception ex2) {
					logger.error("Second attempt failed to save data for key : " + keyToLogError, ex2);
				}
			}
			logger.error("Data could not be saved for key: " + keyToLogError, ex1);
		}
	}

	@SuppressWarnings("unchecked")
	public void savePropertiesToDatabase(EntityManager entityManager, Map<String, Object> properties, PropEntity parent,
			String parentKey, Long majorVersion, Long minorVersion, TenantEntity tenant, Timestamp createdAt,
			String createdBy) {
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			String key = entry.getKey();
			String value = (entry.getValue() != null && !(entry.getValue() instanceof Map))
					? entry.getValue().toString()
					: null;
			String localFullKey = buildFullKey(parentKey, key);
			PropEntity propEntity = PropEntity.of(null, key, value, null, parent, majorVersion, minorVersion, null,
					tenant, localFullKey, createdAt, createdBy, null, null);

			saveEntity(entityManager, propEntity);

			if (entry.getValue() instanceof Map) {
				savePropertiesToDatabase(entityManager, (Map<String, Object>) entry.getValue(), propEntity,
						localFullKey, majorVersion, minorVersion, tenant, createdAt, createdBy);
			}
		}
	}

	private String buildFullKey(String parentKey, String currentKey) {
		return StringUtils.hasText(parentKey) ? parentKey + "." + currentKey : currentKey;
	}

	public void deleteAll(EntityManager entityManager) {
		entityManager.createQuery("DELETE FROM AppPropsEntity a WHERE a.id<" + Long.MAX_VALUE).executeUpdate();
	}

}
