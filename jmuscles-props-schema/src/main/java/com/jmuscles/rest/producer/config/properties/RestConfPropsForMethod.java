/**
 * 
 */
package com.jmuscles.rest.producer.config.properties;

import java.util.HashMap;
import java.util.List;
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

	public static Map<String, Object> objectToMap2(Map<String, RestConfPropsForMethod> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
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

	public static Map<String, RestConfPropsForMethod> mapToObject2(Map<String, Object> map, List<String> requestPath) {
		Map<String, RestConfPropsForMethod> returnMap = null;
		if (requestPath != null && requestPath.size() > 0) {
			returnMap = new HashMap<>();
			String nextPath = requestPath.remove(0);
			returnMap.put(nextPath, mapToObject(map, requestPath, nextPath));
		} else {
			returnMap = mapToObject2((Map<String, Object>) map.get("configByHttpMethods"));
		}
		return returnMap;
	}

	public static Map<String, RestConfPropsForMethod> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static RestConfPropsForMethod mapToObject(Map<String, Object> map, List<String> requestPath, String path) {
		RestConfPropsForMethod returnObject = null;
		if (requestPath != null && requestPath.size() > 0) {
			returnObject = new RestConfPropsForMethod();
			String nextPath = requestPath.remove(0);
			switch (nextPath) {
			case "processingConfig":
				returnObject
						.setProcessingConfig(ProducerConfigProperties.mapToObject((Map) map.get("processingConfig")));
				break;
			case "responseConfig":
				returnObject.setResponseConfig(RestResponseConfig.mapToObject2(map, requestPath));
				break;
			}

		} else {
			returnObject = mapToObject((Map<String, Object>) map.get(path));
		}

		return returnObject;
	}

	public static RestConfPropsForMethod mapToObject(Map<String, Object> map) {
		return new RestConfPropsForMethod(ProducerConfigProperties.mapToObject((Map) map.get("processingConfig")),
				RestResponseConfig.mapToObject2((Map) map.get("responseConfig")));
	}

}
