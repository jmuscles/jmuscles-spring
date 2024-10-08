package com.jmuscles.dbprops.jpa.entity.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.dbprops.dto.PropVersionDto;
import com.jmuscles.dbprops.jpa.entity.PropVersionEntity;
import com.jmuscles.dbprops.jpa.entity.PropVersionKey;
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

	private Long findMaxMajorVersion(EntityManager entityManager, Long propTenantId) {
		if (propTenantId == null) {
			throw new RuntimeException("tenantId can not be null");
		}
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("propTenantId", propTenantId);
		List<Long> propVersionEntities = repositorySetup.runNamedQuery(entityManager,
				"PropVersionEntity.findMaxMajorVersionByTenant", parameters, Long.class);
		return (propVersionEntities != null && propVersionEntities.get(0) != null) ? propVersionEntities.get(0) : 0L;
	}

	public PropVersionEntity createWithNewMajorVersion(EntityManager entityManager,
			PropVersionEntity propVersionEntity) {
		PropVersionKey propVersionKey = propVersionEntity.getPropVersionKey();
		Long majorVersion = findMaxMajorVersion(entityManager, propVersionKey.getPropTenantId());
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

	private Long findMaxMinorVersion(EntityManager entityManager, Long propTenantId, Long majorVersion) {
		if (propTenantId == null || majorVersion == null) {
			throw new RuntimeException("propTenantId/majorVersion can not be null");
		}
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("propTenantId", propTenantId);
		parameters.put("majorVersion", majorVersion);
		List<Long> propVersionEntities = repositorySetup.runNamedQuery(entityManager,
				"PropVersionEntity.findMaxMinorVersionByTenantAndMajorVersion", parameters, Long.class);
		return propVersionEntities != null ? propVersionEntities.get(0) : null;
	}

	public PropVersionEntity createWithNewMinorVersion(EntityManager entityManager,
			PropVersionEntity propVersionEntity) {
		PropVersionKey propVersionKey = propVersionEntity.getPropVersionKey();
		Long minorVersion = findMaxMinorVersion(entityManager, propVersionKey.getPropTenantId(),
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
		return createPropVersion(propVersionDto.toPropVersionEntity());
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
			List<PropVersionEntity> localEntities = getPropVersions(entityManager, tenantId, majorVersion, minorVersion,
					name, propFullKey, parentPropId);
			if (localEntities != null && localEntities.size() > 0) {
				entities.addAll(localEntities);
			}
		});
		return entities.size() > 0 ? entities : null;
	}

	public List<PropVersionEntity> getPropVersions(EntityManager em, Long propTenantId, Long majorVersion,
			Long minorVersion, String name, String propFullKey, Long parentPropId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PropVersionEntity> cq = cb.createQuery(PropVersionEntity.class);
		Root<PropVersionEntity> root = cq.from(PropVersionEntity.class);

		List<Predicate> predicates = new ArrayList<>();
		if (propTenantId != null) {
			predicates.add(cb.equal(root.get("propVersionKey").get("propTenantId"), propTenantId));
		}
		if (majorVersion != null && majorVersion > 0) {
			predicates.add(cb.equal(root.get("propVersionKey").get("majorVersion"), majorVersion));
		}
		if (minorVersion != null) {
			predicates.add(cb.equal(root.get("propVersionKey").get("minorVersion"), minorVersion));
		}
		if (name != null) {
			predicates.add(cb.equal(root.get("name"), name));
		}
		if (propFullKey != null) {
			predicates.add(cb.equal(root.get("propFullKey"), propFullKey));
		}
		if (parentPropId != null) {
			predicates.add(cb.equal(root.get("parentPropId"), parentPropId));
		}
		cq.where(predicates.toArray(new Predicate[0]));

		// Order by clause
		cq.orderBy(cb.desc(root.get("propVersionKey").get("propTenantId")),
				cb.desc(root.get("propVersionKey").get("majorVersion")),
				cb.desc(root.get("propVersionKey").get("minorVersion")));
		TypedQuery<PropVersionEntity> query = em.createQuery(cq);
		return query.getResultList();
	}

	public List<PropVersionDto> getPropVersionDtos(Long propTenantId, Long majorVersion, Long minorVersion, String name,
			String propFullKey, Long parentPropId) {
		List<PropVersionEntity> entities = this.getPropVersions(propTenantId, majorVersion, minorVersion, name,
				propFullKey, parentPropId);
		return entities != null
				? entities.stream().map(entity -> PropVersionDto.of(entity)).collect(Collectors.toList())
				: null;
	}

}
