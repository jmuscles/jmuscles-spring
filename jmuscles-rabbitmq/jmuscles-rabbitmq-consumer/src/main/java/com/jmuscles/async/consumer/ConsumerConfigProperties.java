/**
 * 
 */
package com.jmuscles.async.consumer;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import com.jmuscles.async.consumer.config.properties.RabbitmqConfig;
import com.jmuscles.async.consumer.config.setup.RabbitmqSetupConfigurator;
import com.jmuscles.async.producer.RabbitTemplateProvider;
import com.jmuscles.processing.SpringBeanUtil;

/**
 * @author manish goel
 *
 */
public class ConsumerConfigProperties implements BeanFactoryAware {

	private BeanFactory beanFactory;

	@ConditionalOnMissingBean(RabbitTemplateProvider.class)
	@Bean("rabbitTemplateProvider")
	public RabbitTemplateProvider rabbitTemplateProvider(ConnectionFactory connectionFactory,
			@Value("${spring.application.name}") String appname) {
		return new RabbitTemplateProvider(connectionFactory, appname);
	}

	@Bean(name = "rabbitmqConfig")
	@ConfigurationProperties(value = "jmuscles.rabbitmq-config")
	public RabbitmqConfig rabbitmqConfig() {
		return new RabbitmqConfig();
	}

	@Bean(name = "rabbitmqSetupConfigurator")
	public RabbitmqSetupConfigurator rabbitmqSetupConfigurator(
			@Qualifier("rabbitmqConfig") RabbitmqConfig rabbitmqConfig,
			@Qualifier("rabbitTemplateProvider") RabbitTemplateProvider rabbitTemplateProvider) {
		return new RabbitmqSetupConfigurator(rabbitmqConfig, rabbitTemplateProvider, this.beanFactory);
	}

	@EventListener(RefreshScopeRefreshedEvent.class)
	public void refreshRabbitmqConfig() {
		RabbitmqSetupConfigurator rabbitmqSetupConfigurator = (RabbitmqSetupConfigurator) SpringBeanUtil
				.getBean("rabbitmqSetupConfigurator", this.beanFactory);
		rabbitmqSetupConfigurator.configure();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
