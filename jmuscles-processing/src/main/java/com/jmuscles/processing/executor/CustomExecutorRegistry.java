/**
 * 
 */
package com.jmuscles.processing.executor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author manish goel
 *
 */
public class CustomExecutorRegistry {

	private static final Logger logger = LoggerFactory.getLogger(CustomExecutorRegistry.class);

	private final Map<String, CustomExecutor> executorRegistryMap = new HashMap<>();

	public void register(CustomExecutor executor) {
		executorRegistryMap.put(executor.getExecutorConfigKey(), executor);
	}

	public CustomExecutor getExecutor(String executorConfigKey) {
		logger.debug("getExecutor type : " + executorConfigKey + ".. start .... ");
		CustomExecutor executor = null;
		if (executorConfigKey != null) {
			executor = executorRegistryMap.get(executorConfigKey);
		}
		logger.debug(" ... getExecutor type end");

		return executor;
	}

}
