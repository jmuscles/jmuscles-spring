/**
 * @author manish goel
 *
 */
package com.jmuscles.rest.producer;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.jmuscles.props.util.RefreshBeansUtil;

/**
 * 
 */
public class RefreshBeans {

	public static final String[] BEANS_ARRAY = new String[] { "refreshBeanDbProps", "refreshBeanProps",
			"refreshBeanProcessing", "refreshBeanProducer" };

	private BeanFactory beanFactory;

	public RefreshBeans(BeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	@EventListener(RefreshScopeRefreshedEvent.class)
	public void handleRefreshEvent() {
		RefreshBeansUtil.refresh(BEANS_ARRAY, beanFactory);
	}

}
