/**
 * 
 */
package com.jmuscles.async.producer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmuscles.async.producer.constant.ProducerDataMapKeys;
import com.jmuscles.async.producer.producing.Producer;
import com.jmuscles.async.producer.properties.ProducerConfigProperties;
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
		return send(asyncPayload, trackingDetail, null, null, producerConfigProperties, false);
	}

	public Payload send(Payload asyncPayload, TrackingDetail trackingDetail, String routingKey, String exchange,
			ProducerConfigProperties asyncProducerConfig, boolean isNonPersistentDeliveryMode)
			throws JsonProcessingException {

		Payload unprocessedData = null;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ProducerDataMapKeys.PAYLOAD, asyncPayload);
		map.put(ProducerDataMapKeys.TRACKING_DETAIL, trackingDetail);
		map.put(ProducerDataMapKeys.ROUTING_KEY, routingKey);
		map.put(ProducerDataMapKeys.EXCHANGE, exchange);
		map.put(ProducerDataMapKeys.NON_PERSISTENT_DELIVERY_MODE, isNonPersistentDeliveryMode);

		Iterator<String> iterator = asyncProducerConfig.getActiveProducersInOrder().iterator();
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
