/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class QueueSetConfig {
	private String name;
	private String exchange;
	private boolean retrySetupDisabled;

	private Map<String, Map<String, Object>> arguments = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public boolean isRetrySetupDisabled() {
		return retrySetupDisabled;
	}

	public void setRetrySetupDisabled(boolean retrySetupDisabled) {
		this.retrySetupDisabled = retrySetupDisabled;
	}

	public Map<String, Map<String, Object>> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Map<String, Object>> arguments) {
		this.arguments = arguments;
	}

}
