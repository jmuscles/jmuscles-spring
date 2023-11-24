package com.jmuscles.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.props.config.AppPropsDBConfig;
import com.jmuscles.props.config.AppPropsRestConfig;
import com.jmuscles.props.config.JmusclesConfig;
import com.jmuscles.props.util.RefreshBean;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * @author manish goel
 */
public class RefreshBeanProps implements RefreshBean {

	private static final Logger logger = LoggerFactory.getLogger(RefreshBeanProps.class);

	private JmusclesPropsBeans jmusclesPropsBeans;

	public RefreshBeanProps(JmusclesPropsBeans jmusclesPropsBeans) {
		super();
		this.jmusclesPropsBeans = jmusclesPropsBeans;
	}

	public void handleRefreshEvent() {
		logger.info("Refresh start...");
		BeanFactory beanFactory = this.jmusclesPropsBeans.getBeanFactory();

		// Retrieve all JmusclesConfigs
		JmusclesConfig jmusclesConfig = (JmusclesConfig) SpringBeanUtil.getBean("jmusclesConfig", beanFactory);
		JmusclesConfig jmusclesConfigProps = (JmusclesConfig) SpringBeanUtil.getBean("jmusclesConfigProps",
				beanFactory);
		JmusclesConfig jmusclesDbProps = (JmusclesConfig) SpringBeanUtil.getBean("jmusclesDbProps", beanFactory);

		// Retrieve all DataSourceGenerators
		DataSourceProvider dataSourceProvider = (DataSourceProvider) SpringBeanUtil.getBean("dataSourceProvider",
				beanFactory);
		DataSourceGenerator dsgConfigProps = dataSourceProvider.getDsgConfigProps();
		DataSourceGenerator dsgDbProps = dataSourceProvider.getDsgDbProps();

		// Refresh config props DataSourceGenerator
		if (dsgConfigProps != null) {
			dsgConfigProps.refresh();
		}
		handleProps(beanFactory, dsgDbProps, jmusclesDbProps, jmusclesConfigProps, jmusclesConfig);
		logger.info(" ...Refresh end");
	}

	private void handleProps(BeanFactory beanFactory, DataSourceGenerator dsgDbProps, JmusclesConfig jmusclesDbProps,
			JmusclesConfig jmusclesConfigProps, JmusclesConfig jmusclesConfig) {
		// check if remote properties are enabled
		AppPropsRestConfig appPropsRestConfig = (AppPropsRestConfig) SpringBeanUtil.getBean("appPropsRestConfig",
				beanFactory);
		AppPropsDBConfig appPropsDBConfig = (AppPropsDBConfig) SpringBeanUtil.getBean("appPropsDBConfig", beanFactory);

		boolean isRestConfigEnabled = jmusclesPropsBeans.isRestConfigEnabled(appPropsRestConfig);
		boolean isDbConfigEnabled = jmusclesPropsBeans.isDbConfigEnabled(appPropsDBConfig);
		if (isRestConfigEnabled || isDbConfigEnabled) {
			handleRemoteProps(appPropsRestConfig, appPropsDBConfig, isRestConfigEnabled, isDbConfigEnabled,
					jmusclesDbProps, jmusclesConfig, dsgDbProps);
		} else {
			if (jmusclesDbProps != null) {
				jmusclesDbProps.clear();
				if (dsgDbProps != null) {
					dsgDbProps.refresh();
				}
				jmusclesConfig.replaceValues(jmusclesConfigProps);
			}
		}
	}

	private void handleRemoteProps(AppPropsRestConfig appPropsRestConfig, AppPropsDBConfig appPropsDBConfig,
			boolean isRestConfigEnabled, boolean isDbConfigEnabled, JmusclesConfig jmusclesDbProps,
			JmusclesConfig jmusclesConfig, DataSourceGenerator dsgDbProps) {
		JmusclesConfig jmusclesDbProps_new = jmusclesPropsBeans.getRemoteProps(appPropsRestConfig, appPropsDBConfig,
				isRestConfigEnabled, isDbConfigEnabled);
		if (jmusclesDbProps_new != null) {
			if (jmusclesDbProps == null) {
				SpringBeanUtil.registerSingletonBean("jmusclesDbProps", jmusclesDbProps,
						jmusclesPropsBeans.getBeanFactory());
			} else {
				jmusclesDbProps.replaceValues(jmusclesDbProps_new);
			}
			jmusclesConfig.replaceValues(jmusclesDbProps_new);
			if (dsgDbProps == null) {
				jmusclesPropsBeans.createDbPropsDsg(jmusclesDbProps_new.getDbProperties());
			} else {
				dsgDbProps.refresh();
			}
		}
	}

}
