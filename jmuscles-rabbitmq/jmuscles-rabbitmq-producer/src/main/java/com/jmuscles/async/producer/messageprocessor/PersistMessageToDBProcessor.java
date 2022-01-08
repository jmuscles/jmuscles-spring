/**
 * 
 */
package com.jmuscles.async.producer.messageprocessor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.util.StringUtils;

import com.jmuscles.async.producer.constant.AsyncMessageConstants;
import com.jmuscles.async.producer.jpa.asyncpayload.AsyncPayloadPersister;

/**
 * @author manish goel
 *
 */
public class PersistMessageToDBProcessor implements MessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(PersistMessageToDBProcessor.class);

	private AsyncPayloadPersister asyncPayloadPersister;

	public PersistMessageToDBProcessor(AsyncPayloadPersister asyncPayloadPersister) {
		super();
		this.asyncPayloadPersister = asyncPayloadPersister;
	}

	@Override
	public Message process(Message message) {
		Message unProcessed = null;
		try {
			asyncPayloadPersister.persist(message.getBody(), buildPayloadPropsFromMessage(message));
		} catch (Exception e) {
			logger.error("Error while persisting message in to database : ", e);
			unProcessed = message;
		}
		return unProcessed;
	}

	private Map<String, String> buildPayloadPropsFromMessage(Message message) {
		MessageProperties messageProperties = message.getMessageProperties();
		Map<String, String> payloadProps = new HashMap<String, String>();
		if (StringUtils.hasText(messageProperties.getReceivedExchange())
				|| StringUtils.hasText(messageProperties.getReceivedRoutingKey())) {
			payloadProps.put(AsyncMessageConstants.CREATED_BY,
					messageProperties.getReceivedExchange() + ":" + messageProperties.getReceivedRoutingKey());
		}
		messageProperties.getHeaders().entrySet()
				.forEach(header -> payloadProps.put(header.getKey(), String.valueOf(header.getValue())));

		return payloadProps;
	}

}
