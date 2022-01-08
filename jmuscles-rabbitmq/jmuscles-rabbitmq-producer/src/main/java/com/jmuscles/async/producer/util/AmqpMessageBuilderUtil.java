/**
 * 
 */
package com.jmuscles.async.producer.util;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

import com.jmuscles.async.producer.constant.AsyncMessageConstants;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;

/**
 * @author manish goel
 *
 */
public class AmqpMessageBuilderUtil {

	public static Message buildMessage(byte[] payload, TrackingDetail trackingDetail,
			MessageProperties messageProperties, MessageDeliveryMode messageDeliveryMode) {
		return new Message(payload, buildMessageProperties(trackingDetail, messageProperties, messageDeliveryMode));
	}

	public static MessageProperties buildMessageProperties(TrackingDetail trackingDetail,
			MessageProperties messageProperties, MessageDeliveryMode messageDeliveryMode) {
		if (messageProperties == null) {
			messageProperties = new MessageProperties();
		}
		if (messageDeliveryMode == null) {
			messageDeliveryMode = MessageDeliveryMode.PERSISTENT;
		}
		messageProperties.setDeliveryMode(messageDeliveryMode);
		Map<String, Object> headers = messageProperties.getHeaders();
		headers.put(AsyncMessageConstants.PAYLOAD_TYPE, Payload.class.getSimpleName());
		if (trackingDetail != null) {
			trackingDetail.getAttributes().entrySet().forEach(entry -> headers.put(entry.getKey(), entry.getValue()));
		}
		return messageProperties;
	}

}
