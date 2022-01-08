/**
 * 
 */
package com.jmuscles.processing.executor;

/**
 * @author manish goel
 *
 */
public abstract class SelfRegisteredExecutor implements BaseExecutor {

	public SelfRegisteredExecutor(ExecutorRegistry executorRegistry) {
		executorRegistry.register(getExecutorRequestDataClass(), this);
	}

	public abstract Class<?> getExecutorRequestDataClass();
}
