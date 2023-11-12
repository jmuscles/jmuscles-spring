package com.jmuscles.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
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
import com.jmuscles.props.jpa.entity.repository.PropReadRepository;
import com.jmuscles.props.jpa.entity.repository.PropWriteRepository;
import com.jmuscles.props.jpa.entity.repository.RepositorySetup;
import com.jmuscles.props.service.ReadPropsFromDBService;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * @author manish goel
 *
 */
@Import(DataSourceEssentialBeans.class)
@DependsOn("dataSourceGenerator")
public class JmusclesPropsBeans implements BeanFactoryAware, EnvironmentAware {

	public static final String PROP_ENABLED_DB_CONFIG = "enabled-db-config";

	private static final Logger logger = LoggerFactory.getLogger(JmusclesPropsBeans.class);

	private BeanFactory beanFactory;
	private Environment environment;

	/**
	 * @return the enabledDbConfig
	 */
	public boolean isEnabledDbConfig() {
		return environment.getProperty(PROP_ENABLED_DB_CONFIG, Boolean.class, false);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Bean("appPropsDBConfig")
	@ConfigurationProperties(prefix = "config-props-from-db")
	public AppPropsDBConfig appPropsDBConfig() {
		return new AppPropsDBConfig();
	}

	@Bean("appPropsRepositorySetup")
	public RepositorySetup appPropsRepositorySetup(
			@Qualifier("dataSourceProvider") DataSourceProvider dataSourceProvider,
			@Qualifier("appPropsDBConfig") AppPropsDBConfig appPropsDBConfig) {
		return new RepositorySetup(environment.getProperty("spring.application.name"), dataSourceProvider,
				appPropsDBConfig);
	}

	@Bean("appPropsReadRepository")
	public PropReadRepository appPropsReadRepository(
			@Qualifier("appPropsRepositorySetup") RepositorySetup appPropsRepositorySetup) {
		return new PropReadRepository(appPropsRepositorySetup);
	}

	@Bean("appPropsWriteRepository")
	public PropWriteRepository appPropsWriteRepository(
			@Qualifier("appPropsRepositorySetup") RepositorySetup appPropsRepositorySetup,
			@Qualifier("appPropsReadRepository") PropReadRepository appPropsReadRepository) {
		return new PropWriteRepository(appPropsRepositorySetup, appPropsReadRepository);
	}

	@Bean("readPropsFromDBService")
	public ReadPropsFromDBService readPropsFromDBService(
			@Qualifier("appPropsReadRepository") PropReadRepository appPropsReadRepository) {
		return new ReadPropsFromDBService(appPropsReadRepository);
	}

	@Bean("jmusclesConfigProps")
	@ConfigurationProperties(prefix = "jmuscles", ignoreInvalidFields = true, ignoreUnknownFields = true)
	public JmusclesConfig jmusclesConfig() {
		return new JmusclesConfig();
	}

	@Bean("jmusclesConfig")
	public JmusclesConfig jmusclesConfig(@Qualifier("jmusclesConfigProps") JmusclesConfig jmusclesConfig,
			@Autowired(required = false) @Qualifier("readPropsFromDBService") ReadPropsFromDBService readPropsFromDBService) {
		JmusclesConfig jmusclesConfig2 = null;
		if (isEnabledDbConfig() && readPropsFromDBService != null) {
			readPropsFromDBService.initialize();
			jmusclesConfig2 = readPropsFromDBService.getJmusclesConfig();
			createDbPropsDsg(jmusclesConfig2.getDbProperties());
		} else {
			jmusclesConfig2 = jmusclesConfig;
		}
		return jmusclesConfig2;
	}

	public DataSourceGenerator createDbPropsDsg(DatabaseProperties databaseProperties) {
		return new DataSourceGenerator(databaseProperties, JasyptUtil.getJasyptDecryptors(),
				DataSourceProvider.DSG_DB_PROPS,
				(DataSourceOperatorRegistry) SpringBeanUtil.getBean("dataSourceOperatorRegistry", this.beanFactory),
				(DataSourceProvider) SpringBeanUtil.getBean("dataSourceProvider", this.beanFactory));
	}

	@Bean("refreshBeanProps")
	public RefreshBeanProps refreshBeanProps() {
		return new RefreshBeanProps(this, this.beanFactory, this.environment);
	}

}
