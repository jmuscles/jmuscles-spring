/**
 * 
 */
package com.jmuscles.props.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.props.AppPropsDBConfig;

/**
 * @author manish goel
 *
 */
public class AppPropsRepository {

	private static final Logger logger = LoggerFactory.getLogger(AppPropsRepository.class);

	public static final String PACKAGES_TO_SCAN = "com.jmuscles.props.jpa";
	public static final String PERSISTENCE_UNIT_NAME = "appPropsResourceUnit";

	private EntityManagerFactory emf;
	public String applicationName;
	private DataSourceGenerator dataSourceGenerator;
	private AppPropsDBConfig appPropsDBConfig;

	public AppPropsRepository(String applicationName, DataSourceGenerator dataSourceGenerator,
			AppPropsDBConfig appPropsDBConfig) {
		super();
		this.applicationName = applicationName;
		this.dataSourceGenerator = dataSourceGenerator;
		this.appPropsDBConfig = appPropsDBConfig;
	}

	@PostConstruct
	private void initialize() {
		setupEntityManagerFactory();
	}

	public void refresh() {
		setupEntityManagerFactory();
	}

	public void save(AppPropsEntity appPropsEntity) {
		executeInTransaction(em -> em.persist(appPropsEntity));
	}

	public void merge(AppPropsEntity appPropsEntity) {
		executeInTransaction(em -> em.merge(appPropsEntity));
	}

	public void saveAll(List<AppPropsEntity> appPropsEntities) {
		executeInTransaction(em -> {
			for (AppPropsEntity appPropEntity : appPropsEntities) {
				em.persist(appPropEntity);
			}
		});
	}

	public AppPropsEntity findByKeyPath(String[] keyPath) {
		AppPropsEntity currentEntity = null;

		for (String key : keyPath) {
			currentEntity = findChildByKey(currentEntity, key);
			if (currentEntity == null) {
				// Entity with the given key doesn't exist, so break the loop
				break;
			}
		}

		return currentEntity;
	}

	private AppPropsEntity findChildByKey(AppPropsEntity parent, String key) {
		List<AppPropsEntity> result = new ArrayList<>();

		if (parent == null) {
			// If parent is null, search for the root entity
			executeInTransaction(em -> {
				TypedQuery<AppPropsEntity> query = em.createQuery(
						"SELECT e FROM AppPropsEntity e WHERE e.prop_key = :key AND e.parent IS NULL",
						AppPropsEntity.class);
				query.setParameter("key", key);
				result.addAll(query.getResultList());
			});
			return result.isEmpty() ? null : result.get(0);
		} else {
			// Search for a child entity
			executeInTransaction(em -> {
				TypedQuery<AppPropsEntity> query = em.createQuery(
						"SELECT e FROM AppPropsEntity e WHERE e.prop_key = :key AND e.parent = :parent",
						AppPropsEntity.class);
				query.setParameter("key", key);
				query.setParameter("parent", parent);
				result.addAll(query.getResultList());
			});
			return result.isEmpty() ? null : result.get(0);
		}
	}

	public List<AppPropsEntity> findAllByParent(Long parentId) {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em
					.createQuery("SELECT a FROM AppPropsEntity a WHERE a.parent.id = :parentId", AppPropsEntity.class);
			query.setParameter("parentId", parentId);
			result.addAll(query.getResultList());
		});
		return result;
	}

	public List<AppPropsEntity> findAllByParentIsNull() {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em.createQuery("SELECT p FROM AppPropsEntity p WHERE p.parent IS NULL",
					AppPropsEntity.class);
			result.addAll(query.getResultList());
		});
		return result;
	}

	public List<AppPropsEntity> findAll() {
		List<AppPropsEntity> result = new ArrayList<>();
		executeInTransaction(em -> {
			TypedQuery<AppPropsEntity> query = em.createQuery("SELECT a FROM AppPropsEntity a", AppPropsEntity.class);
			result.addAll(query.getResultList());
		});
		return result;
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
		if (this.dataSourceGenerator == null) {
			logger.error("dataSourceGenerator is null hence AppPropsResource can not be initialized.");
		} else if (this.appPropsDBConfig == null || this.appPropsDBConfig.getDataSourceKey() == null) {
			logger.error("'config-props-from-db.datasourceKey' is not configured "
					+ "hence AppPropsResource can not be initialized.");
		} else {
			dataSource = this.dataSourceGenerator.get(this.appPropsDBConfig.getDataSourceKey());
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
