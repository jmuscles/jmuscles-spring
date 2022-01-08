/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

/**
 * @author manish goel
 *
 */
public class RetryOnlyProcessingConfig {

	private boolean acceptingMessage;
	private int retryAttempt;
	private boolean retryAfterDelay;
	private int[] retryInterval;

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

}
