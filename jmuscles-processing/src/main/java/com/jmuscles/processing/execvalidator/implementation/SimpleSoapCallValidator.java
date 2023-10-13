/**
 * 
 */
package com.jmuscles.processing.execvalidator.implementation;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

import com.jmuscles.processing.config.properties.RestCallConfig;
import com.jmuscles.processing.execvalidator.RestValidator;
import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class SimpleSoapCallValidator extends RestValidator {

	@Override
	public Class<?> responseEntityClass() {
		return String.class;
	}

	@Override
	public boolean validateRequest(RequestData requestData, Object config) {
		return true;
	}

	@Override
	public boolean validateResponse(RequestData requestData, Object response, Object config) {
		RestCallConfig restConfig = (RestCallConfig) config;
		boolean validated = false;
		if (response instanceof RestClientResponseException) {
			RestClientResponseException exception = (RestClientResponseException) response;
			validated = checkResponse(exception.getRawStatusCode(), exception.getResponseBodyAsString(), restConfig);
		} else if (response instanceof ResponseEntity<?>) {
			ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
			validated = checkResponse(responseEntity.getStatusCodeValue(), responseEntity.getBody(), restConfig);
		}

		return validated;
	}

	private boolean checkResponse(int statusCode, Object body, RestCallConfig restConfig) {
		boolean isSuccess = false;
		Map<Integer, List<String>> successCodePatterns = restConfig.getSuccessCodePatterns();
		if (successCodePatterns == null || successCodePatterns.isEmpty()) {
			isSuccess = statusCode > 199 && statusCode < 300;
		} else {
			isSuccess = restConfig.getSuccessCodePatterns().containsKey(statusCode)
					? checkResponseBodyValidation(statusCode, body, successCodePatterns.get(statusCode))
					: false;
		}
		return isSuccess;
	}

	private boolean checkResponseBodyValidation(int statusCode, Object body, List<String> bodyPatterns) {
		boolean isSuccess = false;
		if (bodyPatterns != null && !bodyPatterns.isEmpty() && null != body) {
			String bodyInString = body.toString();
			for (String pattern : bodyPatterns) {
				if (Pattern.matches(pattern, bodyInString)) {
					isSuccess = true;
					break;
				}
			}
		} else {
			isSuccess = true;
		}
		return isSuccess;
	}

}
