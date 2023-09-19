/**
 * 
 */
package com.jmuscles.rest.producer.config;

import java.util.Map;

import com.jmuscles.async.producer.properties.ProducerConfigProperties;

/**
 * 
 */
public class RestConfPropsForConfigKey {

	private Map<String, RestConfPropsForMethod> configByHttpMethods;
	private ProducerConfigProperties processingConfig;
	private String responseBuilder;

	public Map<String, RestConfPropsForMethod> getConfigByHttpMethods() {
		return configByHttpMethods;
	}

	public void setConfigByHttpMethods(Map<String, RestConfPropsForMethod> configByHttpMethods) {
		this.configByHttpMethods = configByHttpMethods;
	}

	public ProducerConfigProperties getProcessingConfig() {
		return processingConfig;
	}

	public void setProcessingConfig(ProducerConfigProperties processingConfig) {
		this.processingConfig = processingConfig;
	}

	public String getResponseBuilder() {
		return responseBuilder;
	}

	public void setResponseBuilder(String responseBuilder) {
		this.responseBuilder = responseBuilder;
	}

}
