/**
 * 
 */
package com.jmuscles.processing.schema.requestdata;

import java.io.Serializable;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class RestRequestData implements RequestData {

	private static final long serialVersionUID = 6851357139004279014L;

	private String method;
	private String configKey;
	private String urlSuffix;
	private Serializable body;
	private Map<String, String> httpHeader;

	public RestRequestData() {
	}

	public RestRequestData(String method, String configKey, String urlSuffix, Serializable body,
			Map<String, String> httpHeader) {
		super();
		this.method = method;
		this.configKey = configKey;
		this.urlSuffix = urlSuffix;
		this.body = body;
		this.httpHeader = httpHeader;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getUrlSuffix() {
		return urlSuffix;
	}

	public void setUrlSuffix(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}

	public Serializable getBody() {
		return body;
	}

	public void setBody(Serializable body) {
		this.body = body;
	}

	public Map<String, String> getHttpHeader() {
		return httpHeader;
	}

	public void setHttpHeader(Map<String, String> httpHeader) {
		this.httpHeader = httpHeader;
	}

}
