/**
 * 
 */
package com.jmuscles.processing;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.jmuscles.processing.config.properties.ExecutorConfigProperties;
import com.jmuscles.processing.config.properties.RestConfig;

/**
 * @author manish goel
 *
 */
public class RestTemplateProvider {
	private static final Logger logger = LoggerFactory.getLogger(RestTemplateProvider.class);

	private RestConfig restTemplateConfigProps;

	private RestTemplate restTemplateSimpleClient;
	private RestTemplate restTemplateHttpComponentsClient;

	public RestTemplateProvider(ExecutorConfigProperties configProperties) {
		logger.debug("Constructor....");
		this.restTemplateConfigProps = configProperties.getRestConfig();
		initForSimpleClient();
		initForHttpComponentsClient();
	}

	private void initForSimpleClient() {
		logger.debug("initForSimpleClient() method start .... ");
		restTemplateSimpleClient = new RestTemplate();

		SimpleClientHttpRequestFactory httpRequestFactory = (SimpleClientHttpRequestFactory) restTemplateSimpleClient
				.getRequestFactory();
		httpRequestFactory.setConnectTimeout(
				restTemplateConfigProps.getConnectionTimeout() != null ? restTemplateConfigProps.getConnectionTimeout()
						: 10000);
		httpRequestFactory.setReadTimeout(
				restTemplateConfigProps.getReadTimeout() != null ? restTemplateConfigProps.getReadTimeout() : 10000);
		restTemplateSimpleClient.setRequestFactory(httpRequestFactory);

		logger.debug(".....initForSimpleClient() method end");
	}

	private void initForHttpComponentsClient() {
		logger.debug("initForHttpComponentsClient() method start .... ");
		restTemplateHttpComponentsClient = new RestTemplate();

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);
		httpRequestFactory.setConnectTimeout(
				restTemplateConfigProps.getConnectionTimeout() != null ? restTemplateConfigProps.getConnectionTimeout()
						: 10000);
		httpRequestFactory.setReadTimeout(
				restTemplateConfigProps.getReadTimeout() != null ? restTemplateConfigProps.getReadTimeout() : 10000);
		restTemplateHttpComponentsClient.setRequestFactory(httpRequestFactory);

		logger.debug(".....initForHttpComponentsClient() method end");
	}

	public RestTemplate getRestTemplateSimpleClient() {
		return restTemplateSimpleClient;
	}

	public RestTemplate getRestTemplateHttpComponentsClient() {
		return restTemplateHttpComponentsClient;
	}

}
