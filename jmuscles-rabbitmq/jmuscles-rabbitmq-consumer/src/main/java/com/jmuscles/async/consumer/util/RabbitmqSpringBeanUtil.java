/**
 * 
 */
package com.jmuscles.async.consumer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.processing.SpringBeanUtil;

/**
 * @author manish goel
 *
 */
public class RabbitmqSpringBeanUtil {

	private static final Logger logger = LoggerFactory.getLogger(RabbitmqSpringBeanUtil.class);
	private static final String ENTITY_NAME_PATTERN = "[^0-9a-zA-Z_]";

	private static String buildBeanNameForRabbitEntity(String entityName, Class<?> entityClass) {
		entityName = entityName.replaceAll(ENTITY_NAME_PATTERN, "");
		return (entityClass.getSimpleName() + "bean_" + entityName).toLowerCase();
	}

	public static Object getRabbitEntitySpringBean(String entityName, Class<?> entityClass, BeanFactory beanFactory) {
		return SpringBeanUtil.getBean(buildBeanNameForRabbitEntity(entityName, entityClass), beanFactory);
	}

	public static void registerRabbitEntityAsSpringBean(String entityName, Class<?> entityClass, Object entity,
			BeanFactory beanFactory) {
		SpringBeanUtil.registerSingletonBean(buildBeanNameForRabbitEntity(entityName, entityClass), entity,
				beanFactory);
	}

}
