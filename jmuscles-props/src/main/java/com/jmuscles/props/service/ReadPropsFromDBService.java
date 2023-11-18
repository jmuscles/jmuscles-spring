
package com.jmuscles.props.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.jmuscles.props.AppPropsDBConfig;
import com.jmuscles.props.JmusclesConfig;
import com.jmuscles.props.converter.JmusclesConfigUtil;
import com.jmuscles.props.jpa.entity.AppPropsProvisionEntity;
import com.jmuscles.props.jpa.entity.repository.AppProvisionRepository;
import com.jmuscles.props.jpa.entity.repository.PropReadRepository;

/**
 * @author manish goel
 */
public class ReadPropsFromDBService {
	private static final Logger logger = LoggerFactory.getLogger(ReadPropsFromDBService.class);

	private AppProvisionRepository appProvisionRepository;
	private PropReadRepository propReadRepository;
	private AppPropsDBConfig appPropsDBConfig;

	public ReadPropsFromDBService(AppProvisionRepository appProvisionRepository, PropReadRepository propReadRepository,
			AppPropsDBConfig appPropsDBConfig) {
		super();
		this.appProvisionRepository = appProvisionRepository;
		this.propReadRepository = propReadRepository;
		this.appPropsDBConfig = appPropsDBConfig;
	}

	public JmusclesConfig getLatestProperties() {
		Map<String, String> propsSelectionKey = appPropsDBConfig.getPropsSelectionKeys();
		if (propsSelectionKey != null) {
			String appName = propsSelectionKey.get("appName");
			String appGroupName = propsSelectionKey.get("appGroupName");
			String env = propsSelectionKey.get("env");
			return getJmusclesConfig(appGroupName, appName, env);
		}
		return null;
	}

	public void refresh(JmusclesConfig jmusclesConfig) {
		logger.info("Refresh RestTemplateProvider start....");
		JmusclesConfig latestProperties = getLatestProperties();
		if (jmusclesConfig != null) {
			jmusclesConfig.replaceValues(latestProperties);
		} else {
			jmusclesConfig = latestProperties;
		}
		logger.info("....Refresh RestTemplateProvider end");
	}

	public JmusclesConfig getJmusclesConfig(String appGroupName, String appName, String env) {
		JmusclesConfig returnObject = null;
		AppPropsProvisionEntity entity = appProvisionRepository.get(appName, appGroupName, env);
		if (entity != null) {
			returnObject = (JmusclesConfig) getProperties(null, entity.getPropTenantId(), entity.getPropMajorVersion(),
					entity.getPropMinorVersion());
		}

		return returnObject;
	}

	public Object getProperties(String requestPath, Long tenantId, Long majorVersion, Long minorVersion) {
		Object returnObject = null;
		Map<String, Object> map = readDataFromDatabase(requestPath, tenantId, majorVersion, minorVersion);
		if (map != null) {
			try {
				List<String> paths = null;
				if (StringUtils.hasText(requestPath)) {
					paths = new ArrayList<>(Arrays.asList(requestPath.split("\\.")));
				}
				returnObject = JmusclesConfigUtil.mapToJmusclesConfig(map, paths);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnObject;
	}

	public Map<String, Object> readDataFromDatabase(String requestPath, Long tenantId, Long majorVersion,
			Long minorVersion) {
		return propReadRepository.readDataFromDatabase(requestPath, tenantId, majorVersion, minorVersion);
	}

}
