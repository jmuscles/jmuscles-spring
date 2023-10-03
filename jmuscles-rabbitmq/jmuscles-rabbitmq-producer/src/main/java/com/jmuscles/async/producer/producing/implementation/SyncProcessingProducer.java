/**
 * 
 */
package com.jmuscles.async.producer.producing.implementation;

import java.util.Map;

import com.jmuscles.async.producer.constant.ProducerDataMapKeys;
import com.jmuscles.async.producer.producing.SelfRegisteredProducer;
import com.jmuscles.processing.executor.StandardExecutorRegistry;
import com.jmuscles.processing.processor.Processor;
import com.jmuscles.processing.schema.Payload;

/**
 * @author manish goel
 *
 */
public class SyncProcessingProducer extends SelfRegisteredProducer {

	private StandardExecutorRegistry executorRegistry;

	public SyncProcessingProducer(StandardExecutorRegistry executorRegistry) {
		super();
		this.executorRegistry = executorRegistry;
	}

	@Override
	public void send(Map<String, Object> map) {
		Payload asyncPayload = (Payload) map.get(ProducerDataMapKeys.PAYLOAD);
		Payload unprocessedPayload = Processor.process(asyncPayload, executorRegistry);
		if (unprocessedPayload != null) {
			map.put(ProducerDataMapKeys.PAYLOAD, unprocessedPayload);
			throw new RuntimeException("Message was not successfully processed");
		}
	}

	@Override
	public String getProducerKey() {
		// TODO Auto-generated method stub
		return "syncProcessing";
	}

}
