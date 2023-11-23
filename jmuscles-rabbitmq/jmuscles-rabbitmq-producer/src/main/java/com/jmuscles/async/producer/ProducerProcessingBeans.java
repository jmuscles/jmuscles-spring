
/**
 * @author manish goel
 *
 */
/**
 * 
 */
package com.jmuscles.async.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.jmuscles.async.producer.config.properties.ProducerConfigProperties;
import com.jmuscles.async.producer.jpa.asyncpayload.AsyncPayloadPersister;
import com.jmuscles.async.producer.messageprocessor.AsyncPayloadMessageProcessor;
import com.jmuscles.async.producer.messageprocessor.PersistMessageToDBProcessor;
import com.jmuscles.async.producer.producing.implementation.DBProducer;
import com.jmuscles.async.producer.producing.implementation.RabbitmqProducer;
import com.jmuscles.async.producer.producing.implementation.SyncProcessingProducer;
import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.processing.JmuscleProcessingBeans;
import com.jmuscles.processing.executor.StandardExecutorRegistry;
import com.jmuscles.props.config.JmusclesConfig;

/**
 * @author manish goel
 *
 */
@Import(JmuscleProcessingBeans.class)
public class ProducerProcessingBeans implements BeanFactoryAware, EnvironmentAware {

	private static final Logger logger = LoggerFactory.getLogger(JmuscleProcessingBeans.class);

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

	@Bean("producerConfigProperties")
	public ProducerConfigProperties producerConfigProperties(
			@Qualifier("jmusclesConfig") JmusclesConfig jmusclesConfig) {
		return jmusclesConfig.getAsyncProducerConfig();
	}

	@ConditionalOnMissingBean(RabbitTemplateProvider.class)
	@Bean("rabbitTemplateProvider")
	public RabbitTemplateProvider rabbitTemplateProvider(ConnectionFactory connectionFactory) {
		return new RabbitTemplateProvider(connectionFactory, environment.getProperty("spring.application.name"));
	}

	@Bean("asyncPayloadDeliverer")
	public AsyncPayloadDeliverer asyncPayloadDeliverer(ProducerConfigProperties producerConfigProperties) {
		return new AsyncPayloadDeliverer(producerConfigProperties);
	}

	@Bean("asyncPayloadPersister")
	public AsyncPayloadPersister asyncPayloadPersister(
			@Qualifier("producerConfigProperties") ProducerConfigProperties producerConfigProperties,
			@Qualifier("dataSourceProvider") DataSourceProvider dataSourceProvider) {
		return new AsyncPayloadPersister(environment.getProperty("spring.application.name"), dataSourceProvider,
				producerConfigProperties);
	}

	/* producer beans start here .... */
	@Bean("dbProducer")
	public DBProducer dbProducer(@Qualifier("asyncPayloadPersister") AsyncPayloadPersister asyncPayloadPersister) {
		return new DBProducer(asyncPayloadPersister);
	}

	@Bean("rabbitmqProducer")
	public RabbitmqProducer rabbitmqProducer(
			@Qualifier("producerConfigProperties") ProducerConfigProperties producerConfigProperties,
			@Qualifier("rabbitTemplateProvider") RabbitTemplateProvider rabbitTemplateProvider) {
		return new RabbitmqProducer(producerConfigProperties, rabbitTemplateProvider);
	}

	@ConditionalOnMissingBean(StandardExecutorRegistry.class)
	@Bean("standardExecutorRegistry")
	public StandardExecutorRegistry standardExecutorRegistry() {
		return new StandardExecutorRegistry();
	}

	@Bean("syncProcessingProducer")
	public SyncProcessingProducer syncProcessingProducer(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry) {
		return new SyncProcessingProducer(standardExecutorRegistry);
	}
	/* producer beans end here .... */

	/* MessageProcessor */
	@Bean
	public AsyncPayloadMessageProcessor asyncPayloadMessageProcessor(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry) {
		return new AsyncPayloadMessageProcessor(standardExecutorRegistry);
	}

	/* MessageProcessor */
	@Bean
	public PersistMessageToDBProcessor persistMessageToDBProcessor(
			@Qualifier("asyncPayloadPersister") AsyncPayloadPersister asyncPayloadPersister) {
		return new PersistMessageToDBProcessor(asyncPayloadPersister);
	}

	@Bean("refreshBeanProducer")
	public RefreshBeanProducer refreshBeanProducer() {
		return new RefreshBeanProducer(this.beanFactory);
	}

}
