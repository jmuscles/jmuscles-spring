/**
 * 
 */
package com.jmuscles.rest.producer.response;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author manish goel
 *
 */
public class ResponseBuilderRegistry {

	private static final Logger logger = LoggerFactory.getLogger(ResponseBuilderRegistry.class);

	private static final Map<String, BaseResponseBuilder> registryMap = new HashMap<>();

	public static void register(BaseResponseBuilder responseBuilder) {
		registryMap.put(responseBuilder.getClass().getSimpleName(), responseBuilder);
	}

	public static BaseResponseBuilder get(String responseBuilderName) {
		return registryMap.get(responseBuilderName);
	}

}
