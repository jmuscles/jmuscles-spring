/**
 * @author manish goel
 *
 */
package com.jmuscles.props.util;

import java.util.HashMap;
import java.util.Map;

import com.jmuscles.async.consumer.config.properties.RabbitmqConfig;
import com.jmuscles.async.producer.config.properties.ProducerConfigProperties;
import com.jmuscles.datasource.properties.DatabaseProperties;
import com.jmuscles.processing.config.properties.ExecutorConfigProperties;
import com.jmuscles.rest.producer.config.properties.RestConfPropsForConfigKey;

/**
 * 
 */
public class JmusclesConfig {

	private Map<String, RestConfPropsForConfigKey> restProducerConfig = new HashMap<>();
	private ProducerConfigProperties asyncProducerConfig;
	private RabbitmqConfig rabbitmqConfig;
	private ExecutorConfigProperties executorsConfig;
	private DatabaseProperties dbProperties;

	public JmusclesConfig() {
		// TODO Auto-generated constructor stub
	}

	public JmusclesConfig(Map<String, RestConfPropsForConfigKey> restProducerConfig,
			ProducerConfigProperties asyncProducerConfig, RabbitmqConfig rabbitmqConfig,
			ExecutorConfigProperties executorsConfig, DatabaseProperties dbProperties) {
		super();
		this.restProducerConfig = restProducerConfig;
		this.asyncProducerConfig = asyncProducerConfig;
		this.rabbitmqConfig = rabbitmqConfig;
		this.executorsConfig = executorsConfig;
		this.dbProperties = dbProperties;
	}

	public void replaceValues(JmusclesConfig jmusclesConfig) {
		if (jmusclesConfig != null) {
			if (this.restProducerConfig != null) {
				this.restProducerConfig.clear();
				if (jmusclesConfig.getRestProducerConfig() != null) {
					this.restProducerConfig.putAll(jmusclesConfig.getRestProducerConfig());
				}
			} else {
				this.restProducerConfig = jmusclesConfig.getRestProducerConfig();
			}
			if (this.asyncProducerConfig != null) {
				this.asyncProducerConfig.replaceValues(jmusclesConfig.getAsyncProducerConfig());
			}
			if (this.rabbitmqConfig != null) {
				this.rabbitmqConfig.replaceValues(jmusclesConfig.getRabbitmqConfig());
			}
			if (this.executorsConfig != null) {
				this.executorsConfig.replaceValues(jmusclesConfig.getExecutorsConfig());
			}
			if (this.dbProperties != null) {
				this.dbProperties.replaceValues(jmusclesConfig.getDbProperties());
			}
		} else {
			clear();
		}
	}

	public void clear() {
		if (this.restProducerConfig != null) {
			this.restProducerConfig.clear();
		}
		if (this.asyncProducerConfig != null) {
			this.asyncProducerConfig.clear();
		}
		if (this.rabbitmqConfig != null) {
			this.rabbitmqConfig.clear();
		}
		if (this.executorsConfig != null) {
			this.executorsConfig.clear();
		}
		if (this.dbProperties != null) {
			this.dbProperties.clear();
		}
	}

	/**
	 * @return the restProducerConfig
	 */
	public Map<String, RestConfPropsForConfigKey> getRestProducerConfig() {
		return restProducerConfig;
	}

	/**
	 * @param restProducerConfig the restProducerConfig to set
	 */
	public void setRestProducerConfig(Map<String, RestConfPropsForConfigKey> restProducerConfig) {
		this.restProducerConfig = restProducerConfig;
	}

	/**
	 * @return the asyncProducerConfig
	 */
	public ProducerConfigProperties getAsyncProducerConfig() {
		return asyncProducerConfig;
	}

	/**
	 * @param asyncProducerConfig the asyncProducerConfig to set
	 */
	public void setAsyncProducerConfig(ProducerConfigProperties asyncProducerConfig) {
		this.asyncProducerConfig = asyncProducerConfig;
	}

	/**
	 * @return the rabbitmqConfig
	 */
	public RabbitmqConfig getRabbitmqConfig() {
		return rabbitmqConfig;
	}

	/**
	 * @param rabbitmqConfig the rabbitmqConfig to set
	 */
	public void setRabbitmqConfig(RabbitmqConfig rabbitmqConfig) {
		this.rabbitmqConfig = rabbitmqConfig;
	}

	/**
	 * @return the executorsConfig
	 */
	public ExecutorConfigProperties getExecutorsConfig() {
		return executorsConfig;
	}

	/**
	 * @param executorsConfig the executorsConfig to set
	 */
	public void setExecutorsConfig(ExecutorConfigProperties executorsConfig) {
		this.executorsConfig = executorsConfig;
	}

	/**
	 * @return the dbProperties
	 */
	public DatabaseProperties getDbProperties() {
		return dbProperties;
	}

	/**
	 * @param dbProperties the dbProperties to set
	 */
	public void setDbProperties(DatabaseProperties dbProperties) {
		this.dbProperties = dbProperties;
	}

	public static JmusclesConfig mapToObject(Map<String, Object> map) {
		return new JmusclesConfig(RestConfPropsForConfigKey.mapToObject2((Map) map.get("restProducerConfig")),
				ProducerConfigProperties.mapToObject((Map) map.get("asyncProducerConfig")),
				RabbitmqConfig.mapToObject((Map) map.get("rabbitmqConfig")),
				ExecutorConfigProperties.mapToObject((Map) map.get("executorsConfig")),
				DatabasePropertiesMapper.mapToObject((Map) map.get("dbProperties")));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		if (this.getRestProducerConfig() != null) {
			map.put("restProducerConfig", RestConfPropsForConfigKey.objectToMap2(this.getRestProducerConfig()));
		}
		if (this.getRabbitmqConfig() != null) {
			map.put("rabbitmqConfig", this.getRabbitmqConfig().objectToMap());
		}
		if (this.getExecutorsConfig() != null) {
			map.put("executorsConfig", this.getExecutorsConfig().objectToMap());
		}
		if (this.getDbProperties() != null) {
			map.put("dbProperties", DatabasePropertiesMapper.objectToMap(this.getDbProperties()));
		}
		if (this.getAsyncProducerConfig() != null) {
			map.put("asyncProducerConfig", this.getAsyncProducerConfig().objectToMap());
		}
		return map;
	}

	public Map<String, Object> objectToMapForSnakeCaseYaml() {
		Map<String, Object> map = new HashMap<>();
		if (this.getRestProducerConfig() != null) {
			map.put("rest-producer-config", this.getRestProducerConfig());
		}
		if (this.getRabbitmqConfig() != null) {
			map.put("rabbitmq-config", this.getRabbitmqConfig());
		}
		if (this.getExecutorsConfig() != null) {
			map.put("executors-config", this.getExecutorsConfig());
		}
		if (this.getDbProperties() != null) {
			map.put("db-properties", this.getDbProperties());
		}
		if (this.getAsyncProducerConfig() != null) {
			map.put("async-producer-config", this.getAsyncProducerConfig());
		}
		return map;
	}

}
