package com.jmuscles.props.jpa.entity.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmuscles.props.jpa.entity.PropEntity;
import com.jmuscles.props.jpa.entity.PropVersionEntity;

/**
 * @author manish goel
 */

public class PropReadRepository {

	private static final Logger logger = LoggerFactory.getLogger(PropReadRepository.class);

	private RepositorySetup repositorySetup;
	private PropVersionCrudRepository propVersionCrudRepository;

	public PropReadRepository(RepositorySetup repositorySetup, PropVersionCrudRepository propVersionCrudRepository) {
		this.repositorySetup = repositorySetup;
		this.propVersionCrudRepository = propVersionCrudRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		repositorySetup.executeInTransaction(action);
	}

	public PropEntity findByKeyPath(EntityManager entityManager, String propFullKey, Long propTenantId,
			Long majorVersion, Long minorVersion) {
		StringBuffer jpql = new StringBuffer(
				" SELECT p FROM " + PropEntity.class.getSimpleName() + " p WHERE p.propTenantId = :propTenantId");
		jpql.append(" AND p.propFullKey = :propFullKey");
		if (majorVersion != null) {
			jpql.append(" AND p.majorVersion <= :majorVersion");
		}
		if (minorVersion != null) {
			jpql.append(" AND p.minorVersion <= :minorVersion");
		}
		jpql.append(" ORDER BY p.majorVersion DESC, p.minorVersion DESC");
		Query query = entityManager.createQuery(jpql.toString(), PropEntity.class);
		query.setParameter("propTenantId", propTenantId);
		query.setParameter("propFullKey", propFullKey);
		if (majorVersion != null) {
			query.setParameter("majorVersion", majorVersion);
		}
		if (minorVersion != null) {
			query.setParameter("minorVersion", minorVersion);
		}
		query.setMaxResults(1);

		List<PropEntity> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	public List<PropEntity> findImmediateChildren(EntityManager entityManager, Long parentPropId, Long propTenantId,
			Long majorVersion, Long minorVersion) {
		List<PropEntity> returnedResult = null;
		StringBuffer jpqlBuffer = new StringBuffer();
		jpqlBuffer.append("SELECT p FROM PropEntity p ");
		jpqlBuffer.append("WHERE p.propTenantId = :propTenantId ");
		jpqlBuffer.append("AND p.majorVersion = :majorVersion ");
		jpqlBuffer.append("AND p.parentPropId = :parentPropId ");
		jpqlBuffer.append("AND p.minorVersion <= :minorVersion ");
		jpqlBuffer.append("ORDER BY p.propKey, p.minorVersion DESC");

		Query query = entityManager.createQuery(jpqlBuffer.toString(), PropEntity.class);
		query.setParameter("propTenantId", propTenantId);
		query.setParameter("majorVersion", majorVersion);
		query.setParameter("parentPropId", parentPropId);
		query.setParameter("minorVersion", minorVersion);

		List<PropEntity> resultList = query.getResultList();
		if (resultList != null) {
			Map<String, PropEntity> latestByPropKey = resultList.stream()
					.collect(Collectors.toMap(PropEntity::getPropKey, propEntity -> propEntity, (e1, e2) -> e1));
			returnedResult = latestByPropKey.values().stream().collect(Collectors.toList());
		}

		return returnedResult;
	}

	public Map<String, Object> readDataFromDatabase(String requestPath, Long propTenantId, Long majorVersion,
			Long minorVersion) {
		Map<String, Object> returnMap = new HashMap<>();
		this.executeInTransaction(entityManager -> {
			Map<String, Object> localMap = readDataFromDatabase(entityManager, requestPath, propTenantId, majorVersion,
					minorVersion);
			if (localMap != null) {
				returnMap.putAll(localMap);
			}
		});
		return returnMap.size() != 0 ? returnMap : null;
	}

	public Map<String, Object> readDataFromDatabase(EntityManager entityManager, String requestPath, Long propTenantId,
			Long majorVersion, Long minorVersion) {
		if (majorVersion == null || minorVersion == null) {
			List<PropVersionEntity> versions = this.propVersionCrudRepository.getPropVersions(entityManager,
					propTenantId, majorVersion, minorVersion, null, null, null);
			if (versions != null && !versions.isEmpty()) {
				PropVersionEntity version = versions.get(0);
				majorVersion = version.getPropVersionKey().getMajorVersion();
				minorVersion = version.getPropVersionKey().getMinorVersion();
			} else {
				// TODO throw error
			}
		}

		Map<String, Object> returnMap = new HashMap<>();
		PropEntity parent = findByKeyPath(entityManager, StringUtils.hasText(requestPath) ? requestPath : "jmuscles",
				propTenantId, majorVersion, minorVersion);
		if (parent != null) {
			returnMap.put(parent.getPropKey(),
					readDataFromDatabase(entityManager, parent, propTenantId, majorVersion, minorVersion));
		}
		return returnMap.size() != 0 ? returnMap : null;
	}

	public Object readDataFromDatabase(EntityManager entityManager, PropEntity propsEntity, Long propTenantId,
			Long majorVersion, Long minorVersion) {
		Map<String, Object> nestedMap = new HashMap<>();
		List<PropEntity> children = findImmediateChildren(entityManager, propsEntity.getId(), propTenantId,
				majorVersion, minorVersion);
		if (children != null) {
			for (PropEntity child : children) {
				nestedMap.put(child.getPropKey(),
						readDataFromDatabase(entityManager, child, propTenantId, majorVersion, minorVersion));

			}
		}
		if (propsEntity.getPropValue() != null) {
			// Check if the value is a JSON string representing a map and parse it
			try {
				return new ObjectMapper().readValue(propsEntity.getPropValue(), Map.class);
			} catch (Exception e) {
				// Value is not a valid JSON, return it as a string
				return propsEntity.getPropValue();
			}
		}
		return nestedMap;
	}

}
