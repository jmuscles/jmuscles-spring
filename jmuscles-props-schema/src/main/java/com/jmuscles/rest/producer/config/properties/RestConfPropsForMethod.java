/**
 * 
 */
package com.jmuscles.rest.producer.config.properties;

import java.util.Map;

import com.jmuscles.async.producer.config.properties.ProducerConfigProperties;

/**
 * 
 */
public class RestConfPropsForMethod {

	private ProducerConfigProperties processingConfig;
	private Map<String, RestResponseConfig> responseConfig;

	public RestConfPropsForMethod() {
		// TODO Auto-generated constructor stub
	}

	public RestConfPropsForMethod(ProducerConfigProperties processingConfig,
			Map<String, RestResponseConfig> responseConfig) {
		super();
		this.processingConfig = processingConfig;
		this.responseConfig = responseConfig;
	}

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
