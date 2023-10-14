/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class ExchangeConfig {

	private String name;
	private String type;
	private String parent;

	public ExchangeConfig() {
		// TODO Auto-generated constructor stub
	}

	public ExchangeConfig(String name, String type, String parent) {
		super();
		this.name = name;
		this.type = type;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String exchangeType) {
		this.type = exchangeType;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parentExchange) {
		this.parent = parentExchange;
	}

	public static ExchangeConfig mapToObject(Map<String, Object> map) {
		return map != null
				? new ExchangeConfig(Util.getString(map, "name"), Util.getString(map, "type"),
						Util.getString(map, "parent"))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", this.getName());
		map.put("type", this.getType());
		map.put("parent", this.getParent());

		return map;
	}

	public static Map<String, ExchangeConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, ExchangeConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}
