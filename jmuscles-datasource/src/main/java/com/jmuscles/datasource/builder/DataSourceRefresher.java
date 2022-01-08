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
public class DataSourceRefresher {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DataSourceRefresher.class);

	public static boolean refresh(DatabaseProperties databaseProperties,
			DataSourceOperatorRegistry dataSourceOperatorRegistry, DataSourceHolder dataSourceHolder) {
		Set<String> dataSourceNames = databaseProperties.getDataSources().keySet();
		if (dataSourceNames != null && !dataSourceNames.isEmpty()) {
			dataSourceNames.forEach(dataSourceName -> {
				if (dataSourceHolder.get(dataSourceName) != null) {
					refresh(dataSourceName, dataSourceHolder.get(dataSourceName), databaseProperties,
							dataSourceOperatorRegistry);
				} else {
					DataSourceCreator.createNewDataSourceInHolder(dataSourceName, databaseProperties,
							dataSourceOperatorRegistry, dataSourceHolder);
				}
			});
		}

		return true;
	}

	private static boolean refresh(String dataSourceName, DataSource dataSource, DatabaseProperties databaseProperties,
			DataSourceOperatorRegistry dataSourceOperatorRegistry) {
		DataSourceConfig dataSourceConfig = DataSourcePropertiesUtil.getDataSourceConfig(dataSourceName,
				databaseProperties);
		String dataSourceType = DataSourcePropertiesUtil.getDataSourceType(dataSourceConfig);
		Properties newProperties = DataSourcePropertiesUtil.getResolvedProperties(dataSourceName, dataSourceConfig,
				databaseProperties);
		if (newProperties != null) {
			dataSourceOperatorRegistry.get(dataSourceType).refresh(dataSourceName, dataSource, newProperties);
		} else {
			logger.error("Issue with the properties for dataSource : " + dataSourceName
					+ ". Datasource can not be refreshed.");
		}

		return true;
	}

}
