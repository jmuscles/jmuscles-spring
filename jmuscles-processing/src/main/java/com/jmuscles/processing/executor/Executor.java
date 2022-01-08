/**
 * 
 */
package com.jmuscles.processing.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class Executor {

	private static final Logger logger = LoggerFactory.getLogger(Executor.class);

	public static RequestData execute(RequestData requestData, ExecutorRegistry executorRegistry) {
		logger.debug("start ... " + requestData);
		RequestData response = null;
		BaseExecutor executor = executorRegistry.getExecutor(requestData);
		if (executor == null) {
			logger.error("Executor not found for " + requestData.getClass().getName());
			response = requestData;
		} else {
			try {
				response = executor.execute(requestData);
			} catch (Exception e) {
				logger.error("Error while executing ", e);
				response = requestData;
			}
		}
		logger.debug("end ... " + response);
		return response;
	}
}
