/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

import java.util.List;

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

}
