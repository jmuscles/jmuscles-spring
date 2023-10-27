/**
 * 
 */
package com.jmuscles.datasource;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
		initilize();
	}

	public DataSource get(String dskey) {
		return dataSourceHolder.get(dskey);
	}

	private void initilize() {
		logger.info("Datasources initialization start....");
		DataSourceCreator.create(databaseProperties, dataSourceOperatorRegistry, dataSourceHolder);
		// registerDataSourcesAsBean();
		logger.info("Datasources initialized");
	}

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

	/**
	 * @return the databaseProperties
	 */
	public DatabaseProperties getDatabaseProperties() {
		return databaseProperties;
	}

	/**
	 * @return the dataSourceOperatorRegistry
	 */
	public DataSourceOperatorRegistry getDataSourceOperatorRegistry() {
		return dataSourceOperatorRegistry;
	}

	/**
	 * @return the dataSourceHolder
	 */
	public DataSourceHolder getDataSourceHolder() {
		return dataSourceHolder;
	}

	/**
	 * @param databaseProperties the databaseProperties to set
	 */
	public void setDatabaseProperties(DatabaseProperties databaseProperties) {
		this.databaseProperties = databaseProperties;
	}

	public void replace(DataSourceGenerator dataSourceGenerator) {
		this.databaseProperties = dataSourceGenerator.getDatabaseProperties();
		this.dataSourceOperatorRegistry = dataSourceGenerator.getDataSourceOperatorRegistry();
		this.dataSourceHolder = dataSourceGenerator.getDataSourceHolder();
	}

}
