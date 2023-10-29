/**
 * 
 */
package com.jmuscles.datasource.builder;

import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.datasource.operator.DataSourceOperatorRegistry;
import com.jmuscles.datasource.properties.DataSourceConfig;
import com.jmuscles.datasource.properties.DataSourcePropertiesUtil;
import com.jmuscles.datasource.properties.DatabaseProperties;

/**
 * @author manish goel
 *
 */
public class DataSourceCreator {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceCreator.class);

	public static void create(DatabaseProperties databaseProperties,
			DataSourceOperatorRegistry dataSourceOperatorRegistry, DataSourceHolder dataSourceHolder) {
		Set<String> dataSourceNames = databaseProperties.getDataSources().keySet();
		if (dataSourceNames != null && !dataSourceNames.isEmpty()) {
			dataSourceNames.forEach(name -> createNewDataSourceInHolder(name, databaseProperties,
					dataSourceOperatorRegistry, dataSourceHolder));
		}
	}

	public static void createNewDataSourceInHolder(String dataSourceName, DatabaseProperties databaseProperties,
			DataSourceOperatorRegistry dataSourceOperatorRegistry, DataSourceHolder dataSourceHolder) {
		JmusclesDataSource jmusclesDataSource = create(dataSourceName, databaseProperties, dataSourceOperatorRegistry,
				dataSourceHolder.getIdentifier());
		if (jmusclesDataSource != null) {
			dataSourceHolder.add(dataSourceName, jmusclesDataSource);
		}
	}

	private static JmusclesDataSource create(String dataSourceName, DatabaseProperties databaseProperties,
			DataSourceOperatorRegistry dataSourceOperatorRegistry, String dsgIdentifier) {
		DataSourceConfig dataSourceConfig = DataSourcePropertiesUtil.getDataSourceConfig(dataSourceName,
				databaseProperties);
		String dataSourceType = DataSourcePropertiesUtil.getDataSourceType(dataSourceConfig);
		Properties newProperties = DataSourcePropertiesUtil.getResolvedProperties(dataSourceName, dataSourceConfig,
				databaseProperties);
		DataSource dataSource = null;
		if (newProperties != null) {
			dataSource = dataSourceOperatorRegistry.get(dataSourceType).create(dataSourceName, newProperties);
		} else {
			logger.error("dsgIdentifier : " + dsgIdentifier + " Issue with the properties for dataSource : "
					+ dataSourceName + ". Datasource can not be created.");
		}

		return dataSource != null ? JmusclesDataSource.of(dataSource).addType(dataSourceType).addDsgId(dsgIdentifier)
				: null;
	}

}
