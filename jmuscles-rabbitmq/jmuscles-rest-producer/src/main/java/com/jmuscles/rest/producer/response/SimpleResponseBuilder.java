/**
 * 
 */
package com.jmuscles.rest.producer.response;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.jmuscles.rest.producer.config.RestConfPropsForConfigKey;
import com.jmuscles.rest.producer.config.RestResponseConfig;

/**
 * 
 */
public class SimpleResponseBuilder extends BaseResponseBuilder {

	private static final Logger logger = LoggerFactory.getLogger(SimpleResponseBuilder.class);

	@Autowired
	private Map<String, RestConfPropsForConfigKey> restProducerConfigPropertiesMap;

	@Override
	public ResponseEntity<?> buildResponse(Map<String, Object> map) {

		String configKey = (String) map.get("configKey");
		boolean queued = (boolean) map.get("queued");
		String method = (String) map.get("method");

		RestResponseConfig response = restProducerConfigPropertiesMap.get(configKey).getConfigByHttpMethods()
				.get(method).getResponseConfig().get(queued ? "success" : "failure");
		return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatus());

	}

}
