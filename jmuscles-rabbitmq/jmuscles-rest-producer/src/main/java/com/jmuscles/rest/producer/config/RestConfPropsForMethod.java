/**
 * 
 */
package com.jmuscles.rest.producer.config;

import java.util.Map;

import com.jmuscles.async.producer.properties.ProducerConfigProperties;

/**
 * 
 */
public class RestConfPropsForMethod {

	private ProducerConfigProperties processingConfig;
	private Map<String, RestResponseConfig> responseConfig;

	public ProducerConfigProperties getProcessingConfig() {
		return processingConfig;
	}

	public void setProcessingConfig(ProducerConfigProperties processingConfig) {
		this.processingConfig = processingConfig;
	}

	public Map<String, RestResponseConfig> getResponseConfig() {
		return responseConfig;
	}

	public void setResponseConfig(Map<String, RestResponseConfig> responseConfig) {
		this.responseConfig = responseConfig;
	}

}
