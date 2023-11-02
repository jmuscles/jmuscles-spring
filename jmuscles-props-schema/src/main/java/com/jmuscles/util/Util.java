/**
 * @author manish goel
 *
 */
package com.jmuscles.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	public static ObjectMapper objectMapper = new ObjectMapper();

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static String getString(Map<String, Object> map, String key) {
		Object obj = map.get(key);
		if (obj != null && obj instanceof String) {
			return obj.toString();
		}
		return null;
	}

	public static boolean getBoolean(Object obj) {
		boolean value = false;
		if (obj != null) {
			if (obj instanceof String) {
				value = Boolean.parseBoolean(obj.toString());
			} else if (obj instanceof Boolean) {
				value = (Boolean) obj;
			}
		}
		return value;
	}

	public static boolean getBoolean(Map<String, Object> map, String key) {
		return getBoolean(map.get(key));
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

	// ObjectToMap
	public static String listToString(List list) {
		try {
			return objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			logger.error("Error while listToString for list " + list, e);
		}
		return null;
	}

	// MapToObject
	public static Object stringToList(String string, Class type) {
		try {
			return objectMapper.readValue(string,
					objectMapper.getTypeFactory().constructCollectionType(List.class, type));
		} catch (JsonProcessingException e) {
			logger.error("Error while stringToList  " + string, e);
		}
		return null;
	}

	// MapToObject
	public static Object stringToListFromMap(Map<String, Object> map, String key, Class type) {
		return stringToList(getString(map, key), type);
	}

}
