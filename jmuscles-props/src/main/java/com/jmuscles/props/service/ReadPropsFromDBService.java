
package com.jmuscles.props.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmuscles.props.JmusclesConfig;
import com.jmuscles.props.converter.MapToObjectConverter;
import com.jmuscles.props.jpa.AppPropsEntity;
import com.jmuscles.props.jpa.AppPropsRepository;

/**
 * @author manish goel
 */
public class ReadPropsFromDBService {
	private static final Logger logger = LoggerFactory.getLogger(ReadPropsFromDBService.class);

	private AppPropsRepository appPropsRepository;
	private JmusclesConfig jmusclesConfig;

	public ReadPropsFromDBService(AppPropsRepository appPropsRepository) {
		this.appPropsRepository = appPropsRepository;
	}

	public JmusclesConfig getLatestProperties() {
		return (JmusclesConfig) getProperties(null);
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

	public Object getProperties(String requestPath) {
		Object returnObject = null;
		List<String> paths = null;
		if (StringUtils.hasText(requestPath)) {
			paths = new ArrayList<>(Arrays.asList(requestPath.split("\\.")));
		}
		Map<String, Object> map = readDataFromDatabase(paths);
		if (map != null) {
			returnObject = MapToObjectConverter.mapToObject(map, paths);
		}
		return returnObject;
	}

	public Map<String, Object> readDataFromDatabase(String requestPath) {
		List<String> paths = null;
		if (StringUtils.hasText(requestPath)) {
			paths = Arrays.asList(requestPath.split("\\."));
		}
		return readDataFromDatabase(paths);
	}

	public Map<String, Object> readDataFromDatabase(List<String> paths) {
		List<AppPropsEntity> topLevelProperties = null;
		if (paths == null) {
			topLevelProperties = appPropsRepository.findAllByParentIsNull();
		} else {
			AppPropsEntity appPropsEntity = appPropsRepository.findByKeyPath(paths);
			if (appPropsEntity != null) {
				topLevelProperties = new ArrayList<AppPropsEntity>();
				topLevelProperties.add(appPropsEntity);
			}
		}
		return topLevelProperties != null
				? topLevelProperties.stream()
						.collect(Collectors.toMap(AppPropsEntity::getProp_key, this::buildNestedMap))
				: null;
	}

	private Object buildNestedMap(AppPropsEntity propertyEntity) {
		Map<String, Object> nestedMap = new HashMap<>();
		List<AppPropsEntity> children = appPropsRepository.findAllByParent(propertyEntity.getId());
		for (AppPropsEntity child : children) {
			nestedMap.put(child.getProp_key(), buildNestedMap(child));
		}
		if (propertyEntity.getProp_value() != null) {
			// Check if the value is a JSON string representing a map and parse it
			try {
				return new ObjectMapper().readValue(propertyEntity.getProp_value(), Map.class);
			} catch (IOException e) {
				// Value is not a valid JSON, return it as a string
				return propertyEntity.getProp_value();
			}
		}
		return nestedMap;
	}

}
