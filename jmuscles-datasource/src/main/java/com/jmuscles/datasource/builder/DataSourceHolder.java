/**
 * 
 */
package com.jmuscles.datasource.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class DataSourceHolder {
	private final Map<String, JmusclesDataSource> dataSourceMap = new HashMap<>();
	private String identifier = "";

	public DataSourceHolder() {
		// TODO Auto-generated constructor stub
	}

	public DataSourceHolder(String identifier) {
		super();
		this.identifier = identifier;
	}

	public void add(String name, JmusclesDataSource jmusclesDataSource) {
		dataSourceMap.put(name, jmusclesDataSource);
	}

	public JmusclesDataSource remove(String key) {
		return dataSourceMap.remove(key);
	}

	public JmusclesDataSource get(String key) {
		return dataSourceMap.get(key);
	}

	public List<String> getDataSourceKeys() {
		return new ArrayList<>(dataSourceMap.keySet());
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

}
