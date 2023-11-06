/**
 * 
 */
package com.jmuscles.async.producer.config.properties;

import java.util.List;

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

	public void replaceValues(ProducerConfigProperties producerConfigProperties) {
		clear();
		if (producerConfigProperties != null) {
			this.activeProducersInOrder = producerConfigProperties.getActiveProducersInOrder();
			this.rabbitmq = producerConfigProperties.getRabbitmq();
			this.database = producerConfigProperties.getDatabase();
		}
	}

	public void clear() {
		this.activeProducersInOrder = null;
		this.rabbitmq = null;
		this.database = null;
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
}
