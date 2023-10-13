/**
 * @author manish goel
 *
 */
package com.jmuscles.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 */
public class MapToObjectConverter<T> {
	public static <T> Map<String, T> mapToObject(Map<String, Object> map, Function<Map, T> mapToObject) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> mapToObject.apply((Map) e.getValue())));
	}

}
