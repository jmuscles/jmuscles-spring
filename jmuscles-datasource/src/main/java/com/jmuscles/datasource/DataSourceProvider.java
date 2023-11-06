/**
 * @author manish goel
 *
 */
package com.jmuscles.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jmuscles.datasource.builder.JmusclesDataSource;

/**
 * 
 */
public class DataSourceProvider {

	public static final String DSG_CONFIG_PROPS = "dsg-config-props";
	public static final String DSG_DB_PROPS = "dsg-db-props";

	private Map<String, DataSourceGenerator> dsgMap = new HashMap<>();

	public void addDataSourceGenerator(DataSourceGenerator dsg) {
		dsgMap.put(dsg.getIdentifier(), dsg);
	}

	public void removeDataSourceGenerator(DataSourceGenerator dsg) {
		dsgMap.remove(dsg.getIdentifier());
	}

	public DataSourceGenerator getDataSourceGenerator(String ideintifier) {
		return dsgMap.get(ideintifier);
	}

	public DataSourceGenerator getDsgDbProps() {
		return getDataSourceGenerator(DSG_DB_PROPS);
	}

	public DataSourceGenerator getDsgConfigProps() {
		return getDataSourceGenerator(DSG_CONFIG_PROPS);
	}

	public DataSource get(String dsKey) {
		JmusclesDataSource jmusclesDataSource = getJmusclesDataSource(dsKey);
		return jmusclesDataSource != null ? jmusclesDataSource.getDataSource() : null;
	}

	public JmusclesDataSource getJmusclesDataSource(String dsKey) {
		JmusclesDataSource jmusclesDataSource = null;
		for (Map.Entry<String, DataSourceGenerator> entry : this.dsgMap.entrySet()) {
			jmusclesDataSource = entry.getValue().getJmusclesDataSource(dsKey);
			if (jmusclesDataSource != null) {
				break;
			}
		}
		return jmusclesDataSource;
	}

	public void refreshAll() {
		dsgMap.entrySet().forEach(e -> e.getValue().refresh());
	}

}
