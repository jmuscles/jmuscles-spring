/**
 * 
 */
package com.jmuscles.datasource.properties;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.jmuscles.datasource.jasypt.JasyptUtil;
import com.jmuscles.datasource.operator.DataSourceOperator;

/**
 * @author manish goel
 *
 */
public class DataSourcePropertiesUtil {

	private static Logger logger = LoggerFactory.getLogger(DataSourcePropertiesUtil.class);

	public static DataSourceConfig getDataSourceConfig(String dataSourceName, DatabaseProperties databaseProperties) {
		return databaseProperties.getDataSources().get(dataSourceName);
	}

	private static Map<String, Object> getConnectionProps(DataSourceConfig dataSourceConfig,
			DatabaseProperties databaseProperties) {
		Map<String, Object> dbConnectionProps = null;
		if (dataSourceConfig != null && dataSourceConfig.getConnectionPropsKey() != null) {
			dbConnectionProps = databaseProperties.getConnections().get(dataSourceConfig.getConnectionPropsKey());
		}
		return dbConnectionProps;
	}

	public static String getDataSourceType(DataSourceConfig dataSourceConfig) {
		return StringUtils.hasText(dataSourceConfig.getType()) ? dataSourceConfig.getType()
				: DataSourceOperator.DEFAULT_DATA_SOURCE_TYPE;
	}

	public static Properties getResolvedProperties(String dataSourceName, DataSourceConfig dataSourceConfig,
			DatabaseProperties databaseProperties) {
		Properties connectionProps = null;

		try {

			if (dataSourceConfig != null && databaseProperties != null) {
				Map<String, Object> dbConnectionProps = getConnectionProps(dataSourceConfig, databaseProperties);
				if (dbConnectionProps != null) {
					connectionProps = new Properties();
					connectionProps.putAll(dbConnectionProps);
					if (dataSourceConfig.getConnectionPoolProperties() != null) {
						connectionProps.putAll(dataSourceConfig.getConnectionPoolProperties());
					}
				}
			}

			if (connectionProps != null) {
				connectionProps = JasyptUtil.decryptProperties(connectionProps);
			}
		} catch (Exception e) {
			logger.error("Issue while resolving the properties for datasource: " + dataSourceName+". Data source could not be created", e);
			connectionProps = null;
		}
		return connectionProps;
	}

}
