package com.jmuscles.props.jpa.entity.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.props.dto.PropVersionDto;
import com.jmuscles.props.jpa.entity.PropVersionEntity;
import com.jmuscles.props.jpa.entity.PropVersionKey;
import com.jmuscles.props.util.Util;

/**
 * @author manish goel
 *
 */
public class PropVersionCrudRepository {

	private static final Logger logger = LoggerFactory.getLogger(PropVersionCrudRepository.class);
	public static final int MAX_ATTEMPT_TO_CREATE = 10;

	private String applicationName;
	private RepositorySetup repositorySetup;

	public PropVersionCrudRepository(String applicationName, RepositorySetup repositorySetup) {
		this.applicationName = applicationName;
		this.repositorySetup = repositorySetup;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		this.repositorySetup.executeInTransaction(action);
	}

	private Long findMaxMajorVersion(EntityManager entityManager, Long tenantId) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("tenantId", tenantId);
		List<Long> propVersionEntities = repositorySetup.runNamedQuery(entityManager,
				"PropVersionEntity.findMaxMajorVersionByTenant", parameters, Long.class);
		return propVersionEntities != null ? propVersionEntities.get(0) : null;
	}

	public PropVersionEntity createWithNewMajorVersion(EntityManager entityManager,
			PropVersionEntity propVersionEntity) {
		PropVersionKey propVersionKey = propVersionEntity.getPropVersionKey();
		Long majorVersion = findMaxMajorVersion(entityManager, propVersionKey.getTenantId());
		propVersionKey.setMajorVersion(majorVersion);
		propVersionKey.setMinorVersion(0L);
		for (int currentAttempt = 1; currentAttempt <= MAX_ATTEMPT_TO_CREATE; currentAttempt++) {
			propVersionEntity.getPropVersionKey().increaseMajorVersion();
			try {
				createPropVersion(entityManager, propVersionEntity);
				break;
			} catch (Exception e) {
				logger.info("Atempt: " + currentAttempt
						+ " falied and trying next attempt to save data for propVersionEntity: " + propVersionEntity,
						e);
				continue;
			}
		}
		return propVersionEntity;
	}

	private Long findMaxMinorVersion(EntityManager entityManager, Long tenantId, Long majorVersion) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("tenantId", tenantId);
		parameters.put("majorVersion", majorVersion);
		List<Long> propVersionEntities = repositorySetup.runNamedQuery(entityManager,
				"PropVersionEntity.findMaxMinorVersionByTenantAndMajorVersion", parameters, Long.class);
		return propVersionEntities != null ? propVersionEntities.get(0) : null;
	}

	public PropVersionEntity createWithNewMinorVersion(EntityManager entityManager,
			PropVersionEntity propVersionEntity) {
		PropVersionKey propVersionKey = propVersionEntity.getPropVersionKey();
		Long minorVersion = findMaxMinorVersion(entityManager, propVersionKey.getTenantId(),
				propVersionKey.getMajorVersion());
		propVersionKey.setMinorVersion(minorVersion);
		for (int currentAttempt = 1; currentAttempt <= MAX_ATTEMPT_TO_CREATE; currentAttempt++) {
			propVersionEntity.getPropVersionKey().increaseMinorVersion();
			try {
				createPropVersion(entityManager, propVersionEntity);
				break;
			} catch (Exception e) {
				logger.info("Atempt: " + currentAttempt
						+ " falied and trying next attempt to save data for propVersionEntity: " + propVersionEntity,
						e);
				continue;
			}
		}
		return propVersionEntity;
	}

	public PropVersionEntity createPropVersion(PropVersionDto propVersionDto) {
		return createPropVersion(propVersionDto.getPropVersionEntity());
	}

	public PropVersionEntity createPropVersion(PropVersionEntity propVersionEntity) {
		executeInTransaction(em -> createPropVersion(em, propVersionEntity));
		return propVersionEntity;
	}

	public void createPropVersion(EntityManager entityManager, PropVersionEntity propVersionEntity) {
		if (propVersionEntity.getCreatedAt() == null) {
			propVersionEntity.setCreatedAt(Util.currentTimeStamp());
		}
		if (propVersionEntity.getCreatedBy() == null) {
			propVersionEntity.setCreatedBy(applicationName);
		}
		entityManager.persist(propVersionEntity);
	}

	public List<PropVersionEntity> getPropVersions(Long tenantId, Long majorVersion, Long minorVersion, String name,
			String propFullKey, Long parentPropId) {
		List<PropVersionEntity> entities = new ArrayList<>();
		executeInTransaction(entityManager -> {
			List<PropVersionEntity> localEntities = getPropVersions(tenantId, majorVersion, minorVersion, name,
					propFullKey, parentPropId);
			if (localEntities != null && localEntities.size() > 0) {
				entities.addAll(localEntities);
			}
		});
		return entities.size() > 0 ? entities : null;
	}

	public List<PropVersionEntity> getPropVersions(EntityManager entityManager, Long tenantId, Long majorVersion,
			Long minorVersion, String name, String propFullKey, Long parentPropId) {
		Map<String, Object> parameters = new HashMap<>();
		if (tenantId != null) {
			parameters.put("propVersionKey.tenantId", tenantId);
		}
		if (majorVersion != null) {
			parameters.put("propVersionKey.majorVersion", majorVersion);
		}
		if (minorVersion != null) {
			parameters.put("propVersionKey.minorVersion", minorVersion);
		}
		if (name != null) {
			parameters.put("name", name);
		}
		if (propFullKey != null) {
			parameters.put("prop_full_key", propFullKey);
		}
		if (parentPropId != null) {
			parameters.put("parent_prop.id", parentPropId);
		}

		return this.getPropVersions(entityManager, parameters);
	}

	public List<PropVersionDto> getPropVersionDtos(Long tenantId, Long majorVersion, Long minorVersion, String name,
			String propFullKey, Long parentPropId) {
		List<PropVersionEntity> entities = this.getPropVersions(tenantId, majorVersion, minorVersion, name, propFullKey,
				parentPropId);
		return entities != null
				? entities.stream().map(entity -> PropVersionDto.of(entity)).collect(Collectors.toList())
				: null;

	}

	public List<PropVersionEntity> getPropVersions(EntityManager entityManager, Map<String, Object> parameters) {
		return this.repositorySetup.dynamicSelect(entityManager, parameters, "PropVersionEntity",
				"a.propVersionKey.tenantId DESC, a.propVersionKey.majorVersion DESC, a.propVersionKey.minorVersion DESC");
	}

}
