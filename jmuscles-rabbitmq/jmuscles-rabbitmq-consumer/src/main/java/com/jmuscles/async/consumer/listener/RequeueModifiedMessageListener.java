/**
 * 
 */
package com.jmuscles.async.consumer.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;

import com.jmuscles.async.consumer.config.properties.QueueSetConfig;
import com.jmuscles.async.consumer.config.setup.RabbitmqSetupConfigurator;
import com.jmuscles.async.producer.constant.AsyncMessageConstants;
import com.jmuscles.async.producer.messageprocessor.MessageProcessor;
import com.jmuscles.processing.constant.Constants;

/**
 * @author manish goel
 *
 */
public class RequeueModifiedMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RequeueModifiedMessageListener.class);

	private final RequeueModifiedMessageHelper queueProcessingHelper;

	private RequeueModifiedMessageListener(String queueSetProcessingKey, String queueTypeTobeProcessed,
			QueueSetConfig queueSetConfig, RabbitmqSetupConfigurator rabbitmqConfigurator) {
		super();
		queueProcessingHelper = RequeueModifiedMessageHelper.of(queueSetProcessingKey, queueTypeTobeProcessed,
				queueSetConfig, rabbitmqConfigurator);
	}

	public static RequeueModifiedMessageListener of(String queueSetProcessingKey, String queueTypeTobeProcessed,
			QueueSetConfig queueSetConfig, RabbitmqSetupConfigurator rabbitmqConfigurator) {
		return new RequeueModifiedMessageListener(queueSetProcessingKey, queueTypeTobeProcessed, queueSetConfig,
				rabbitmqConfigurator);
	}

	private void setLogInfo(Message message) {
		MessageProperties messageProperties = message.getMessageProperties();
		messageProperties.getHeaders().entrySet().forEach(e -> MDC.put(e.getKey(), e.getValue().toString()));
		MDC.put(AsyncMessageConstants.QUEUE_NAME, messageProperties.getReceivedRoutingKey());
		MDC.put(AsyncMessageConstants.EXCHANGE, messageProperties.getReceivedExchange());
		MDC.put(Constants.CURRENT_ATTEMPT, "" + getCurrentAttempt(messageProperties));
	}

	@Override
	public void onMessage(Message message) {
		try {
			setLogInfo(message);
			logger.info("Message processing start...");
			Message modifiedMessage = null;
			try {
				MessageProcessor processor = queueProcessingHelper.getMessageProcessor();
				modifiedMessage = processor.process(message);
			} catch (Exception e) {
				logger.error("Error while processing message", e);
				modifiedMessage = message;
			}
			if (modifiedMessage != null) {
				queueProcessingHelper.requeueModifiedMessage(modifiedMessage);
				logger.error("Message was not successfully processed and requeued");
			} else {
				logger.info("Message processed successfully");
			}
		} finally {
			MDC.clear();
		}

	}

	private int getCurrentAttempt(MessageProperties messageProperties) {
		Map<String, Object> headers = messageProperties.getHeaders();
		int attemptMade = headers.get(AsyncMessageConstants.PROCESS_ATTEMPTS) == null ? 1
				: ((int) headers.get(AsyncMessageConstants.PROCESS_ATTEMPTS)) + 1;
		headers.put(AsyncMessageConstants.PROCESS_ATTEMPTS, attemptMade);

		return attemptMade;
	}

}
