/**
 * @author manish goel
 *
 */
package com.jmuscles.props;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.props.service.ReadPropsFromDBService;
import com.jmuscles.props.util.RefreshBean;

/**
 * 
 */
public class RefreshBeanProps implements RefreshBean {

	private BeanFactory beanFactory;
	private Environment environment;
	boolean enabledDbConfig;

	public RefreshBeanProps(boolean enabledDbConfig, BeanFactory beanFactory, Environment environment) {
		super();
		this.enabledDbConfig = enabledDbConfig;
		this.beanFactory = beanFactory;
		this.environment = environment;
	}

	public void handleRefreshEvent() {
		// boolean enabledDbConfig =
		// environment.getProperty(Constants.PROP_ENABLED_DB_CONFIG, Boolean.class,
		// false);

		if (enabledDbConfig) {
			ReadPropsFromDBService readPropsFromDBService = (ReadPropsFromDBService) beanFactory
					.getBean("readPropsFromDBService");
			if (readPropsFromDBService != null) {
				readPropsFromDBService.refresh();
				DataSourceGenerator jmusclesDatasources = (DataSourceGenerator) beanFactory
						.getBean("jmusclesDatasources");
				if (jmusclesDatasources != null) {
					jmusclesDatasources
							.setDatabaseProperties(readPropsFromDBService.getJmusclesConfig().getDbProperties());
					jmusclesDatasources.refresh();
				}
			}
		} else {
			DataSourceGenerator dataSourceGenerator = (DataSourceGenerator) beanFactory.getBean("dataSourceGenerator");
			// DataSourceGenerator dsGeneratorToCleanup = (DataSourceGenerator)
			// beanFactory.getBean("jmusclesDatasources");
			if (dataSourceGenerator != null) {
				dataSourceGenerator.refresh();
				// SpringBeanUtil.registerSingletonBean("jmusclesDatasources",
				// dataSourceGenerator, this.beanFactory);
			}
		}
	}

}
