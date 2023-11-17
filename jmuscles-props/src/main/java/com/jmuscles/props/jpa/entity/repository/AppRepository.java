/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa.entity.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import com.jmuscles.props.jpa.entity.AppEntity;
import com.jmuscles.props.jpa.entity.AppGroupEntity;
import com.jmuscles.props.util.Util;

/**
 * 
 */
public class AppRepository {

	private String applicationName;
	private RepositorySetup dbRepository;

	public AppRepository(String applicationName, RepositorySetup dbRepository) {
		this.applicationName = applicationName;
		this.dbRepository = dbRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbRepository.executeInTransaction(action);
	}

	public AppEntity create(String name, Long groupId, String description) {
		AppEntity entity = AppEntity.of(null, name, description, AppGroupEntity.of(groupId, null, null, null, null),
				Util.currentTimeStamp(), applicationName);
		executeInTransaction(em -> em.persist(entity));
		return entity;
	}

	public List<AppEntity> get(Long id, String name, Long appGroupId) {
		Map<String, Object> parameters = new HashMap<>();
		if (id != null) {
			parameters.put("id", id);
		}
		if (name != null) {
			parameters.put("name", name);
		}
		if (appGroupId != null) {
			parameters.put("appGroupEntity.id", appGroupId);
		}

		return this.get(parameters);
	}

	public List<AppEntity> get(Map<String, Object> parameters) {
		List<AppEntity> result = new ArrayList<>();
		executeInTransaction(
				em -> result.addAll(dbRepository.dynamicSelect(em, parameters, AppEntity.class.getSimpleName(), null)));
		return result;
	}

}
