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

import com.jmuscles.props.dto.PropTenantDto;
import com.jmuscles.props.jpa.entity.PropTenantEntity;
import com.jmuscles.props.util.Util;

/**
 * 
 */
public class TenantCrudRepository {

	private String applicationName;
	private RepositorySetup dbRepository;

	public TenantCrudRepository(String applicationName, RepositorySetup dbRepository) {
		this.applicationName = applicationName;
		this.dbRepository = dbRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbRepository.executeInTransaction(action);
	}

	public PropTenantEntity createTenant(PropTenantDto tenant) {
		PropTenantEntity tenantEntity = tenant.toTenantEntity();
		tenantEntity.changeCreateUpdate(Util.currentTimeStamp(), applicationName, null, null);
		executeInTransaction(em -> em.persist(tenantEntity));
		return tenantEntity;
	}

	public List<PropTenantEntity> getTenants(Long id, String name) {
		Map<String, Object> parameters = new HashMap<>();
		if (id != null) {
			parameters.put("id", id);
		}
		if (name != null) {
			parameters.put("name", name);
		}
		return this.getTenants(parameters);
	}

	public List<PropTenantEntity> getTenants(Map<String, Object> parameters) {
		List<PropTenantEntity> result = new ArrayList<>();
		executeInTransaction(em -> result
				.addAll(dbRepository.dynamicSelect(em, parameters, PropTenantEntity.class.getSimpleName(), null)));
		return result;
	}

}
