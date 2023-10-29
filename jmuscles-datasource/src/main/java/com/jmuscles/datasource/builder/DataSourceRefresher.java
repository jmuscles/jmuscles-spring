/**
 * 
 */
package com.jmuscles.datasource.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

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

	private static final Logger logger = LoggerFactory.getLogger(DataSourceRefresher.class);

	public static boolean refresh(DatabaseProperties databaseProperties,
			DataSourceOperatorRegistry dataSourceOperatorRegistry, DataSourceHolder dataSourceHolder) {
		List<String> newDataSourceNames = new ArrayList<>(databaseProperties.getDataSources().keySet());
		List<String> oldDataSourceNames = dataSourceHolder.getDataSourceKeys();

		if (newDataSourceNames != null && !newDataSourceNames.isEmpty()) {
			newDataSourceNames.forEach(dataSourceName -> refresh(dataSourceName, dataSourceHolder, databaseProperties,
					dataSourceOperatorRegistry));
		}

		// remove the data sources which are not present in new properties
		removeOldDataSources(oldDataSourceNames, newDataSourceNames, dataSourceOperatorRegistry, dataSourceHolder);

		return true;
	}

	private static boolean refresh(String dataSourceName, DataSourceHolder dataSourceHolder,
			DatabaseProperties databaseProperties, DataSourceOperatorRegistry dataSourceOperatorRegistry) {
		if (dataSourceHolder.get(dataSourceName) != null) {
			JmusclesDataSource jmusclesDataSource = dataSourceHolder.get(dataSourceName);
			DataSourceConfig dataSourceConfig = DataSourcePropertiesUtil.getDataSourceConfig(dataSourceName,
					databaseProperties);
			Properties newProperties = DataSourcePropertiesUtil.getResolvedProperties(dataSourceName, dataSourceConfig,
					databaseProperties);
			if (newProperties != null) {
				dataSourceOperatorRegistry.get(jmusclesDataSource.getType()).refresh(dataSourceName,
						jmusclesDataSource.getDataSource(), newProperties);
			} else {
				logger.error("dsgIdentifier : " + jmusclesDataSource.getDsgId()
						+ "Issue with the properties for dataSource : " + dataSourceName
						+ ". Datasource can not be refreshed.");
			}
		} else {
			DataSourceCreator.createNewDataSourceInHolder(dataSourceName, databaseProperties,
					dataSourceOperatorRegistry, dataSourceHolder);
		}

		return true;
	}

	private static void removeOldDataSources(List<String> oldDataSourceNames, List<String> newDataSourceNames,
			DataSourceOperatorRegistry dataSourceOperatorRegistry, DataSourceHolder dataSourceHolder) {
		List<String> dsToBeDeleted = oldDataSourceNames.stream().filter(old -> !newDataSourceNames.contains(old))
				.collect(Collectors.toList());
		if (dsToBeDeleted != null && !dsToBeDeleted.isEmpty()) {
			for (String ds : dsToBeDeleted) {
				JmusclesDataSource jmusclesDataSource = dataSourceHolder.remove(ds);
				if (jmusclesDataSource != null) {
					dataSourceOperatorRegistry.get(jmusclesDataSource.getType())
							.close(jmusclesDataSource.getDataSource());
				}
			}
		}
	}

}
