/**
 * 
 */
package com.jmuscles.async.producer.messageprocessor;

import org.springframework.amqp.core.Message;

/**
 * @author manish goel
 *
 */
@FunctionalInterface
public interface MessageProcessor {

	public Message process(Message message);
}