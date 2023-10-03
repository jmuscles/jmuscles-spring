/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import com.jmuscles.processing.executor.Executor;
import com.jmuscles.processing.executor.StandardExecutor;
import com.jmuscles.processing.executor.StandardExecutorRegistry;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.SequentialRequestData;

/**
 * @author manish goel
 *
 */
public class SequentialRequestExecutor extends StandardExecutor {

	private StandardExecutorRegistry executorRegistry;

	public SequentialRequestExecutor(StandardExecutorRegistry executorRegistry) {
		super(executorRegistry);
	}

	@Override
	public RequestData execute(RequestData requestData) {
		SequentialRequestData sequentialRequestData = (SequentialRequestData) requestData;
		while (null != sequentialRequestData.getSequentialDataList()
				&& !sequentialRequestData.getSequentialDataList().isEmpty()) {
			if (Executor.execute(sequentialRequestData.getSequentialDataList().get(0), executorRegistry) == null) {
				sequentialRequestData.getSequentialDataList().remove(0);
			} else {
				break;
			}
		}
		return sequentialRequestData.getSequentialDataList().isEmpty() ? null : sequentialRequestData;
	}

	@Override
	public Class<?> getExecutorRequestDataClass() {
		return SequentialRequestData.class;
	}

}
