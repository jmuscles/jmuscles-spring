/**
 * 
 */
package com.jmuscles.processing.executor.implementation;

import com.jmuscles.processing.executor.Executor;
import com.jmuscles.processing.executor.ExecutorRegistry;
import com.jmuscles.processing.executor.SelfRegisteredExecutor;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.SequentialRequestData;

/**
 * @author manish goel
 *
 */
public class SequentialRequestExecutor extends SelfRegisteredExecutor {

	private ExecutorRegistry executorRegistry;

	public SequentialRequestExecutor(ExecutorRegistry executorRegistry) {
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
