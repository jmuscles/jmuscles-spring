/**
 * @author manish goel
 *
 */
package com.jmuscles.props.converter;

/**
 * 
 */
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectToMapConverter {

	private static final Logger logger = LoggerFactory.getLogger(ObjectToMapConverter.class);

	public static Map<String, Object> objectToMap(Object obj) {
		if (obj instanceof Map) {
			return mapToMap((Map<?, ?>) obj);
		} else if (ConverterUtil.isLocalType(obj.getClass())) {
			return localObjectToMap(obj);
		}

		return null;
	}

	public static Map<String, Object> localObjectToMap(Object obj) {
		Map<String, Object> map = new HashMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object fieldValue = null;
			try {
				fieldValue = field.get(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (fieldValue != null) {
				addToMap(fieldName, fieldValue, map);
			}
		}
		return map;
	}

	private static Map<String, Object> mapToMap(Map<?, ?> originalMap) {
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<?, ?> entry : originalMap.entrySet()) {
			if (entry.getValue() != null) {
				addToMap(entry.getKey(), entry.getValue(), map);
			}
		}
		return map;
	}

	public static void addToMap(Object key, Object value, Map<String, Object> map) {
		map.put(String.valueOf(key), resolveValue(value));
	}

	private static Object resolveValue(Object value) {
		Object returnValue = null;
		try {
			if (ConverterUtil.isPrimitive(value) || value instanceof String) {
				returnValue = String.valueOf(value);
			} else if (value instanceof Map) {
				returnValue = mapToMap((Map<?, ?>) value);
			} else if (value instanceof List) {
				returnValue = ConverterUtil.listToString((List<?>) value);
			} else if (ConverterUtil.isLocalType(value.getClass())) {
				returnValue = localObjectToMap(value);
			} else {
				returnValue = value;
			}
		} catch (Exception e) {
			logger.error("error for field with value : " + value, e);
		}
		return returnValue;
	}

}
