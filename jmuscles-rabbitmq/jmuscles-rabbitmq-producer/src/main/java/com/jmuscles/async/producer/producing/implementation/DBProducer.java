/**
 * 
 */
package com.jmuscles.async.producer.producing.implementation;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmuscles.async.producer.constant.ProducerDataMapKeys;
import com.jmuscles.async.producer.jpa.asyncpayload.AsyncPayloadPersister;
import com.jmuscles.async.producer.producing.SelfRegisteredProducer;
import com.jmuscles.async.producer.util.PayloadObjectMapper;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;

/**
 * @author manish goel
 *
 */
public class DBProducer extends SelfRegisteredProducer {

	private AsyncPayloadPersister asyncPayloadPersister;

	public DBProducer(AsyncPayloadPersister asyncPayloadPersister) {
		super();
		this.asyncPayloadPersister = asyncPayloadPersister;
	}

	@Override
	public void send(Map<String, Object> map) throws JsonProcessingException {
		byte[] messageBody = (byte[]) map.get(ProducerDataMapKeys.MESSAGE_BODY);
		if (messageBody == null) {
			messageBody = PayloadObjectMapper.serialize((Payload) map.get(ProducerDataMapKeys.PAYLOAD));
			map.put(ProducerDataMapKeys.MESSAGE_BODY, messageBody);
		}
		asyncPayloadPersister.persist(messageBody,
				((TrackingDetail) map.get(ProducerDataMapKeys.TRACKING_DETAIL)).getAttributes());
	}

	@Override
	public String getProducerKey() {
		// TODO Auto-generated method stub
		return "database";
	}

}
