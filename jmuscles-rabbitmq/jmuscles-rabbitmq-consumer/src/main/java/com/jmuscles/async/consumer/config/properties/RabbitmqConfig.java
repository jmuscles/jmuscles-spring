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
