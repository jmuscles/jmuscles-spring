/**
 * 
 */
package com.jmuscles.props.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author manish goel
 *
 */
public class SpringBeanUtil {

	private static final Logger logger = LoggerFactory.getLogger(SpringBeanUtil.class);

	// private static BeanFactory localBeanFactory;

	/*
	 * public static Object getBean(String beanName) { return getBean(beanName,
	 * localBeanFactory); }
	 * 
	 * public static Object getBean(Class<?> iClass) { return getBean(iClass,
	 * localBeanFactory); }
	 */

	public static Object getBean(String beanName, BeanFactory beanFactory) {

		Object bean = null;
		try {
			bean = beanFactory.getBean(beanName);
		} catch (NoSuchBeanDefinitionException e) {
			logger.debug("Error creating bean :" + beanName, e);
			bean = null;
		}

		return bean;
	}

	public static Object getBean(Class<?> iClass, BeanFactory beanFactory) throws NoSuchBeanDefinitionException {

		Object bean = null;
		try {
			bean = beanFactory.getBean(iClass);
		} catch (NoSuchBeanDefinitionException e) {
			logger.debug("Error creating for class :" + iClass, e);
			bean = null;
		}

		return bean;
	}

	/*
	 * public static void registerSingletonBean(String beanName, Object bean) {
	 * registerSingletonBean(beanName, bean, localBeanFactory); }
	 */

	public static void registerSingletonBean(String beanName, Object bean, BeanFactory beanFactory) {
		ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
		configurableBeanFactory.registerSingleton(beanName, bean);
	}

}
