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
		this.initialize(this.configProperties != null ? this.configProperties.getRestConfig() : null);
	}

	public void initialize(RestConfig restConfig) {
		Integer connectionTimeOut = 10000;
		Integer readtimeOutTimeOut = 10000;
		if (restConfig != null) {
			if (restConfig.getConnectionTimeout() != null) {
				connectionTimeOut = restConfig.getConnectionTimeout();
			}
			if (restConfig.getReadTimeout() != null) {
				readtimeOutTimeOut = restConfig.getReadTimeout();
			}
		}
		this.initialize(connectionTimeOut, readtimeOutTimeOut);
	}

	public void initialize(Integer connectionTimeOut, Integer readtimeOutTimeOut) {
		this.initForSimpleClient(connectionTimeOut, readtimeOutTimeOut);
		this.initForHttpComponentsClient(connectionTimeOut, readtimeOutTimeOut);
	}

	public void refresh() {
		logger.info("Refresh RestTemplateProvider start....");
		initialize();
		logger.info("....Refresh RestTemplateProvider end");
	}

	private void initForSimpleClient(Integer connectionTimeOut, Integer readtimeOutTimeOut) {
		logger.debug("initForSimpleClient() method start .... ");
		RestTemplate restTemplate = new RestTemplate();

		SimpleClientHttpRequestFactory httpRequestFactory = (SimpleClientHttpRequestFactory) restTemplate
				.getRequestFactory();
		httpRequestFactory.setConnectTimeout(connectionTimeOut);
		httpRequestFactory.setReadTimeout(readtimeOutTimeOut);
		restTemplate.setRequestFactory(httpRequestFactory);

		restTemplateSimpleClient = restTemplate;

		logger.debug(".....initForSimpleClient() method end");
	}

	private void initForHttpComponentsClient(Integer connectionTimeOut, Integer readtimeOutTimeOut) {
		logger.debug("initForHttpComponentsClient() method start .... ");
		RestTemplate restTemplate = new RestTemplate();

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);
		httpRequestFactory.setConnectTimeout(connectionTimeOut);
		httpRequestFactory.setReadTimeout(readtimeOutTimeOut);
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
