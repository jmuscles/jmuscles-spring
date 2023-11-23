/**
 * @author manish goel
 *
 */
package com.jmuscles.dbprops.jpa.entity.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import org.springframework.util.StringUtils;

import com.jmuscles.dbprops.jpa.entity.AppGroupEntity;
import com.jmuscles.props.util.Triplet;
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
		Map<String, Object> fieldsToBeChecked = new HashMap<String, Object>();
		fieldsToBeChecked.put("name", entity.getName());
		this.dbRepository.saveEntityWithDuplicateCheck(entity, fieldsToBeChecked);

		return entity;
	}

	public List<AppGroupEntity> getGroup(Long id, String name) {
		List<Triplet<String, String, Object>> list = new ArrayList<>();
		if (id != null && id > 0) {
			list.add(Triplet.of("id", "id", id));
		}
		if (StringUtils.hasText(name)) {
			list.add(Triplet.of("name", "name", name));
		}

		return this.getGroup(list);
	}

	public List<AppGroupEntity> getGroup(List<Triplet<String, String, Object>> parameters) {
		List<AppGroupEntity> result = new ArrayList<>();
		executeInTransaction(em -> result
				.addAll(dbRepository.dynamicSelect(em, parameters, AppGroupEntity.class.getSimpleName(), null)));
		return result;
	}

}
