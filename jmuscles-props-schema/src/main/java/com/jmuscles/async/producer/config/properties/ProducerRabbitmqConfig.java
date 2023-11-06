/**
 * 
 */
package com.jmuscles.async.producer.config.properties;

/**
 * @author manish goel
 *
 */
public class ProducerRabbitmqConfig {

	private String defaultRoutingKey;
	private String defaultExchange;
	private boolean nonPersistentDeliveryMode;

	public ProducerRabbitmqConfig() {
		// TODO Auto-generated constructor stub
	}

	public ProducerRabbitmqConfig(String defaultRoutingKey, String defaultExchange, boolean nonPersistentDeliveryMode) {
		super();
		this.defaultRoutingKey = defaultRoutingKey;
		this.defaultExchange = defaultExchange;
		this.nonPersistentDeliveryMode = nonPersistentDeliveryMode;
	}

	public String getDefaultRoutingKey() {
		return defaultRoutingKey;
	}

	public void setDefaultRoutingKey(String defaultRoutingKey) {
		this.defaultRoutingKey = defaultRoutingKey;
	}

	public String getDefaultExchange() {
		return defaultExchange;
	}

	public void setDefaultExchange(String defaultExchange) {
		this.defaultExchange = defaultExchange;
	}

	public boolean isNonPersistentDeliveryMode() {
		return nonPersistentDeliveryMode;
	}

	public void setNonPersistentDeliveryMode(boolean nonPersistentDeliveryMode) {
		this.nonPersistentDeliveryMode = nonPersistentDeliveryMode;
	}

}
