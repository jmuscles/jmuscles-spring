/**
 * 
 */
package com.jmuscles.async.consumer.config.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.async.consumer.config.properties.RabbitmqConfig;
import com.jmuscles.async.producer.RabbitTemplateProvider;

/**
 * @author manish goel
 *
 */
public class RabbitmqSetupConfigurator {

	private static final Logger logger = LoggerFactory.getLogger(RabbitmqSetupConfigurator.class);

	private RabbitmqConfig rabbitmqConfig;
	private RabbitTemplateProvider rabbitTemplateProvider;
	private BeanFactory beanFactory;

	private boolean configuredSetup = false;

	public RabbitmqSetupConfigurator(RabbitmqConfig rabbitmqConfig, RabbitTemplateProvider rabbitTemplateProvider,
			BeanFactory beanFactory) {
		super();
		this.rabbitmqConfig = rabbitmqConfig;
		this.rabbitTemplateProvider = rabbitTemplateProvider;
		this.beanFactory = beanFactory;

		configure();
	}

	public void refresh() {
		logger.info("Refresh RabbitmqSetupConfigurator start....");
		configure();
		logger.info("....Refresh RabbitmqSetupConfigurator end");
	}

	public void configure() {
		configureSetup();
		configureProcessingSetup();
	}

	private void configureSetup() {
		if (!configuredSetup) {
			ExchangesSetupConfigurator.configure(this);
			QueuesSetupConfigurator.configure(this);
			configuredSetup = true;
		}
	}

	private void configureProcessingSetup() {
		QueuesProcessingConfigurator.configure(this);
	}

	public RabbitmqConfig getRabbitmqConfig() {
		return rabbitmqConfig;
	}

	public RabbitTemplateProvider getRabbitTemplateProvider() {
		return rabbitTemplateProvider;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

}
