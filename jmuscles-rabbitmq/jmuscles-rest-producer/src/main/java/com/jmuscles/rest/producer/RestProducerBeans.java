/**
 * 
 */
package com.jmuscles.rest.producer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.jmuscles.async.producer.ProducerProcessingBeans;
import com.jmuscles.props.JmusclesConfig;
import com.jmuscles.rest.producer.config.properties.RestConfPropsForConfigKey;
import com.jmuscles.rest.producer.helper.JmusclesProducerHelper;
import com.jmuscles.rest.producer.helper.JmusclesRestControllerBean;
import com.jmuscles.rest.producer.response.ResponseBuilder;
import com.jmuscles.rest.producer.response.SimpleResponseBuilder;

/**
 * @author manish goel
 *
 */
@Import(ProducerProcessingBeans.class)
class RestProducerBeans implements BeanFactoryAware {

	private static final Logger logger = LoggerFactory.getLogger(RestProducerBeans.class);

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Bean("restProducerConfigPropertiesMap")
	public Map<String, RestConfPropsForConfigKey> restProducerConfigProperties(
			@Qualifier("jmusclesConfig") JmusclesConfig jmusclesConfig) {
		return jmusclesConfig.getRestProducerConfig();
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
	public ResponseBuilder responseBuilder(
			@Qualifier("restProducerConfigPropertiesMap") Map<String, RestConfPropsForConfigKey> restProducerConfigProperties) {
		return new ResponseBuilder(restProducerConfigProperties);
	}

	@Bean("simpleResponseBuilder")
	public SimpleResponseBuilder simpleResponseBuilder(
			@Qualifier("restProducerConfigPropertiesMap") Map<String, RestConfPropsForConfigKey> restProducerConfigProperties) {
		return new SimpleResponseBuilder(restProducerConfigProperties);
	}

	@Bean("jmusclesProducerHelper")
	public JmusclesProducerHelper jmusclesProducerHelper(
			@Qualifier("restProducerConfigPropertiesMap") Map<String, RestConfPropsForConfigKey> restProducerConfigProperties,
			@Qualifier("responseBuilder") ResponseBuilder responseBuilder) {
		return new JmusclesProducerHelper(restProducerConfigProperties, responseBuilder);
	}

	@Bean("jmusclesRestControllerBean")
	public JmusclesRestControllerBean jmusclesRestControllerBean(
			@Qualifier("jmusclesProducerHelper") JmusclesProducerHelper jmusclesProducerHelper) {
		return new JmusclesRestControllerBean(jmusclesProducerHelper);
	}

	@Bean("jmusclesRestRefreshBeans")
	private RefreshBeans jmusclesRestRefreshBeans() {
		return new RefreshBeans(this.beanFactory);
	}

}
