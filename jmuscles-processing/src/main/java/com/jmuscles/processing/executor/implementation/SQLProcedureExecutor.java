/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.processing.config.ExecutorConfigProperties;
import com.jmuscles.processing.config.SQLProcedureCallConfig;
import com.jmuscles.processing.executor.ExecutorRegistry;
import com.jmuscles.processing.executor.SelfRegisteredExecutor;
import com.jmuscles.processing.execvalidator.SQLProcedureValidator;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.SQLProcedureRequestData;

/**
 * @author manish goel
 *
 */
public class SQLProcedureExecutor extends SelfRegisteredExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SQLProcedureExecutor.class);

	private ExecutorConfigProperties executorConfigProperties;
	private DataSourceGenerator dataSourceGenerator;

	public SQLProcedureExecutor(ExecutorRegistry executorRegistry, ExecutorConfigProperties executorConfigProperties,
			DataSourceGenerator dataSourceGenerator) {
		super(executorRegistry);
		this.executorConfigProperties = executorConfigProperties;
		this.dataSourceGenerator = dataSourceGenerator;
	}

	@Override
	public RequestData execute(RequestData requestData) {
		logger.info("SQLProcedure start executing");

		SQLProcedureRequestData procRequestData = (SQLProcedureRequestData) requestData;
		SQLProcedureCallConfig procedureConfig = getExecutorSQLProcedureConfig(procRequestData.getConfigKey());
		Object response = invoke(procRequestData, procedureConfig.getProcedure(),
				dataSourceGenerator.get(procedureConfig.getDskey()));

		RequestData responseRequestData = SQLProcedureValidator.get(procedureConfig.getValidator())
				.validateResponse(procRequestData, response, procedureConfig) ? null : requestData;

		logger.info("SQLProcedure end executing");
		return responseRequestData;
	}

	public Map<String, Object> invoke(SQLProcedureRequestData procRequestData, String procDetail,
			DataSource dataSource) {
		String[] procDetails = procDetail.split("\\.");
		SimpleJdbcCall call = new SimpleJdbcCall(dataSource).withProcedureName(procDetails[2])
				.withCatalogName(procDetails[1]).withSchemaName(procDetails[0]);
		MapSqlParameterSource inputMap = new MapSqlParameterSource(procRequestData.getParams());
		logger.debug("inputMap : " + inputMap.toString());
		Map<String, Object> response = call.execute(inputMap);
		logger.debug("stored proc response : " + response);
		return response;
	}

	public SQLProcedureCallConfig getExecutorSQLProcedureConfig(String key) {
		return executorConfigProperties.getSqlProcedures().get(key);
	}

	@Override
	public Class<?> getExecutorRequestDataClass() {
		return SQLProcedureRequestData.class;
	}

}
