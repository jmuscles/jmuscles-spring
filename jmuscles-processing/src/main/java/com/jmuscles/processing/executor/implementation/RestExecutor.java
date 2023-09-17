/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.jmuscles.processing.RestTemplateProvider;
import com.jmuscles.processing.config.ExecutorConfigProperties;
import com.jmuscles.processing.config.RestCallConfig;
import com.jmuscles.processing.executor.ExecutorRegistry;
import com.jmuscles.processing.executor.SelfRegisteredExecutor;
import com.jmuscles.processing.execvalidator.RestValidator;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.RestRequestData;

/**
 * @author manish goel
 *
 */
public class RestExecutor extends SelfRegisteredExecutor {

	private static final Logger logger = LoggerFactory.getLogger(RestExecutor.class);

	private ExecutorConfigProperties executorConfigProperties;
	private RestTemplateProvider restTemplateProvider;

	public RestExecutor(ExecutorRegistry executorRegistry, ExecutorConfigProperties executorConfigProperties,
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
		Object response = invoke(restRequestData, restConfig.getUrl(), restValidator.responseEntityClass());
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

	private Object invoke(RestRequestData restRequestData, String url, Class<?> responseEntityClass) {
		HttpHeaders headers = new HttpHeaders();
		if (restRequestData.getHttpHeader() != null) {
			restRequestData.getHttpHeader().entrySet().stream()
					.forEach(entry -> headers.add(entry.getKey(), entry.getValue()));
		}
		Object response = null;

		try {
			String finalUrl = generateUrl(url, restRequestData.getUrlSuffix());
			HttpMethod httpMethod = HttpMethod.valueOf(restRequestData.getMethod());
			RestTemplate restTemplate = restTemplateProvider.get();
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
		logger.info("rest call done for url: " + url);
		return response;
	}

	private String generateUrl(String url, String urlSuffix) {
		logger.debug("url: " + url + ", urlSuffix: " + urlSuffix);
		if (StringUtils.hasText(urlSuffix)) {
			url = url.trim();
			urlSuffix = urlSuffix.trim();
			url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
			urlSuffix = urlSuffix.startsWith("/") ? urlSuffix.substring(1, urlSuffix.length()) : urlSuffix;
		}
		url = StringUtils.hasText(urlSuffix) ? url + "/" + urlSuffix : url;
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
