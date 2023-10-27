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

	private ExecutorConfigProperties configProperties;

	private RestTemplate restTemplateSimpleClient;
	private RestTemplate restTemplateHttpComponentsClient;

	public RestTemplateProvider(ExecutorConfigProperties configProperties) {
		logger.debug("Constructor....");
		this.configProperties = configProperties;
		initialize();
	}

	public void initialize() {
		RestConfig restTemplateConfigProps = configProperties.getRestConfig();
		initForSimpleClient(restTemplateConfigProps);
		initForHttpComponentsClient(restTemplateConfigProps);
	}

	public void refresh() {
		logger.info("Refresh RestTemplateProvider start....");
		initialize();
		logger.info("....Refresh RestTemplateProvider end");
	}

	private void initForSimpleClient(RestConfig restTemplateConfigProps) {
		logger.debug("initForSimpleClient() method start .... ");
		RestTemplate restTemplate = new RestTemplate();

		SimpleClientHttpRequestFactory httpRequestFactory = (SimpleClientHttpRequestFactory) restTemplate
				.getRequestFactory();
		httpRequestFactory.setConnectTimeout(
				restTemplateConfigProps.getConnectionTimeout() != null ? restTemplateConfigProps.getConnectionTimeout()
						: 10000);
		httpRequestFactory.setReadTimeout(
				restTemplateConfigProps.getReadTimeout() != null ? restTemplateConfigProps.getReadTimeout() : 10000);
		restTemplate.setRequestFactory(httpRequestFactory);

		restTemplateSimpleClient = restTemplate;

		logger.debug(".....initForSimpleClient() method end");
	}

	private void initForHttpComponentsClient(RestConfig restTemplateConfigProps) {
		logger.debug("initForHttpComponentsClient() method start .... ");
		RestTemplate restTemplate = new RestTemplate();

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);
		httpRequestFactory.setConnectTimeout(
				restTemplateConfigProps.getConnectionTimeout() != null ? restTemplateConfigProps.getConnectionTimeout()
						: 10000);
		httpRequestFactory.setReadTimeout(
				restTemplateConfigProps.getReadTimeout() != null ? restTemplateConfigProps.getReadTimeout() : 10000);
		restTemplate.setRequestFactory(httpRequestFactory);

		restTemplateHttpComponentsClient = restTemplate;

		logger.debug(".....initForHttpComponentsClient() method end");
	}

	public RestTemplate getRestTemplateSimpleClient() {
		return restTemplateSimpleClient;
	}

	public RestTemplate getRestTemplateHttpComponentsClient() {
		return restTemplateHttpComponentsClient;
	}

}
