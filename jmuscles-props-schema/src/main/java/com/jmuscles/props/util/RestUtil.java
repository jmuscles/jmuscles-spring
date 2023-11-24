package com.jmuscles.props.util;

import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author manish goel
 *
 */
public class RestUtil {

	public static RestTemplate createRestTemplateWithTimeouts(Integer connectionTimeout, Integer readTimeout) {

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(connectionTimeout != null ? connectionTimeout : 10000);
		requestFactory.setReadTimeout(readTimeout != null ? readTimeout : 10000);

		ClientHttpRequestFactory clientHttpRequestFactory = requestFactory;

		return new RestTemplateBuilder().requestFactory(() -> clientHttpRequestFactory).build();
	}

	public static <T> ResponseEntity<T> execute(String url, Map<String, String> httpHeaderMap, HttpMethod httpMethod,
			Object body, Class<T> responseClass, RestTemplate restTemplate) {

		HttpHeaders headers = new HttpHeaders();
		if (httpHeaderMap != null) {
			httpHeaderMap.entrySet().stream().forEach(entry -> headers.add(entry.getKey(), entry.getValue()));
		}

		// Create a HttpEntity with headers
		HttpEntity<?> entity = new HttpEntity<>(body, headers);

		// Make the request with headers
		ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, entity, responseClass);

		return responseEntity;

	}

}
