package com.jmuscles.rest.producer.response;

import java.util.Map;

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
				.get(map.get("configKey"));

		BaseResponseBuilder responseBuilder = ResponseBuilderRegistry
				.get(StringUtils.hasText(restConfPropsForConfigKey.getResponseBuilder())
						? restConfPropsForConfigKey.getResponseBuilder()
						: SimpleResponseBuilder.class.getSimpleName());

		logger.debug("ResponseBuilder: " + responseBuilder.getClass().getSimpleName());
		
		return responseBuilder.buildResponse(map);
	}

}
