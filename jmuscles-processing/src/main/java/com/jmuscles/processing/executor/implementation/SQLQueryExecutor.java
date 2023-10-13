/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.processing.config.properties.ExecutorConfigProperties;
import com.jmuscles.processing.config.properties.SQLQueryCallConfig;
import com.jmuscles.processing.executor.StandardExecutor;
import com.jmuscles.processing.executor.StandardExecutorRegistry;
import com.jmuscles.processing.execvalidator.SQLQueryValidator;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.SQLQueryRequestData;

/**
 * @author manish goel
 *
 */
public class SQLQueryExecutor extends StandardExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SQLQueryExecutor.class);

	private ExecutorConfigProperties executorConfigProperties;
	private DataSourceGenerator dataSourceGenerator;

	public SQLQueryExecutor(StandardExecutorRegistry executorRegistry, ExecutorConfigProperties executorConfigProperties,
			DataSourceGenerator dataSourceGenerator) {
		super(executorRegistry);
		this.executorConfigProperties = executorConfigProperties;
		this.dataSourceGenerator = dataSourceGenerator;
	}

	@Override
	public RequestData execute(RequestData requestData) {
		logger.info("SQL Query start executing");

		SQLQueryRequestData queryRequestData = (SQLQueryRequestData) requestData;
		SQLQueryCallConfig queryConfig = getExecutorSQLQueryConfig(queryRequestData.getConfigKey());
		int response = invoke(queryRequestData, queryConfig.getQuery(),
				dataSourceGenerator.get(queryConfig.getDskey()));

		logger.debug("response for queryconfigKey- " + queryRequestData.getConfigKey() + ": " + response);

		RequestData reponseRequestData = SQLQueryValidator.get(queryConfig.getValidator()).validateResponse(requestData,
				response, queryConfig) ? null : requestData;

		logger.info("SQL Query end executing");

		return reponseRequestData;
	}

	public int invoke(SQLQueryRequestData queryRequestData, String sql, DataSource dataSource) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		int queryResponse = jdbcTemplate.update(sql, queryRequestData.getParams());
		return queryResponse;
	}

	public SQLQueryCallConfig getExecutorSQLQueryConfig(String key) {
		SQLQueryCallConfig sqlQueryCallConfig = executorConfigProperties.getSqlQueries().get(key);
		if (sqlQueryCallConfig == null) {
			logger.error("SQLQueryCallConfig in missing for key: " + key);
			throw new RuntimeException("SQLQueryCallConfig in missing for key: " + key);
		}
		return sqlQueryCallConfig;
	}

	@Override
	public Class<?> getExecutorRequestDataClass() {
		return SQLQueryRequestData.class;
	}

}
