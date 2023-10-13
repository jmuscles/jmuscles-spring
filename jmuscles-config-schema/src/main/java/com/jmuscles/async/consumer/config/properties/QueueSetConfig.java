/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author manish goel
 *
 */
public class QueueSetConfig {
	private String name;
	private String exchange;
	private boolean retrySetupDisabled;
	private Map<String, Map<String, Object>> arguments = new HashMap<>();

	public QueueSetConfig() {
		// TODO Auto-generated constructor stub
	}

	public QueueSetConfig(String name, String exchange, boolean retrySetupDisabled,
			Map<String, Map<String, Object>> arguments) {
		super();
		this.name = name;
		this.exchange = exchange;
		this.retrySetupDisabled = retrySetupDisabled;
		this.arguments = arguments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public boolean isRetrySetupDisabled() {
		return retrySetupDisabled;
	}

	public void setRetrySetupDisabled(boolean retrySetupDisabled) {
		this.retrySetupDisabled = retrySetupDisabled;
	}

	public Map<String, Map<String, Object>> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Map<String, Object>> arguments) {
		this.arguments = arguments;
	}

	public static QueueSetConfig mapToObject(Map<String, Object> map) {
		return new QueueSetConfig((String) map.get("name"), (String) map.get("exchange"),
				(boolean) map.get("retrySetupDisabled"), resolveArguments((Map) map.get("arguments")));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", this.getName());
		map.put("exchange", this.getExchange());
		map.put("retrySetupDisabled", this.isRetrySetupDisabled());
		map.put("arguments", this.getArguments());

		return map;
	}

	// Map<String, Map<String, Object>> arguments = new HashMap<>()
	private static Map<String, Map<String, Object>> resolveArguments(Map<String, Object> map) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> resolveArgument((Map) e.getValue())));
	}

	private static Map<String, Object> resolveArgument(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}

	public static Map<String, QueueSetConfig> mapToObject2(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
	}

	public static Map<String, Object> objectToMap2(Map<String, QueueSetConfig> objectsMap) {
		return objectsMap.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	}

}
