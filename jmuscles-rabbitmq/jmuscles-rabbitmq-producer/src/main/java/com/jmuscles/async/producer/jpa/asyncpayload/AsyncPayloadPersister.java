/**
 * 
 */
package com.jmuscles.async.producer.jpa.asyncpayload;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;

import com.jmuscles.async.producer.constant.AsyncMessageConstants;
import com.jmuscles.async.producer.constant.MessageStatus;
import com.jmuscles.async.producer.properties.ProducerConfigProperties;
import com.jmuscles.datasource.DataSourceGenerator;

/**
 * @author manish goel
 *
 */
public class AsyncPayloadPersister {

	private static final Logger logger = LoggerFactory.getLogger(AsyncPayloadPersister.class);

	public static final int PAYLOAD_TYPE_MAX_LENGTH = 25;
	public static final int CREATED_BY_MAX_LENGTH = 50;
	public static final int MODIFIED_BY_MAX_LENGTH = 50;
	public static final int PAYLOAD_PROPS_KEY_MAX_LENGTH = 500;
	public static final int PAYLOAD_PROPS_VALUE_MAX_LENGTH = 500;

	public String applicationName;
	private DataSourceGenerator dataSourceGenerator;
	ProducerConfigProperties producerConfigProperties;

	public AsyncPayloadEntity persist(byte[] payload, Map<String, String> payloadProps) {
		initialize();
		if (this.emf == null) {
			logger.error("Database entity manager is not setup, hence payload can not be saved.... "
					+ "Please try setting up the async database and entities.");
			throw new RuntimeException("Async DB is not setup/ Entity Manager factory is null.");
		}
		AsyncPayloadEntity asyncPayloadEntity = buildAsyncPayloadEntity(payload, payloadProps);
		asyncPayloadEntity.setProps(buildAsyncPayloadProps(asyncPayloadEntity, payloadProps));
		return persist(asyncPayloadEntity);
	}

	public AsyncPayloadPersister(String applicationName, DataSourceGenerator dataSourceGenerator,
			ProducerConfigProperties producerConfigProperties) {
		super();
		this.applicationName = applicationName;
		this.dataSourceGenerator = dataSourceGenerator;
		this.producerConfigProperties = producerConfigProperties;
	}

	private EntityManagerFactory emf;

	@PostConstruct
	private void initialize() {
		setupEntityManagerFactory();
	}

	public void refresh() {
		setupEntityManagerFactory();
	}

	private synchronized void setupEntityManagerFactory() {
		DataSource dataSource = getProducerDataSource();
		if (this.emf == null && dataSource != null) {
			final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
			em.setDataSource(dataSource);
			em.setPackagesToScan("com.jmuscles.async.producer.jpa.asyncpayload");
			em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			em.setJpaProperties(jpaProperties());
			em.setPersistenceUnitName("asyncPayloadPersistenceUnit");
			em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
			em.afterPropertiesSet();
			this.emf = em.getObject();
		}
	}

	private DataSource getProducerDataSource() {
		DataSource dataSource = null;
		if (this.dataSourceGenerator == null) {
			logger.error("dataSourceGenerator is null hence AsyncPayloadPersister can not be initialized.");
		} else if (this.producerConfigProperties == null || this.producerConfigProperties.getDatabase() == null
				|| this.producerConfigProperties.getDatabase().getDataSourceKey() == null) {
			logger.error("'async-producer-config.database.datasourceKey' is not configured "
					+ "hence AsyncPayloadPersister can not be initialized.");
		} else {
			dataSource = this.dataSourceGenerator.get(this.producerConfigProperties.getDatabase().getDataSourceKey());
		}
		return dataSource;
	}

	private Properties jpaProperties() {
		final Properties properties = new Properties();
		if (producerConfigProperties != null && producerConfigProperties.getDatabase() != null
				&& producerConfigProperties.getDatabase().getJpaProperties() != null) {
			properties.putAll(producerConfigProperties.getDatabase().getJpaProperties());
		}
		return properties;
	}

	private AsyncPayloadEntity persist(AsyncPayloadEntity asyncPayloadEntity) {
		EntityManager em = this.emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(asyncPayloadEntity);
		em.getTransaction().commit();

		return asyncPayloadEntity;
	}

	private AsyncPayloadEntity buildAsyncPayloadEntity(byte[] payload, Map<String, String> payloadProps) {
		AsyncPayloadEntity asyncPayloadEntity = new AsyncPayloadEntity();
		asyncPayloadEntity.setPayload(payload);
		String payLoadType = payloadProps.remove(AsyncMessageConstants.PAYLOAD_TYPE);
		if (StringUtils.hasText(payLoadType)) {
			asyncPayloadEntity.setPayloadType(
					payLoadType.length() > PAYLOAD_TYPE_MAX_LENGTH ? payLoadType.substring(0, PAYLOAD_TYPE_MAX_LENGTH)
							: payLoadType);
		}
		asyncPayloadEntity.setExecAttempt(payloadProps.remove(AsyncMessageConstants.PROCESS_ATTEMPTS));

		String createdBy = payloadProps.remove(AsyncMessageConstants.CREATED_BY);
		createdBy = StringUtils.hasText(createdBy) ? createdBy : applicationName;
		if (StringUtils.hasText(createdBy)) {
			asyncPayloadEntity.setCreatedBy(
					createdBy.length() > CREATED_BY_MAX_LENGTH ? createdBy.substring(0, CREATED_BY_MAX_LENGTH)
							: createdBy);
		}

		String status = payloadProps.remove(AsyncMessageConstants.MESSAGE_STATUS);
		asyncPayloadEntity.setStatus(StringUtils.hasText(status) ? status : String.valueOf(MessageStatus.READY));
		asyncPayloadEntity.setProcessingLock("N");

		return asyncPayloadEntity;
	}

	private List<AsyncPayloadPropEntity> buildAsyncPayloadProps(AsyncPayloadEntity asyncPayloadEntity,
			Map<String, String> payloadProps) {
		List<AsyncPayloadPropEntity> asyncPayloadPropsEntityList = null;
		if (payloadProps != null) {
			asyncPayloadPropsEntityList = payloadProps.entrySet().stream().map(prop -> {
				AsyncPayloadPropEntity asyncPayloadPropEntity = new AsyncPayloadPropEntity();
				String key = prop.getKey();
				String value = prop.getValue();
				if (checkNonNull(key) && checkNonNull(value)) {
					asyncPayloadPropEntity.setKey(
							key.length() > PAYLOAD_PROPS_KEY_MAX_LENGTH ? key.substring(0, PAYLOAD_PROPS_KEY_MAX_LENGTH)
									: key);
					asyncPayloadPropEntity.setValue(value.length() > PAYLOAD_PROPS_VALUE_MAX_LENGTH
							? key.substring(0, PAYLOAD_PROPS_VALUE_MAX_LENGTH)
							: value);
					asyncPayloadPropEntity.setCreatedBy(asyncPayloadEntity.getCreatedBy());
				}
				return asyncPayloadPropEntity;
			}).collect(Collectors.toList());
		}
		return asyncPayloadPropsEntityList;
	}

	private boolean checkNonNull(String str) {
		return StringUtils.hasText(str) && !"NULL".equalsIgnoreCase(str);
	}

}
