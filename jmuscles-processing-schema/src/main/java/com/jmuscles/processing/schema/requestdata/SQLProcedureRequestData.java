/**
 * 
 */
package com.jmuscles.processing.schema.requestdata;

import java.util.Map;

/**
 * @author manish goel
 *
 */
public class SQLProcedureRequestData implements RequestData {

	private static final long serialVersionUID = -2345724388192463955L;

	private Map<String, Object> params;
	private String configKey;

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
