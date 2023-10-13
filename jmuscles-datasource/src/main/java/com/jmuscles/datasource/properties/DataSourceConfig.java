/**
 * 
 */
package com.jmuscles.datasource.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

	public static DataSourceConfig mapToObject(Map<String, Object> map) {
		return new DataSourceConfig((String) map.get("connectionPropsKey"), (String) map.get("type"),
				(Map) map.get("connectionPoolProperties"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("connectionPropsKey", this.getConnectionPropsKey());
		map.put("type", this.getType());
		map.put("connectionPoolProperties", this.getConnectionPoolProperties());

		return map;
	}

	public static Map<String, DataSourceConfig> mapToObject2(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
	}

	public static Map<String, Object> objectToMap2(Map<String, DataSourceConfig> objectsMap) {
		return objectsMap.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	}

}
