/**
 * @author manish goel
 *
 */
package com.jmuscles.props.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.props.JmusclesConfig;

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
