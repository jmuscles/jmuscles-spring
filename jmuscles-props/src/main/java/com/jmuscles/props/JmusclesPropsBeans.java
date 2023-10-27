/**
 * 
 */
package com.jmuscles.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.jmuscles.datasource.DataSourceEssentialBeans;
import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.datasource.jasypt.JasyptUtil;
import com.jmuscles.props.jpa.AppPropsRepository;
import com.jmuscles.props.service.ReadPropsFromDBService;
import com.jmuscles.props.util.JmusclesConfig;

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

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Bean("appPropsDBConfig")
	@ConfigurationProperties(prefix = "config-props-from-db")
	@ConditionalOnProperty(name = "enabled-db-config", havingValue = "true")
	public AppPropsDBConfig appPropsDBConfig() {
		return new AppPropsDBConfig();
	}

	@Bean("appPropsRepository")
	@ConditionalOnProperty(name = "enabled-db-config", havingValue = "true")
	public AppPropsRepository appPropsRepository(
			@Qualifier("dataSourceGenerator") DataSourceGenerator dataSourceGenerator,
			@Qualifier("appPropsDBConfig") AppPropsDBConfig appPropsDBConfig) {
		return new AppPropsRepository(environment.getProperty("spring.application.name"), dataSourceGenerator,
				appPropsDBConfig);
	}

	@Bean("readPropsFromDBService")
	@ConditionalOnProperty(name = "enabled-db-config", havingValue = "true")
	public ReadPropsFromDBService readPropsFromDBService(
			@Qualifier("appPropsRepository") AppPropsRepository appPropsRepository) {
		return new ReadPropsFromDBService(appPropsRepository);
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
			jmusclesConfig2 = readPropsFromDBService.getJmusclesConfig();
		} else {
			jmusclesConfig2 = jmusclesConfig;
		}
		return jmusclesConfig2;
	}

	@Bean("jmusclesDatasources")
	public DataSourceGenerator jmusclesDatasources(
			@Qualifier("dataSourceGenerator") DataSourceGenerator dataSourceGenerator,
			@Autowired(required = false) @Qualifier("readPropsFromDBService") ReadPropsFromDBService readPropsFromDBService) {
		DataSourceGenerator dataSourceGenerator2 = null;
		if (isEnabledDbConfig() && readPropsFromDBService != null) {
			dataSourceGenerator2 = new DataSourceGenerator(readPropsFromDBService.getJmusclesConfig().getDbProperties(),
					JasyptUtil.getJasyptDecryptors());
		} else {
			dataSourceGenerator2 = dataSourceGenerator;
		}
		return dataSourceGenerator2;
	}

	@Bean("refreshBeanProps")
	public RefreshBeanProps refreshBeanProps() {
		return new RefreshBeanProps(isEnabledDbConfig(), this.beanFactory, this.environment);
	}

}
