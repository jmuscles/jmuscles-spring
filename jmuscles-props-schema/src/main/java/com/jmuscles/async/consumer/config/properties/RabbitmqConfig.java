/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class RabbitmqConfig {

	private List<ExchangeConfig> exchanges = new ArrayList<>();
	private Map<String, QueueSetConfig> queueSetsConfig = new HashMap<>();
	private Map<String, Map<String, QueueProcessingConfig>> queueSetsProcessingConfig = new HashMap<>();

	public RabbitmqConfig() {
		// TODO Auto-generated constructor stub
	}

	public RabbitmqConfig(List<ExchangeConfig> exchanges, Map<String, QueueSetConfig> queueSetsConfig,
			Map<String, Map<String, QueueProcessingConfig>> queueSetsProcessingConfig) {
		super();
		this.exchanges = exchanges;
		this.queueSetsConfig = queueSetsConfig;
		this.queueSetsProcessingConfig = queueSetsProcessingConfig;
	}

	public void replaceValues(RabbitmqConfig rabbitmqConfig) {
		clear();
		if (rabbitmqConfig != null) {
			this.exchanges = rabbitmqConfig.getExchanges();
			this.queueSetsConfig = rabbitmqConfig.getQueueSetsConfig();
			this.queueSetsProcessingConfig = rabbitmqConfig.getQueueSetsProcessingConfig();
		}
	}

	public void clear() {
		if (this.exchanges != null) {
			this.exchanges.clear();
		} else {
			this.exchanges = new ArrayList<>();
		}
		if (this.queueSetsConfig != null) {
			this.queueSetsConfig.clear();
		} else {
			this.queueSetsConfig = new HashMap<>();
		}
		if (this.queueSetsProcessingConfig != null) {
			this.queueSetsProcessingConfig.clear();
		} else {
			this.queueSetsProcessingConfig = new HashMap<>();
		}
	}

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

	public static RabbitmqConfig mapToObject(Map<String, Object> map) {
		return map != null
				? new RabbitmqConfig(
						(List<ExchangeConfig>) Util.stringToListFromMap(map, "exchanges", ExchangeConfig.class),
						QueueSetConfig.mapToObject2((Map) map.get("queueSetsConfig")),
						QueueProcessingConfig.mapOfMapToMapOfObject((Map) map.get("queueSetsProcessingConfig")))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		// map.put("exchanges", ExchangeConfig.objectToList(this.getExchanges()));
		map.put("exchanges", Util.listToString(this.getExchanges()));
		map.put("queueSetsConfig", QueueSetConfig.objectToMap2(this.getQueueSetsConfig()));
		map.put("queueSetsProcessingConfig",
				QueueProcessingConfig.objectToMapOfMap(this.getQueueSetsProcessingConfig()));

		return map;
	}

	public static Map<String, RabbitmqConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, QueueSetConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}
