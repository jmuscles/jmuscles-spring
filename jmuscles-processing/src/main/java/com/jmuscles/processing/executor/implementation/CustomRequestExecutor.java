/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmuscles.processing.executor.CustomExecutor;
import com.jmuscles.processing.executor.CustomExecutorRegistry;
import com.jmuscles.processing.executor.StandardExecutor;
import com.jmuscles.processing.executor.StandardExecutorRegistry;
import com.jmuscles.processing.schema.requestdata.CustomRequestData;
import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class CustomRequestExecutor extends StandardExecutor {

	private static final Logger logger = LoggerFactory.getLogger(CustomRequestExecutor.class);

	private CustomExecutorRegistry customExecutorRegistry;

	public CustomRequestExecutor(StandardExecutorRegistry standardExecutorRegistry,
			CustomExecutorRegistry customExecutorRegistry) {
		super(standardExecutorRegistry);
	}

	@Override
	public RequestData execute(RequestData requestData) {
		logger.debug("start ......");
		CustomRequestData customRequestData = (CustomRequestData) requestData;

		CustomExecutor customExecutor = customExecutorRegistry.getExecutor(customRequestData.getConfigKey());
		CustomRequestData response = customExecutor.execute(customRequestData);

		logger.debug("...... end");
		return response;
	}

	@Override
	public Class<?> getExecutorRequestDataClass() {
		return CustomRequestData.class;
	}

}
