/**
 * 
 */
package com.jmuscles.processing.config.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class ExecutorConfigProperties {

	private RestConfig restConfig = new RestConfig();
	private Map<String, RestCallConfig> restCalls = new HashMap<>();
	private Map<String, SQLProcedureCallConfig> sqlProcedures = new HashMap<>();
	private Map<String, SQLQueryCallConfig> sqlQueries = new HashMap<>();

	public ExecutorConfigProperties() {
		// TODO Auto-generated constructor stub
	}

	public ExecutorConfigProperties(RestConfig restConfig, Map<String, RestCallConfig> restCalls,
			Map<String, SQLProcedureCallConfig> sqlProcedures, Map<String, SQLQueryCallConfig> sqlQueries) {
		super();
		this.restConfig = restConfig;
		this.restCalls = restCalls;
		this.sqlProcedures = sqlProcedures;
		this.sqlQueries = sqlQueries;
	}

	public void replaceValues(ExecutorConfigProperties executorConfigProperties) {
		clear();
		if (executorConfigProperties != null) {
			this.restConfig = executorConfigProperties.getRestConfig();
			this.restCalls = executorConfigProperties.getRestCalls();
			this.sqlProcedures = executorConfigProperties.getSqlProcedures();
			this.sqlQueries = executorConfigProperties.getSqlQueries();
		}
	}

	public void clear() {
		this.restConfig = new RestConfig();
		if (this.restCalls != null) {
			this.restCalls.clear();
		} else {
			this.restCalls = new HashMap<>();
		}
		if (this.sqlProcedures != null) {
			this.sqlProcedures.clear();
		} else {
			this.sqlProcedures = new HashMap<>();
		}
		if (this.sqlQueries != null) {
			this.sqlQueries.clear();
		} else {
			this.sqlQueries = new HashMap<>();
		}
	}

	public RestConfig getRestConfig() {
		return restConfig;
	}

	public void setRestConfig(RestConfig restConfig) {
		this.restConfig = restConfig;
	}

	public Map<String, RestCallConfig> getRestCalls() {
		return restCalls;
	}

	public void setRestCalls(Map<String, RestCallConfig> restCalls) {
		this.restCalls = restCalls;
	}

	public Map<String, SQLProcedureCallConfig> getSqlProcedures() {
		return sqlProcedures;
	}

	public void setSqlProcedures(Map<String, SQLProcedureCallConfig> sqlProcedures) {
		this.sqlProcedures = sqlProcedures;
	}

	public Map<String, SQLQueryCallConfig> getSqlQueries() {
		return sqlQueries;
	}

	public void setSqlQueries(Map<String, SQLQueryCallConfig> sqlQueries) {
		this.sqlQueries = sqlQueries;
	}

	public static ExecutorConfigProperties mapToObject(Map<String, Object> map) {
		return map != null
				? new ExecutorConfigProperties(RestConfig.mapToObject((Map) map.get("restConfig")),
						RestCallConfig.mapToObject2((Map) map.get("restCalls")),
						SQLProcedureCallConfig.mapToObject2((Map) map.get("sqlProcedures")),
						SQLQueryCallConfig.mapToObject2((Map) map.get("sqlQueries")))
				: null;
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("restConfig", this.getRestConfig().objectToMap());
		map.put("restCalls", RestCallConfig.objectToMap2(this.getRestCalls()));
		map.put("sqlProcedures", SQLProcedureCallConfig.objectToMap2(this.getSqlProcedures()));
		map.put("sqlQueries", SQLQueryCallConfig.objectToMap2(this.getSqlQueries()));

		return map;
	}

}
