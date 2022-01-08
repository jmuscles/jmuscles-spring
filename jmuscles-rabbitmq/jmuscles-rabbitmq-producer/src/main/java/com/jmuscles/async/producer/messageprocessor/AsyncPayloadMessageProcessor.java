/**
 * 
 */
package com.jmuscles.async.producer.messageprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import com.jmuscles.async.producer.util.AmqpMessageBuilderUtil;
import com.jmuscles.async.producer.util.PayloadObjectMapper;
import com.jmuscles.processing.executor.ExecutorRegistry;
import com.jmuscles.processing.processor.Processor;
import com.jmuscles.processing.schema.Payload;

/**
 * @author manish goel
 *
 */
public class AsyncPayloadMessageProcessor implements MessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AsyncPayloadMessageProcessor.class);

	private ExecutorRegistry executorRegistry;

	public AsyncPayloadMessageProcessor(ExecutorRegistry executorRegistry) {
		super();
		this.executorRegistry = executorRegistry;
	}

	@Override
	public Message process(Message message) {
		Message updatedMessage = null;
		try {
			Payload deserializedPayload = PayloadObjectMapper.deserialize(message.getBody());
			logger.debug("Deserialized message : " + deserializedPayload);

			Payload updatedPayload = Processor.process(deserializedPayload, executorRegistry);

			if (updatedPayload != null) {
				logger.info(
						"Message was not completely processed and updated payload is being serialized in to a message");
				updatedMessage = AmqpMessageBuilderUtil.buildMessage(PayloadObjectMapper.serialize(updatedPayload),
						null, message.getMessageProperties(), null);
			}

		} catch (Exception e) {
			logger.error("Error", e);
			updatedMessage = message;
		}
		return updatedMessage;
	}

}
