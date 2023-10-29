/**
 * 
 */
package com.jmuscles.datasource.operator.implementation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.datasource.operator.DataSourceOperator;
import com.jmuscles.datasource.operator.DataSourceOperatorRegistry;

/**
 * @author manish goel
 *
 */
public class TomcatDataSourceOperator extends DataSourceOperator {

	private static final Logger logger = LoggerFactory.getLogger(TomcatDataSourceOperator.class);

	private static final String TOMCAT_DATASOURCE_FACTORY_CLASS_NAME = "org.apache.tomcat.jdbc.pool.DataSourceFactory";
	private static final String TOMCAT_DATASOURCE_CREATE_METHOD_NAME = "createDataSource";

	private Object tomcatDataSourceFactoryInstance;
	private Method createDataSourceMethod;

	private boolean initialized = false;

	public TomcatDataSourceOperator(DataSourceOperatorRegistry dataSourceOperatorRegistry) {
		super(dataSourceOperatorRegistry);
	}

	private void initialize() {
		if (!initialized) {

			Class<?> factoryClass = null;
			try {
				factoryClass = Class.forName(TOMCAT_DATASOURCE_FACTORY_CLASS_NAME, false,
						TomcatDataSourceOperator.class.getClassLoader());
				tomcatDataSourceFactoryInstance = factoryClass.newInstance();
			} catch (ClassNotFoundException e) {
				logger.error("Error initializing Tomcat datasources", e);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("Error initializing Tomcat datasources", e);
			}
			if (tomcatDataSourceFactoryInstance != null) {
				try {
					createDataSourceMethod = factoryClass.getMethod(TOMCAT_DATASOURCE_CREATE_METHOD_NAME,
							Properties.class);
				} catch (NoSuchMethodException | SecurityException e) {
					logger.error("Error initializing Tomcat datasources", e);
				}
			}

			initialized = true;
		}
	}

	@Override
	public DataSource create(String dataSourceName, Properties properties) {
		initialize();
		DataSource dataSource = null;
		if (createDataSourceMethod != null) {
			try {
				if (properties.get("jdbcUrl") != null && properties.get("url") == null) {
					properties.put("url", properties.remove("jdbcUrl"));
				}
				dataSource = (DataSource) createDataSourceMethod.invoke(tomcatDataSourceFactoryInstance, properties);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				logger.error("Error initializing Tomcat datasources", e);
			}
		}

		return dataSource;
	}

	@Override
	public void refresh(String dataSourceName, DataSource dataSource, Properties newProperties) {
		logger.warn(
				"Tomcat datasource can not be updated at run time. Please restart app to update the datasource properties.");
	}

	@Override
	public void close(DataSource dataSource) {
		logger.warn(
				"Tomcat datasource can not be closed at run time. Please restart app to update the datasource properties.");

	}

	@Override
	public String dataSourceType() {
		return "TOMCAT";
	}

}
