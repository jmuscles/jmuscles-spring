/**
 * 
 */
package com.jmuscles.async.producer.producing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author manish goel
 *
 */
@FunctionalInterface
public interface Producer {

	public static final Map<String, Producer> producerRegistryMap = new HashMap<>();

	public void send(Map<String, Object> map) throws Exception;

	public static void register(String registryKey, Producer producer) {
		producerRegistryMap.put(registryKey.toLowerCase(), producer);
	}

	public static Producer get(String key) {
		return producerRegistryMap.get(key.toLowerCase());
	}

}
