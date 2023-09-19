package com.jmuscles.rest.producer.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.jmuscles.rest.producer.config.RestConfPropsForConfigKey;

public class ResponseBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

	@Autowired
	private Map<String, RestConfPropsForConfigKey> restProducerConfigPropertiesMap;

	public ResponseEntity<?> buildResponse(Map<String, Object> map) {

		RestConfPropsForConfigKey restConfPropsForConfigKey = (RestConfPropsForConfigKey) restProducerConfigPropertiesMap
				.get(map.get(ResponseBuilderMapKeys.CONFIG_KEY));

		BaseResponseBuilder responseBuilder = ResponseBuilderRegistry
				.get(StringUtils.hasText(restConfPropsForConfigKey.getResponseBuilder())
						? restConfPropsForConfigKey.getResponseBuilder()
						: SimpleResponseBuilder.class.getSimpleName());

		logger.debug("ResponseBuilder: " + responseBuilder.getClass().getSimpleName());

		return responseBuilder.buildResponse(map);
	}

	public Map<String, Object> createMap(boolean queued, Serializable requestBody, Map<String, String> httpHeader,
			HttpServletRequest request, String method, String configKey) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(ResponseBuilderMapKeys.IS_MESSAGE_QUEUED, queued);
		map.put(ResponseBuilderMapKeys.REQUEST_BODY, requestBody);
		map.put(ResponseBuilderMapKeys.HTTP_HEADERS, httpHeader);
		map.put(ResponseBuilderMapKeys.HTTP_REQUEST, request);
		map.put(ResponseBuilderMapKeys.HTTP_METHOD, method);
		map.put(ResponseBuilderMapKeys.CONFIG_KEY, configKey);

		return map;
	}

}
