/**
 * @author manish goel
 *
 */
package com.jmuscles.dbprops.jpa.entity.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import org.springframework.util.StringUtils;

import com.jmuscles.dbprops.dto.PropTenantDto;
import com.jmuscles.dbprops.jpa.entity.PropTenantEntity;
import com.jmuscles.props.util.Triplet;
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
		tenantEntity.setId(null);
		executeInTransaction(em -> em.persist(tenantEntity));
		return tenantEntity;
	}

	public List<PropTenantEntity> getTenants(Long id, String name) {
		List<Triplet<String, String, Object>> list = new ArrayList<>();
		if (id != null && id > 0) {
			list.add(Triplet.of("id", "id", id));
		}
		if (StringUtils.hasText(name)) {
			list.add(Triplet.of("name", "name", name));
		}
		return this.getTenants(list);
	}

	public List<PropTenantEntity> getTenants(List<Triplet<String, String, Object>> parameters) {
		List<PropTenantEntity> result = new ArrayList<>();
		executeInTransaction(em -> result
				.addAll(dbRepository.dynamicSelect(em, parameters, PropTenantEntity.class.getSimpleName(), null)));
		return result;
	}

}
