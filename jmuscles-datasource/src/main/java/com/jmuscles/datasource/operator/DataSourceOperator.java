/**
 * 
 */
package com.jmuscles.datasource.operator;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * @author manish goel
 *
 */
public abstract class DataSourceOperator {

	public static String DEFAULT_DATA_SOURCE_TYPE = "HIKARI";

	public DataSourceOperator(DataSourceOperatorRegistry dataSourceOperatorRegistry) {
		dataSourceOperatorRegistry.register(this);
	}

	public abstract DataSource create(String dataSourceName, Properties props);

	public abstract void refresh(String dataSourceName, DataSource dataSource, Properties newProperties);

	public abstract String dataSourceType();

}
