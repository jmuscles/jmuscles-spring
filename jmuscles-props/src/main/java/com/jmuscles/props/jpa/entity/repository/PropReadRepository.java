package com.jmuscles.props.jpa.entity.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmuscles.props.jpa.entity.PropEntity;
import com.jmuscles.props.util.Constants;

/**
 * @author manish goel
 */

public class PropReadRepository {

	private RepositorySetup repositorySetup;

	public PropReadRepository(RepositorySetup repositorySetup) {
		this.repositorySetup = repositorySetup;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		repositorySetup.executeInTransaction(action);
	}

	// TODO delete this
	public PropEntity findByKeyPath(List<String> keyPath, String status, Long tenantId, Long versionId) {
		Map<String, PropEntity> map = new HashMap<>();
		executeInTransaction(em -> {
			PropEntity currentEntity = findByKeyPath(em, keyPath, status, tenantId, versionId);
			if (currentEntity != null) {
				map.put("currentEntity", currentEntity);
			}
		});
		return map.get("currentEntity");
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

	public PropEntity findByKeyPath(EntityManager entityManager, List<String> keyPath, String status, Long tenantId,
			Long versionId) {
		PropEntity currentEntity = null;
		for (String key : keyPath) {
			currentEntity = findChildByKey(entityManager, currentEntity, key, status, tenantId, versionId);
			if (currentEntity == null) {
				// Entity with the given key doesn't exist, so break the loop
				break;
			}
		}
		return currentEntity;
	}

	private PropEntity findChildByKey(EntityManager entityManager, PropEntity parent, String prop_key, String status,
			Long tenantId, Long versionId) {
		Map<String, Object> params = new HashMap<>();
		params.put("prop_key", prop_key);
		params.put("status", status);
		if (parent == null) {
			params.put("parent", Constants.VALUE_FOR_IS_NULL_CHECK);
		} else {
			params.put("parent", parent);
		}
		List<PropEntity> result = repositorySetup.selectProperties(entityManager, params);
		return result.isEmpty() ? null : result.get(0);
	}

	private List<PropEntity> findImmediateChildren(EntityManager entityManager, String status, PropEntity parent) {
		Map<String, Object> params = new HashMap<>();
		if (parent == null) {
			params.put("parent", Constants.VALUE_FOR_IS_NULL_CHECK);
		} else {
			params.put("parent", parent);
		}
		params.put("status", status);
		return repositorySetup.selectProperties(entityManager, params);
	}

	public List<PropEntity> findEntireDescendants(EntityManager entityManager, PropEntity parent, String status) {
		Map<String, Object> params = new HashMap<>();
		params.put("parent", parent);
		params.put("status", status);
		List<PropEntity> children = repositorySetup.selectProperties(entityManager, params);
		// Create a separate list to collect the grandchildren
		List<PropEntity> allGrandchildren = new ArrayList<>();
		for (PropEntity child : children) {
			// Recursively select children of children
			List<PropEntity> grandchildren = findEntireDescendants(entityManager, child, status);
			allGrandchildren.addAll(grandchildren);
		}
		// Add the grandchildren to the children list after the loop
		children.addAll(allGrandchildren);
		return children;
	}

	public Map<String, Object> readDataFromDatabase(List<String> paths, String status, Long tenantId, Long versionId) {
		List<PropEntity> topLevelProperties = new ArrayList<PropEntity>();
		Map<String, Object> returnMap = new HashMap<>();
		executeInTransaction(entityManager -> {
			if (paths == null) {
				topLevelProperties.addAll(findImmediateChildren(entityManager, status, null));
			} else {
				PropEntity appPropsEntity = findByKeyPath(entityManager, paths, status, tenantId, versionId);
				if (appPropsEntity != null) {
					topLevelProperties.add(appPropsEntity);
				}
			}
			if (topLevelProperties != null && !topLevelProperties.isEmpty()) {
				returnMap.putAll(topLevelProperties.stream().collect(Collectors.toMap(entity -> entity.getProp_key(),
						entity -> buildNestedMap(entityManager, entity, status))));
			}

		});

		return returnMap.size() != 0 ? returnMap : null;
	}

	private Object buildNestedMap(EntityManager entityManager, PropEntity appPropsEntity, String status) {
		Map<String, Object> nestedMap = new HashMap<>();
		List<PropEntity> children = findImmediateChildren(entityManager, status, appPropsEntity);
		for (PropEntity child : children) {
			nestedMap.put(child.getProp_key(), buildNestedMap(entityManager, child, status));
		}
		if (appPropsEntity.getProp_value() != null) {
			// Check if the value is a JSON string representing a map and parse it
			try {
				return new ObjectMapper().readValue(appPropsEntity.getProp_value(), Map.class);
			} catch (IOException e) {
				// Value is not a valid JSON, return it as a string
				return appPropsEntity.getProp_value();
			}
		}
		return nestedMap;
	}

}
