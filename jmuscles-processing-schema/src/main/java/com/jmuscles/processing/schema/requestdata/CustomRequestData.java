/**
 * 
 */
package com.jmuscles.processing.schema.requestdata;

import java.io.Serializable;

/**
 * @author manish goel
 *
 */
public class CustomRequestData implements RequestData {

	private static final long serialVersionUID = -1381576752336615492L;

	private String configKey;
	private Serializable data;

	public CustomRequestData() {
	}

	public CustomRequestData(Serializable data) {
		super();
		this.data = data;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

}
