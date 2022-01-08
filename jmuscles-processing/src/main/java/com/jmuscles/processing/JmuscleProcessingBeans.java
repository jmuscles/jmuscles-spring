package com.jmuscles.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.processing.config.ExecutorConfigProperties;
import com.jmuscles.processing.executor.ExecutorRegistry;
import com.jmuscles.processing.executor.implementation.DemoRequestExecutor;
import com.jmuscles.processing.executor.implementation.RestExecutor;
import com.jmuscles.processing.executor.implementation.SQLProcedureExecutor;
import com.jmuscles.processing.executor.implementation.SQLQueryExecutor;
import com.jmuscles.processing.executor.implementation.SequentialRequestExecutor;

public class JmuscleProcessingBeans {

	private static final Logger logger = LoggerFactory.getLogger(JmuscleProcessingBeans.class);

	@Bean("executorConfigProperties")
	@ConfigurationProperties(value = "jmuscles.executors-config")
	public ExecutorConfigProperties executorConfigProperties() {
		return new ExecutorConfigProperties();
	}

	@Bean("restTemplateProvider")
	public RestTemplateProvider restTemplateProvider(
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties) {
		return new RestTemplateProvider(executorConfigProperties);
	}

	@ConditionalOnMissingBean(ExecutorRegistry.class)
	@Bean("executorRegistry")
	public ExecutorRegistry executorRegistry() {
		return new ExecutorRegistry();
	}

	@Bean("demoRequestExecutor")
	public DemoRequestExecutor demoRequestExecutor(@Qualifier("executorRegistry") ExecutorRegistry executorRegistry) {
		return new DemoRequestExecutor(executorRegistry);
	}

	@Bean("sequentialRequestExecutor")
	public SequentialRequestExecutor sequentialRequestExecutor(
			@Qualifier("executorRegistry") ExecutorRegistry executorRegistry) {
		return new SequentialRequestExecutor(executorRegistry);
	}

	@Bean("restExecutor")
	public RestExecutor restExecutor(@Qualifier("executorRegistry") ExecutorRegistry executorRegistry,
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties,
			@Qualifier("restTemplateProvider") RestTemplateProvider restTemplateProvider) {
		return new RestExecutor(executorRegistry, executorConfigProperties, restTemplateProvider);
	}

	@Bean("sQLQueryExecutor")
	public SQLQueryExecutor sQLQueryExecutor(@Qualifier("executorRegistry") ExecutorRegistry executorRegistry,
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties,
			@Qualifier("dataSourceGenerator") DataSourceGenerator dataSourceGenerator) {
		return new SQLQueryExecutor(executorRegistry, executorConfigProperties, dataSourceGenerator);
	}

	@Bean("sQLProcedureExecutor")
	public SQLProcedureExecutor sQLProcedureExecutor(@Qualifier("executorRegistry") ExecutorRegistry executorRegistry,
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties,
			@Qualifier("dataSourceGenerator") DataSourceGenerator dataSourceGenerator) {
		return new SQLProcedureExecutor(executorRegistry, executorConfigProperties, dataSourceGenerator);
	}

}
