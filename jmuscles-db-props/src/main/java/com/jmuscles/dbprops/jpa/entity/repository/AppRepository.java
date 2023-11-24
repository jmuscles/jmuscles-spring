package com.jmuscles.dbprops.jpa.entity.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import org.springframework.util.StringUtils;

import com.jmuscles.dbprops.jpa.entity.AppEntity;
import com.jmuscles.dbprops.jpa.entity.AppGroupEntity;
import com.jmuscles.props.util.Triplet;
import com.jmuscles.props.util.Util;

/**
 * @author manish goel
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
		Map<String, Object> fieldsToBeChecked = new HashMap<String, Object>();
		fieldsToBeChecked.put("name", entity.getName());
		fieldsToBeChecked.put("appGroupEntity.id", entity.getAppGroupEntity().getId());
		this.dbRepository.saveEntityWithDuplicateCheck(entity, fieldsToBeChecked);

		return entity;
	}

	public List<AppEntity> get(Long id, String name, Long appGroupId) {
		List<Triplet<String, String, Object>> list = new ArrayList<>();
		if (id != null && id > 0) {
			list.add(Triplet.of("id", "id", id));
		}
		if (StringUtils.hasText(name)) {
			list.add(Triplet.of("name", "name", name));
		}
		if (appGroupId != null && appGroupId > 0) {
			list.add(Triplet.of("appGroupEntity.id", "appGroupId", appGroupId));
		}

		return this.get(list);
	}

	public List<AppEntity> get(List<Triplet<String, String, Object>> parameters) {
		List<AppEntity> result = new ArrayList<>();
		executeInTransaction(
				em -> result.addAll(dbRepository.dynamicSelect(em, parameters, AppEntity.class.getSimpleName(), null)));
		return result;
	}

}
