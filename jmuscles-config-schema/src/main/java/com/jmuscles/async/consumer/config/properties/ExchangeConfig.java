/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		return new ExchangeConfig((String) map.get("name"), (String) map.get("type"), (String) map.get("parent"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", this.getName());
		map.put("type", this.getType());
		map.put("parent", this.getParent());

		return map;
	}

	public static Map<String, ExchangeConfig> mapToObject2(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
	}

	public static Map<String, Object> objectToMap2(Map<String, ExchangeConfig> objectsMap) {
		return objectsMap.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	}

	public static List<ExchangeConfig> listToObject(List<Map<String, Object>> list) {
		return list.stream().map(obj -> mapToObject(obj)).collect(Collectors.toList());
	}

	public static List<Map<String, Object>> objectToList(List<ExchangeConfig> list) {
		return list.stream().map(exchange -> exchange.objectToMap()).collect(Collectors.toList());
	}

}
