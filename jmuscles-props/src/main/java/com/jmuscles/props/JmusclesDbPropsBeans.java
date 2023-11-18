package com.jmuscles.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.jmuscles.datasource.DataSourceEssentialBeans;
import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.datasource.jasypt.JasyptUtil;
import com.jmuscles.datasource.operator.DataSourceOperatorRegistry;
import com.jmuscles.datasource.properties.DatabaseProperties;
import com.jmuscles.props.jpa.entity.repository.AppGroupRepository;
import com.jmuscles.props.jpa.entity.repository.AppProvisionRepository;
import com.jmuscles.props.jpa.entity.repository.AppRepository;
import com.jmuscles.props.jpa.entity.repository.PropReadRepository;
import com.jmuscles.props.jpa.entity.repository.PropVersionCrudRepository;
import com.jmuscles.props.jpa.entity.repository.PropWriteRepository;
import com.jmuscles.props.jpa.entity.repository.RepositorySetup;
import com.jmuscles.props.jpa.entity.repository.TenantCrudRepository;
import com.jmuscles.props.service.ReadPropsFromDBService;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * @author manish goel
 *
 */
@Import(DataSourceEssentialBeans.class)
@DependsOn("dataSourceGenerator")
public class JmusclesDbPropsBeans {

	public static final String PROP_ENABLED_DB_CONFIG = "enabled-db-config";

	private static final Logger logger = LoggerFactory.getLogger(JmusclesDbPropsBeans.class);

	private BeanFactory beanFactory;
	private Environment environment;

	public JmusclesDbPropsBeans(BeanFactory beanFactory, Environment environment) {
		super();
		this.beanFactory = beanFactory;
		this.environment = environment;
	}

	/**
	 * @return the enabledDbConfig
	 */
	public boolean isEnabledDbConfig() {
		return environment.getProperty(PROP_ENABLED_DB_CONFIG, Boolean.class, false);
	}

	public BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	@Bean("appPropsDBConfig")
	@ConfigurationProperties(prefix = "config-props-from-db")
	public AppPropsDBConfig appPropsDBConfig() {
		return new AppPropsDBConfig();
	}

	@Bean("repositorySetup")
	public RepositorySetup repositorySetup(@Qualifier("dataSourceProvider") DataSourceProvider dataSourceProvider,
			@Qualifier("appPropsDBConfig") AppPropsDBConfig appPropsDBConfig) {
		return new RepositorySetup(environment.getProperty("spring.application.name"), dataSourceProvider,
				appPropsDBConfig);
	}

	@Bean("tenantCrudRepository")
	public TenantCrudRepository tenantCrudRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new TenantCrudRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("propVersionCrudRepository")
	public PropVersionCrudRepository propVersionCrudRepository(
			@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new PropVersionCrudRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("propReadRepository")
	public PropReadRepository propReadRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup,
			@Qualifier("propVersionCrudRepository") PropVersionCrudRepository propVersionCrudRepository) {
		return new PropReadRepository(repositorySetup, propVersionCrudRepository);
	}

	@Bean("propWriteRepository")
	public PropWriteRepository propWriteRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup,
			@Qualifier("propVersionCrudRepository") PropVersionCrudRepository propVersionCrudRepository,
			@Qualifier("propReadRepository") PropReadRepository propReadRepository) {
		return new PropWriteRepository(environment.getProperty("spring.application.name"), repositorySetup,
				propVersionCrudRepository, propReadRepository);

	}

	@Bean("appRepository")
	public AppRepository appRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new AppRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("appGroupRepository")
	public AppGroupRepository appGroupRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new AppGroupRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("appProvisionRepository")
	public AppProvisionRepository appProvisionRepository(
			@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new AppProvisionRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("readPropsFromDBService")
	public ReadPropsFromDBService readPropsFromDBService(
			@Qualifier("appProvisionRepository") AppProvisionRepository appProvisionRepository,
			@Qualifier("propReadRepository") PropReadRepository propReadRepository,
			@Qualifier("appPropsDBConfig") AppPropsDBConfig appPropsDBConfig) {
		return new ReadPropsFromDBService(appProvisionRepository, propReadRepository, appPropsDBConfig);
	}

	@Bean("jmusclesConfigProps")
	@ConfigurationProperties(prefix = "jmuscles", ignoreInvalidFields = true, ignoreUnknownFields = true)
	public JmusclesConfig jmusclesConfig() {
		return new JmusclesConfig();
	}

	@Bean("jmusclesConfig")
	public JmusclesConfig jmusclesConfig(@Qualifier("jmusclesConfigProps") JmusclesConfig jmusclesConfig,
			@Autowired(required = false) @Qualifier("readPropsFromDBService") ReadPropsFromDBService readPropsFromDBService) {
		JmusclesConfig localJmusclesConfig = null;
		if (isEnabledDbConfig() && readPropsFromDBService != null) {
			localJmusclesConfig = readPropsFromDBService.getLatestProperties();
			createDbPropsDsg(localJmusclesConfig.getDbProperties());
		} else {
			localJmusclesConfig = jmusclesConfig;
		}
		return localJmusclesConfig;
	}

	public DataSourceGenerator createDbPropsDsg(DatabaseProperties databaseProperties) {
		return new DataSourceGenerator(databaseProperties, JasyptUtil.getJasyptDecryptors(),
				DataSourceProvider.DSG_DB_PROPS,
				(DataSourceOperatorRegistry) SpringBeanUtil.getBean("dataSourceOperatorRegistry", this.beanFactory),
				(DataSourceProvider) SpringBeanUtil.getBean("dataSourceProvider", this.beanFactory));
	}

}