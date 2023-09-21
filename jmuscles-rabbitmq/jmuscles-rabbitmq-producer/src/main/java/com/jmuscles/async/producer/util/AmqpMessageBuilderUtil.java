/**
 * 
 */
package com.jmuscles.async.producer.util;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.util.StringUtils;

import com.jmuscles.async.producer.constant.AsyncMessageConstants;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;

/**
 * @author manish goel
 *
 */
public class AmqpMessageBuilderUtil {

	private static final Logger logger = LoggerFactory.getLogger(AmqpMessageBuilderUtil.class);

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
		trackingDetail = resolveForTraceId(trackingDetail);
		if (trackingDetail != null) {
			trackingDetail.getAttributes().entrySet().forEach(entry -> headers.put(entry.getKey(), entry.getValue()));
		}
		return messageProperties;
	}

	public static TrackingDetail resolveForTraceId(TrackingDetail trackingDetail) {
		String jmuscleTraceId = null;
		if (trackingDetail == null) {
			trackingDetail = TrackingDetail.of();
		} else {
			jmuscleTraceId = trackingDetail.getAttributes().get(JmusclesProducerConstants.JMUSCLE_TRACE_ID);
		}
		if (!StringUtils.hasText(jmuscleTraceId)) {
			jmuscleTraceId = MDC.get(JmusclesProducerConstants.JMUSCLE_TRACE_ID);
			if (!StringUtils.hasText(jmuscleTraceId)) {
				jmuscleTraceId = UUID.randomUUID().toString();
				logger.info("jmuscleTraceId is created and bwing added to the trackingDetail {} : {} ",
						JmusclesProducerConstants.JMUSCLE_TRACE_ID, jmuscleTraceId);
			}
			trackingDetail.getAttributes().put(JmusclesProducerConstants.JMUSCLE_TRACE_ID, jmuscleTraceId);
		}
		return trackingDetail;
	}

}
