/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa.entity.repository;

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

	public TenantCrudRepository(String applicationName) {
		this.applicationName = applicationName;
	}

	private RepositorySetup dbRepository;

	public TenantCrudRepository(RepositorySetup dbRepository) {
		this.dbRepository = dbRepository;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		dbRepository.executeInTransaction(action);
	}

	public TenantEntity createTenant(TenantDto tenant) {
		TenantEntity tenantEntity = tenant.getTenantEntity();
		tenantEntity.changeCreateUpdate(Util.currentTimeStamp(), applicationName, null, null);
		executeInTransaction(em -> em.persist(tenant));
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
		return dbRepository.dynamicSelect(parameters, "TenantEntity");
	}

}
