/**
 * @author manish goel
 *
 */
package com.jmuscles.props.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.datasource.properties.DataSourceConfig;
import com.jmuscles.datasource.properties.DatabaseProperties;
import com.jmuscles.util.Util;

/**
 * 
 */
public class DatabasePropertiesMapper {

	public static DatabaseProperties mapToObject(Map<String, Object> map) {
		return map != null
				? new DatabaseProperties(mapToObjectConnections((Map) map.get("connections")),
						mapToObjectDataSources((Map) map.get("dataSources")))
				: null;
	}

	public static Map<String, Object> objectToMap(DatabaseProperties dbProps) {
		Map<String, Object> map = new HashMap<>();
		if (dbProps.getConnections() != null) {
			map.put("connections", dbProps.getConnections());
		}
		if (dbProps.getDataSources() != null) {
			map.put("dataSources", objectToMap2DataSourceConfig(dbProps.getDataSources()));
		}
		return map;
	}

	public static Map<String, Map<String, Object>> mapToObjectConnections(Map<String, Object> map) {
		return map != null ? map.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObjectConnection((Map) e.getValue()))) : null;
	}

	private static Map<String, Object> mapToObjectConnection(Map<String, Object> map) {
		return map != null ? map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()))
				: null;
	}

	public static Map<String, DataSourceConfig> mapToObjectDataSources(Map<String, Object> map) {
		return map != null ? map.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObjectDataSource((Map) e.getValue()))) : null;
	}

	private static DataSourceConfig mapToObjectDataSource(Map<String, Object> map) {
		return new DataSourceConfig(Util.getString(map, "connectionPropsKey"), Util.getString(map, "type"),
				(Map) map.get("connectionPoolProperties"));
	}

	public static Map<String, Object> objectToMap2DataSourceConfig(Map<String, DataSourceConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> objectToMapDataSourceConfig(e.getValue())));
		} else {
			return null;
		}
	}

	private static Map<String, Object> objectToMapDataSourceConfig(DataSourceConfig dataSourceConfig) {
		Map<String, Object> map = new HashMap<>();
		if (dataSourceConfig != null) {
			map.put("connectionPropsKey", dataSourceConfig.getConnectionPropsKey());
			map.put("type", dataSourceConfig.getType());
			map.put("connectionPoolProperties", dataSourceConfig.getConnectionPoolProperties());
		}
		return map;
	}

}
