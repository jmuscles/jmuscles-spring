
package com.jmuscles.props.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.jmuscles.props.JmusclesConfig;
import com.jmuscles.props.converter.JmusclesConfigUtil;
import com.jmuscles.props.jpa.entity.repository.PropReadRepository;

/**
 * @author manish goel
 */
public class ReadPropsFromDBService {
	private static final Logger logger = LoggerFactory.getLogger(ReadPropsFromDBService.class);

	private PropReadRepository propReadRepository;
	private JmusclesConfig jmusclesConfig;

	public ReadPropsFromDBService(PropReadRepository propReadRepository) {
		this.propReadRepository = propReadRepository;
	}

	public JmusclesConfig getLatestProperties() {
		return (JmusclesConfig) getProperties(null, 1L, null, null);
	}

	public void initialize() {
		this.jmusclesConfig = getLatestProperties();
	}

	public void refresh() {
		logger.info("Refresh RestTemplateProvider start....");
		JmusclesConfig latestProperties = getLatestProperties();
		if (this.jmusclesConfig != null) {
			jmusclesConfig.replaceValues(latestProperties);
		} else {
			jmusclesConfig = latestProperties;
		}
		logger.info("....Refresh RestTemplateProvider end");
	}

	/**
	 * @return the jmusclesConfig
	 */
	public JmusclesConfig getJmusclesConfig() {
		return jmusclesConfig;
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
