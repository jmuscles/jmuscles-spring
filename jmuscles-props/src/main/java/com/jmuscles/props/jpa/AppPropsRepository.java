/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * 
 */
public class AppPropsRepository {

	private AppPropsRepositorySetup dbCommunicator;

	public AppPropsRepository(AppPropsRepositorySetup dbCommunicator) {
		this.dbCommunicator = dbCommunicator;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbCommunicator.executeInTransaction(action);
	}

	public void save(AppPropsEntity appPropsEntity) {
		executeInTransaction(em -> em.persist(appPropsEntity));
	}

	public void merge(AppPropsEntity appPropsEntity) {
		executeInTransaction(em -> em.merge(appPropsEntity));
	}

	public void saveAll(List<AppPropsEntity> appPropsEntities) {
		executeInTransaction(em -> {
			for (AppPropsEntity appPropEntity : appPropsEntities) {
				em.persist(appPropEntity);
			}
		});
	}

	public AppPropsEntity findByKeyPath(List<String> keyPath, String status) {
		AppPropsEntity currentEntity = null;
		for (String key : keyPath) {
			currentEntity = findChildByKey(currentEntity, key, status);
			if (currentEntity == null) {
				// Entity with the given key doesn't exist, so break the loop
				break;
			}
		}
		return currentEntity;
	}

	private AppPropsEntity findChildByKey(AppPropsEntity parent, String key, String status) {
		String query = null;
		Map<String, Object> params = new HashMap<>();
		if (parent == null) {
			query = "SELECT a FROM AppPropsEntity a WHERE a.prop_key = :key AND a.parent IS NULL AND a.status= :status";
		} else {
			query = "SELECT a FROM AppPropsEntity a WHERE a.prop_key = :key AND a.parent = :parent AND a.status= :status";
			params.put("parent", parent);
		}
		params.put("key", key);
		params.put("status", status);
		List<AppPropsEntity> result = dbCommunicator.selectAppPropsEntity(query, params);
		return result.isEmpty() ? null : result.get(0);
	}

	public List<AppPropsEntity> findAllByParent(Long parentId, String status) {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em.createQuery(
					"SELECT a FROM AppPropsEntity a WHERE a.parent.id = :parentId AND a.status= :status",
					AppPropsEntity.class);
			query.setParameter("parentId", parentId);
			query.setParameter("status", status);
			result.addAll(query.getResultList());
		});
		return result;
	}

	public List<AppPropsEntity> findAllByParentIsNull(String status) {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em.createQuery(
					"SELECT a FROM AppPropsEntity a WHERE a.parent IS NULL AND a.status= :status",
					AppPropsEntity.class);
			query.setParameter("status", status);
			result.addAll(query.getResultList());
		});
		return result;
	}

	public List<AppPropsEntity> findAll() {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em.createQuery("SELECT a FROM AppPropsEntity a", AppPropsEntity.class);
			result.addAll(query.getResultList());
		});
		return result;
	}

	public void updateAllChildren(AppPropsEntity parentEntity, String status) {
		updateAllChildren(parentEntity, status, new Timestamp((new java.util.Date()).getTime()), "SYSTEM");
	}

	public void updateAllChildren(AppPropsEntity parentEntity, String status, Timestamp updatedAt, String updatedBy) {
		executeInTransaction(em -> {
			bulkUpdateStatus(findAllChildren(parentEntity, em), status, updatedAt, updatedBy, em);
		});
	}

	private void bulkUpdateStatus(List<AppPropsEntity> entities, String status, Timestamp updatedAt, String updatedBy,
			EntityManager em) {
		for (AppPropsEntity entity : entities) {
			entity.setStatus(status);
			entity.setUpdatedAt(updatedAt);
			entity.setUpdatedBy(updatedBy);
			em.merge(entity);
		}
	}

	public List<AppPropsEntity> findAllChildren(AppPropsEntity parent) {
		List<AppPropsEntity> returnResult = new ArrayList<>();
		executeInTransaction(em -> {
			List<AppPropsEntity> result = findAllChildren(parent, em);
			if (result != null) {
				returnResult.addAll(result);
			}
		});
		return returnResult;
	}

	private List<AppPropsEntity> findAllChildren(AppPropsEntity parent, EntityManager entityManager) {
		Query query = entityManager.createQuery("SELECT a FROM AppPropsEntity a WHERE a.parent = :parent");
		query.setParameter("parent", parent);
		@SuppressWarnings("unchecked")
		List<AppPropsEntity> children = query.getResultList();

		// Create a separate list to collect the grandchildren
		List<AppPropsEntity> allGrandchildren = new ArrayList<>();

		for (AppPropsEntity child : children) {
			// Recursively select children of children
			List<AppPropsEntity> grandchildren = findAllChildren(child, entityManager);
			allGrandchildren.addAll(grandchildren);
		}

		// Add the grandchildren to the children list after the loop
		children.addAll(allGrandchildren);

		return children;
	}

	public void deleteAll() {
		executeInTransaction(em -> {
			em.createQuery("DELETE FROM AppPropsEntity a WHERE a.id<" + Long.MAX_VALUE).executeUpdate();
		});
	}

}
