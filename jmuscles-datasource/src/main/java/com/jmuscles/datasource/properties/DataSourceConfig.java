/**
 * 
 */
package com.jmuscles.datasource.properties;

import java.util.Map;

/**
 * @author manish goel
 *
 */
public class DataSourceConfig {
	private String connectionPropsKey;
	private String type;
	private Map<String, Object> connectionPoolProperties;

	public DataSourceConfig() {
		// TODO Auto-generated constructor stub
	}

	public DataSourceConfig(String connectionPropsKey, String type, Map<String, Object> connectionPoolProperties) {
		super();
		this.connectionPropsKey = connectionPropsKey;
		this.type = type;
		this.connectionPoolProperties = connectionPoolProperties;
	}

	public String getConnectionPropsKey() {
		return connectionPropsKey;
	}

	public void setConnectionPropsKey(String connectionPropsKey) {
		this.connectionPropsKey = connectionPropsKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String dataSourceType) {
		this.type = dataSourceType;
	}

	public Map<String, Object> getConnectionPoolProperties() {
		return connectionPoolProperties;
	}

	public void setConnectionPoolProperties(Map<String, Object> connectionPoolProperties) {
		this.connectionPoolProperties = connectionPoolProperties;
	}
}
