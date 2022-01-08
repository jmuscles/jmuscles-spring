/**
 * 
 */
package com.jmuscles.processing.config;

/**
 * @author manish goel
 *
 */
public class RestConfig {

	private Integer connectionTimeout;
	private Integer readTimeout;

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

}
