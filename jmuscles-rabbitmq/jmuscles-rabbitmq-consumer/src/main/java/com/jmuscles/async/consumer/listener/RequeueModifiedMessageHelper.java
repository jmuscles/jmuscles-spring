/**
 * 
 */
package com.jmuscles.async.consumer.listener;

import java.util.AbstractMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

import com.jmuscles.async.consumer.config.properties.QueueProcessingConfig;
import com.jmuscles.async.consumer.config.properties.QueueSetConfig;
import com.jmuscles.async.consumer.config.properties.RabbitmqConfig;
import com.jmuscles.async.consumer.config.properties.RetryOnlyProcessingConfig;
import com.jmuscles.async.consumer.config.setup.RabbitmqSetupConfigurator;
import com.jmuscles.async.consumer.util.QueueTypes;
import com.jmuscles.async.producer.messageprocessor.MessageProcessor;
import com.jmuscles.processing.SpringBeanUtil;

/**
 * @author manish goel
 *
 */
public class RequeueModifiedMessageHelper {

	private static final Logger logger = LoggerFactory.getLogger(RequeueModifiedMessageHelper.class);

	private static final String X_RETRY_ATTEMPT = "x-retry-attempt";
	private static final boolean nextDestinationFromConfigWhenNoRetry = true;
	private static final int defaultRetryDelay = 600000;

	private final String queueSetProcessingKey;
	private final String queueTypeTobeProcessed;
	private final QueueSetConfig queueSetConfig;

	private RabbitmqSetupConfigurator rabbitmqConfigurator;

	private RequeueModifiedMessageHelper(String queueSetProcessingKey, String queueTypeTobeProcessed,
			QueueSetConfig queueSetConfig, RabbitmqSetupConfigurator rabbitmqConfigurator) {
		this.queueSetProcessingKey = queueSetProcessingKey;
		this.queueTypeTobeProcessed = queueTypeTobeProcessed;
		this.queueSetConfig = queueSetConfig;
		this.rabbitmqConfigurator = rabbitmqConfigurator;
	}

	public static RequeueModifiedMessageHelper of(String queueSetProcessingKey, String queueTypeTobeProcessed,
			QueueSetConfig queueSetConfig, RabbitmqSetupConfigurator rabbitmqConfigurator) {
		return new RequeueModifiedMessageHelper(queueSetProcessingKey, queueTypeTobeProcessed, queueSetConfig,
				rabbitmqConfigurator);
	}

	private RabbitmqConfig getRabbitmqConfig() {
		return (RabbitmqConfig) SpringBeanUtil.getBean(RabbitmqConfig.class, rabbitmqConfigurator.getBeanFactory());
	}

	private Map<String, QueueProcessingConfig> getQueuesProcessingConfig() {
		return getRabbitmqConfig().getQueueSetsProcessingConfig().get(queueSetProcessingKey);
	}

	public void requeueModifiedMessage(Message message) {
		MessageProperties messageProperties = message.getMessageProperties();
		AbstractMap.SimpleEntry<String, String> destinationExhangeAndRoutingKey = null;
		if (!queueSetConfig.isRetrySetupDisabled()) {
			if (!QueueTypes.ABANDONED.equals(queueTypeTobeProcessed)) {
				QueueProcessingConfig retryQueueProcessingConfig = getQueuesProcessingConfig().get(QueueTypes.RETRY);
				RetryOnlyProcessingConfig retryOnlyConfig = retryQueueProcessingConfig.getRetryOnlyConfig();
				if (retryQueueProcessingConfig != null && retryOnlyConfig != null
						&& retryOnlyConfig.isAcceptingMessage()) {
					destinationExhangeAndRoutingKey = determineDestinationWhenRetry(messageProperties, retryOnlyConfig);
				}
			}
		}
		if (destinationExhangeAndRoutingKey == null) {
			destinationExhangeAndRoutingKey = nextDestinationFromConfigWhenNoRetry
					? getNextExchangeAndRoutingKeyFromConfig()
					: getNextExchangeAndRoutingKeyFromQueue(messageProperties.getReceivedRoutingKey(),
							messageProperties);
		}
		messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		logger.info("Requeing message to - " + destinationExhangeAndRoutingKey.getKey() + ", "
				+ destinationExhangeAndRoutingKey.getValue());
		rabbitmqConfigurator.getRabbitTemplateProvider().getRabbitTemplate()
				.send(destinationExhangeAndRoutingKey.getKey(), destinationExhangeAndRoutingKey.getValue(), message);
	}

	private AbstractMap.SimpleEntry<String, String> getNextExchangeAndRoutingKeyFromConfig() {
		return new AbstractMap.SimpleEntry<String, String>(queueSetConfig.getExchange(),
				QueueTypes.queueName(queueSetConfig.getName(), QueueTypes.ABANDONED));
	}

	private AbstractMap.SimpleEntry<String, String> getNextExchangeAndRoutingKeyFromQueue(String receivedRoutingKey,
			MessageProperties messageProperties) {
		return new AbstractMap.SimpleEntry<String, String>(queueSetConfig.getExchange(),
				QueueTypes.queueName(queueSetConfig.getName(), QueueTypes.ABANDONED));
	}

	private AbstractMap.SimpleEntry<String, String> determineDestinationWhenRetry(MessageProperties messageProperties,
			RetryOnlyProcessingConfig retryOnlyConfig) {
		AbstractMap.SimpleEntry<String, String> destinationExhangeAndRoutingKey = null;
		int maxRetryAttempt = retryOnlyConfig.getRetryAttempt();
		if (maxRetryAttempt > 0) {
			Map<String, Object> headers = messageProperties.getHeaders();
			Integer currentRetryAttempt = 0;
			if (QueueTypes.RETRY.equals(queueTypeTobeProcessed)) {
				currentRetryAttempt = (Integer) headers.get(X_RETRY_ATTEMPT);
				currentRetryAttempt++;
			}
			if (currentRetryAttempt < maxRetryAttempt) {
				headers.put(X_RETRY_ATTEMPT, currentRetryAttempt);
				String destinationRoutingKey;
				if (retryOnlyConfig.isRetryAfterDelay()) {
					destinationRoutingKey = QueueTypes.queueName(queueSetConfig.getName(), QueueTypes.WAIT);
					setTTL(messageProperties, currentRetryAttempt, retryOnlyConfig);
				} else {
					destinationRoutingKey = QueueTypes.queueName(queueSetConfig.getName(), QueueTypes.RETRY);
				}
				destinationExhangeAndRoutingKey = new AbstractMap.SimpleEntry<String, String>(
						queueSetConfig.getExchange(), destinationRoutingKey);
			} else {
				headers.remove(X_RETRY_ATTEMPT);
			}
		}

		return destinationExhangeAndRoutingKey;
	}

	private void setTTL(MessageProperties messageProperties, int currentRetryAttempt,
			RetryOnlyProcessingConfig retryOnlyConfig) {
		messageProperties.setExpiration("" + getRetryDelay(currentRetryAttempt, retryOnlyConfig.getRetryInterval()));
	}

	private int getRetryDelay(Integer retryIntervalArrayIndex, int[] retryIntervals) {
		int retryDelay = defaultRetryDelay;
		if (retryIntervalArrayIndex != null && retryIntervals != null) {
			retryIntervalArrayIndex = retryIntervals.length < (retryIntervalArrayIndex + 1) ? retryIntervals.length - 1
					: retryIntervalArrayIndex;
			retryDelay = retryIntervals[retryIntervalArrayIndex];
		}

		return retryDelay;
	}

	public MessageProcessor getMessageProcessor() {
		QueueProcessingConfig processingConfig = getQueuesProcessingConfig().get(queueTypeTobeProcessed);
		MessageProcessor processor = null;
		try {
			processor = (MessageProcessor) SpringBeanUtil.getBean(processingConfig.getProcessor(),
					rabbitmqConfigurator.getBeanFactory());
		} catch (Exception exception) {
			logger.error("Error while getting the processor for : " + processingConfig.getProcessor());
			processor = null;
		}
		MDC.put("processor", processor != null ? processor.getClass().getSimpleName() : "null");

		return processor;
	}

}
