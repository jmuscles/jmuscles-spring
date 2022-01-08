/**
 * 
 */
package com.jmuscles.async.producer.producing;

/**
 * @author manish goel
 *
 */
public abstract class SelfRegisteredProducer implements Producer {

	public SelfRegisteredProducer() {
		Producer.register(getProducerKey(), this);
	}

	public abstract String getProducerKey();
}
