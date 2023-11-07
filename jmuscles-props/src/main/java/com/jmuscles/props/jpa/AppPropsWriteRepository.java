/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.props.util.Constants;

/**
 * 
 */
public class AppPropsWriteRepository {
	private static final Logger logger = LoggerFactory.getLogger(AppPropsWriteRepository.class);

	private AppPropsRepositorySetup dbCommunicator;
	private AppPropsReadRepository appPropsReadRepository;

	public AppPropsWriteRepository(AppPropsRepositorySetup dbCommunicator,
			AppPropsReadRepository appPropsReadRepository) {
		this.dbCommunicator = dbCommunicator;
		this.appPropsReadRepository = appPropsReadRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbCommunicator.executeInTransaction(action);
	}

	public void replaceDescendants(Map<String, Object> properties, AppPropsEntity parentEntity) {
		Timestamp updatedAt = new Timestamp((new Date()).getTime());
		String updatedBy = "SYSTEM";
		replaceDescendants(properties, parentEntity, updatedAt, updatedBy);
	}

	public void replaceDescendants(Map<String, Object> properties, AppPropsEntity parentEntity, Timestamp updatedAt,
			String updatedBy) {
		executeInTransaction(em -> {
			updateStatusToDescendants(em, parentEntity, Constants.STATUS_ACTIVE, Constants.STATUS_DELETE, updatedAt,
					updatedBy);
			savePropertiesToDatabase(em, properties, parentEntity, Constants.STATUS_ACTIVE, updatedAt, updatedBy);
		});
	}

	public void updateStatusToDescendants(EntityManager em, AppPropsEntity parentEntity, String currentStatus,
			String newStatus, Timestamp updatedAt, String updatedBy) {
		List<AppPropsEntity> list = appPropsReadRepository.findEntireDescendants(em, parentEntity, currentStatus);
		bulkUpdateStatus(em, list, newStatus, updatedAt, updatedBy);
	}

	private void bulkUpdateStatus(EntityManager em, List<AppPropsEntity> entities, String status, Timestamp updatedAt,
			String updatedBy) {
		for (AppPropsEntity entity : entities) {
			entity.setStatus(status);
			entity.setUpdatedAt(updatedAt);
			entity.setUpdatedBy(updatedBy);
			em.merge(entity);
		}
	}

	public void replaceAllProperties(Map<String, Object> properties) {
		String status = Constants.STATUS_ACTIVE;
		Timestamp createdAt = new Timestamp((new Date()).getTime());
		String createdBy = "SYSTEM";
		executeInTransaction(em -> {
			deleteAll(em);
			savePropertiesToDatabase(em, properties, null, status, createdAt, createdBy);
		});
	}

	@SuppressWarnings("unchecked")
	public void savePropertiesToDatabase(EntityManager entityManager, Map<String, Object> properties,
			AppPropsEntity parent, String status, Timestamp createdAt, String createdBy) {
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			String key = entry.getKey();
			String value = (entry.getValue() != null && !(entry.getValue() instanceof Map))
					? entry.getValue().toString()
					: null;
			AppPropsEntity propertyEntity = AppPropsEntity.of(key, value, status, null, parent, null, createdAt,
					createdBy);
			try {
				entityManager.persist(propertyEntity);
			} catch (Exception ex1) {
				String keyToLogError = (parent != null ? parent.getProp_key() + "." : "") + key;
				if (value != null && value.length() > Constants.PROP_VALUE_LENGTH) {
					logger.info(
							"First attempt falied and trying second attempt to save data for key: " + keyToLogError);
					propertyEntity.setProp_value("BLOB");
					propertyEntity.setProp_value_blob(value.getBytes());
					try {
						entityManager.merge(propertyEntity);
						continue;
					} catch (Exception ex2) {
						logger.error("Second attempt failed to save data for key : " + keyToLogError, ex2);
					}
				}
				logger.error("Data could not be saved for key: " + keyToLogError, ex1);
			}
			if (entry.getValue() instanceof Map) {
				savePropertiesToDatabase(entityManager, (Map<String, Object>) entry.getValue(), propertyEntity, status,
						createdAt, createdBy);
			}
		}
	}

	public void deleteAll(EntityManager entityManager) {
		entityManager.createQuery("DELETE FROM AppPropsEntity a WHERE a.id<" + Long.MAX_VALUE).executeUpdate();
	}

}
