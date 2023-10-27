/**
 * @author manish goel
 *
 */
package com.jmuscles.async.consumer;

import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.async.consumer.config.setup.RabbitmqSetupConfigurator;
import com.jmuscles.props.util.RefreshBean;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * 
 */
public class RefreshBeanConsumer implements RefreshBean {

	private BeanFactory beanFactory;

	public RefreshBeanConsumer(BeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	public void handleRefreshEvent() {
		RabbitmqSetupConfigurator rabbitmqSetupConfigurator = (RabbitmqSetupConfigurator) SpringBeanUtil
				.getBean("rabbitmqSetupConfigurator", this.beanFactory);
		rabbitmqSetupConfigurator.refresh();
	}

}
