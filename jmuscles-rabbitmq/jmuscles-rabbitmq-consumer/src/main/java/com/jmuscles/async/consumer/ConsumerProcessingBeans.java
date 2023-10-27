/**
 * 
 */
package com.jmuscles.async.consumer;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.jmuscles.async.consumer.config.properties.RabbitmqConfig;
import com.jmuscles.async.consumer.config.setup.RabbitmqSetupConfigurator;
import com.jmuscles.async.producer.ProducerProcessingBeans;
import com.jmuscles.async.producer.RabbitTemplateProvider;
import com.jmuscles.props.util.JmusclesConfig;

/**
 * @author manish goel
 *
 */
@Import(ProducerProcessingBeans.class)
public class ConsumerProcessingBeans implements BeanFactoryAware, EnvironmentAware {

	private BeanFactory beanFactory;
	private Environment environment;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@ConditionalOnMissingBean(RabbitTemplateProvider.class)
	@Bean("rabbitTemplateProvider")
	public RabbitTemplateProvider rabbitTemplateProvider(ConnectionFactory connectionFactory) {
		return new RabbitTemplateProvider(connectionFactory, environment.getProperty("spring.application.name"));
	}

	@Bean(name = "rabbitmqConfig")
	public RabbitmqConfig rabbitmqConfig(@Qualifier("jmusclesConfig") JmusclesConfig jmusclesConfig) {
		return jmusclesConfig.getRabbitmqConfig();
	}

	@Bean(name = "rabbitmqSetupConfigurator")
	public RabbitmqSetupConfigurator rabbitmqSetupConfigurator(
			@Qualifier("rabbitmqConfig") RabbitmqConfig rabbitmqConfig,
			@Qualifier("rabbitTemplateProvider") RabbitTemplateProvider rabbitTemplateProvider) {
		return new RabbitmqSetupConfigurator(rabbitmqConfig, rabbitTemplateProvider, this.beanFactory);
	}

	@Bean("refreshBeanConsumer")
	public RefreshBeanConsumer refreshBeanConsumer() {
		return new RefreshBeanConsumer(this.beanFactory);
	}

}
