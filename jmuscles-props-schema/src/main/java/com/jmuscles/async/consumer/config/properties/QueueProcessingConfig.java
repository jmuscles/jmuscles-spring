/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class QueueProcessingConfig {

	private boolean disableProcessing;
	private String listenerType;
	private String concurrency;
	private String processor;
	private RetryOnlyProcessingConfig retryOnlyConfig;

	public QueueProcessingConfig() {
		// TODO Auto-generated constructor stub
	}

	public QueueProcessingConfig(boolean disableProcessing, String listenerType, String concurrency, String processor,
			RetryOnlyProcessingConfig retryOnlyConfig) {
		super();
		this.disableProcessing = disableProcessing;
		this.listenerType = listenerType;
		this.concurrency = concurrency;
		this.processor = processor;
		this.retryOnlyConfig = retryOnlyConfig;
	}

	public String getListenerType() {
		return listenerType;
	}

	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
	}

	public String getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(String concurrency) {
		this.concurrency = concurrency;
	}

	public boolean isDisableProcessing() {
		return disableProcessing;
	}

	public void setDisableProcessing(boolean disableProcessing) {
		this.disableProcessing = disableProcessing;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public RetryOnlyProcessingConfig getRetryOnlyConfig() {
		return retryOnlyConfig;
	}

	public void setRetryOnlyConfig(RetryOnlyProcessingConfig retryOnlyConfig) {
		this.retryOnlyConfig = retryOnlyConfig;
	}

	public static QueueProcessingConfig mapToObject(Map<String, Object> map) {
		return map != null
				? new QueueProcessingConfig(Util.getBoolean(map, "disableProcessing"),
						Util.getString(map, "listenerType"), Util.getString(map, "concurrency"),
						Util.getString(map, "processor"),
						RetryOnlyProcessingConfig.mapToObject((Map) map.get("retryOnlyConfig")))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("disableProcessing", this.isDisableProcessing());
		map.put("listenerType", this.getListenerType());
		map.put("concurrency", this.getConcurrency());
		map.put("processor", this.getProcessor());
		if (this.getRetryOnlyConfig() != null) {
			map.put("retryOnlyConfig", this.getRetryOnlyConfig().objectToMap());
		}

		return map;
	}

	public static Map<String, QueueProcessingConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, QueueProcessingConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

	public static Map<String, Map<String, QueueProcessingConfig>> mapOfMapToMapOfObject(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject2((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMapOfMap(Map<String, Map<String, QueueProcessingConfig>> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> objectToMap2(e.getValue())));
		} else {
			return null;
		}
	}

}
