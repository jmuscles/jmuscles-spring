/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.jmuscles.processing.RestTemplateProvider;
import com.jmuscles.processing.config.properties.ExecutorConfigProperties;
import com.jmuscles.processing.config.properties.RestCallConfig;
import com.jmuscles.processing.executor.StandardExecutor;
import com.jmuscles.processing.executor.StandardExecutorRegistry;
import com.jmuscles.processing.execvalidator.RestValidator;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.RestRequestData;

/**
 * @author manish goel
 *
 */
public class RestExecutor extends StandardExecutor {

	private static final Logger logger = LoggerFactory.getLogger(RestExecutor.class);

	private ExecutorConfigProperties executorConfigProperties;
	private RestTemplateProvider restTemplateProvider;

	public RestExecutor(StandardExecutorRegistry executorRegistry, ExecutorConfigProperties executorConfigProperties,
			RestTemplateProvider restTemplateProvider) {
		super(executorRegistry);
		this.executorConfigProperties = executorConfigProperties;
		this.restTemplateProvider = restTemplateProvider;
	}

	@Override
	public RequestData execute(RequestData requestData) {
		logger.info("strat ...... ");
		RestRequestData restRequestData = (RestRequestData) requestData;
		RestCallConfig restConfig = getExecutorRestConfig(restRequestData.getConfigKey());
		RestValidator restValidator = RestValidator.get(restConfig.getValidator());
		Object response = invoke(restRequestData, restConfig, restValidator.responseEntityClass());
		boolean validated = restValidator.validateResponse(restRequestData, response, restConfig);
		logger.info("...... end");
		return validated ? null : restRequestData;
	}

	private RestCallConfig getExecutorRestConfig(String key) {
		RestCallConfig restCallConfig = (RestCallConfig) executorConfigProperties.getRestCalls().get(key);
		if (restCallConfig == null) {
			logger.error("Rest configuration in missing for key: " + key);
			throw new RuntimeException("Rest configuration in missing for key: " + key);
		}
		return restCallConfig;
	}

	public Object invoke(RestRequestData restRequestData, RestCallConfig restConfig, Class<?> responseEntityClass) {

		Object response = null;
		try {
			HttpHeaders headers = resolveHeaders(restRequestData, restConfig);
			String finalUrl = generateUrl(restConfig.getUrl(), restRequestData.getUrlSuffix());
			HttpMethod httpMethod = HttpMethod.valueOf(restRequestData.getMethod());
			RestTemplate restTemplate = httpMethod.equals(HttpMethod.PATCH)
					? restTemplateProvider.getRestTemplateHttpComponentsClient()
					: restTemplateProvider.getRestTemplateSimpleClient();
			if (logger.isDebugEnabled()) {
				printRequestDataForDebugLogs(finalUrl, httpMethod, headers, restRequestData.getBody(),
						responseEntityClass);
			}
			HttpEntity<?> entity = new HttpEntity<>(restRequestData.getBody(), headers);
			response = restTemplate.exchange(finalUrl, httpMethod, entity, responseEntityClass);
		} catch (Exception e) {
			logger.error("Error while processing the rest", e);
			response = e;
		}
		logger.info("rest call done for url: " + restConfig.getUrl());
		return response;
	}

	/**
	 * Headers are set and over-ridden in following order: 1. Common Headers 2.
	 * Coming from producer 3. Rest Call configuration
	 * 
	 * @param restRequestData
	 * @param restConfig
	 * @return
	 */
	private HttpHeaders resolveHeaders(RestRequestData restRequestData, RestCallConfig restConfig) {
		Map<String, String> httpHeaderMap = new HashMap<>();
		if (executorConfigProperties.getRestConfig() != null
				&& executorConfigProperties.getRestConfig().getCommonHeaders() != null) {
			httpHeaderMap.putAll(executorConfigProperties.getRestConfig().getCommonHeaders());
		}
		if (restRequestData.getHttpHeader() != null) {
			httpHeaderMap.putAll(restRequestData.getHttpHeader());
		}
		if (restConfig.getHttpHeader() != null) {
			httpHeaderMap.putAll(restConfig.getHttpHeader());
		}

		HttpHeaders headers = new HttpHeaders();
		httpHeaderMap.entrySet().stream().forEach(entry -> headers.add(entry.getKey(), entry.getValue()));
		return headers;
	}

	private String generateUrl(String url, String urlSuffix) throws UnsupportedEncodingException {
		logger.debug("url: " + url + ", urlSuffix: " + urlSuffix);
		if (StringUtils.hasText(urlSuffix)) {
			url = url.trim();
			urlSuffix = urlSuffix.trim();
			url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
			urlSuffix = urlSuffix.startsWith("/") ? urlSuffix.substring(1, urlSuffix.length()) : urlSuffix;
		}
		if (StringUtils.hasText(urlSuffix)) {
			String decodedUrlSuffix = URLDecoder.decode(urlSuffix, StandardCharsets.UTF_8.toString());
			url = url + "/" + decodedUrlSuffix;
		}
		logger.info("rest url: " + url);
		return url;
	}

	@Override
	public Class<?> getExecutorRequestDataClass() {
		return RestRequestData.class;
	}

	private void printRequestDataForDebugLogs(String finalUrl, HttpMethod httpMethod, HttpHeaders headers,
			Serializable body, Class<?> responseEntityClass) {
		try {
			String bodyAsString = "";
			logger.debug("url : " + finalUrl);
			logger.debug("httpMethod : " + httpMethod);
			logger.debug("headers : " + headers.toString());
			try {
				bodyAsString = body.toString();
			} catch (Exception e) {
				logger.debug("Request body can't be printed ..body.toString() returns error");
			}
			logger.debug("body : " + bodyAsString);

		} catch (Exception e) {
			logger.debug("Error while printing the rest request data", e);
		}
	}

}
