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

	public QueueSetConfig() {
		// TODO Auto-generated constructor stub
	}

	public QueueSetConfig(String name, String exchange, boolean retrySetupDisabled,
			Map<String, Map<String, Object>> arguments) {
		super();
		this.name = name;
		this.exchange = exchange;
		this.retrySetupDisabled = retrySetupDisabled;
		this.arguments = arguments;
	}

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
