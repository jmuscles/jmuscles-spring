/**
 * @author manish goel
 *
 */
package com.jmuscles.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class Util {

	public static ObjectMapper objectMapper = new ObjectMapper();

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static Map objectToMap(Object object) {
		return objectMapper.convertValue(object, Map.class);
	}

	public static Object mapToObject(Object object, Class type) {
		return objectMapper.convertValue(object, type);
	}

	public static String getString(Map<String, Object> map, String key) {
		Object obj = map.get(key);
		if (obj != null && obj instanceof String) {
			return obj.toString();
		}
		return null;
	}

	public static List<String> getListOfString(Map<String, Object> map, String key) {
		return getListOfString(map.get(key));
	}

	public static List<String> getListOfString(Object obj) {
		List<String> list = null;
		if (obj != null) {
			list = new ArrayList<>();
			if (obj instanceof String) {
				list.add((String) obj);
			} else if (obj instanceof List) {
				list = (List<String>) ((List) obj).stream().map(e -> e.toString()).collect(Collectors.toList());
			} else {
				list = null;
			}
		}
		return list;
	}

	public static boolean getBoolean(Map<String, Object> map, String key) {
		boolean value = false;
		Object obj = map.get(key);
		if (obj != null) {
			if (obj instanceof String) {
				value = Boolean.getBoolean(obj.toString());
			} else if (obj instanceof Boolean) {
				value = (Boolean) obj;
			}
		}
		return value;
	}

	public static Map<String, List<String>> resolveMapOfStringList(Map<String, Object> map, String key) {
		Map<String, List<String>> returnMap = null;
		Object obj = map.get(key);
		if (obj != null) {
			if (obj instanceof Map) {
				Map<String, Object> innerMap = (Map) obj;
				returnMap = (Map<String, List<String>>) innerMap.entrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey(), e -> getListOfString(e.getValue())));
			}
		}

		return returnMap;
	}

	public static Integer getInt(Map<String, Object> map, String key) {
		return getInt(map.get(key));
	}

	public static Integer getInt(Object obj) {
		Integer returnInt = null;
		if (obj != null) {
			if (obj instanceof String) {
				returnInt = Integer.parseInt(obj.toString());
			} else if (obj instanceof Integer) {
				returnInt = (Integer) obj;
			}
		}
		return returnInt;
	}

	public static int[] getIntArray(Map<String, Object> map, String key) {
		return getIntArray(map.get(key));
	}

	public static int[] getIntArray(Object obj) {
		int[] list = null;
		if (obj != null) {
			if (obj instanceof String) {
				list = new int[] { getInt(obj) };
			} else if (obj instanceof int[]) {
				list = (int[]) obj;
			} else {
				list = null;
			}
		}
		return list;
	}

}
