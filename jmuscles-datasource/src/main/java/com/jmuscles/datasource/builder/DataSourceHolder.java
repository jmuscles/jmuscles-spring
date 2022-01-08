/**
 * 
 */
package com.jmuscles.datasource.builder;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * @author manish goel
 *
 */
public class DataSourceHolder {
	private final Map<String, DataSource> dataSourceMap = new HashMap<>();

	public void add(String name, DataSource dataSource) {
		dataSourceMap.put(name, dataSource);
	}

	public DataSource get(String name) {
		return dataSourceMap.get(name);
	}

}
