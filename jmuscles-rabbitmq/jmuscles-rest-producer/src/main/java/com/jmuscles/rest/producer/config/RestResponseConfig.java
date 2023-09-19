/**
 * 
 */
package com.jmuscles.rest.producer.config;

import org.springframework.util.MultiValueMap;

/**
 * 
 */
public class RestResponseConfig {

	private int status;
	private Object body;
	private MultiValueMap<String, String> headers;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public MultiValueMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(MultiValueMap<String, String> headers) {
		this.headers = headers;
	}

}
