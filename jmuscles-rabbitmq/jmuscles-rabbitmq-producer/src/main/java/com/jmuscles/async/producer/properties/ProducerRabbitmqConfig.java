/**
 * 
 */
package com.jmuscles.async.producer.properties;

/**
 * @author manish goel
 *
 */
public class ProducerRabbitmqConfig {

	private String defaultRoutingKey;
	private String defaultExchange;
	private boolean nonPersistentDeliveryMode;

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
