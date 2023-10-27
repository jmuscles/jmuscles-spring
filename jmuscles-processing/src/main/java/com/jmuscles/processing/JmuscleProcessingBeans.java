package com.jmuscles.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.processing.config.properties.ExecutorConfigProperties;
import com.jmuscles.processing.executor.CustomExecutorRegistry;
import com.jmuscles.processing.executor.StandardExecutorRegistry;
import com.jmuscles.processing.executor.implementation.CustomRequestExecutor;
import com.jmuscles.processing.executor.implementation.DemoRequestExecutor;
import com.jmuscles.processing.executor.implementation.RestExecutor;
import com.jmuscles.processing.executor.implementation.SQLProcedureExecutor;
import com.jmuscles.processing.executor.implementation.SQLQueryExecutor;
import com.jmuscles.processing.executor.implementation.SequentialRequestExecutor;
import com.jmuscles.props.JmusclesPropsBeans;
import com.jmuscles.props.util.JmusclesConfig;

@Import(JmusclesPropsBeans.class)
public class JmuscleProcessingBeans implements BeanFactoryAware {

	private static final Logger logger = LoggerFactory.getLogger(JmuscleProcessingBeans.class);

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Bean("executorConfigProperties")
	public ExecutorConfigProperties executorConfigProperties(
			@Qualifier("jmusclesConfig") JmusclesConfig jmusclesConfig) {
		return jmusclesConfig.getExecutorsConfig();
	}

	@Bean("restTemplateProvider")
	public RestTemplateProvider restTemplateProvider(
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties) {
		return new RestTemplateProvider(executorConfigProperties);
	}

	@ConditionalOnMissingBean(StandardExecutorRegistry.class)
	@Bean("standardExecutorRegistry")
	public StandardExecutorRegistry standardExecutorRegistry() {
		return new StandardExecutorRegistry();
	}

	@ConditionalOnMissingBean(CustomExecutorRegistry.class)
	@Bean("customExecutorRegistry")
	public CustomExecutorRegistry customExecutorRegistry() {
		return new CustomExecutorRegistry();
	}

	@Bean("demoRequestExecutor")
	public DemoRequestExecutor demoRequestExecutor(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry) {
		return new DemoRequestExecutor(standardExecutorRegistry);
	}

	@Bean("sequentialRequestExecutor")
	public SequentialRequestExecutor sequentialRequestExecutor(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry) {
		return new SequentialRequestExecutor(standardExecutorRegistry);
	}

	@Bean("customRequestExecutor")
	public CustomRequestExecutor customRequestExecutor(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry,
			@Qualifier("customExecutorRegistry") CustomExecutorRegistry customExecutorRegistry) {
		return new CustomRequestExecutor(standardExecutorRegistry, customExecutorRegistry);
	}

	@Bean("restExecutor")
	public RestExecutor restExecutor(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry,
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties,
			@Qualifier("restTemplateProvider") RestTemplateProvider restTemplateProvider) {
		return new RestExecutor(standardExecutorRegistry, executorConfigProperties, restTemplateProvider);
	}

	@Bean("sQLQueryExecutor")
	public SQLQueryExecutor sQLQueryExecutor(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry,
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties,
			@Qualifier("dataSourceGenerator") DataSourceGenerator dataSourceGenerator) {
		return new SQLQueryExecutor(standardExecutorRegistry, executorConfigProperties, dataSourceGenerator);
	}

	@Bean("sQLProcedureExecutor")
	public SQLProcedureExecutor sQLProcedureExecutor(
			@Qualifier("standardExecutorRegistry") StandardExecutorRegistry standardExecutorRegistry,
			@Qualifier("executorConfigProperties") ExecutorConfigProperties executorConfigProperties,
			@Qualifier("dataSourceGenerator") DataSourceGenerator dataSourceGenerator) {
		return new SQLProcedureExecutor(standardExecutorRegistry, executorConfigProperties, dataSourceGenerator);
	}

	@Bean("refreshBeanProcessing")
	public RefreshBeanProcessing refreshBeanProcessing() {
		return new RefreshBeanProcessing(this.beanFactory);
	}

}
