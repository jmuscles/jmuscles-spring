/**
 * 
 */
package com.jmuscles.processing.executor;

/**
 * @author manish goel
 *
 */
public abstract class StandardExecutor implements BaseExecutor {

	public StandardExecutor(StandardExecutorRegistry executorRegistry) {
		executorRegistry.register(getExecutorRequestDataClass(), this);
	}

	public abstract Class<?> getExecutorRequestDataClass();
}
