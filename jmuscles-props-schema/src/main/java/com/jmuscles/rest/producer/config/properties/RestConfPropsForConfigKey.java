/**
 * 
 */
package com.jmuscles.rest.producer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.async.producer.config.properties.ProducerConfigProperties;
import com.jmuscles.util.Util;

/**
 * 
 */
public class RestConfPropsForConfigKey {

	private Map<String, RestConfPropsForMethod> configByHttpMethods;
	private ProducerConfigProperties processingConfig;
	private String responseBuilder;

	public RestConfPropsForConfigKey() {
		// TODO Auto-generated constructor stub
	}

	public RestConfPropsForConfigKey(Map<String, RestConfPropsForMethod> configByHttpMethods,
			ProducerConfigProperties processingConfig, String responseBuilder) {
		super();
		this.configByHttpMethods = configByHttpMethods;
		this.processingConfig = processingConfig;
		this.responseBuilder = responseBuilder;
	}

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

	public static RestConfPropsForConfigKey mapToObject(Map<String, Object> map) {
		return map != null
				? new RestConfPropsForConfigKey(
						RestConfPropsForMethod.mapToObject2((Map) map.get("configByHttpMethods")),
						ProducerConfigProperties.mapToObject((Map) map.get("processingConfig")),
						Util.getString(map, "responseBuilder"))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		if (this.getConfigByHttpMethods() != null) {
			map.put("configByHttpMethods", RestConfPropsForMethod.objectToMap2(this.getConfigByHttpMethods()));
		}
		if (this.getProcessingConfig() != null) {
			map.put("processingConfig", this.getProcessingConfig().objectToMap());
		}
		if (this.getResponseBuilder() != null) {
			map.put("responseBuilder", this.getResponseBuilder());
		}

		return map;
	}

	public static Map<String, RestConfPropsForConfigKey> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, RestConfPropsForConfigKey> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}
