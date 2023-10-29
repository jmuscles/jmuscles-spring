/**
 * @author manish goel
 *
 */
package com.jmuscles.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.props.util.RefreshBean;

/**
 * 
 */

public class RefreshBeanProcessing implements RefreshBean {

	private static final Logger logger = LoggerFactory.getLogger(RefreshBeanProcessing.class);

	private BeanFactory beanFactory;

	public RefreshBeanProcessing(BeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	public void handleRefreshEvent() {
		logger.info("Refresh start...");

		RestTemplateProvider restTemplateProvider = (RestTemplateProvider) beanFactory.getBean("restTemplateProvider");
		restTemplateProvider.refresh();
		logger.info(" ...Refresh end");

	}

}
