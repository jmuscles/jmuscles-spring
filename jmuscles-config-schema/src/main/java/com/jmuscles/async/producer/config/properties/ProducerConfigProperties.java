/**
 * 
 */
package com.jmuscles.async.producer.config.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class ProducerConfigProperties {

	private List<String> activeProducersInOrder;
	private ProducerRabbitmqConfig rabbitmq;
	private ProducerDBConfig database;

	public ProducerConfigProperties() {
		// TODO Auto-generated constructor stub
	}

	public ProducerConfigProperties(List<String> activeProducersInOrder, ProducerRabbitmqConfig rabbitmq,
			ProducerDBConfig database) {
		super();
		this.activeProducersInOrder = activeProducersInOrder;
		this.rabbitmq = rabbitmq;
		this.database = database;
	}

	public List<String> getActiveProducersInOrder() {
		return activeProducersInOrder;
	}

	public void setActiveProducersInOrder(List<String> activeProducersInOrder) {
		this.activeProducersInOrder = activeProducersInOrder;
	}

	public ProducerRabbitmqConfig getRabbitmq() {
		return rabbitmq;
	}

	public void setRabbitmq(ProducerRabbitmqConfig rabbitmq) {
		this.rabbitmq = rabbitmq;
	}

	public ProducerDBConfig getDatabase() {
		return database;
	}

	public void setDatabase(ProducerDBConfig database) {
		this.database = database;
	}

	public static ProducerConfigProperties mapToObject(Map<String, Object> map) {
		return map != null
				? new ProducerConfigProperties(Util.getListOfString(map, "activeProducersInOrder"),
						ProducerRabbitmqConfig.mapToObject((Map) map.get("rabbitmq")),
						ProducerDBConfig.mapToObject((Map) map.get("database")))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("activeProducersInOrder", this.getActiveProducersInOrder());
		if (this.getRabbitmq() != null) {
			map.put("rabbitmq", this.getRabbitmq().objectToMap());
		}
		if (this.getDatabase() != null) {
			map.put("database", this.getDatabase().objectToMap());
		}

		return map;
	}

	public static Map<String, ProducerConfigProperties> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, ProducerConfigProperties> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream().collect(
					Collectors.toMap(e -> e.getKey(), e -> e.getValue() != null ? e.getValue().objectToMap() : null));
		} else {
			return null;
		}
	}

}
