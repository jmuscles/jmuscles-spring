/**
 * 
 */
package com.jmuscles.rest.producer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

	public static RestConfPropsForMethod mapToObject(Map<String, Object> map) {
		return new RestConfPropsForMethod(ProducerConfigProperties.mapToObject((Map) map.get("processingConfig")),
				RestResponseConfig.mapToObject2((Map) map.get("responseConfig")));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		if (this.getProcessingConfig() != null) {
			map.put("processingConfig", this.getProcessingConfig().objectToMap());
		}
		if (this.getResponseConfig() != null) {
			map.put("responseConfig", RestResponseConfig.objectToMap2(this.getResponseConfig()));
		}

		return map;
	}

	public static Map<String, RestConfPropsForMethod> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, RestConfPropsForMethod> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}
