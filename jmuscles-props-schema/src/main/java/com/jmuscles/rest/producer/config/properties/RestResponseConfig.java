/**
 * 
 */
package com.jmuscles.rest.producer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * 
 */
public class RestResponseConfig {

	private int status;
	private String body;
	private Map<String, String> headers;

	public RestResponseConfig() {
		// TODO Auto-generated constructor stub
	}

	public RestResponseConfig(int status, String body, Map<String, String> headers) {
		super();
		this.status = status;
		this.body = body;
		this.headers = headers;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public static RestResponseConfig mapToObject(Map<String, Object> map) {
		String statusStr = Util.getString(map, "status");
		return new RestResponseConfig(statusStr != null ? Integer.parseInt(statusStr) : 0, Util.getString(map, "body"),
				(Map) map.get("headers"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("status", String.valueOf(this.getStatus()));
		if (this.getBody() != null) {
			map.put("body", this.getBody());
		}
		if (this.getHeaders() != null) {
			map.put("headers", this.getHeaders());
		}

		return map;

	}

	public static Map<String, RestResponseConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, RestResponseConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}
