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

import com.jmuscles.props.dto.TenantDto;
import com.jmuscles.props.jpa.entity.TenantEntity;
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

	public TenantEntity createTenant(TenantDto tenant) {
		TenantEntity tenantEntity = tenant.toTenantEntity();
		tenantEntity.changeCreateUpdate(Util.currentTimeStamp(), applicationName, null, null);
		executeInTransaction(em -> em.persist(tenantEntity));
		return tenantEntity;
	}

	public List<TenantEntity> getTenants(Long id, String name) {
		Map<String, Object> parameters = new HashMap<>();
		if (id != null) {
			parameters.put("id", id);
		}
		if (id != null) {
			parameters.put("name", name);
		}
		return this.getTenants(parameters);
	}

	public List<TenantEntity> getTenants(Map<String, Object> parameters) {
		List<TenantEntity> result = new ArrayList<>();
		executeInTransaction(em -> result.addAll(dbRepository.dynamicSelect(em, parameters, "TenantEntity", null)));
		return result;
	}

}
