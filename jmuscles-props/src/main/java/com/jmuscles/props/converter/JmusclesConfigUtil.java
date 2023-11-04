/**
 * @author manish goel
 *
 */
package com.jmuscles.props.converter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

	public static Class<?> getClass(List<String> paths) {
		if (paths == null || paths.size() < 2) {
			return JmusclesConfig.class;
		} else {
			return findTypeByPath(new JmusclesConfig(), paths.subList(1, paths.size()));
		}
	}

	public static Class<?> findTypeByPath(JmusclesConfig config, List<String> fieldPath) {
		Class<?> currentType = config.getClass();
		for (int i = 0; i < fieldPath.size(); i++) {
			String fieldOrKey = fieldPath.get(i);
			if (currentType == null) {
				return null; // The type is null; stop searching.
			}
			Field currentField;
			try {
				currentField = currentType.getDeclaredField(fieldOrKey);
			} catch (NoSuchFieldException e) {
				return null; // Field not found, but continue the search.
			}
			if (i < fieldPath.size() - 1 && currentField.getType() == Map.class) {
				i++; // Skip the next field in the path.
			}
			if (currentField.getGenericType() instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) currentField.getGenericType();
				Type innerValueType = parameterizedType
						.getActualTypeArguments()[1]; /* Assuming Map<String, CustomObject> */
				if (innerValueType instanceof Class<?>) {
					currentType = (Class<?>) innerValueType;
				} else if (innerValueType instanceof ParameterizedType) {
					currentType = (Class<?>) ((ParameterizedType) innerValueType).getRawType();
				} else {
					logger.error("error while dtermining the for request path ", fieldPath);
				}

			} else {
				currentType = currentField.getType();
			}

		}
		return currentType;
	}

}
