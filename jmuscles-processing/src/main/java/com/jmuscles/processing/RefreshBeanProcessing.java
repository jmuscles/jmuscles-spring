/**
 * @author manish goel
 *
 */
package com.jmuscles.processing;

import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.props.util.RefreshBean;

/**
 * 
 */

public class RefreshBeanProcessing implements RefreshBean {

	private BeanFactory beanFactory;

	public RefreshBeanProcessing(BeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	public void handleRefreshEvent() {
		RestTemplateProvider restTemplateProvider = (RestTemplateProvider) beanFactory.getBean("restTemplateProvider");
		restTemplateProvider.refresh();;
	}

}
