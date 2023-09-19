/**
 * 
 */
package com.jmuscles.rest.producer.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jmuscles.async.producer.AsyncPayloadDeliverer;
import com.jmuscles.async.producer.properties.ProducerConfigProperties;
import com.jmuscles.async.producer.util.JmusclesRestResponseException;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.RestRequestData;
import com.jmuscles.rest.producer.config.RestConfPropsForConfigKey;
import com.jmuscles.rest.producer.config.RestConfPropsForMethod;
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
		boolean queued = false;
		try {
			if (asyncPayloadDeliverer.send(payload, trackingDetail) == null) {
				queued = true;
			}
		} catch (Exception e) {
			logger.error("issue in queuePayload: " + trackingDetail.toString(), e);
		}
		return queued ? ResponseEntity.ok("Success")
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
		String method = request.getMethod();
		ProducerConfigProperties producerConfigProperties = null;

		Map<String, RestConfPropsForMethod> configByHttpMethods = restConfPropsForConfigKey.getConfigByHttpMethods();
		if (configByHttpMethods != null && configByHttpMethods.get(method) != null) {
			producerConfigProperties = configByHttpMethods.get(method).getProcessingConfig();
		}

		if (producerConfigProperties == null) {
			producerConfigProperties = restConfPropsForConfigKey.getProcessingConfig();
		}

		boolean queued = queueRestRequest(requestBody, httpHeader, method, configKey, urlSuffix,
				producerConfigProperties);
		return responseBuilder
				.buildResponse(responseBuilder.createMap(queued, requestBody, httpHeader, request, method, configKey));
	}

	public boolean queueRestRequest(Serializable requestBody, Map<String, String> httpHeader, String httpMethod,
			String configKey, String urlSuffix, ProducerConfigProperties producerConfigProperties) {

		Payload payload = buildPayload(httpMethod, configKey, urlSuffix, requestBody, httpHeader);

		boolean queued = false;
		try {
			Payload remainingPayload = null;
			if (producerConfigProperties == null) {
				remainingPayload = asyncPayloadDeliverer.send(payload, TrackingDetail.of());
			} else {
				remainingPayload = asyncPayloadDeliverer.send(payload, TrackingDetail.of(),
						producerConfigProperties.getActiveProducersInOrder(), producerConfigProperties.getRabbitmq());
			}
			if (remainingPayload == null) {
				queued = true;
			}
		} catch (Exception e) {
			logger.error("issue in queueRestRequest: " + configKey, e);
		}

		return queued;
	}

	private Payload buildPayload(String method, String configKey, String urlSuffix, Serializable body,
			Map<String, String> httpHeader) {
		RestRequestData restRequestData = new RestRequestData(method, configKey, urlSuffix, body, httpHeader);

		ArrayList<RequestData> list = new ArrayList<RequestData>();
		list.add(restRequestData);
		return new Payload(list);
	}

}
