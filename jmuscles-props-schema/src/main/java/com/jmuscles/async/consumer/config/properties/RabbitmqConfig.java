/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
