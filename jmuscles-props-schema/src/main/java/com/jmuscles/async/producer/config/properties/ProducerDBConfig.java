/**
 * 
 */
package com.jmuscles.async.producer.config.properties;

import java.util.HashMap;
import java.util.Map;

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

}
