/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

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

}
