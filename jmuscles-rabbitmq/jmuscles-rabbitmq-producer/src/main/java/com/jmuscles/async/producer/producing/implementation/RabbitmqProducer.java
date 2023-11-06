/**
 * 
 */
package com.jmuscles.async.producer.producing.implementation;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmuscles.async.producer.RabbitTemplateProvider;
import com.jmuscles.async.producer.config.properties.ProducerConfigProperties;
import com.jmuscles.async.producer.config.properties.ProducerRabbitmqConfig;
import com.jmuscles.async.producer.constant.ProducerDataMapKeys;
import com.jmuscles.async.producer.producing.SelfRegisteredProducer;
import com.jmuscles.async.producer.util.AmqpMessageBuilderUtil;
import com.jmuscles.async.producer.util.PayloadObjectMapper;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;

/**
 * @author manish goel
 *
 */
public class RabbitmqProducer extends SelfRegisteredProducer {

	private static final Logger logger = LoggerFactory.getLogger(RabbitmqProducer.class);

	private ProducerConfigProperties producerConfigProperties;
	private RabbitTemplateProvider rabbitTemplateProvider;

	public RabbitmqProducer(ProducerConfigProperties producerConfigProperties,
			RabbitTemplateProvider rabbitTemplateProvider) {
		super();
		this.producerConfigProperties = producerConfigProperties;
		this.rabbitTemplateProvider = rabbitTemplateProvider;
	}

	@Override
	public void send(Map<String, Object> map) throws JsonProcessingException {
		send(map, producerConfigProperties.getRabbitmq());
	}

	public void send(Map<String, Object> map, ProducerRabbitmqConfig rabbitConfig) throws JsonProcessingException {
		if (rabbitConfig == null) {
			logger.error("ProducerRabbitmqConfig is not set hence Message can not be delivered to rabbitMQ");
			throw new RuntimeException(
					"ProducerRabbitmqConfig is not set hence Message can not be delivered to rabbitMQ");
		}
		String exchange = map.get(ProducerDataMapKeys.EXCHANGE) != null ? (String) map.get(ProducerDataMapKeys.EXCHANGE)
				: rabbitConfig.getDefaultExchange();
		String routingKey = map.get(ProducerDataMapKeys.ROUTING_KEY) != null
				? (String) map.get(ProducerDataMapKeys.ROUTING_KEY)
				: rabbitConfig.getDefaultRoutingKey();

		byte[] messageBody = (byte[]) map.get(ProducerDataMapKeys.MESSAGE_BODY);
		if (messageBody == null) {
			messageBody = PayloadObjectMapper.serialize((Payload) map.get(ProducerDataMapKeys.PAYLOAD));
			map.put(ProducerDataMapKeys.MESSAGE_BODY, messageBody);
		}
		MessageDeliveryMode messageDeliveryMode = getMessageDeliveryMode(
				map.get(ProducerDataMapKeys.NON_PERSISTENT_DELIVERY_MODE) != null
						? (boolean) map.get(ProducerDataMapKeys.NON_PERSISTENT_DELIVERY_MODE)
						: rabbitConfig.isNonPersistentDeliveryMode());
		Message message = AmqpMessageBuilderUtil.buildMessage(messageBody,
				(TrackingDetail) map.get(ProducerDataMapKeys.TRACKING_DETAIL), null, messageDeliveryMode);

		this.rabbitTemplateProvider.getRabbitTemplate().send(exchange, routingKey, message);
	}

	private MessageDeliveryMode getMessageDeliveryMode(boolean isNonPersistentDeliveryMode) {
		return isNonPersistentDeliveryMode ? MessageDeliveryMode.NON_PERSISTENT : MessageDeliveryMode.PERSISTENT;
	}

	@Override
	public String getProducerKey() {
		// TODO Auto-generated method stub
		return "rabbitmq";
	}

}
