/**
 * 
 */
package com.jmuscles.processing.config.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author manish goel
 *
 */
public class RestCallConfig {

	private String url;
	private String validator;
	private Map<Integer, List<String>> successCodePatterns = new HashMap<>();
	private Map<String, String> httpHeader;

	public RestCallConfig() {
		// TODO Auto-generated constructor stub
	}

	public RestCallConfig(String url, String validator, Map<Integer, List<String>> successCodePatterns,
			Map<String, String> httpHeader) {
		super();
		this.url = url;
		this.validator = validator;
		this.successCodePatterns = successCodePatterns;
		this.httpHeader = httpHeader;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public Map<Integer, List<String>> getSuccessCodePatterns() {
		return successCodePatterns;
	}

	public void setSuccessCodePatterns(Map<Integer, List<String>> successCodePatterns) {
		this.successCodePatterns = successCodePatterns;
	}

	public Map<String, String> getHttpHeader() {
		return httpHeader;
	}

	public void setHttpHeader(Map<String, String> httpHeader) {
		this.httpHeader = httpHeader;
	}

	public static RestCallConfig mapToObject(Map<String, Object> map) {
		return new RestCallConfig((String) map.get("url"), (String) map.get("validator"),
				(Map) map.get("successCodePatterns"), (Map) map.get("httpHeader"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("url", this.getUrl());
		map.put("validator", this.getValidator());
		map.put("successCodePatterns", this.getSuccessCodePatterns());
		map.put("httpHeader", this.getHttpHeader());

		return map;
	}

	public static Map<String, RestCallConfig> mapToObject2(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
	}

	public static Map<String, Object> objectToMap2(Map<String, RestCallConfig> objectsMap) {
		return objectsMap.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	}

}
