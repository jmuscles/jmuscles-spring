/**
 * @author manish goel
 *
 */
package com.jmuscles.async.producer;

import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.async.producer.jpa.asyncpayload.AsyncPayloadPersister;
import com.jmuscles.props.util.RefreshBean;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * 
 */
public class RefreshBeanProducer implements RefreshBean {

	private BeanFactory beanFactory;

	public RefreshBeanProducer(BeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	public void handleRefreshEvent() {
		AsyncPayloadPersister asyncPayloadPersister = (AsyncPayloadPersister) SpringBeanUtil
				.getBean("asyncPayloadPersister", beanFactory);
		asyncPayloadPersister.refresh();
	}

}
