/**
 * 
 */
package com.jmuscles.datasource.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class DatabaseProperties {

	private Map<String, Map<String, Object>> connections = new HashMap<>();
	private Map<String, DataSourceConfig> dataSources = new HashMap<>();

	public Map<String, Map<String, Object>> getConnections() {
		return connections;
	}

	public void setConnections(Map<String, Map<String, Object>> connections) {
		this.connections = connections;
	}

	public Map<String, DataSourceConfig> getDataSources() {
		return dataSources;
	}

	public void setDataSources(Map<String, DataSourceConfig> dataSources) {
		this.dataSources = dataSources;
	}

}
