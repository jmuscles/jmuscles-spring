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

public class MapToObjectConverter {

	private static final Logger logger = LoggerFactory.getLogger(MapToObjectConverter.class);

	public static Object mapToObject(Map<String, Object> map, List<String> paths, Object mainObject) throws Exception {
		Object currentObject = mainObject;
		for (int i = 1; i < paths.size(); i++) {
			Field field = currentObject.getClass().getDeclaredField(paths.get(i));
			field.setAccessible(true);
			Class<?> fieldType = field.getType();
			Object nextObject = null;
			if (i == (paths.size() - 1)) {
				nextObject = mapToObject(map.get(paths.get(i)), fieldType, field, null);
				field.set(currentObject, nextObject);
			} else {
				// Map<String, Map<String, Map<String, RestConfig>>>
				if (field.getType() == Map.class) {
					Map<String, Object> localMap = mapToObject_helperMethod_whenFieldIsMap(map, field, paths, i,
							currentObject);
					nextObject = localMap.get("nextObject");
					i = (Integer) localMap.get("pathsPoiter");
				} else {
					nextObject = getNewInstance(fieldType);
					field.set(currentObject, nextObject);
				}
			}
			currentObject = nextObject;
		}

		return mainObject;
	}

	public static Map<String, Object> mapToObject_helperMethod_whenFieldIsMap(Map<String, Object> map, Field field,
			List<String> paths, int pathsPoiter, Object currentObject) throws Exception {
		Map<String, Object> fieldMap = new HashMap<>();
		field.set(currentObject, fieldMap);
		Object nextObject = fieldMap;
		List<Type> valueTypes = ConverterUtil.getNestedValueTypes(field);
		for (int vtCounter = 0; vtCounter < valueTypes.size(); vtCounter++) {
			pathsPoiter++;
			Class<?> lType = (Class<?>) valueTypes.get(vtCounter);
			if (pathsPoiter == (paths.size() - 1)) {
				Object newInstance = mapToObject(map.get(paths.get(pathsPoiter)), lType, null,
						valueTypes.subList(vtCounter + 1, valueTypes.size()));
				((Map) nextObject).put(paths.get(pathsPoiter), newInstance);
				nextObject = newInstance;
				break;
			}
			if (lType == Map.class) {
				Map<String, Object> innerMap = new HashMap<>();
				((Map) nextObject).put(paths.get(pathsPoiter), innerMap);
				nextObject = innerMap;
			} else {
				Object newInstance = getNewInstance(lType);
				((Map) nextObject).put(paths.get(pathsPoiter), newInstance);
				nextObject = newInstance;
				break;
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("nextObject", nextObject);
		returnMap.put("pathsPoiter", pathsPoiter);
		return returnMap;
	}

	public static Object getNewInstance(Class<?> clazz) {
		Object obj = null;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
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
					field.set(obj, mapToObject(value, field.getType(), field, null));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	private static Object mapToObject(Object value, Class<?> type, Field field, List<Type> valueTypes) {
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
				returnValue = mapToObject_fieldTypeList(value, valueTypes, field);
			} else if (ConverterUtil.isLocalType(type) && value instanceof Map) {
				returnValue = mapToObject((Map<String, Object>) value, type);
			} else {
				returnValue = value;
			}
		} catch (Exception e) {
			logger.error("error for field with value : " + value, e);
		}
		return returnValue;
	}

	private static Object mapToObject_fieldTypeMap(Object value, List<Type> valueTypes, Field field) {
		if (valueTypes == null && field != null) {
			valueTypes = ConverterUtil.getNestedValueTypes(field);
		}
		Object returnValue = null;
		if (value instanceof Map) {
			Map<String, Object> inputMapValue = (Map<String, Object>) value;
			Map<String, Object> returnMap = new HashMap<>();
			if (valueTypes != null && valueTypes.size() > 0) {
				List<Type> valueTypeForProcessing = new ArrayList<Type>(valueTypes);
				Type localClazz = valueTypeForProcessing.remove(0);
				for (Entry<String, Object> entry : inputMapValue.entrySet()) {
					returnMap.put(entry.getKey(),
							mapToObject(entry.getValue(), (Class<?>) localClazz, null, valueTypeForProcessing));
				}
			}
			returnValue = returnMap;
		}
		return returnValue;
	}

	private static Object mapToObject_fieldTypeList(Object value, List<Type> valueTypes, Field field) {
		if (valueTypes == null && field != null) {
			valueTypes = ConverterUtil.getNestedValueTypes(field);
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
