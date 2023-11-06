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
import com.jmuscles.datasource.builder.JmusclesDataSource;
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
	private String identifier;

	private BeanFactory beanFactory;

	public DataSourceGenerator(DatabaseProperties databaseProperties, List<JasyptDecryptor> jasyptDecryptors,
			String identifier, DataSourceOperatorRegistry dataSourceOperatorRegistry,
			DataSourceProvider dataSourceProvider) {
		this.databaseProperties = databaseProperties;
		this.identifier = identifier;
		this.dataSourceHolder = new DataSourceHolder(identifier);

		if (jasyptDecryptors != null) {
			JasyptUtil.setJasyptDecryptors(jasyptDecryptors);
		}
		if (dataSourceOperatorRegistry == null) {
			dataSourceOperatorRegistry = new DataSourceOperatorRegistry();
		}
		this.dataSourceOperatorRegistry = dataSourceOperatorRegistry;
		initilize();
		if (dataSourceProvider != null) {
			dataSourceProvider.addDataSourceGenerator(this);
		}
	}

	public DataSourceGenerator(DatabaseProperties databaseProperties, List<JasyptDecryptor> jasyptDecryptors) {
		this(databaseProperties, jasyptDecryptors, "", null, null);
	}

	public DataSourceGenerator(DatabaseProperties databaseProperties) {
		this(databaseProperties, null);
	}

	public DataSource get(String dskey) {
		return getJmusclesDataSource(dskey).getDataSource();
	}

	public JmusclesDataSource getJmusclesDataSource(String dskey) {
		return this.dataSourceHolder.get(dskey);
	}

	private void initilize() {
		logger.info("DataSourceGenerator :" + identifier + " initialization start....");
		DataSourceCreator.create(databaseProperties, dataSourceOperatorRegistry, dataSourceHolder);
		// registerDataSourcesAsBean();
		logger.info("DataSourceGenerator :" + identifier + " initialized");
	}

	public void refresh() {
		logger.info("DataSourceGenerator :" + identifier + " refresh start....");
		DataSourceRefresher.refresh(databaseProperties, dataSourceOperatorRegistry, dataSourceHolder);
		logger.info("DataSourceGenerator :" + identifier + " refreshed");
	}

	private void registerDataSourcesAsBean() {
		Set<String> dataSourceNames = databaseProperties.getDataSources().keySet();
		ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
		if (dataSourceNames != null && !dataSourceNames.isEmpty()) {
			dataSourceNames.forEach(dataSourceName -> configurableBeanFactory.registerSingleton(dataSourceName,
					this.dataSourceHolder.get(dataSourceName)));
		}
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
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

}
