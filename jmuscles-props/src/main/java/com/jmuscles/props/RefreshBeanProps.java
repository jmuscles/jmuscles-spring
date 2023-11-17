package com.jmuscles.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;

import com.jmuscles.datasource.DataSourceGenerator;
import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.props.service.ReadPropsFromDBService;
import com.jmuscles.props.util.RefreshBean;
import com.jmuscles.props.util.SpringBeanUtil;

/**
 * @author manish goel
 */
public class RefreshBeanProps implements RefreshBean {

	private static final Logger logger = LoggerFactory.getLogger(RefreshBeanProps.class);

	private JmusclesPropsBeans jmusclesPropsBeans;

	public RefreshBeanProps(JmusclesPropsBeans jmusclesPropsBeans, BeanFactory beanFactory, Environment environment) {
		super();
		this.jmusclesPropsBeans = jmusclesPropsBeans;
	}

	public void handleRefreshEvent() {
		logger.info("Refresh start...");
		BeanFactory beanFactory = this.jmusclesPropsBeans.getBeanFactory();
		JmusclesConfig jmusclesConfig = (JmusclesConfig) SpringBeanUtil.getBean("jmusclesConfig", beanFactory);
		DataSourceProvider dataSourceProvider = (DataSourceProvider) SpringBeanUtil.getBean("dataSourceProvider",
				beanFactory);
		DataSourceGenerator dsgConfigProps = dataSourceProvider.getDsgConfigProps();
		DataSourceGenerator dsgDbProps = dataSourceProvider.getDsgDbProps();
		if (dsgConfigProps != null) {
			dsgConfigProps.refresh();
		}
		ReadPropsFromDBService readPropsFromDBService = (ReadPropsFromDBService) SpringBeanUtil
				.getBean("readPropsFromDBService", beanFactory);
		if (this.jmusclesPropsBeans.isEnabledDbConfig()) {
			readPropsFromDBService.refresh(jmusclesConfig);
			if (dsgDbProps == null) {
				jmusclesPropsBeans.createDbPropsDsg(jmusclesConfig.getDbProperties());
			} else {
				dsgDbProps.refresh();
			}
		} else {
			if (jmusclesConfig != null) {
				jmusclesConfig.clear();
				if (dsgDbProps != null) {
					dsgDbProps.refresh();
				}
				JmusclesConfig jmusclesConfigProps = (JmusclesConfig) SpringBeanUtil.getBean("jmusclesConfigProps",
						beanFactory);
				jmusclesConfig.replaceValues(jmusclesConfigProps);
			}
		}
		logger.info(" ...Refresh end");
	}

}
