/**
 * 
 */
package com.jmuscles.rest.producer.helper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jmuscles.async.producer.AsyncPayloadDeliverer;
import com.jmuscles.async.producer.config.properties.ProducerConfigProperties;
import com.jmuscles.async.producer.util.JmusclesRestResponseException;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.RestRequestData;
import com.jmuscles.rest.producer.config.properties.RestConfPropsForConfigKey;
import com.jmuscles.rest.producer.config.properties.RestConfPropsForMethod;
import com.jmuscles.rest.producer.response.ResponseBuilder;

/**
 * 
 */
public class JmusclesProducerHelper {

	private static final Logger logger = LoggerFactory.getLogger(JmusclesProducerHelper.class);

	@Autowired
	private AsyncPayloadDeliverer asyncPayloadDeliverer;

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private Map<String, RestConfPropsForConfigKey> restProducerConfigPropertiesMap;

	public ResponseEntity<?> queuePayload(Payload payload, TrackingDetail trackingDetail) {
		return queuePayload(payload, trackingDetail, null) ? ResponseEntity.ok("Success")
				: new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> processRestRequest(Serializable requestBody, Map<String, String> httpHeader,
			HttpServletRequest request, String configKey, String urlSuffix) {

		RestConfPropsForConfigKey restConfPropsForConfigKey = restProducerConfigPropertiesMap.get(configKey);
		if (restConfPropsForConfigKey == null) {
			throw JmusclesRestResponseException.of(
					"Invalid url, Please onboard the endpoint ending with : " + configKey,
					org.springframework.http.HttpStatus.NOT_FOUND);
		}
		ProducerConfigProperties producerConfigProperties = null;

		String method = request.getMethod();
		Map<String, RestConfPropsForMethod> configByHttpMethods = restConfPropsForConfigKey.getConfigByHttpMethods();
		if (configByHttpMethods != null && configByHttpMethods.get(method) != null) {
			producerConfigProperties = configByHttpMethods.get(method).getProcessingConfig();
		}

		if (producerConfigProperties == null) {
			producerConfigProperties = restConfPropsForConfigKey.getProcessingConfig();
		}
		RestRequestData restRequestData = new RestRequestData(method, configKey, urlSuffix, requestBody, httpHeader);
		Payload payload = new Payload(Arrays.asList((RequestData) restRequestData));

		boolean queued = queuePayload(payload, null, producerConfigProperties);
		return responseBuilder.buildResponse(queued, restRequestData, request, payload);
	}

	public boolean queuePayload(Payload payload, TrackingDetail trackingDetail,
			ProducerConfigProperties producerConfigProperties) {

		Payload remainingPayload = null;
		try {
			if (producerConfigProperties == null) {
				remainingPayload = asyncPayloadDeliverer.send(payload, trackingDetail);
			} else {
				remainingPayload = asyncPayloadDeliverer.send(payload, trackingDetail,
						producerConfigProperties.getActiveProducersInOrder(), producerConfigProperties.getRabbitmq());
			}
		} catch (Exception e) {
			logger.error("issue while queueing the payload", e);
		}

		return (remainingPayload == null);
	}

}
