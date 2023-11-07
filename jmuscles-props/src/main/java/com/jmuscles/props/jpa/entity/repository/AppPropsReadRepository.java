/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa.entity.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmuscles.props.jpa.entity.PropEntity;
import com.jmuscles.props.util.Constants;

/**
 * 
 */
public class AppPropsReadRepository {

	private AppPropsRepositorySetup dbCommunicator;

	public AppPropsReadRepository(AppPropsRepositorySetup dbCommunicator) {
		this.dbCommunicator = dbCommunicator;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbCommunicator.executeInTransaction(action);
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

	private PropEntity findChildByKey(EntityManager entityManager, PropEntity parent, String prop_key,
			String status, Long tenantId, Long versionId) {
		Map<String, Object> params = new HashMap<>();
		params.put("prop_key", prop_key);
		params.put("status", status);
		if (parent == null) {
			params.put("parent", Constants.VALUE_FOR_IS_NULL_CHECK);
		} else {
			params.put("parent", parent);
		}
		List<PropEntity> result = dbCommunicator.selectAppProps(entityManager, params);
		return result.isEmpty() ? null : result.get(0);
	}

	private List<PropEntity> findImmediateChildren(EntityManager entityManager, String status,
			PropEntity parent) {
		Map<String, Object> params = new HashMap<>();
		if (parent == null) {
			params.put("parent", Constants.VALUE_FOR_IS_NULL_CHECK);
		} else {
			params.put("parent", parent);
		}
		params.put("status", status);
		return dbCommunicator.selectAppProps(entityManager, params);
	}

	public List<PropEntity> findEntireDescendants(EntityManager entityManager, PropEntity parent,
			String status) {
		Map<String, Object> params = new HashMap<>();
		params.put("parent", parent);
		params.put("status", status);
		List<PropEntity> children = dbCommunicator.selectAppProps(entityManager, params);
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
