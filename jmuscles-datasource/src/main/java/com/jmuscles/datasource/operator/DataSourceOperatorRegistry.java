/**
 * 
 */
package com.jmuscles.datasource.operator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.jmuscles.datasource.operator.implementation.HikariDataSourceOperator;
import com.jmuscles.datasource.operator.implementation.TomcatDataSourceOperator;

/**
 * @author manish goel
 *
 */
public final class DataSourceOperatorRegistry {

	private final Map<String, DataSourceOperator> dataSourceHandlerRegistryMap = new HashMap<>();

	public DataSourceOperatorRegistry() {
		build();
	}

	public void register(DataSourceOperator dataSourceOperator) {
		if (dataSourceOperator != null && StringUtils.hasText(dataSourceOperator.dataSourceType())) {
			dataSourceHandlerRegistryMap.put(dataSourceOperator.dataSourceType().toLowerCase(), dataSourceOperator);
		}
	}

	public DataSourceOperator get(String dataSourceType) {
		return StringUtils.hasText(dataSourceType) ? dataSourceHandlerRegistryMap.get(dataSourceType.toLowerCase())
				: null;
	}

	private void build() {
		new HikariDataSourceOperator(this);
		new TomcatDataSourceOperator(this);
	}

}
