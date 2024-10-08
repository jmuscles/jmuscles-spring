/**
 * @author manish goel
 *
 */
package com.jmuscles.props.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.jmuscles.props.config.JmusclesConfig;

/**
 * 
 */
public class JmusclesConfigUtil {

	private static final Logger logger = LoggerFactory.getLogger(JmusclesConfigUtil.class);

	public static JmusclesConfig mapToJmusclesConfigForSnakeCaseYaml(Map<String, Object> map) {
		return MapToObjectConverter.mapToObject(JmusclesConfigUtil.convertJmusclesFieldsFromSnakeToCamelCase(map),
				JmusclesConfig.class);
	}

	public static JmusclesConfig mapToJmusclesConfig(Map<String, Object> map, List<String> paths) throws Exception {
		JmusclesConfig jmusclesConfig = null;
		if (paths == null || paths.isEmpty() || paths.size() == 1) {
			jmusclesConfig = MapToObjectConverter.mapToObject((Map<String, Object>) map.get("jmuscles"),
					JmusclesConfig.class);
		} else {
			jmusclesConfig = (JmusclesConfig) MapToObjectConverter.mapToObject(map, paths, new JmusclesConfig());
		}
		return jmusclesConfig;
	}

	public static Map<String, Object> jmusclesConfigToMap_SnakCaseKeys(JmusclesConfig jmusclesConfig) {
		return jmusclesConfigToMap(jmusclesConfig, true);
	}

	public static Map<String, Object> jmusclesConfigToMap(JmusclesConfig jmusclesConfig) {
		return jmusclesConfigToMap(jmusclesConfig, false);
	}

	public static Map<String, Object> jmusclesConfigToMap(JmusclesConfig jmusclesConfig, boolean snakeCaseKeys) {
		Map<String, Object> objectToMap = ObjectToMapConverter.objectToMap(jmusclesConfig);
		if (snakeCaseKeys) {
			objectToMap = JmusclesConfigUtil.convertJmusclesFieldsFromCamelCaseToSnake(objectToMap);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("jmuscles", objectToMap);

		return map;
	}

	public static Object jmusclesConfigToMap(JmusclesConfig jmusclesConfig, String requestPath) {
		List<String> paths = null;
		if (StringUtils.hasText(requestPath)) {
			paths = new ArrayList<>(Arrays.asList(requestPath.split("\\.")));
		}
		return ObjectToMapConverter.resolveValue(getObjectByPath(jmusclesConfig, paths));
	}

	public static Object getObjectByPath(JmusclesConfig jmusclesConfig, String requestPath) {
		List<String> paths = null;
		if (StringUtils.hasText(requestPath)) {
			paths = new ArrayList<>(Arrays.asList(requestPath.split("\\.")));
		}
		return getObjectByPath(jmusclesConfig, paths);
	}

	public static Object getObjectByPath(JmusclesConfig jmusclesConfig, List<String> paths) {
		Object returnObject = null;
		if (paths == null || paths.isEmpty() || paths.size() == 1) {
			returnObject = jmusclesConfig;
		} else {
			Object currentObject = jmusclesConfig;
			for (int i = 1; i < paths.size(); i++) {
				if (currentObject instanceof Map) {
					currentObject = ((Map<?, ?>) currentObject).get(paths.get(i));
				} else if (ConverterUtil.isLocalType(currentObject.getClass())) {
					try {
						Field field = currentObject.getClass().getDeclaredField(paths.get(i));
						field.setAccessible(true);
						currentObject = field.get(currentObject);
					} catch (Exception e) {
						logger.error(
								"Error while finding out the object represented by request path from JmusclesConfig",
								e);
					}
				} else {
					break;
				}
			}
			returnObject = currentObject;
		}
		return returnObject;

	}

	public static Map<String, Object> convertJmusclesFieldsFromSnakeToCamelCase(Map<String, Object> map) {
		if (map != null) {
			if (map.get("async-producer-config") != null) {
				map.put("asyncProducerConfig", map.get("async-producer-config"));
			}
			if (map.get("rest-producer-config") != null) {
				map.put("restProducerConfig", map.get("rest-producer-config"));
			}
			if (map.get("rabbitmq-config") != null) {
				map.put("rabbitmqConfig", map.get("rabbitmq-config"));
			}
			if (map.get("executors-config") != null) {
				map.put("executorsConfig", map.get("executors-config"));
			}
			if (map.get("db-properties") != null) {
				map.put("dbProperties", map.get("db-properties"));
			}
		}
		return map;
	}

	public static Map<String, Object> convertJmusclesFieldsFromCamelCaseToSnake(Map<String, Object> map) {
		if (map != null) {
			if (map.get("asyncProducerConfig") != null) {
				map.put("async-producer-config", map.get("asyncProducerConfig"));
			}
			if (map.get("restProducerConfig") != null) {
				map.put("rest-producer-config", map.get("restProducerConfig"));
			}
			if (map.get("rabbitmqConfig") != null) {
				map.put("rabbitmq-config", map.get("rabbitmqConfig"));
			}
			if (map.get("executorsConfig") != null) {
				map.put("executors-config", map.get("executorsConfig"));
			}
			if (map.get("dbProperties") != null) {
				map.put("db-properties", map.get("dbProperties"));
			}
		}
		return map;
	}

}
