/**
 * 
 */
package com.jmuscles.async.producer.properties;

import java.util.List;

/**
 * @author manish goel
 *
 */
public class ProducerConfigProperties {

	private List<String> activeProducersInOrder;
	private ProducerRabbitmqConfig rabbitmq;
	private ProducerDBConfig database;

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
