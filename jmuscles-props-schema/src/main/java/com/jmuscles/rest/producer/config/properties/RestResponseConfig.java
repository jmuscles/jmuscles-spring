/**
 * 
 */
package com.jmuscles.rest.producer.config.properties;

import java.util.Map;

/**
 * 
 */
public class RestResponseConfig {

	private int status;
	private String body;
	private Map<String, String> headers;

	public RestResponseConfig() {
		// TODO Auto-generated constructor stub
	}

	public RestResponseConfig(int status, String body, Map<String, String> headers) {
		super();
		this.status = status;
		this.body = body;
		this.headers = headers;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

}
