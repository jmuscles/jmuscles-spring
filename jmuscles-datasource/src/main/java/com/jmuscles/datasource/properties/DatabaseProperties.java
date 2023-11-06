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

	public DatabaseProperties() {
		// TODO Auto-generated constructor stub
	}

	public DatabaseProperties(Map<String, Map<String, Object>> connections, Map<String, DataSourceConfig> dataSources) {
		super();
		this.connections = connections;
		this.dataSources = dataSources;
	}

	public void replaceValues(DatabaseProperties databaseProperties) {
		clear();
		if (databaseProperties != null) {
			this.connections = databaseProperties.getConnections();
			this.dataSources = databaseProperties.getDataSources();
		}
	}

	public void clear() {
		if (this.connections != null) {
			this.connections.clear();
		} else {
			this.connections = new HashMap<>();
		}
		if (this.dataSources != null) {
			this.dataSources.clear();
		} else {
			this.dataSources = new HashMap<>();
		}
	}

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

	private boolean add(DatabaseProperties databaseProperties) {
		if (databaseProperties != null) {
			this.connections.putAll(databaseProperties.getConnections());
			this.dataSources.putAll(databaseProperties.getDataSources());
			return true;
		}
		return false;
	}

}
