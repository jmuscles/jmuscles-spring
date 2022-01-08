/**
 * 
 */
package com.jmuscles.datasource.operator.implementation;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.jmuscles.datasource.operator.DataSourceOperator;
import com.jmuscles.datasource.operator.DataSourceOperatorRegistry;

/**
 * @author manish goel
 *
 */
public class HikariDataSourceOperator extends DataSourceOperator {

	private final Logger logger = LoggerFactory.getLogger(HikariDataSourceOperator.class);

	private static final String HIKARI_DATASOURCE_CLASS_NAME = "com.zaxxer.hikari.HikariDataSource";
	private static final String GET_HIKARI_CONFIG_MXBEAN_METHOD = "getHikariConfigMXBean";
	private static final String HIKARI_CONFIG_CLASS_NAME = "com.zaxxer.hikari.HikariConfig";
	private static final String HIKARI_CONFIG_MXBEAN_CLASS_NAME = "com.zaxxer.hikari.HikariConfigMXBean";

	private static Class<?> hikariDataSourceClass;
	private static Constructor<?> hikariDataSourceConstructor;
	private static Method getHikariConfigMXBeanMethod;

	private static Class<?> hikariConfigClass;
	private static Constructor<?> hikariConfigConstructor;

	private static Class<?> hikariConfigMXBeanClass;
	private static Method[] hikariConfigMXBeanClassMethods;

	private static boolean initialized = false;

	public HikariDataSourceOperator(DataSourceOperatorRegistry dataSourceOperatorRegistry) {
		super(dataSourceOperatorRegistry);
	}

	private void initialize() {
		if (!initialized) {

			try {
				hikariDataSourceClass = Class.forName(HIKARI_DATASOURCE_CLASS_NAME, false,
						HikariDataSourceOperator.class.getClassLoader());
				hikariConfigClass = Class.forName(HIKARI_CONFIG_CLASS_NAME, false,
						HikariDataSourceOperator.class.getClassLoader());

				hikariConfigConstructor = hikariConfigClass.getConstructor(Properties.class);
				hikariDataSourceConstructor = hikariDataSourceClass.getConstructor(hikariConfigClass);
				getHikariConfigMXBeanMethod = hikariDataSourceClass.getMethod(GET_HIKARI_CONFIG_MXBEAN_METHOD);
				hikariConfigMXBeanClass = Class.forName(HIKARI_CONFIG_MXBEAN_CLASS_NAME, false,
						HikariDataSourceOperator.class.getClassLoader());
				hikariConfigMXBeanClassMethods = hikariConfigMXBeanClass.getDeclaredMethods();

			} catch (ClassNotFoundException e) {
				logger.error("Error initializing Hikari datasources", e);
			} catch (NoSuchMethodException e) {
				logger.error("Error initializing Hikari datasources", e);
			} catch (SecurityException e) {
				logger.error("Error initializing Hikari datasources", e);
			}

			initialized = true;
		}
	}

	@Override
	public DataSource create(String dataSourceName, Properties props) {
		initialize();
		DataSource dataSource = null;
		try {
			props.put("registerMbeans", true);
			if (!StringUtils.hasText((String) props.get("poolName"))) {
				props.put("poolName", dataSourceName + "-Pool");
			}
			dataSource = (DataSource) hikariDataSourceConstructor
					.newInstance(hikariConfigConstructor.newInstance(props));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("Error initializing Hikari datasources", e);
		}

		return dataSource;
	}

	private Object convert(Class<?> targetType, String text) {
		PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
		editor.setAsText(text);
		return editor.getValue();
	}

	@Override
	public void refresh(String dataSourceName, DataSource dataSource, Properties newProperties) {
		initialize();
		Object hikariConfigMXBean = null;

		if (hikariDataSourceClass.isInstance(dataSource)) {
			try {
				hikariConfigMXBean = getHikariConfigMXBeanMethod.invoke(dataSource);
			} catch (Exception e) {
				logger.error("Error refreshing hikari datasource while retrieving hikariConfigMXBean", e);
				return;
			}
			for (Object propKey : newProperties.keySet()) {
				String prop = ((String) propKey);
				String methodName = "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
				Method hikariConfigMXBeanClassMethod = Arrays.stream(hikariConfigMXBeanClassMethods)
						.filter(method -> method.getName().equals(methodName)).findFirst().orElse(null);
				if (hikariConfigMXBeanClassMethod != null) {
					Class<?> type = hikariConfigMXBeanClassMethod.getParameterTypes()[0];
					try {
						hikariConfigMXBeanClassMethod.invoke(hikariConfigMXBean,
								convert(type, String.valueOf(newProperties.get(prop))));
					} catch (Exception e) {
						logger.error("Error refreshing hikari datasource while setting property: " + prop
								+ ", methodName: " + methodName + ", type: " + type, e);
						return;
					}
				}
			}

		}
	}

	@Override
	public String dataSourceType() {
		// TODO Auto-generated method stub
		return "HIKARI";
	}

}
