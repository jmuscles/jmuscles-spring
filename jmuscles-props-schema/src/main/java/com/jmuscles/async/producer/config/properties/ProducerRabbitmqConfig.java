/**
 * 
 */
package com.jmuscles.async.producer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class ProducerRabbitmqConfig {

	private String defaultRoutingKey;
	private String defaultExchange;
	private boolean nonPersistentDeliveryMode;

	public ProducerRabbitmqConfig() {
		// TODO Auto-generated constructor stub
	}

	public ProducerRabbitmqConfig(String defaultRoutingKey, String defaultExchange, boolean nonPersistentDeliveryMode) {
		super();
		this.defaultRoutingKey = defaultRoutingKey;
		this.defaultExchange = defaultExchange;
		this.nonPersistentDeliveryMode = nonPersistentDeliveryMode;
	}

	public String getDefaultRoutingKey() {
		return defaultRoutingKey;
	}

	public void setDefaultRoutingKey(String defaultRoutingKey) {
		this.defaultRoutingKey = defaultRoutingKey;
	}

	public String getDefaultExchange() {
		return defaultExchange;
	}

	public void setDefaultExchange(String defaultExchange) {
		this.defaultExchange = defaultExchange;
	}

	public boolean isNonPersistentDeliveryMode() {
		return nonPersistentDeliveryMode;
	}

	public void setNonPersistentDeliveryMode(boolean nonPersistentDeliveryMode) {
		this.nonPersistentDeliveryMode = nonPersistentDeliveryMode;
	}

	public static ProducerRabbitmqConfig mapToObject(Map<String, Object> map) {
		return map != null
				? new ProducerRabbitmqConfig(Util.getString(map, "defaultRoutingKey"),
						Util.getString(map, "defaultExchange"), Util.getBoolean(map, "nonPersistentDeliveryMode"))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("defaultRoutingKey", this.getDefaultRoutingKey());
		map.put("defaultExchange", this.getDefaultExchange());
		map.put("nonPersistentDeliveryMode", this.isNonPersistentDeliveryMode());

		return map;
	}

	public static Map<String, ProducerRabbitmqConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, ProducerRabbitmqConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}
