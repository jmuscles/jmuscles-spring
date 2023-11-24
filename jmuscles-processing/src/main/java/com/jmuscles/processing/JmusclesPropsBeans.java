package com.jmuscles.processing;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.datasource.jasypt.JasyptUtil;
import com.jmuscles.datasource.operator.DataSourceOperatorRegistry;
import com.jmuscles.datasource.properties.DatabaseProperties;
import com.jmuscles.props.config.AppPropsDBConfig;
import com.jmuscles.props.config.AppPropsRestConfig;
import com.jmuscles.props.config.JmusclesConfig;
import com.jmuscles.props.util.RestUtil;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * @author manish goel
 *
 */
public class JmusclesPropsBeans implements BeanFactoryAware {

	private static final Logger logger = LoggerFactory.getLogger(JmusclesPropsBeans.class);

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	@Bean("appPropsRestConfig")
	@ConfigurationProperties(value = "config-props-from-rest", ignoreInvalidFields = true, ignoreUnknownFields = true)
	public AppPropsRestConfig appPropsRestConfig() {
		return new AppPropsRestConfig();
	}

	@Bean("jmusclesConfigProps")
	@ConfigurationProperties(prefix = "jmuscles", ignoreInvalidFields = true, ignoreUnknownFields = true)
	public JmusclesConfig jmusclesConfig() {
		return new JmusclesConfig();
	}

	@Bean("jmusclesDbProps")
	public JmusclesConfig jmusclesConfig(
			@Autowired(required = false) @Qualifier("appPropsRestConfig") AppPropsRestConfig appPropsRestConfig) {
		JmusclesConfig jmusclesConfig = getRemoteProps(appPropsRestConfig);
		if (jmusclesConfig != null && jmusclesConfig.getDbProperties() != null) {
			this.createDbPropsDsg(jmusclesConfig.getDbProperties());
		}
		return jmusclesConfig;
	}

	@Bean("jmusclesConfig")
	public JmusclesConfig jmusclesConfig(
			@Autowired(required = false) @Qualifier("jmusclesDbProps") JmusclesConfig jmusclesDbProps,
			@Autowired(required = false) @Qualifier("jmusclesConfigProps") JmusclesConfig jmusclesConfigProps) {
		if (jmusclesDbProps != null) {
			return jmusclesDbProps;
		} else {
			return jmusclesConfigProps;
		}
	}

	@Bean("refreshBeanProps")
	public RefreshBeanProps refreshBeanProps() {
		return new RefreshBeanProps(this);
	}

	public JmusclesConfig getRemoteProps(AppPropsRestConfig appPropsRestConfig) {
		return getRemoteProps(appPropsRestConfig, null, isRestConfigEnabled(appPropsRestConfig), true);
	}

	public JmusclesConfig getRemoteProps(AppPropsRestConfig appPropsRestConfig, AppPropsDBConfig appPropsDBConfig,
			boolean isRestConfigEnabled, boolean isDbConfigEnabled) {
		JmusclesConfig jmusclesConfig = null;
		if (isRestConfigEnabled) {
			try {
				jmusclesConfig = getJmusclesConfigByRest(appPropsRestConfig);
			} catch (Exception e) {
				logger.error(
						"Error while making the rest call to get properties. Please check the url and make sure rest is up and accessible.",
						e);
				logger.error(e.getStackTrace().toString());
			}
		}
		if (jmusclesConfig == null && isDbConfigEnabled) {
			if (appPropsDBConfig != null) {
				jmusclesConfig = getJmusclesConfigFromDB(appPropsDBConfig);
			} else {
				jmusclesConfig = getJmusclesConfigFromDB();
			}
		}
		return jmusclesConfig;
	}

	public JmusclesConfig getJmusclesConfigByRest(AppPropsRestConfig restConfig) {
		RestTemplate restTemplate = RestUtil.createRestTemplateWithTimeouts(restConfig.getConnectionTimeout(),
				restConfig.getReadTimeout());
		ResponseEntity<JmusclesConfig> responseEntity = RestUtil.execute(restConfig.getUrl(), restConfig.getHeaders(),
				StringUtils.hasText(restConfig.getMethod()) ? HttpMethod.valueOf(restConfig.getMethod())
						: HttpMethod.GET,
				restConfig.getBody(), JmusclesConfig.class, restTemplate);
		return responseEntity.getBody();
	}

	public JmusclesConfig getJmusclesConfigFromDB() {
		Object jmusclesDbPropsBeans = SpringBeanUtil.getBean("jmusclesDbPropsBeans", this.beanFactory);
		// code to make sure JmusclesPropsBeans is loaded
		if (jmusclesDbPropsBeans == null) {
			try {
				Class<?> JmusclesDbPropsBeansClass = Class.forName("com.jmuscles.dbprops.JmusclesDbPropsBeans", true,
						JmusclesPropsBeans.class.getClassLoader());
				if (JmusclesDbPropsBeansClass != null) {
					JmusclesDbPropsBeansClass.newInstance();
					jmusclesDbPropsBeans = SpringBeanUtil.getBean("jmusclesDbPropsBeans", this.beanFactory);
				}
			} catch (Exception e) {
				logger.error(e.getStackTrace().toString());
			}
		}
		AppPropsDBConfig appPropsDBConfig = (AppPropsDBConfig) SpringBeanUtil.getBean("appPropsDBConfig",
				this.beanFactory);
		if (appPropsDBConfig != null) {
			return getJmusclesConfigFromDB(appPropsDBConfig);
		} else {
			logger.error(
					"Looks like jmuscles-db-props jar is not present. Hence properties can not be retrieved from database");
		}
		return null;
	}

	public JmusclesConfig getJmusclesConfigFromDB(AppPropsDBConfig appPropsDBConfig) {
		JmusclesConfig jmusclesConfig = null;
		try {
			if (isDbConfigEnabled(appPropsDBConfig)) {
				Object readPropsFromDBService = SpringBeanUtil.getBean("readPropsFromDBService", this.beanFactory);
				Class<?> serviceClass = readPropsFromDBService.getClass();
				// Get the Method object for the getProperties method
				Method getJmusclesConfigMethod = serviceClass.getDeclaredMethod("getJmusclesConfig",
						AppPropsDBConfig.class);
				// Set the method accessible if it is not public
				getJmusclesConfigMethod.setAccessible(true);
				// Call the getProperties method on the service instance
				jmusclesConfig = (JmusclesConfig) getJmusclesConfigMethod.invoke(readPropsFromDBService,
						appPropsDBConfig);
			}
		} catch (Exception e) {
			logger.error("Error while retreiving the jmusclesConfig from readPropsFromDBService bean ", e);
			logger.error(e.getStackTrace().toString());
		}
		return jmusclesConfig;
	}

	public DataSourceGenerator createDbPropsDsg(DatabaseProperties databaseProperties) {
		return new DataSourceGenerator(databaseProperties, JasyptUtil.getJasyptDecryptors(),
				DataSourceProvider.DSG_DB_PROPS,
				(DataSourceOperatorRegistry) SpringBeanUtil.getBean("dataSourceOperatorRegistry", this.beanFactory),
				(DataSourceProvider) SpringBeanUtil.getBean("dataSourceProvider", this.beanFactory));
	}

	public boolean isRestConfigEnabled(AppPropsRestConfig appPropsRestConfig) {
		return appPropsRestConfig != null && StringUtils.hasText(appPropsRestConfig.getUrl());
	}

	public boolean isDbConfigEnabled(AppPropsDBConfig appPropsDBConfig) {
		boolean returnedFlag = true;
		if (appPropsDBConfig != null) {
			Map<String, String> selectionKeys = appPropsDBConfig.getSelectionKeys();
			String appGroupName = selectionKeys.get("appGroupName");
			String appName = selectionKeys.get("appName");
			String env = selectionKeys.get("env");
			if (StringUtils.hasText(appName) && StringUtils.hasText(appGroupName) && StringUtils.hasText(env)) {
				returnedFlag = true;
			}
		}
		return returnedFlag;
	}

}
