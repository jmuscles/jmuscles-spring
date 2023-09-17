/**
 * 
 */
package com.jmuscles.async.producer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmuscles.async.producer.constant.ProducerDataMapKeys;
import com.jmuscles.async.producer.producing.Producer;
import com.jmuscles.async.producer.properties.ProducerConfigProperties;
import com.jmuscles.async.producer.properties.ProducerRabbitmqConfig;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;

/**
 * @author manish goel
 *
 */
public class AsyncPayloadDeliverer {

	private static final Logger logger = LoggerFactory.getLogger(AsyncPayloadDeliverer.class);

	private ProducerConfigProperties producerConfigProperties;

	public AsyncPayloadDeliverer(ProducerConfigProperties producerConfigProperties) {
		this.producerConfigProperties = producerConfigProperties;
	}

	public Payload send(Payload asyncPayload, TrackingDetail trackingDetail) throws JsonProcessingException {
		return send(asyncPayload, trackingDetail, null, null);
	}

	public Payload send(Payload asyncPayload, TrackingDetail trackingDetail, List<String> activeProducersInOrder,
			ProducerRabbitmqConfig rabbitmqConfig) throws JsonProcessingException {

		String routingKey = null;
		String exchange = null;
		boolean isNonPersistentDeliveryMode = false;
		if (rabbitmqConfig != null) {
			routingKey = rabbitmqConfig.getDefaultRoutingKey();
			exchange = rabbitmqConfig.getDefaultExchange();
			isNonPersistentDeliveryMode = rabbitmqConfig.isNonPersistentDeliveryMode();
		}
		if (activeProducersInOrder == null) {
			activeProducersInOrder = this.producerConfigProperties.getActiveProducersInOrder();
		}
		return send(asyncPayload, trackingDetail, routingKey, exchange,
				producerConfigProperties.getActiveProducersInOrder(), isNonPersistentDeliveryMode);
	}

	public Payload send(Payload asyncPayload, TrackingDetail trackingDetail, String routingKey, String exchange,
			List<String> activeProducersInOrder, boolean isNonPersistentDeliveryMode) throws JsonProcessingException {

		Payload unprocessedData = null;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ProducerDataMapKeys.PAYLOAD, asyncPayload);
		map.put(ProducerDataMapKeys.TRACKING_DETAIL, trackingDetail);
		map.put(ProducerDataMapKeys.ROUTING_KEY, routingKey);
		map.put(ProducerDataMapKeys.EXCHANGE, exchange);
		map.put(ProducerDataMapKeys.NON_PERSISTENT_DELIVERY_MODE, isNonPersistentDeliveryMode);

		Iterator<String> iterator = activeProducersInOrder.iterator();
		while (true) {
			if (iterator.hasNext()) {
				String key = iterator.next();
				try {
					Producer producer = Producer.get(key);
					producer.send(map);
				} catch (Exception e) {
					logger.error("issue while sending the message with producer: " + key, e);
					continue;
				}
			} else {
				unprocessedData = (Payload) map.get(ProducerDataMapKeys.PAYLOAD);
				logger.error("No more producer available to successfully process the message. trackingDetail : "
						+ trackingDetail);
			}
			break;
		}

		return unprocessedData;
	}

}
