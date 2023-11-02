/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
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

	public AppPropsEntity findByKeyPath(List<String> keyPath) {
		AppPropsEntity currentEntity = null;

		for (String key : keyPath) {
			currentEntity = findChildByKey(currentEntity, key);
			if (currentEntity == null) {
				// Entity with the given key doesn't exist, so break the loop
				break;
			}
		}

		return currentEntity;
	}

	private AppPropsEntity findChildByKey(AppPropsEntity parent, String key) {
		List<AppPropsEntity> result = new ArrayList<>();

		if (parent == null) {
			// If parent is null, search for the root entity
			executeInTransaction(em -> {
				TypedQuery<AppPropsEntity> query = em.createQuery(
						"SELECT e FROM AppPropsEntity e WHERE e.prop_key = :key AND e.parent IS NULL",
						AppPropsEntity.class);
				query.setParameter("key", key);
				result.addAll(query.getResultList());
			});
			return result.isEmpty() ? null : result.get(0);
		} else {
			// Search for a child entity
			executeInTransaction(em -> {
				TypedQuery<AppPropsEntity> query = em.createQuery(
						"SELECT e FROM AppPropsEntity e WHERE e.prop_key = :key AND e.parent = :parent",
						AppPropsEntity.class);
				query.setParameter("key", key);
				query.setParameter("parent", parent);
				result.addAll(query.getResultList());
			});
			return result.isEmpty() ? null : result.get(0);
		}
	}

	public List<AppPropsEntity> findAllByParent(Long parentId) {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em
					.createQuery("SELECT a FROM AppPropsEntity a WHERE a.parent.id = :parentId", AppPropsEntity.class);
			query.setParameter("parentId", parentId);
			result.addAll(query.getResultList());
		});
		return result;
	}

	public List<AppPropsEntity> findAllByParentIsNull() {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em.createQuery("SELECT p FROM AppPropsEntity p WHERE p.parent IS NULL",
					AppPropsEntity.class);
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

	public void deleteAll() {
		executeInTransaction(em -> {
			em.createQuery("DELETE FROM AppPropsEntity a WHERE a.id<" + Long.MAX_VALUE).executeUpdate();
		});
	}

}
