/**
 * 
 */
package com.jmuscles.processing.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class RestCallConfig{

	private String url;
	private String validator;
	private Map<Integer, List<String>> successCodePatterns = new HashMap<>();

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

	public Map<Integer, List<String>> getSuccessCodePatterns() {
		return successCodePatterns;
	}

	public void setSuccessCodePatterns(Map<Integer, List<String>> successCodePatterns) {
		this.successCodePatterns = successCodePatterns;
	}

}
