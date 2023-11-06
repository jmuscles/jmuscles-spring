/**
 * @author manish goel
 *
 */
package com.jmuscles.datasource.builder;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 
 */
public class JmusclesDataSource {

	private DataSource dataSource;
	private final Map<String, String> additionalProperties = new HashMap<>();

	public static JmusclesDataSource of(DataSource dataSource) {
		return new JmusclesDataSource(dataSource, null);
	}

	public JmusclesDataSource(DataSource dataSource, Map<String, String> additionalProperties) {
		super();
		this.dataSource = dataSource;
		if (additionalProperties != null) {
			this.additionalProperties.putAll(additionalProperties);
		}
	}

	public JmusclesDataSource addAdditionalProperties(Map<String, String> additionalProperties) {
		if (additionalProperties != null) {
			this.additionalProperties.putAll(additionalProperties);
		}

		return this;
	}

	public JmusclesDataSource addAdditionalProperty(String key, String value) {
		additionalProperties.put(key, value);
		return this;
	}

	public JmusclesDataSource addType(String type) {
		return addAdditionalProperty("type", type);
	}

	public String getType() {
		return additionalProperties.get("type");
	}

	public JmusclesDataSource addDsgId(String dsgId) {
		return addAdditionalProperty("dsgId", dsgId);
	}

	public String getDsgId() {
		return additionalProperties.get("dsgId");
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @return the additionalProperties
	 */
	public Map<String, String> getAdditionalProperties() {
		return additionalProperties;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
