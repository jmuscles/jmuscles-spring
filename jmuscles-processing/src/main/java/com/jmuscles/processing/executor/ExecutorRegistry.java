/**
 * 
 */
package com.jmuscles.processing.executor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class ExecutorRegistry {

	private static final Logger logger = LoggerFactory.getLogger(ExecutorRegistry.class);

	private final Map<String, BaseExecutor> executorRegistryMap = new HashMap<>();

	public void register(Class<?> requestDataClass, BaseExecutor executor) {
		executorRegistryMap.put(requestDataClass.getName(), executor);
	}

	public BaseExecutor getExecutor(RequestData requestData) {
		return getExecutor(getExecutorType(requestData));
	}

	private BaseExecutor getExecutor(String type) {
		logger.debug("getExecutor type : " + type + ".. start .... ");
		BaseExecutor executor = null;
		if (type != null) {
			executor = executorRegistryMap.get(type);
		}
		logger.debug(" ... getExecutor type end");

		return executor;
	}

	private String getExecutorType(RequestData requestData) {
		logger.debug("getExecutorType .. start .... ");
		String type = null;
		if (requestData != null) {
			type = requestData.getClass().getName();
		}
		logger.debug("getExecutorType .. end .... ");
		return type;
	}

}
