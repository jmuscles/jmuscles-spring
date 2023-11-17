/**
 * 
 */
package com.jmuscles.props;

import java.util.HashMap;
import java.util.Map;

/**
 * @author manish goel
 */
//config-props-from-db
public class AppPropsDBConfig {

	private String dataSourceKey;
	private Map<String, String> jpaProperties = new HashMap<>();
	private Map<String, String> propsSelectionKeys = new HashMap<>();

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
	 * @return the propsSelectionKey
	 */
	public Map<String, String> getPropsSelectionKeys() {
		return propsSelectionKeys;
	}

	/**
	 * @param propsSelectionKey the propsSelectionKey to set
	 */
	public void setPropsSelectionKeys(Map<String, String> propsSelectionKeys) {
		this.propsSelectionKeys = propsSelectionKeys;
	}

}
