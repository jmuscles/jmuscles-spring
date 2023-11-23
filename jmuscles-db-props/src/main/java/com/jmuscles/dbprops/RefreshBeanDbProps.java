package com.jmuscles.dbprops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.jmuscles.dbprops.jpa.entity.repository.RepositorySetup;
import com.jmuscles.props.util.RefreshBean;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * @author manish goel
 */
public class RefreshBeanDbProps implements RefreshBean {

	private static final Logger logger = LoggerFactory.getLogger(RefreshBeanDbProps.class);

	private JmusclesDbPropsBeans jmusclesPropsBeans;

	public RefreshBeanDbProps(JmusclesDbPropsBeans jmusclesPropsBeans) {
		super();
		this.jmusclesPropsBeans = jmusclesPropsBeans;
	}

	public void handleRefreshEvent() {
		logger.info("Refresh start...");
		BeanFactory beanFactory = this.jmusclesPropsBeans.getBeanFactory();
		RepositorySetup repositorySetup = (RepositorySetup) SpringBeanUtil.getBean("repositorySetup", beanFactory);

		if (repositorySetup != null) {
			repositorySetup.refresh();
		}
		logger.info(" ...Refresh end");
	}

}
