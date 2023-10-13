/**
 * 
 */
package com.jmuscles.processing.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class SQLQueryCallConfig {
	private String dskey;
	private String query;
	private String validator;

	public SQLQueryCallConfig() {
		// TODO Auto-generated constructor stub
	}

	public SQLQueryCallConfig(String dskey, String query, String validator) {
		super();
		this.dskey = dskey;
		this.query = query;
		this.validator = validator;
	}

	public String getDskey() {
		return dskey;
	}

	public void setDskey(String dskey) {
		this.dskey = dskey;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public static SQLQueryCallConfig mapToObject(Map<String, Object> map) {
		return new SQLQueryCallConfig(Util.getString(map, "dskey"), Util.getString(map, "query"),
				Util.getString(map, "validator"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		if (this.getDskey() != null) {
			map.put("dskey", this.getDskey());
		}
		if (this.getQuery() != null) {
			map.put("query", this.getQuery());
		}
		if (this.getValidator() != null) {
			map.put("validator", this.getValidator());
		}

		return map;
	}

	public static Map<String, SQLQueryCallConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, SQLQueryCallConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}
}
