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

import com.jmuscles.props.jpa.entity.AppGroupEntity;
import com.jmuscles.props.util.Util;

/**
 * 
 */
public class AppGroupRepository {

	private String applicationName;
	private RepositorySetup dbRepository;

	public AppGroupRepository(String applicationName, RepositorySetup dbRepository) {
		this.applicationName = applicationName;
		this.dbRepository = dbRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbRepository.executeInTransaction(action);
	}

	public AppGroupEntity createGroup(String name, String description) {
		AppGroupEntity entity = AppGroupEntity.of(null, name, description, Util.currentTimeStamp(), applicationName);
		executeInTransaction(em -> em.persist(entity));
		return entity;
	}

	public List<AppGroupEntity> getGroup(Long id, String name) {
		Map<String, Object> parameters = new HashMap<>();
		if (id != null) {
			parameters.put("id", id);
		}
		if (name != null) {
			parameters.put("name", name);
		}
		return this.getGroup(parameters);
	}

	public List<AppGroupEntity> getGroup(Map<String, Object> parameters) {
		List<AppGroupEntity> result = new ArrayList<>();
		executeInTransaction(em -> result
				.addAll(dbRepository.dynamicSelect(em, parameters, AppGroupEntity.class.getSimpleName(), null)));
		return result;
	}

}
