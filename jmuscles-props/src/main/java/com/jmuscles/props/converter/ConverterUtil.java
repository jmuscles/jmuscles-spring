/**
 * @author manish goel
 *
 */
package com.jmuscles.props.converter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class ConverterUtil {

	private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);

	private static Set<String> localPackages;
	public static ObjectMapper objectMapper = new ObjectMapper();

	static {
		// Initialize the set of allowed packages in the static block
		localPackages = new HashSet<>();

		localPackages.add("com.jmuscles.props");
		localPackages.add("com.jmuscles.datasource.properties");
		localPackages.add("com.jmuscles.async.consumer.config.properties");
		localPackages.add("com.jmuscles.async.producer.config.properties");
		localPackages.add("com.jmuscles.processing.config.properties");
		localPackages.add("com.jmuscles.rest.producer.config.properties");
	}

	public static List<Type> getNestedValueTypes(Field field) {
		List<Type> valueTypes = new ArrayList<>();
		Type valueType = field.getGenericType();
		int i = 1;
		while (valueType instanceof ParameterizedType) {
			ParameterizedType mapType = (ParameterizedType) valueType;
			Type innerValueType = null;

			if (((ParameterizedType) valueType).getRawType() == Map.class) {
				innerValueType = mapType.getActualTypeArguments()[1];
			} else if (((ParameterizedType) valueType).getRawType() == List.class) {
				innerValueType = mapType.getActualTypeArguments()[0];
			}
			Class<?> classType = null;
			if (innerValueType instanceof Class<?>) {
				classType = (Class<?>) innerValueType;
			} else if (innerValueType instanceof ParameterizedType) {
				classType = (Class<?>) ((ParameterizedType) innerValueType).getRawType();
			} else {
				logger.error("error while getting the getNestedMapValueTypes for field %s for %d th attempt", field, i);
			}
			if (classType != null) {
				valueTypes.add(classType);
			}
			valueType = innerValueType;
			i++;
		}

		return valueTypes;
	}

	public static boolean isLocalType(Class<?> clazz) {
		for (String packageName : localPackages) {
			if (clazz.getName().startsWith(packageName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPrimitive(Object value) {
		return value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof Float
				|| value instanceof Short || value instanceof Byte || value instanceof Character
				|| value instanceof Boolean || value.getClass().isPrimitive();
	}

	// Handle primitive types
	public static Object getPrimitiveValue(Object value, Class<?> type) {
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

	public static boolean isWrapperClass(Class<?> clazz) {
		return clazz == Integer.class || clazz == Double.class || clazz == Float.class || clazz == Long.class
				|| clazz == Short.class || clazz == Byte.class || clazz == Character.class || clazz == Boolean.class;
	}

	public static Object getWrapperValue(Object value, Class<?> type) {
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

	public static Map<String, Object> fixKeysAndValues(Map<Object, Object> map, Map<String, Object> newMap) {
		if (newMap == null) {
			newMap = new HashMap<String, Object>();
		}
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value != null) {
				if (value instanceof Map) {
					// Recursively process nested maps
					value = fixKeysAndValues((Map) value, null);
				} else if (value instanceof List) {
					value = listToString((List<?>) value);
				}
			}
			newMap.put(String.valueOf(entry.getKey()), value);
		}
		return newMap;
	}

	// MapToObject
	public static Object stringToList(String string, Class<?> type) {
		try {
			return objectMapper.readValue(string,
					objectMapper.getTypeFactory().constructCollectionType(List.class, type));
		} catch (JsonProcessingException e) {
			logger.error("Error while stringToList  " + string, e);
		}
		return null;
	}

	// ObjectToMap
	public static String listToString(List<?> list) {
		try {
			return objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			logger.error("Error while listToString for list " + list, e);
		}
		return null;
	}

}
