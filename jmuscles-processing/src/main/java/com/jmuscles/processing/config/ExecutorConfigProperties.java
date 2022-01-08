/**
 * 
 */
package com.jmuscles.processing.config;

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

}
