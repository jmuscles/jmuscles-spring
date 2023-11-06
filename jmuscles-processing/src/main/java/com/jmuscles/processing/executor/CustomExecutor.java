/**
 * 
 */
package com.jmuscles.processing.executor;

import com.jmuscles.processing.schema.requestdata.CustomRequestData;

/**
 * @author manish goel In order to write your own executor inhert this class and
 *         expose as spring bean eg. use @Component etc.
 */
public abstract class CustomExecutor {

	public CustomExecutor(CustomExecutorRegistry customExecutorRegistry) {
		customExecutorRegistry.register(this);
	}

	public abstract CustomRequestData execute(CustomRequestData requestData);

	public abstract String getExecutorConfigKey();

}
