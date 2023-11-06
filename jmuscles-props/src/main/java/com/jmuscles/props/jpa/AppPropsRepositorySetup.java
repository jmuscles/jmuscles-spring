/**
 * 
 */
package com.jmuscles.props.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;

import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.props.AppPropsDBConfig;

/**
 * @author manish goel
 *
 */
public class AppPropsRepositorySetup {

	private static final Logger logger = LoggerFactory.getLogger(AppPropsRepositorySetup.class);

	public static final String PACKAGES_TO_SCAN = "com.jmuscles.props.jpa";
	public static final String PERSISTENCE_UNIT_NAME = "appPropsResourceUnit";

	private EntityManagerFactory emf;
	public String applicationName;
	private DataSourceProvider dataSourceProvider;
	private AppPropsDBConfig appPropsDBConfig;

	public AppPropsRepositorySetup(String applicationName, DataSourceProvider dataSourceProvider,
			AppPropsDBConfig appPropsDBConfig) {
		super();
		this.applicationName = applicationName;
		this.dataSourceProvider = dataSourceProvider;
		this.appPropsDBConfig = appPropsDBConfig;
	}

	private void initialize() {
		setupEntityManagerFactory();
	}

	public void refresh() {
		setupEntityManagerFactory();
	}

	public List<AppPropsEntity> selectAppPropsEntity(String queryString, Map<String, Object> queryParams) {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> result.addAll(selectAppPropsEntity(queryString, queryParams, em)));
		return result;
	}

	public List<AppPropsEntity> selectAppPropsEntity(String queryString, Map<String, Object> queryParams,
			EntityManager entityManager) {
		List<AppPropsEntity> result = new ArrayList<>();
		TypedQuery<AppPropsEntity> query = entityManager.createQuery(queryString, AppPropsEntity.class);
		if (queryParams != null) {
			queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
		}
		result.addAll(query.getResultList());
		return result;
	}

	private AppPropsEntity findChildByKey(AppPropsEntity parent, String key, String status) {
		String query = null;
		Map<String, Object> params = new HashMap<>();
		if (parent == null) {
			query = "SELECT a FROM AppPropsEntity a WHERE a.prop_key = :key AND a.parent IS NULL AND a.status= :status";
		} else {
			query = "SELECT a FROM AppPropsEntity a WHERE a.prop_key = :key AND a.parent = :parent AND a.status= :status";
			params.put("parent", parent);
		}
		params.put("key", key);
		params.put("status", status);
		List<AppPropsEntity> result = dbCommunicator.selectAppPropsEntity(query, params);
		return result.isEmpty() ? null : result.get(0);
	}

	public List<AppPropsEntity> dynamicSelect(EntityManager em, Map<String, Object> parameters) {
		StringBuilder jpql = new StringBuilder("SELECT ap FROM AppPropsEntity ap WHERE ap.tenant = :tenant");
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("tenant", tenant); // Replace 'tenant' with the actual TenantEntity object.

		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				String paramName = entry.getKey();
				Object paramValue = entry.getValue();

				if (paramValue != null) {
					jpql.append(" AND ap.").append(paramName).append(" = :").append(paramName);
					queryParameters.put(paramName, paramValue);
				}
			}
		}

		Query query = em.createQuery(jpql.toString());
		for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		List<AppPropsEntity> results = query.getResultList();
		return results;
	}

	public void executeInTransaction(Consumer<EntityManager> action) {
		initialize();
		if (this.emf == null) {
			logger.error("Database entity manager is not setup, hence payload can not be saved.... "
					+ "Please try setting up the AppPropsDB database and entities.");
			throw new RuntimeException("AppPropsDB is not setup/ Entity Manager factory is null.");
		}

		EntityManager em = this.emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			action.accept(em);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	private synchronized void setupEntityManagerFactory() {
		DataSource dataSource = getProducerDataSource();
		if (this.emf == null && dataSource != null) {
			final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
			em.setDataSource(dataSource);
			em.setPackagesToScan(PACKAGES_TO_SCAN);
			em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			em.setJpaProperties(jpaProperties());
			em.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
			em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
			em.afterPropertiesSet();
			this.emf = em.getObject();
		}
	}

	private DataSource getProducerDataSource() {
		DataSource dataSource = null;
		if (this.dataSourceProvider == null) {
			logger.error("dataSourceGenerator is null hence AppPropsResource can not be initialized.");
		} else if (this.appPropsDBConfig == null || this.appPropsDBConfig.getDataSourceKey() == null) {
			logger.error("'config-props-from-db.datasourceKey' is not configured "
					+ "hence AppPropsResource can not be initialized.");
		} else {
			dataSource = this.dataSourceProvider.get(this.appPropsDBConfig.getDataSourceKey());
		}
		return dataSource;
	}

	private Properties jpaProperties() {
		final Properties properties = new Properties();
		if (appPropsDBConfig != null && appPropsDBConfig.getJpaProperties() != null) {
			properties.putAll(appPropsDBConfig.getJpaProperties());
		}
		return properties;
	}

	private boolean checkNonNull(String str) {
		return StringUtils.hasText(str) && !"NULL".equalsIgnoreCase(str);
	}

}
