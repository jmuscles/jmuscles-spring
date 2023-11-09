/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa.entity.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.jmuscles.props.dto.PropVersionDto;
import com.jmuscles.props.jpa.entity.PropVersionEntity;
import com.jmuscles.props.util.Util;

/**
 * 
 */
public class PropVersionCrudRepository {

	private String applicationName;
	private RepositorySetup repositorySetup;

	public PropVersionCrudRepository(String applicationName, RepositorySetup repositorySetup) {
		this.applicationName = applicationName;
		this.repositorySetup = repositorySetup;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		this.repositorySetup.executeInTransaction(action);
	}

	public PropVersionEntity createPropVersion(PropVersionDto propVersionDto) {
		PropVersionEntity propVersionEntity = propVersionDto.getPropVersionEntity();
		propVersionEntity.changeCreateUpdate(Util.currentTimeStamp(), applicationName, null, null);
		executeInTransaction(em -> em.persist(propVersionEntity));
		return propVersionEntity;
	}

	public List<PropVersionEntity> getPropVersions(String tenantName, Long tenantId, Integer majorVersion,
			Integer minorVersion) {
		Map<String, Object> parameters = new HashMap<>();
		if (tenantId != null) {
			parameters.put("tenant.id", tenantId);
		}
		if (tenantName != null) {
			parameters.put("tenant.name", tenantName);
		}
		if (majorVersion != null) {
			parameters.put("majorVersion", majorVersion);
		}
		if (minorVersion != null) {
			parameters.put("minorVersion", minorVersion);
		}

		return this.getPropVersions(parameters);
	}

	public List<PropVersionDto> getPropVersionDtos(String tenantName, Long tenantId, Integer majorVersion,
			Integer minorVersion) {
		List<PropVersionEntity> entities = this.getPropVersions(tenantName, null, majorVersion, minorVersion);
		return entities != null
				? entities.stream().map(entity -> PropVersionDto.of(entity)).collect(Collectors.toList())
				: null;

	}

	public List<PropVersionEntity> getPropVersions(Map<String, Object> parameters) {
		return this.repositorySetup.dynamicSelect(parameters, "PropVersionEntity");
	}

}
