package com.jmuscles.props.jpa.entity.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jmuscles.props.dto.AppDto;
import com.jmuscles.props.dto.AppGroupDto;
import com.jmuscles.props.dto.AppPropsProvisionDto;
import com.jmuscles.props.jpa.entity.AppPropsProvisionEntity;
import com.jmuscles.props.util.Constants;
import com.jmuscles.props.util.Triplet;
import com.jmuscles.props.util.Util;

/**
 * @author manish goel
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

	public List<AppPropsProvisionEntity> get(List<Triplet<String, String, Object>> parameters) {
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
		List<Triplet<String, String, Object>> selectionCriteria = buildSelectParameters(dto, true);
		List<Triplet<String, String, Object>> updateFields = new ArrayList<>();
		updateFields.add(Triplet.of("status", "updateStatus", Constants.STATUS_INACTIVE));

		AppPropsProvisionEntity entity = dto.toEntity();
		entity.resetCreateUpdate(createdAt, createdBy, null, null);

		executeInTransaction(em -> {
			update(em, selectionCriteria, updateFields, createdAt, createdBy);
			em.persist(entity);
		});
		return entity;
	}

	public int update(EntityManager entityManager, List<Triplet<String, String, Object>> selectionCriteria,
			List<Triplet<String, String, Object>> updatedFields, Timestamp updatedAt, String updatedBy) {

		StringBuilder jpql = new StringBuilder("UPDATE AppPropsProvisionEntity SET ");

		updatedFields.add(Triplet.of("updatedAt", "updatedAtValue", updatedAt));
		updatedFields.add(Triplet.of("updatedBy", "updatedByValue", updatedBy));

		// Set the updated fields in the JPQL query
		for (Triplet<String, String, Object> entry : updatedFields) {
			jpql.append(entry.getFirst()).append(" = :").append(entry.getSecond()).append(", ");
		}
		jpql.delete(jpql.length() - 2, jpql.length()); // Remove the trailing comma

		jpql.append(" WHERE ");

		// Set the selection criteria in the JPQL query
		for (Triplet<String, String, Object> entry : selectionCriteria) {
			jpql.append(entry.getFirst()).append(" = :").append(entry.getSecond()).append(" AND ");
		}
		jpql.delete(jpql.length() - 5, jpql.length()); // Remove the trailing "AND"

		// Create the query
		Query query = entityManager.createQuery(jpql.toString());

		// Set parameters for the updated fields
		for (Triplet<String, String, Object> entry : updatedFields) {
			query.setParameter(entry.getSecond(), entry.getThird());
		}

		// Set parameters for the selection criteria
		for (Triplet<String, String, Object> entry : selectionCriteria) {
			query.setParameter(entry.getSecond(), entry.getThird());
		}

		// Execute the update query
		int rowsUpdated = query.executeUpdate();
		return rowsUpdated;
	}

	private List<Triplet<String, String, Object>> buildSelectParameters(boolean isToUpdate, Long appId, String appName,
			Long appGroupId, String appGroupName, String env, String status, Long propMajorVersion,
			Long propMinorVersion) {

		AppPropsProvisionDto dto = AppPropsProvisionDto.of(null,
				AppDto.of(appId, appName, null, AppGroupDto.of(appGroupId, appGroupName, null, null, null), null, null),
				env, status, propMajorVersion, propMinorVersion, null, null, null, null, null, null, null);

		return buildSelectParameters(dto, isToUpdate);
	}

	private List<Triplet<String, String, Object>> buildSelectParameters(AppPropsProvisionDto dto, boolean isUpdate) {
		List<Triplet<String, String, Object>> list = new ArrayList<>();
		if (dto.getAppDto() != null) {
			AppDto appDto = dto.getAppDto();
			if (appDto.getId() != null) {
				list.add(Triplet.of("appEntity.id", "appId", appDto.getId()));
			}
			if (appDto.getName() != null) {
				list.add(Triplet.of("appEntity.name", "appName", appDto.getName()));
			}
			AppGroupDto appGroupDto = appDto.getAppGroupDto();
			if (appGroupDto != null) {
				if (appGroupDto.getId() != null) {
					list.add(Triplet.of("appEntity.appGroupEntity.id", "appGroupId", appGroupDto.getId()));
				}
				if (appGroupDto.getName() != null) {
					list.add(Triplet.of("appEntity.appGroupEntity.name", "appGroupName", appGroupDto.getName()));
				}
			}
		}
		if (dto.getEnv() != null) {
			list.add(Triplet.of("env", "env", dto.getEnv()));
		}
		if (dto.getStatus() != null) {
			list.add(Triplet.of("status", "status", dto.getStatus()));

		}
		if (!isUpdate) {
			if (dto.getPropMajorVersion() != null) {
				list.add(Triplet.of("propMajorVersion", "propMajorVersion", dto.getPropMajorVersion()));

			}
			if (dto.getPropMinorVersion() != null) {
				list.add(Triplet.of("propMinorVersion", "propMinorVersion", dto.getPropMinorVersion()));

			}
			if (dto.getPropTenantId() != null) {
				list.add(Triplet.of("propTenantId", "propTenantId", dto.getPropTenantId()));
			}
		}
		return list;
	}

}
