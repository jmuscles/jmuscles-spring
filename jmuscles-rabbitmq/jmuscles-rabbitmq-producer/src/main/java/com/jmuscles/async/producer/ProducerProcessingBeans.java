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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import com.jmuscles.async.producer.jpa.asyncpayload.AsyncPayloadPersister;
import com.jmuscles.async.producer.messageprocessor.AsyncPayloadMessageProcessor;
import com.jmuscles.async.producer.messageprocessor.PersistMessageToDBProcessor;
import com.jmuscles.async.producer.producing.implementation.DBProducer;
import com.jmuscles.async.producer.producing.implementation.RabbitmqProducer;
import com.jmuscles.async.producer.producing.implementation.SyncProcessingProducer;
import com.jmuscles.async.producer.properties.ProducerConfigProperties;
import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.processing.JmuscleProcessingBeans;
import com.jmuscles.processing.SpringBeanUtil;
import com.jmuscles.processing.executor.ExecutorRegistry;

/**
 * @author manish goel
 *
 */
public class ProducerProcessingBeans implements BeanFactoryAware {

	private static final Logger logger = LoggerFactory.getLogger(JmuscleProcessingBeans.class);

	private BeanFactory beanFactory;

	@Bean("producerConfigProperties")
	@ConfigurationProperties(value = "async-producer-config")
	public ProducerConfigProperties producerConfigProperties() {
		return new ProducerConfigProperties();
	}

	@ConditionalOnMissingBean(RabbitTemplateProvider.class)
	@Bean("rabbitTemplateProvider")
	public RabbitTemplateProvider rabbitTemplateProvider(ConnectionFactory connectionFactory,
			@Value("${spring.application.name}") String appname) {
		return new RabbitTemplateProvider(connectionFactory, appname);
	}

	@Bean("asyncPayloadDeliverer")
	public AsyncPayloadDeliverer asyncPayloadDeliverer(ProducerConfigProperties producerConfigProperties) {
		return new AsyncPayloadDeliverer(producerConfigProperties);
	}

	@Bean("asyncPayloadPersister")
	public AsyncPayloadPersister asyncPayloadPersister(@Value("${spring.application.name}") String applicationName,
			@Qualifier("producerConfigProperties") ProducerConfigProperties producerConfigProperties,
			@Qualifier("dataSourceGenerator") DataSourceGenerator dataSourceGenerator) {
		return new AsyncPayloadPersister(applicationName, dataSourceGenerator, producerConfigProperties);
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

	@ConditionalOnMissingBean(ExecutorRegistry.class)
	@Bean("executorRegistry")
	public ExecutorRegistry executorRegistry() {
		return new ExecutorRegistry();
	}

	@Bean("syncProcessingProducer")
	public SyncProcessingProducer syncProcessingProducer(
			@Qualifier("executorRegistry") ExecutorRegistry executorRegistry) {
		return new SyncProcessingProducer(executorRegistry);
	}
	/* producer beans end here .... */

	/* MessageProcessor */
	@Bean
	public AsyncPayloadMessageProcessor asyncPayloadMessageProcessor(
			@Qualifier("executorRegistry") ExecutorRegistry executorRegistry) {
		return new AsyncPayloadMessageProcessor(executorRegistry);
	}

	/* MessageProcessor */
	@Bean
	public PersistMessageToDBProcessor persistMessageToDBProcessor(
			@Qualifier("asyncPayloadPersister") AsyncPayloadPersister asyncPayloadPersister) {
		return new PersistMessageToDBProcessor(asyncPayloadPersister);
	}

	@EventListener(RefreshScopeRefreshedEvent.class)
	public void refreshAsyncPayloadPersister() {
		AsyncPayloadPersister asyncPayloadPersister = (AsyncPayloadPersister) SpringBeanUtil
				.getBean("asyncPayloadPersister", beanFactory);
		asyncPayloadPersister.refresh();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
