/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.jmuscles.processing.constant.Constants;
import com.jmuscles.processing.executor.ExecutorRegistry;
import com.jmuscles.processing.executor.SelfRegisteredExecutor;
import com.jmuscles.processing.schema.requestdata.DemoRequestData;
import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class DemoRequestExecutor extends SelfRegisteredExecutor {

	private static final Logger logger = LoggerFactory.getLogger(DemoRequestExecutor.class);

	public DemoRequestExecutor(ExecutorRegistry executorRegistry) {
		super(executorRegistry);
	}

	@Override
	public RequestData execute(RequestData requestData) {
		logger.debug("start ......");
		DemoRequestData demoRequestData = (DemoRequestData) requestData;
		Integer currentAttempt = 1;
		if (MDC.get(Constants.CURRENT_ATTEMPT) != null) {
			currentAttempt = Integer.parseInt((String) MDC.get(Constants.CURRENT_ATTEMPT));
		} else {
			MDC.put(Constants.CURRENT_ATTEMPT, "1");
		}
		logger.info("currentAttempt ......" + currentAttempt);
		if (currentAttempt != null && currentAttempt >= demoRequestData.getSuccessAttempt()) {
			return null;
		}
		logger.debug("...... end");
		return requestData;
	}

	@Override
	public Class<?> getExecutorRequestDataClass() {
		return DemoRequestData.class;
	}

}
