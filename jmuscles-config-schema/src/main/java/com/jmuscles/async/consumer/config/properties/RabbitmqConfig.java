/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author manish goel
 *
 */
public class RabbitmqConfig {

	private List<ExchangeConfig> exchanges = new ArrayList<>();
	private Map<String, QueueSetConfig> queueSetsConfig = new HashMap<>();
	private Map<String, Map<String, QueueProcessingConfig>> queueSetsProcessingConfig = new HashMap<>();


	public List<ExchangeConfig> getExchanges() {
		return exchanges;
	}

	public void setExchanges(List<ExchangeConfig> exchanges) {
		this.exchanges = exchanges;
	}

	public Map<String, QueueSetConfig> getQueueSetsConfig() {
		return queueSetsConfig;
	}

	public void setQueueSetsConfig(Map<String, QueueSetConfig> queueSetsConfig) {
		this.queueSetsConfig = queueSetsConfig;
	}

	public Map<String, Map<String, QueueProcessingConfig>> getQueueSetsProcessingConfig() {
		return queueSetsProcessingConfig;
	}

	public void setQueueSetsProcessingConfig(
			Map<String, Map<String, QueueProcessingConfig>> queueSetsProcessingConfig) {
		this.queueSetsProcessingConfig = queueSetsProcessingConfig;
	}

	/*
	 * public static RabbitmqConfig mapToObject(Map<String, Object> map) { return
	 * new RabbitmqConfig(ExchangeConfig.listToObject((List<Map<String, Object>>)
	 * map.get("exchanges")), QueueSetConfig.mapToObject2((Map)
	 * map.get("queueSetsConfig")),
	 * QueueProcessingConfig.mapOfMapToMapOfObject((Map)
	 * map.get("queueSetsProcessingConfig"))); }
	 * 
	 * public Map<String, Object> objectToMap() { Map<String, Object> map = new
	 * HashMap<>(); map.put("exchanges",
	 * ExchangeConfig.objectToList(this.getExchanges())); map.put("queueSetsConfig",
	 * QueueSetConfig.objectToMap2(this.getQueueSetsConfig()));
	 * map.put("queueSetsProcessingConfig",
	 * QueueProcessingConfig.objectToMapOfMap(this.getQueueSetsProcessingConfig()));
	 * 
	 * return map; }
	 * 
	 * public static Map<String, RabbitmqConfig> mapToObject2(Map<String, Object>
	 * map) { return map.entrySet().stream().collect(Collectors.toMap(e ->
	 * e.getKey(), e -> mapToObject((Map) e.getValue()))); }
	 * 
	 * public static Map<String, Object> objectToMap2(Map<String, QueueSetConfig>
	 * objectsMap) { return objectsMap.entrySet().stream()
	 * .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	 * }
	 */

}
