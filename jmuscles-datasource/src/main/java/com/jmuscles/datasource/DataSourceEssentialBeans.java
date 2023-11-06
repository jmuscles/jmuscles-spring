/**
 * 
 */
package com.jmuscles.datasource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.jmuscles.datasource.jasypt.JasyptDecryptor;
import com.jmuscles.datasource.operator.DataSourceOperatorRegistry;
import com.jmuscles.datasource.properties.DatabaseProperties;

/**
 * @author manish goel
 *
 */
public class DataSourceEssentialBeans implements BeanFactoryAware {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceEssentialBeans.class);

	private List<JasyptDecryptor> jasyptDecryptors;
	private BeanFactory beanFactory;

	public DataSourceEssentialBeans(List<JasyptDecryptor> jasyptDecryptors) {
		this.jasyptDecryptors = jasyptDecryptors;
	}

	@Bean("jmusclesDatabaseProperties")
	@ConfigurationProperties(value = "jmuscles.db-properties", ignoreInvalidFields = true, ignoreUnknownFields = true)
	public DatabaseProperties databaseProperties() {
		return new DatabaseProperties();
	}

	@Bean("dataSourceOperatorRegistry")
	public DataSourceOperatorRegistry dataSourceOperatorRegistry() {
		return new DataSourceOperatorRegistry();
	}

	@Bean("dataSourceProvider")
	public DataSourceProvider dataSourceProvider() {
		return new DataSourceProvider();
	}

	@Bean("dataSourceGenerator")
	public DataSourceGenerator dataSourceGenerator(
			@Qualifier("jmusclesDatabaseProperties") DatabaseProperties databaseProperties,
			@Qualifier("dataSourceOperatorRegistry") @Autowired(required = false) DataSourceOperatorRegistry dataSourceOperatorRegistry,
			@Qualifier("dataSourceProvider") DataSourceProvider dataSourceProvider) {
		return new DataSourceGenerator(databaseProperties, this.jasyptDecryptors, DataSourceProvider.DSG_CONFIG_PROPS,
				dataSourceOperatorRegistry, dataSourceProvider);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
