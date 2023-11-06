package com.jmuscles.rest.producer.response;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.requestdata.RestRequestData;
import com.jmuscles.rest.producer.config.properties.RestConfPropsForConfigKey;

public class ResponseBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

	private Map<String, RestConfPropsForConfigKey> restProducerConfigPropertiesMap;

	public ResponseBuilder(Map<String, RestConfPropsForConfigKey> restProducerConfigPropertiesMap) {
		this.restProducerConfigPropertiesMap = restProducerConfigPropertiesMap;
	}

	public ResponseEntity<?> buildResponse(boolean queued, RestRequestData restRequestData, HttpServletRequest request,
			Payload payload) {

		RestConfPropsForConfigKey restConfPropsForConfigKey = (RestConfPropsForConfigKey) restProducerConfigPropertiesMap
				.get(restRequestData.getConfigKey());

		BaseResponseBuilder responseBuilder = ResponseBuilderRegistry
				.get(StringUtils.hasText(restConfPropsForConfigKey.getResponseBuilder())
						? restConfPropsForConfigKey.getResponseBuilder()
						: SimpleResponseBuilder.class.getSimpleName());

		logger.debug("ResponseBuilder: " + responseBuilder.getClass().getSimpleName());

		return responseBuilder.buildResponse(createMap(queued, restRequestData, request, payload));
	}

	public Map<String, Object> createMap(boolean queued, RestRequestData restRequestData, HttpServletRequest request,
			Payload payload) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(ResponseBuilderMapKeys.IS_MESSAGE_QUEUED, queued);
		map.put(ResponseBuilderMapKeys.REST_REQUEST_DATA, restRequestData);
		map.put(ResponseBuilderMapKeys.HTTP_REQUEST, request);
		map.put(ResponseBuilderMapKeys.PAYLOAD, payload);

		return map;
	}

}
