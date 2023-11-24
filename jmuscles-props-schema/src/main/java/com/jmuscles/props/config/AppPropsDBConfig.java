/**
 * 
 */
package com.jmuscles.props.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author manish goel
 */
//config-props-from-db
public class AppPropsDBConfig {

	private String dataSourceKey;
	private Map<String, String> jpaProperties = new HashMap<>();
	private Map<String, String> selectionKeys = new HashMap<>();

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}

	public Map<String, String> getJpaProperties() {
		return jpaProperties;
	}

	public void setJpaProperties(Map<String, String> jpaProperties) {
		this.jpaProperties = jpaProperties;
	}

	/**
	 * @return the selectionKeys
	 */
	public Map<String, String> getSelectionKeys() {
		return selectionKeys;
	}

	/**
	 * @param selectionKeys the selectionKeys to set
	 */
	public void setSelectionKeys(Map<String, String> selectionKeys) {
		this.selectionKeys = selectionKeys;
	}

}
