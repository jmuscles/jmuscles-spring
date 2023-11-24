/**
 * @author manish goel
 *
 */
package com.jmuscles.props.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;

/**
 * 
 */
public class RefreshBeansUtil {

	public static void refresh(String[] beansArray, BeanFactory beanFactory) {
		getBeansToRefresh(beansArray, beanFactory).forEach(bean -> bean.handleRefreshEvent());
	}

	private static List<RefreshBean> getBeansToRefresh(String[] beansToRefresh, BeanFactory beanFactory) {
		List<RefreshBean> list = new ArrayList<RefreshBean>();
		if (beansToRefresh != null) {
			for (String beanName : beansToRefresh) {
				RefreshBean bean = getRefreshBean(beanName, beanFactory);
				if (bean != null) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	private static RefreshBean getRefreshBean(String beanName, BeanFactory beanFactory) {
		return (RefreshBean) SpringBeanUtil.getBean(beanName, beanFactory);
	}

}
