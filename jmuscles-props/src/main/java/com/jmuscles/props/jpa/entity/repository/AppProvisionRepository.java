/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa.entity.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jmuscles.props.dto.AppDto;
import com.jmuscles.props.dto.AppGroupDto;
import com.jmuscles.props.dto.AppPropsProvisionDto;
import com.jmuscles.props.jpa.entity.AppPropsProvisionEntity;
import com.jmuscles.props.util.Constants;
import com.jmuscles.props.util.Util;

/**
 * 
 */
public class AppProvisionRepository {

	private String applicationName;
	private RepositorySetup dbRepository;

	public AppProvisionRepository(String applicationName, RepositorySetup dbRepository) {
		this.applicationName = applicationName;
		this.dbRepository = dbRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbRepository.executeInTransaction(action);
	}

	public AppPropsProvisionEntity get(String appName, String appGroupName, String env) {
		List<AppPropsProvisionEntity> entities = get(buildSelectParameters(false, null, appName, null, appGroupName,
				env, Constants.STATUS_ACTIVE, null, null));
		return (entities != null && !entities.isEmpty()) ? entities.get(0) : null;
	}

	public List<AppPropsProvisionEntity> get(Long appId, String appName, Long appGroupId, String appGroupName,
			String env, String status) {
		return get(buildSelectParameters(false, appId, appName, appGroupId, appGroupName, env, status, null, null));
	}

	public List<AppPropsProvisionEntity> get(AppPropsProvisionDto dto) {
		return this.get(buildSelectParameters(dto, false));
	}

	public List<AppPropsProvisionEntity> get(Map<String, Object> parameters) {
		List<AppPropsProvisionEntity> result = new ArrayList<>();
		executeInTransaction(em -> result.addAll(
				dbRepository.dynamicSelect(em, parameters, AppPropsProvisionEntity.class.getSimpleName(), null)));
		return result;
	}

	public AppPropsProvisionEntity create(AppPropsProvisionDto dto) {
		Timestamp createdAt = Util.currentTimeStamp();
		String createdBy = applicationName;

		// update to deactivate the existing appProps
		dto.setStatus(Constants.STATUS_ACTIVE);
		Map<String, Object> selectionCriteria = buildSelectParameters(dto, true);
		Map<String, Object> updateFields = new HashMap<String, Object>();
		updateFields.put("status", Constants.STATUS_INACTIVE);

		AppPropsProvisionEntity entity = dto.toEntity();
		entity.resetCreateUpdate(createdAt, createdBy, null, null);

		executeInTransaction(em -> {
			update(em, selectionCriteria, updateFields, createdAt, createdBy);
			em.persist(entity);
		});
		return entity;
	}

	public int update(EntityManager entityManager, Map<String, Object> selectionCriteria,
			Map<String, Object> updatedFields, Timestamp updatedAt, String updatedBy) {

		StringBuilder jpql = new StringBuilder("UPDATE AppPropsProvisionEntity SET ");

		updatedFields.put("updatedAt", updatedAt);
		updatedFields.put("updatedBy", updatedBy);
		// Set the updated fields in the JPQL query
		for (Map.Entry<String, Object> entry : updatedFields.entrySet()) {
			jpql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(", ");
		}
		jpql.delete(jpql.length() - 2, jpql.length()); // Remove the trailing comma

		jpql.append(" WHERE ");

		// Set the selection criteria in the JPQL query
		for (Map.Entry<String, Object> entry : selectionCriteria.entrySet()) {
			jpql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(" AND ");
		}
		jpql.delete(jpql.length() - 5, jpql.length()); // Remove the trailing "AND"

		// Create the query
		Query query = entityManager.createQuery(jpql.toString());

		// Set parameters for the updated fields
		for (Map.Entry<String, Object> entry : updatedFields.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		// Set parameters for the selection criteria
		for (Map.Entry<String, Object> entry : selectionCriteria.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		// Execute the update query
		int rowsUpdated = query.executeUpdate();
		return rowsUpdated;
	}

	private Map<String, Object> buildSelectParameters(boolean isToUpdate, Long appId, String appName, Long appGroupId,
			String appGroupName, String env, String status, Long propMajorVersion, Long propMinorVersion) {

		AppPropsProvisionDto dto = AppPropsProvisionDto.of(
				AppDto.of(appId, appName, null, AppGroupDto.of(appGroupId, appGroupName, null, null, null), null, null),
				env, status, propMajorVersion, propMinorVersion, null, null, null, null, null, null, null);

		return buildSelectParameters(dto, isToUpdate);
	}

	private Map<String, Object> buildSelectParameters(AppPropsProvisionDto dto, boolean isUpdate) {
		Map<String, Object> parameters = new HashMap<>();
		if (dto.getAppDto() != null) {
			AppDto appDto = dto.getAppDto();
			if (appDto.getId() != null) {
				parameters.put("appEntity.id", appDto.getId());
			}
			if (appDto.getName() != null) {
				parameters.put("appEntity.name", appDto.getName());
			}
			AppGroupDto appGroupDto = appDto.getAppGroupDto();
			if (appGroupDto != null) {
				if (appGroupDto.getId() != null) {
					parameters.put("appEntity.appGroupEntity.id", appGroupDto.getId());
				}
				if (appGroupDto.getName() != null) {
					parameters.put("appEntity.appGroupEntity.name", appGroupDto.getName());
				}
			}
		}
		if (dto.getEnv() != null) {
			parameters.put("env", dto.getEnv());
		}
		if (dto.getStatus() != null) {
			parameters.put("status", dto.getStatus());
		}
		if (!isUpdate) {
			if (dto.getPropMajorVersion() != null) {
				parameters.put("propMajorVersion", dto.getPropMajorVersion());
			}
			if (dto.getPropMinorVersion() != null) {
				parameters.put("propMinorVersion", dto.getPropMinorVersion());
			}
			if (dto.getPropTenantId() != null) {
				parameters.put("propTenantId", dto.getPropTenantId());
			}
		}

		return parameters;
	}

}
