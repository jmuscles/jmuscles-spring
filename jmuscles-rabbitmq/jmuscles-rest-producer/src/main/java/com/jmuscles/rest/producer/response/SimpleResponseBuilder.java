/**
 * 
 */
package com.jmuscles.rest.producer.response;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jmuscles.processing.schema.requestdata.RestRequestData;
import com.jmuscles.rest.producer.config.RestConfPropsForConfigKey;
import com.jmuscles.rest.producer.config.RestConfPropsForMethod;
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
		logger.debug("Start building reponse...");

		RestRequestData restRequestData = (RestRequestData) map.get(ResponseBuilderMapKeys.REST_REQUEST_DATA);
		boolean queued = (boolean) map.get(ResponseBuilderMapKeys.IS_MESSAGE_QUEUED);

		Map<String, RestConfPropsForMethod> restConfPropsForMethodmap = restProducerConfigPropertiesMap
				.get(restRequestData.getConfigKey()).getConfigByHttpMethods();

		ResponseEntity<?> responseEntity = null;

		if (restConfPropsForMethodmap != null && restConfPropsForMethodmap.get(restRequestData.getMethod()) != null
				&& restConfPropsForMethodmap.get(restRequestData.getMethod()).getResponseConfig() != null) {
			RestResponseConfig response = restConfPropsForMethodmap.get(restRequestData.getMethod()).getResponseConfig()
					.get(queued ? "success" : "failure");
			if (response != null) {
				responseEntity = new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatus());
			}
		}

		if (responseEntity == null) {
			logger.error("There is no configuartion to build reponse for configKey:{}, method:{} and {} scenario",
					restRequestData.getConfigKey(), restRequestData.getMethod(), (queued ? "success" : "failure"));
			if (queued) {
				responseEntity = ResponseEntity.ok("Request is queued to be processed");
			} else {
				responseEntity = new ResponseEntity<>("Request could not be queued", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		logger.debug("... reponse built successfully");
		return responseEntity;

	}

}
