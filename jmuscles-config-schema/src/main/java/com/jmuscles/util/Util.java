/**
 * @author manish goel
 *
 */
package com.jmuscles.util;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class Util {
	
	public static ObjectMapper objectMapper= new ObjectMapper();
	
	public static Map objectToMap(Object object) {
		return objectMapper.convertValue(object, Map.class);
	}

	public static Object mapToObject(Map map, Class type) {
		return objectMapper.convertValue(map, type);
	}

}
