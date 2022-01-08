/**
 * 
 */
package com.jmuscles.async.consumer.config.setup;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.StringUtils;

import com.jmuscles.async.consumer.config.properties.QueueProcessingConfig;
import com.jmuscles.async.consumer.config.properties.QueueSetConfig;
import com.jmuscles.async.consumer.config.properties.RabbitmqConfig;
import com.jmuscles.async.consumer.listener.RequeueModifiedMessageListener;
import com.jmuscles.async.consumer.util.QueueTypes;
import com.jmuscles.async.consumer.util.RabbitmqSpringBeanUtil;

/**
 * @author manish goel
 *
 */
public class QueuesProcessingConfigurator {

	private static final Logger logger = LoggerFactory.getLogger(QueuesProcessingConfigurator.class);

	public static void configure(RabbitmqSetupConfigurator configurator) {
		RabbitmqConfig rabbitmqConfig = configurator.getRabbitmqConfig();
		rabbitmqConfig.getQueueSetsProcessingConfig().keySet()
				.forEach(queueConfigKey -> setup(queueConfigKey,
						rabbitmqConfig.getQueueSetsProcessingConfig().get(queueConfigKey),
						rabbitmqConfig.getQueueSetsConfig().get(queueConfigKey), configurator));
	}

	private static void setup(String queueConfigKey, Map<String, QueueProcessingConfig> queueProcessingConfigMap,
			QueueSetConfig queueSetConfig, RabbitmqSetupConfigurator configurator) {

		bindListenerContainer(queueConfigKey, QueueTypes.PRIMARY, queueSetConfig.getName(),
				queueProcessingConfigMap.get(QueueTypes.PRIMARY), queueSetConfig, configurator);

		if (!queueSetConfig.isRetrySetupDisabled()) {
			bindListenerContainer(queueConfigKey, QueueTypes.RETRY,
					QueueTypes.queueName(queueSetConfig.getName(), QueueTypes.RETRY),
					queueProcessingConfigMap.get(QueueTypes.RETRY), queueSetConfig, configurator);
		}

		bindListenerContainer(queueConfigKey, QueueTypes.ABANDONED,
				QueueTypes.queueName(queueSetConfig.getName(), QueueTypes.ABANDONED),
				queueProcessingConfigMap.get(QueueTypes.ABANDONED), queueSetConfig, configurator);
	}

	private static void bindListenerContainer(String queueConfigKey, String queueType, String queueName,
			QueueProcessingConfig queueProcessingConfig, QueueSetConfig queueSetConfig,
			RabbitmqSetupConfigurator configurator) {
		if (queueProcessingConfig != null) {
			SimpleMessageListenerContainer container = (SimpleMessageListenerContainer) RabbitmqSpringBeanUtil
					.getRabbitEntitySpringBean(queueName, SimpleMessageListenerContainer.class,
							configurator.getBeanFactory());
			if (queueProcessingConfig.isDisableProcessing()) {
				if (container != null && container.isRunning()) {
					container.stop();
					logger.info("Listener stopped for queue : " + queueName);
				}
			} else {
				if (container == null) {
					container = new SimpleMessageListenerContainer(
							configurator.getRabbitTemplateProvider().getConnectionFactory());
					container.setQueueNames(queueName);
					container.setConcurrency(queueProcessingConfig.getConcurrency());
					container.setMessageListener(getMessageListener(queueProcessingConfig.getListenerType(),
							queueConfigKey, queueType, queueSetConfig, configurator));
					RabbitmqSpringBeanUtil.registerRabbitEntityAsSpringBean(queueName,
							SimpleMessageListenerContainer.class, container, configurator.getBeanFactory());
					logger.info("Listener attached to the queue : " + queueName);
				} else {
					container.setConcurrency(queueProcessingConfig.getConcurrency());
					if (!container.isRunning()) {
						container.start();
						logger.info("Listener started for queue : " + queueName);
					}
				}
			}
		}
	}

	private static MessageListener getMessageListener(String listenerType, String queueConfigKey, String queueType,
			QueueSetConfig queueSetConfig, RabbitmqSetupConfigurator configurator) {
		MessageListener messageListener = null;
		if (!StringUtils.hasText(listenerType)
				|| RequeueModifiedMessageListener.class.getSimpleName().equalsIgnoreCase(listenerType)) {
			messageListener = RequeueModifiedMessageListener.of(queueConfigKey, queueType, queueSetConfig,
					configurator);
		}
		return messageListener;
	}

}
