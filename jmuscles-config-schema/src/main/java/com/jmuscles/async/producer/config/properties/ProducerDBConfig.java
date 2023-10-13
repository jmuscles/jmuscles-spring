/**
 * 
 */
package com.jmuscles.async.producer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author manish goel
 *
 */
public class ProducerDBConfig {

	private String dataSourceKey;
	private Map<String, String> jpaProperties = new HashMap<>();

	public ProducerDBConfig() {
		// TODO Auto-generated constructor stub
	}

	public ProducerDBConfig(String dataSourceKey, Map<String, String> jpaProperties) {
		super();
		this.dataSourceKey = dataSourceKey;
		this.jpaProperties = jpaProperties;
	}

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}

	public Map<String, String> getJpaProperties() {
		return jpaProperties;
	}

	public void setJpaProperties(Map<String, String> jpaProperties) {
		this.jpaProperties = jpaProperties;
	}

	public static ProducerDBConfig mapToObject(Map<String, Object> map) {
		return new ProducerDBConfig((String) map.get("dataSourceKey"), (Map) map.get("jpaProperties"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("dataSourceKey", this.getDataSourceKey());
		map.put("jpaProperties", this.getJpaProperties());

		return map;
	}

	public static Map<String, ProducerDBConfig> mapToObject2(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
	}

	public static Map<String, Object> objectToMap2(Map<String, ProducerDBConfig> objectsMap) {
		return objectsMap.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	}

}
