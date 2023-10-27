/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class RetryOnlyProcessingConfig {

	private boolean acceptingMessage;
	private int retryAttempt;
	private boolean retryAfterDelay;
	private List<Integer> retryInterval;

	public RetryOnlyProcessingConfig() {
		// TODO Auto-generated constructor stub
	}

	public RetryOnlyProcessingConfig(boolean acceptingMessage, int retryAttempt, boolean retryAfterDelay,
			List<Integer> retryInterval) {
		super();
		this.acceptingMessage = acceptingMessage;
		this.retryAttempt = retryAttempt;
		this.retryAfterDelay = retryAfterDelay;
		this.retryInterval = retryInterval;
	}

	public boolean isAcceptingMessage() {
		return acceptingMessage;
	}

	public void setAcceptingMessage(boolean acceptingMessage) {
		this.acceptingMessage = acceptingMessage;
	}

	public int getRetryAttempt() {
		return retryAttempt;
	}

	public void setRetryAttempt(int retryAttempt) {
		this.retryAttempt = retryAttempt;
	}

	public boolean isRetryAfterDelay() {
		return retryAfterDelay;
	}

	public void setRetryAfterDelay(boolean retryAfterDelay) {
		this.retryAfterDelay = retryAfterDelay;
	}

	public List<Integer> getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(List<Integer> retryInterval) {
		this.retryInterval = retryInterval;
	}

	public static RetryOnlyProcessingConfig mapToObject(Map<String, Object> map) {
		return map != null
				? new RetryOnlyProcessingConfig(Util.getBoolean(map, "acceptingMessage"),
						Util.getInt(map, "retryAttempt"), Util.getBoolean(map, "retryAfterDelay"),
						(List<Integer>) Util.stringToListFromMap(map, "retryInterval", Integer.class))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("acceptingMessage", this.isAcceptingMessage());
		map.put("retryAttempt", this.getRetryAttempt());
		map.put("retryAfterDelay", this.isRetryAfterDelay());
		map.put("retryInterval", Util.listToString(this.getRetryInterval()));

		return map;
	}

	public static Map<String, RetryOnlyProcessingConfig> mapToObject2(Map<String, Object> map) {
		if (map != null) {
			return map.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
		} else {
			return null;
		}
	}

	public static Map<String, Object> objectToMap2(Map<String, RetryOnlyProcessingConfig> objectsMap) {
		if (objectsMap != null) {
			return objectsMap.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
		} else {
			return null;
		}
	}

}
