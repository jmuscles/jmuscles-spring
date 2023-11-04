package com.jmuscles.props.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.props.JmusclesConfig;

public class MapToObjectConverter {

	private static final Logger logger = LoggerFactory.getLogger(MapToObjectConverter.class);

	public static JmusclesConfig mapToObjectForSnakeCaseYaml(Map<String, Object> map) {
		return mapToObject(JmusclesConfigUtil.convertJmusclesFieldsFromSnakeToCamelCase(map));
	}

	public static JmusclesConfig mapToObject(Map<String, Object> map) {
		return mapToObject(map, JmusclesConfig.class);
	}

	public static Object mapToObject(Map<String, Object> map, List<String> paths) {
		return mapToObject(
				(Map<String, Object>) map
						.get((paths != null && paths.size() > 0) ? paths.get(paths.size() - 1) : "jmuscles"),
				JmusclesConfigUtil.getClass(paths));
	}

	public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
		T obj = null;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			if (map.containsKey(fieldName)) {
				Object value = map.get(fieldName);
				try {
					field.set(obj, mapToObject(value, field.getType(), fieldName, field, null));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	private static Object mapToObject(Object value, Class<?> type, String fieldName, Field field,
			List<Type> valueTypes) {
		Object returnValue = null;
		try {
			if (type.equals(String.class)) {
				returnValue = String.valueOf(value);
			} else if (type.isPrimitive()) {
				returnValue = ConverterUtil.getPrimitiveValue(value, type);
			} else if (ConverterUtil.isWrapperClass(type)) {
				returnValue = ConverterUtil.getWrapperValue(value, type);
			} else if (type.equals(Map.class)) {
				returnValue = mapToObject_fieldTypeMap(value, valueTypes, field);
			} else if (type.equals(List.class)) {
				returnValue = mapToObject_fieldTypeList(value, fieldName, valueTypes, field);
				// (value, fieldName, field);
			} else if (ConverterUtil.isLocalType(type)) {
				if (value instanceof Map) {
					returnValue = mapToObject((Map<String, Object>) value, type);
				}
			} else {
				returnValue = value;
			}
		} catch (Exception e) {
			logger.error("error for field: " + fieldName + " with value : " + value, e);
		}
		return returnValue;
	}

	private static Object mapToObject_fieldTypeMap(Object value, List<Type> valueTypes, Field field) {
		if (valueTypes == null && field != null) {
			valueTypes = ConverterUtil.getNestedMapValueTypes(field);
		}
		Object returnValue = null;
		if (value instanceof Map) {
			Map<String, Object> inputMapValue = (Map<String, Object>) value;
			Map<String, Object> returnMap = new HashMap<>();
			if (valueTypes != null && valueTypes.size() > 0) {
				List<Type> valueTypeForProcessing = new ArrayList<Type>(valueTypes);
				Type localClazz = valueTypeForProcessing.remove(0);
				for (Entry<String, Object> entry : inputMapValue.entrySet()) {
					returnMap.put(entry.getKey(), mapToObject(entry.getValue(), (Class<?>) localClazz, null, null,
							valueTypeForProcessing));
				}
			}
			returnValue = returnMap;
		}
		return returnValue;
	}

	private static Object mapToObject_fieldTypeList(Object value, String fieldName, List<Type> valueTypes,
			Field field) {
		if (valueTypes == null && field != null) {
			valueTypes = ConverterUtil.getNestedMapValueTypes(field);
		}
		if (value == null || valueTypes == null || valueTypes.size() == 0) {
			return null;
		}
		Class<?> valueType = (Class<?>) valueTypes.remove(0);
		Object returnValue = null;
		if (value instanceof String) {
			returnValue = ConverterUtil.stringToList(value.toString(), valueType);
		} else {
			returnValue = value;
		}
		return returnValue;
	}

}
