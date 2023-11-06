/**
 * 
 */
package com.jmuscles.processing.schema.requestdata;

import java.util.Map;

/**
 * @author manish goel
 *
 */
public class SQLQueryRequestData implements RequestData {

	private static final long serialVersionUID = -1045031154697315786L;

	private Map<String, Object> params;
	private String configKey;

	public SQLQueryRequestData() {
	}

	public SQLQueryRequestData(Map<String, Object> params, String configKey) {
		super();
		this.params = params;
		this.configKey = configKey;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

}
