/**
 * 
 */
package com.jmuscles.async.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author manish goel
 *
 */
public class RabbitTemplateProvider {

	private static final Logger logger = LoggerFactory.getLogger(RabbitTemplateProvider.class);

	private String appName;

	private ConnectionFactory connectionFactory;
	private RabbitTemplate rabbitTemplate;

	public RabbitTemplateProvider(ConnectionFactory connectionFactory, String appName) {
		logger.debug("RabbitTemplateProvider constructor");
		this.connectionFactory = connectionFactory;
		this.appName = appName;
		initialize();
	}

	public void initialize() {
		logger.debug("initialize() method start .... ");

		if (connectionFactory instanceof AbstractConnectionFactory) {
			((AbstractConnectionFactory) connectionFactory).setConnectionNameStrategy(connectionFactory -> appName);
		}
		rabbitTemplate = new RabbitTemplate(connectionFactory);
		logger.debug(".....initialize() method end");
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public ConnectionFactory getConnectionFactory() {
		return this.connectionFactory;
	}

}
