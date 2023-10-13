/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author manish goel
 *
 */
public class RetryOnlyProcessingConfig {

	private boolean acceptingMessage;
	private int retryAttempt;
	private boolean retryAfterDelay;
	private int[] retryInterval;

	public RetryOnlyProcessingConfig() {
		// TODO Auto-generated constructor stub
	}

	public RetryOnlyProcessingConfig(boolean acceptingMessage, int retryAttempt, boolean retryAfterDelay,
			int[] retryInterval) {
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

	public int[] getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int[] retryInterval) {
		this.retryInterval = retryInterval;
	}

	public static RetryOnlyProcessingConfig mapToObject(Map<String, Object> map) {
		return new RetryOnlyProcessingConfig((boolean) map.get("acceptingMessage"), (int) map.get("retryAttempt"),
				(boolean) map.get("retryAfterDelay"), (int[]) map.get("retryInterval"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("acceptingMessage", this.isAcceptingMessage());
		map.put("retryAttempt", this.getRetryAttempt());
		map.put("retryAfterDelay", this.isRetryAfterDelay());
		map.put("retryInterval", this.getRetryInterval());

		return map;
	}

	public static Map<String, RetryOnlyProcessingConfig> mapToObject2(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
	}

	public static Map<String, Object> objectToMap2(Map<String, RetryOnlyProcessingConfig> objectsMap) {
		return objectsMap.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	}

}
