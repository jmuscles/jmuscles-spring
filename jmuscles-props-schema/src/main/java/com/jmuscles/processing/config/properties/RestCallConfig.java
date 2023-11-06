/**
 * 
 */
package com.jmuscles.processing.config.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class RestCallConfig {

	private String url;
	private String validator;
	private Map<String, List<String>> successCodePatterns = new HashMap<>();
	private Map<String, String> httpHeader;

	public RestCallConfig() {
		// TODO Auto-generated constructor stub
	}

	public RestCallConfig(String url, String validator, Map<String, List<String>> successCodePatterns,
			Map<String, String> httpHeader) {
		super();
		this.url = url;
		this.validator = validator;
		this.successCodePatterns = successCodePatterns;
		this.httpHeader = httpHeader;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public Map<String, List<String>> getSuccessCodePatterns() {
		return successCodePatterns;
	}

	public void setSuccessCodePatterns(Map<String, List<String>> successCodePatterns) {
		this.successCodePatterns = successCodePatterns;
	}

	public Map<String, String> getHttpHeader() {
		return httpHeader;
	}

	public void setHttpHeader(Map<String, String> httpHeader) {
		this.httpHeader = httpHeader;
	}

}
