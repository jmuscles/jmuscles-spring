
package com.jmuscles.dbprops.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.jmuscles.dbprops.jpa.entity.AppPropsProvisionEntity;
import com.jmuscles.dbprops.jpa.entity.repository.AppProvisionRepository;
import com.jmuscles.dbprops.jpa.entity.repository.PropReadRepository;
import com.jmuscles.props.config.AppPropsDBConfig;
import com.jmuscles.props.config.JmusclesConfig;
import com.jmuscles.props.converter.JmusclesConfigUtil;

/**
 * @author manish goel
 */
public class ReadPropsFromDBService {
	private static final Logger logger = LoggerFactory.getLogger(ReadPropsFromDBService.class);

	private AppProvisionRepository appProvisionRepository;
	private PropReadRepository propReadRepository;

	public ReadPropsFromDBService(AppProvisionRepository appProvisionRepository,
			PropReadRepository propReadRepository) {
		super();
		this.appProvisionRepository = appProvisionRepository;
		this.propReadRepository = propReadRepository;
	}

	public JmusclesConfig getJmusclesConfig(AppPropsDBConfig appPropsDBConfig) {
		JmusclesConfig returnObject = null;
		if (appPropsDBConfig != null) {
			Map<String, String> selectionKeys = appPropsDBConfig.getSelectionKeys();
			String appGroupName = selectionKeys.get("appGroupName");
			String appName = selectionKeys.get("appName");
			String env = selectionKeys.get("env");
			if (StringUtils.hasText(appName) && StringUtils.hasText(appGroupName) && StringUtils.hasText(env)) {
				AppPropsProvisionEntity entity = appProvisionRepository.get(appName, appGroupName, env);
				if (entity != null) {
					returnObject = (JmusclesConfig) getProperties(null, entity.getPropTenantId(),
							entity.getPropMajorVersion(), entity.getPropMinorVersion());
				}
			}
		}
		return returnObject;
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
				logger.error("Error while retreiving properties ", e);
				logger.error(e.getStackTrace().toString());
			}
		}
		return returnObject;
	}

	public Map<String, Object> readDataFromDatabase(String requestPath, Long tenantId, Long majorVersion,
			Long minorVersion) {
		return propReadRepository.readDataFromDatabase(requestPath, tenantId, majorVersion, minorVersion);
	}

}
