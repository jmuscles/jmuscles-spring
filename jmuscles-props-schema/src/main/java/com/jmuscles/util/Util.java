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

	public static boolean isWrapperClass(Class<?> clazz) {
		return clazz == Integer.class || clazz == Double.class || clazz == Float.class || clazz == Long.class
				|| clazz == Short.class || clazz == Byte.class || clazz == Character.class || clazz == Boolean.class;
	}

	// Handle primitive types
	public static Object handlePrimitive(Object value, Class<?> type) {
		if (type == int.class) {
			return Integer.parseInt(String.valueOf(value));
		} else if (type == boolean.class) {
			return Boolean.parseBoolean(String.valueOf(value));
		} else if (type == double.class) {
			return ((Number) value).doubleValue();
		} else if (type == float.class) {
			return ((Number) value).floatValue();
		} else if (type == long.class) {
			return ((Number) value).longValue();
		} else if (type == short.class) {
			return ((Number) value).shortValue();
		} else if (type == byte.class) {
			return ((Number) value).byteValue();
		} else if (type == char.class) {
			return ((String) value).charAt(0);
		}
		return value;
	}

	public static Object handleWrapper(Object value, Class<?> type) {
		if (type == Integer.class) {
			if (value instanceof Number) {
				return ((Number) value).intValue();
			} else if (value instanceof String) {
				return Integer.parseInt(String.valueOf(value));
			}
		} else if (type == Double.class) {
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			} else if (value instanceof String) {
				return Double.parseDouble(String.valueOf(value));
			}
		} else if (type == Float.class) {
			if (value instanceof Number) {
				return ((Number) value).floatValue();
			} else if (value instanceof String) {
				return Float.parseFloat(String.valueOf(value));
			}
		} else if (type == Long.class) {
			if (value instanceof Number) {
				return ((Number) value).longValue();
			} else if (value instanceof String) {
				return Long.parseLong(String.valueOf(value));
			}
		} else if (type == Short.class) {
			if (value instanceof Number) {
				return ((Number) value).shortValue();
			} else if (value instanceof String) {
				return Short.parseShort(String.valueOf(value));
			}
		} else if (type == Byte.class) {
			if (value instanceof Number) {
				return ((Number) value).byteValue();
			} else if (value instanceof String) {
				return Byte.parseByte(String.valueOf(value));
			}
		} else if (type == Character.class) {
			if (value instanceof String && ((String) value).length() == 1) {
				return ((String) value).charAt(0);
			}
		} else if (type == Boolean.class) {
			if (value instanceof Boolean) {
				return (Boolean) value;
			} else if (value instanceof String) {
				return Boolean.parseBoolean(String.valueOf(value));
			}
		}

		return value; // Return value as is if no conversion was performed
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
