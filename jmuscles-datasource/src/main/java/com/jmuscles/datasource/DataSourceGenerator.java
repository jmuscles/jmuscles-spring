/**
 * 
 */
package com.jmuscles.datasource;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.jmuscles.datasource.builder.DataSourceCreator;
import com.jmuscles.datasource.builder.DataSourceHolder;
import com.jmuscles.datasource.builder.DataSourceRefresher;
import com.jmuscles.datasource.jasypt.JasyptDecryptor;
import com.jmuscles.datasource.jasypt.JasyptUtil;
import com.jmuscles.datasource.operator.DataSourceOperatorRegistry;
import com.jmuscles.datasource.properties.DatabaseProperties;

/**
 * @author manish goel
 *
 */
public class DataSourceGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceGenerator.class);

	private DatabaseProperties databaseProperties;
	private DataSourceOperatorRegistry dataSourceOperatorRegistry;
	private DataSourceHolder dataSourceHolder;

	private BeanFactory beanFactory;

	public DataSourceGenerator(DatabaseProperties databaseProperties, List<JasyptDecryptor> jasyptDecryptors) {
		this.databaseProperties = databaseProperties;
		this.dataSourceOperatorRegistry = new DataSourceOperatorRegistry();
		this.dataSourceHolder = new DataSourceHolder();
		JasyptUtil.setJasyptDecryptors(jasyptDecryptors);
	}

	public DataSource get(String dskey) {
		return dataSourceHolder.get(dskey);
	}

	@PostConstruct
	public void initilize() {
		logger.info("Datasources initialization start....");
		DataSourceCreator.create(databaseProperties, dataSourceOperatorRegistry, dataSourceHolder);
		// registerDataSourcesAsBean();
		logger.info("Datasources initialized");
	}

	@EventListener(RefreshScopeRefreshedEvent.class)
	public void refresh() {
		logger.info("Datasources refresh start....");
		DataSourceRefresher.refresh(databaseProperties, dataSourceOperatorRegistry, dataSourceHolder);
		logger.info("Datasources refreshed");
	}

	private void registerDataSourcesAsBean() {
		Set<String> dataSourceNames = databaseProperties.getDataSources().keySet();
		ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
		if (dataSourceNames != null && !dataSourceNames.isEmpty()) {
			dataSourceNames.forEach(dataSourceName -> configurableBeanFactory.registerSingleton(dataSourceName,
					dataSourceHolder.get(dataSourceName)));
		}
	}

}
