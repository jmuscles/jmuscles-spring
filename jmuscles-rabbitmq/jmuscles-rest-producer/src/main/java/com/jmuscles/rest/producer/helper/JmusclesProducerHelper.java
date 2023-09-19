/**
 * 
 */
package com.jmuscles.rest.producer.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.jmuscles.async.producer.AsyncPayloadDeliverer;
import com.jmuscles.async.producer.properties.ProducerConfigProperties;
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
				: new ResponseEntity<>(null, null, HttpStatus.SC_EXPECTATION_FAILED);
	}

	public ResponseEntity<?> processRestRequest(Serializable requestBody, Map<String, String> httpHeader,
			HttpServletRequest request, String configKey, String urlSuffix) {

		RestConfPropsForConfigKey restConfPropsForConfigKey = restProducerConfigPropertiesMap.get(configKey);
		if (restConfPropsForConfigKey == null) {
			throw new RuntimeException(
					"Invalid url, " + configKey + " is not configured. Please correct the configuration");
		}
		String method = request.getMethod();
		RestConfPropsForMethod restConfPropsForMethod = null;

		Map<String, RestConfPropsForMethod> configByHttpMethods = restConfPropsForConfigKey.getConfigByHttpMethods();
		if (configByHttpMethods != null) {
			restConfPropsForMethod = configByHttpMethods.get(method);
		}
		if (restConfPropsForMethod == null) {
			throw new RuntimeException(
					"Invalid http method, " + method + " is not configured. Please correct the configuration");
		}

		ProducerConfigProperties producerConfigProperties = restConfPropsForMethod.getProcessingConfig();
		if (producerConfigProperties == null) {
			producerConfigProperties = restConfPropsForConfigKey.getProcessingConfig();
		}

		boolean queued = queueRestRequest(requestBody, httpHeader, method, configKey, urlSuffix,
				producerConfigProperties);
		logger.debug("Request is queued: " + queued);
		return responseBuilder.buildResponse(createMap(queued, requestBody, httpHeader, request, method, configKey));
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

	private static Map<String, Object> createMap(boolean queued, Serializable requestBody,
			Map<String, String> httpHeader, HttpServletRequest request, String method, String configKey) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("queued", queued);
		map.put("requestBody", requestBody);
		map.put("httpHeader", httpHeader);
		map.put("request", request);
		map.put("method", method);
		map.put("configKey", configKey);
		return map;
	}

}
