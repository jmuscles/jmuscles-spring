package com.jmuscles.props.config;

import java.io.Serializable;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class AppPropsRestConfig {

	private Integer connectionTimeout;
	private Integer readTimeout;
	private String method;
	private String url;
	private Map<String, String> headers;
	private Serializable body;

	public AppPropsRestConfig() {
		// TODO Auto-generated constructor stub
	}

	public AppPropsRestConfig(Integer connectionTimeout, Integer readTimeout, String method, String url,
			Map<String, String> headers, Serializable body) {
		super();
		this.connectionTimeout = connectionTimeout;
		this.readTimeout = readTimeout;
		this.method = method;
		this.url = url;
		this.headers = headers;
		this.body = body;
	}

	/**
	 * @return the connectionTimeout
	 */
	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the readTimeout
	 */
	public Integer getReadTimeout() {
		return readTimeout;
	}

	/**
	 * @param readTimeout the readTimeout to set
	 */
	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the body
	 */
	public Serializable getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Serializable body) {
		this.body = body;
	}

}
