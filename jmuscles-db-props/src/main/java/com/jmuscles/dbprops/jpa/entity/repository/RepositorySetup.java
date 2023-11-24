/**
 * 
 */
package com.jmuscles.dbprops.jpa.entity.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;

import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.dbprops.exception.DuplicateEntryException;
import com.jmuscles.dbprops.util.Constants;
import com.jmuscles.props.config.AppPropsDBConfig;
import com.jmuscles.props.util.Triplet;

/**
 * @author manish goel
 *
 */
public class RepositorySetup {

	private static final Logger logger = LoggerFactory.getLogger(RepositorySetup.class);

	public static final String PACKAGES_TO_SCAN = "com.jmuscles.dbprops.jpa.entity";
	public static final String PERSISTENCE_UNIT_NAME = "appPropsResourceUnit";

	private EntityManagerFactory emf;
	public String applicationName;
	private DataSourceProvider dataSourceProvider;
	private AppPropsDBConfig appPropsDBConfig;

	public RepositorySetup(String applicationName, DataSourceProvider dataSourceProvider,
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
		logger.info("Refresh RepositorySetup start....");
		this.emf = null;
		setupEntityManagerFactory();
		logger.info("....Refresh RepositorySetup end");
	}

	public <T> List<T> dynamicSelectdelete(EntityManager em, Map<String, Object> parameters, String entityName,
			String orderByClause) {
		StringBuilder jpql = new StringBuilder("SELECT a FROM " + entityName + " a");
		Map<String, Object> queryParameters = new HashMap<>();
		if (parameters != null && !parameters.isEmpty()) {
			jpql.append(" WHERE 1=1"); // Add a dummy condition to start the WHERE clause
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				String paramName = entry.getKey();
				Object paramValue = entry.getValue();

				if (paramValue != null) {
					if (paramValue instanceof String && Constants.VALUE_FOR_IS_NULL_CHECK.equals(paramValue)) {
						jpql.append(" AND a.").append(paramName).append(" IS NULL");
					} else {
						jpql.append(" AND a.").append(paramName).append(" = :").append(paramName);
						queryParameters.put(paramName, paramValue);
					}
				}
			}
		}
		if (orderByClause != null && !orderByClause.isEmpty()) {
			jpql.append(" ORDER BY ").append(orderByClause);
		}

		Query query = em.createQuery(jpql.toString());
		for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		List<T> results = query.getResultList();
		return results;
	}

	public <T> List<T> dynamicSelect(EntityManager em, List<Triplet<String, String, Object>> parameters,
			String entityName, String orderByClause) {
		StringBuilder jpql = new StringBuilder("SELECT a FROM " + entityName + " a");
		if (parameters != null && !parameters.isEmpty()) {
			jpql.append(" WHERE 1=1"); // Add a dummy condition to start the WHERE clause
			for (Triplet<String, String, Object> entry : parameters) {
				String paramName = entry.getFirst();
				Object paramValue = entry.getSecond();
				if (paramValue instanceof String && Constants.VALUE_FOR_IS_NULL_CHECK.equals(paramValue)) {
					jpql.append(" AND a.").append(paramName).append(" IS NULL");
				} else {
					jpql.append(" AND a.").append(paramName).append(" = :").append(paramValue);
				}
			}
		}
		if (orderByClause != null && !orderByClause.isEmpty()) {
			jpql.append(" ORDER BY ").append(orderByClause);
		}

		Query query = em.createQuery(jpql.toString());
		for (Triplet<String, String, Object> entry : parameters) {
			query.setParameter(entry.getSecond(), entry.getThird());
		}
		List<T> results = query.getResultList();
		return results;
	}

	public <T> List<T> runNamedQuery(EntityManager entityManager, String queryString, Map<String, Object> parameters,
			Class<?> entityClass) {
		Query query = null;
		if (entityClass != null) {
			query = entityManager.createNamedQuery(queryString, entityClass);
		} else {
			query = entityManager.createNamedQuery(queryString);
		}
		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		List<T> results = query.getResultList();
		return results;
	}

	public <T> void saveEntityWithDuplicateCheck(T entity, Map<String, Object> fieldsToBeChecked) {
		executeInTransaction(entityManager -> {
			if (!isDuplicateEntry(entityManager, entity.getClass(), fieldsToBeChecked)) {
				entityManager.persist(entity);
			} else {
				String fields = fieldsToBeChecked.keySet().stream().collect(Collectors.joining(", "));
				throw new DuplicateEntryException("Duplicate entry for :" + fields);
			}
		});
	}

	public <T> void saveEntityWithDuplicateCheck(EntityManager entityManager, T entity,
			Map<String, Object> fieldsToBeChecked) {

		if (!isDuplicateEntry(entityManager, entity.getClass(), fieldsToBeChecked)) {
			entityManager.persist(entity);
		} else {
			String fields = fieldsToBeChecked.keySet().stream().collect(Collectors.joining(", "));
			throw new DuplicateEntryException("Duplicate entry for :" + fields);
		}

	}

	public <T> boolean isDuplicateEntry(EntityManager entityManager, Class<T> entityClass, Map<String, Object> fields) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<T> root = query.from(entityClass);

		query.select(cb.count(root));

		List<Predicate> predicates = new ArrayList<>();
		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			if (entry.getKey().contains(".")) {
				// Handle navigation through associations
				String[] parts = entry.getKey().split("\\.");
				Path<?> path = root;
				for (String part : parts) {
					path = path.get(part);
				}
				predicates.add(cb.equal(path, entry.getValue()));
			} else {
				// Handle direct attributes
				predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
			}
		}

		query.where(predicates.toArray(new Predicate[0]));

		Long count = entityManager.createQuery(query).getSingleResult();

		return count > 0;
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
