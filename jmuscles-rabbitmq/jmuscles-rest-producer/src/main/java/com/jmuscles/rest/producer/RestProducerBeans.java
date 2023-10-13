/**
 * 
 */
package com.jmuscles.rest.producer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.jmuscles.rest.producer.config.properties.RestConfPropsForConfigKey;
import com.jmuscles.rest.producer.helper.JmusclesProducerHelper;
import com.jmuscles.rest.producer.helper.JmusclesRestControllerBean;
import com.jmuscles.rest.producer.response.ResponseBuilder;
import com.jmuscles.rest.producer.response.SimpleResponseBuilder;

/**
 * @author manish goel
 *
 */
//@Component
public class RestProducerBeans implements BeanFactoryAware {

	private static final Logger logger = LoggerFactory.getLogger(RestProducerBeans.class);

	private BeanFactory beanFactory;

	@Bean("restProducerConfigPropertiesMap")
	@ConfigurationProperties(value = "jmuscles.rest-producer-config")
	public Map<String, RestConfPropsForConfigKey> restProducerConfigProperties() {
		return new HashMap<>();
	}

	@Bean("httpReqResLoggingIntercetpor")
	public HttpReqResLoggingIntercetpor httpReqResLoggingIntercetpor() {
		return new HttpReqResLoggingIntercetpor();
	}

	@Bean("metricsGenerator")
	public MetricsGenerator metricsGenerator() {
		return new MetricsGenerator();
	}

	@Bean("responseBuilder")
	public ResponseBuilder responseBuilder() {
		return new ResponseBuilder();
	}

	@Bean("simpleResponseBuilder")
	public SimpleResponseBuilder simpleResponseBuilder() {
		return new SimpleResponseBuilder();
	}

	@Bean("jmusclesProducerHelper")
	public JmusclesProducerHelper jmusclesProducerHelper() {
		return new JmusclesProducerHelper();
	}

	@Bean("jmusclesRestControllerBean")
	public JmusclesRestControllerBean jmusclesRestControllerBean() {
		return new JmusclesRestControllerBean();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
