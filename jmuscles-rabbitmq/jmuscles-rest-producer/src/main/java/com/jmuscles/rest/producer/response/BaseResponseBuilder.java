/**
 * 
 */
package com.jmuscles.rest.producer.response;

import java.util.Map;

import org.springframework.http.ResponseEntity;

/**
 * @author manish goel
 *
 */
public abstract class BaseResponseBuilder {

	public BaseResponseBuilder() {
		ResponseBuilderRegistry.register(this);
	}

	/**
	 * 
	 * @param objectMap
	 * @return
	 */
	public abstract ResponseEntity<?> buildResponse(Map<String, Object> objectMap);

}
