/**
 * 
 */
package com.jmuscles.processing.processor;

import java.util.List;
import java.util.stream.Collectors;

import com.jmuscles.processing.executor.Executor;
import com.jmuscles.processing.executor.ExecutorRegistry;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class Processor {

	public static Payload process(Payload payload, ExecutorRegistry executorRegistry) {
		List<RequestData> unProcessedDataList = payload.getRequestDataList().stream()
				.map(requestData -> Executor.execute(requestData, executorRegistry))
				.filter(requestData -> requestData != null).collect(Collectors.toList());
		if (unProcessedDataList != null && !unProcessedDataList.isEmpty()) {
			payload.setRequestDataList(unProcessedDataList);
		} else {
			payload = null;
		}
		return payload;
	}
}