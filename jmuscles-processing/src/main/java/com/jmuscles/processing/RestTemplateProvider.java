/**
 * 
 */
package com.jmuscles.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.jmuscles.processing.config.ExecutorConfigProperties;
import com.jmuscles.processing.config.RestConfig;

/**
 * @author manish goel
 *
 */
public class RestTemplateProvider {
	private static final Logger logger = LoggerFactory.getLogger(RestTemplateProvider.class);

	private RestConfig restTemplateConfigProps;

	private RestTemplate asyncRestTemplate;

	public RestTemplateProvider(ExecutorConfigProperties configProperties) {
		logger.debug("Constructor....");
		this.restTemplateConfigProps = configProperties.getRestConfig();
		initialize();
	}

	private void initialize() {
		logger.debug("initialize() method start .... ");
		asyncRestTemplate = new RestTemplate();
		SimpleClientHttpRequestFactory httpRequestFactory = (SimpleClientHttpRequestFactory) asyncRestTemplate
				.getRequestFactory();
		httpRequestFactory.setConnectTimeout(
				restTemplateConfigProps.getConnectionTimeout() != null ? restTemplateConfigProps.getConnectionTimeout()
						: 10000);
		httpRequestFactory.setReadTimeout(
				restTemplateConfigProps.getReadTimeout() != null ? restTemplateConfigProps.getReadTimeout() : 10000);
		logger.debug(".....initialize() method end");
	}

	public RestTemplate get() {
		return asyncRestTemplate;
	}

}
