/**
 * 
 */
package com.jmuscles.processing.config.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class RestCallConfig {

	private String url;
	private String validator;
	private Map<String, List<String>> successCodePatterns = new HashMap<>();
	private Map<String, String> httpHeader;

	public RestCallConfig() {
		// TODO Auto-generated constructor stub
	}

	public RestCallConfig(String url, String validator, Map<String, List<String>> successCodePatterns,
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

	public Map<String, List<String>> getSuccessCodePatterns() {
		return successCodePatterns;
	}

	public void setSuccessCodePatterns(Map<String, List<String>> successCodePatterns) {
		this.successCodePatterns = successCodePatterns;
	}

	public Map<String, String> getHttpHeader() {
		return httpHeader;
	}

	public void setHttpHeader(Map<String, String> httpHeader) {
		this.httpHeader = httpHeader;
	}

	public static RestCallConfig mapToObject(Map<String, Object> map) {
		return map != null
				? new RestCallConfig(Util.getString(map, "url"), Util.getString(map, "validator"),
						deserailizeSuccessCodePatterns((Map<String, Object>) map.get("successCodePatterns")),
						(Map) map.get("httpHeader"))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		if (this.getUrl() != null) {
			map.put("url", this.getUrl());
		}
		if (this.getValidator() != null) {
			map.put("validator", this.getValidator());
		}
		if (this.getSuccessCodePatterns() != null) {
			map.put("successCodePatterns", serailizeSuccessCodePatterns(this.getSuccessCodePatterns()));
		}
		if (this.getHttpHeader() != null) {
			map.put("httpHeader", this.getHttpHeader());
		}
		return map;
	}

	public static Map<String, Object> serailizeSuccessCodePatterns(Map<String, List<String>> successCodePatterns) {
		return (successCodePatterns != null)
				? successCodePatterns.entrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey(), e -> Util.listToString(e.getValue())))
				: null;
	}

	public static Map<String, List<String>> deserailizeSuccessCodePatterns(Map<String, Object> map) {
		return (map != null) ? map.entrySet().stream().collect(
				Collectors.toMap(e -> e.getKey(), e -> (List<String>) Util.stringToList(e.getValue(), String.class)))
				: null;
	}

	public static Map<String, RestCallConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, RestCallConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}