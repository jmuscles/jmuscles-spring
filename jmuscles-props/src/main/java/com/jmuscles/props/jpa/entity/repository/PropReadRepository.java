package com.jmuscles.props.jpa.entity.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmuscles.props.jpa.entity.PropEntity;
import com.jmuscles.props.jpa.entity.PropVersionEntity;

/**
 * @author manish goel
 */

public class PropReadRepository {

	private RepositorySetup repositorySetup;
	private PropVersionCrudRepository propVersionCrudRepository;

	public PropReadRepository(RepositorySetup repositorySetup, PropVersionCrudRepository propVersionCrudRepository) {
		this.repositorySetup = repositorySetup;
		this.propVersionCrudRepository = propVersionCrudRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		repositorySetup.executeInTransaction(action);
	}

	public PropEntity findByKeyPath(EntityManager entityManager, String fullKeyPath, Long tenantId, Long majorVersion,
			Long minorVersion) {
		StringBuffer jpql = new StringBuffer(" SELECT p FROM PropEntity p WHERE p.tenant.id = :tenantId");
		jpql.append(" AND p.prop_full_key <= :fullKeyPath");
		if (majorVersion != null) {
			jpql.append(" AND p.majorVersion <= :majorVersion");
		}
		if (minorVersion != null) {
			jpql.append(" AND p.minorVersion <= :minorVersion");
		}
		jpql.append(" ORDER BY p.majorVersion DESC, p.minorVersion DESC");
		Query query = entityManager.createQuery(jpql.toString(), PropEntity.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("fullKeyPath", fullKeyPath);
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

	public List<PropEntity> findImmediateChildren(EntityManager entityManager, Long parentPropId, Long tenantId,
			Long majorVersion, Long minorVersion) {
		StringBuffer jpqlBuffer = new StringBuffer();
		jpqlBuffer.append("SELECT p FROM PropEntity p ");
		jpqlBuffer.append("WHERE p.tenant.id = :tenantId ");
		jpqlBuffer.append("AND p.majorVersion = :majorVersion ");
		jpqlBuffer.append("AND p.parent.id = :parentPropId ");
		jpqlBuffer.append("AND p.minorVersion <= :minorVersion ");
		jpqlBuffer.append("ORDER BY p.prop_key, p.minorVersion DESC");

		Query query = entityManager.createQuery(jpqlBuffer.toString(), PropEntity.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("majorVersion", majorVersion);
		query.setParameter("parentPropId", parentPropId);
		query.setParameter("minorVersion", minorVersion);

		List<PropEntity> resultList = query.getResultList();

		Map<String, PropEntity> latestByPropKey = resultList.stream()
				.collect(Collectors.toMap(PropEntity::getProp_key, propEntity -> propEntity, (e1, e2) -> e1));

		return latestByPropKey.values().stream().collect(Collectors.toList());
	}

	public Map<String, Object> readDataFromDatabase(String requestPath, Long tenantId, Long majorVersion,
			Long minorVersion) {
		Map<String, Object> returnMap = new HashMap<>();
		this.executeInTransaction(entityManager -> readDataFromDatabase(entityManager, requestPath, tenantId,
				majorVersion, minorVersion));
		return returnMap.size() != 0 ? returnMap : null;
	}

	public Map<String, Object> readDataFromDatabase(EntityManager entityManager, String requestPath, Long tenantId,
			Long majorVersion, Long minorVersion) {
		if (majorVersion == null || minorVersion == null) {
			List<PropVersionEntity> versions = this.propVersionCrudRepository.getPropVersions(entityManager, tenantId,
					majorVersion, minorVersion, null, null, null);
			if (versions != null) {
				PropVersionEntity version = versions.get(0);
				majorVersion = version.getPropVersionKey().getMajorVersion();
				minorVersion = version.getPropVersionKey().getMinorVersion();
			}
		}

		Map<String, Object> returnMap = new HashMap<>();
		PropEntity parent = findByKeyPath(entityManager, StringUtils.hasText(requestPath) ? requestPath : "jmsucles",
				tenantId, majorVersion, minorVersion);
		if (parent != null) {
			returnMap.put(parent.getProp_key(),
					readDataFromDatabase(entityManager, parent, tenantId, majorVersion, minorVersion));
		}
		return returnMap.size() != 0 ? returnMap : null;
	}

	public Object readDataFromDatabase(EntityManager entityManager, PropEntity propsEntity, Long tenantId,
			Long majorVersion, Long minorVersion) {
		Map<String, Object> nestedMap = new HashMap<>();
		List<PropEntity> children = findImmediateChildren(entityManager, propsEntity.getId(), tenantId, majorVersion,
				minorVersion);
		for (PropEntity child : children) {
			nestedMap.put(child.getProp_key(),
					readDataFromDatabase(entityManager, propsEntity, tenantId, majorVersion, minorVersion));
		}
		if (propsEntity.getProp_value() != null) {
			// Check if the value is a JSON string representing a map and parse it
			try {
				return new ObjectMapper().readValue(propsEntity.getProp_value(), Map.class);
			} catch (IOException e) {
				// Value is not a valid JSON, return it as a string
				return propsEntity.getProp_value();
			}
		}
		return nestedMap;
	}

}
