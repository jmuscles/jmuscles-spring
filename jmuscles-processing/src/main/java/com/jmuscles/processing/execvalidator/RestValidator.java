/**
 * 
 */
package com.jmuscles.processing.execvalidator;

import org.springframework.util.StringUtils;

import com.jmuscles.processing.execvalidator.implementation.SimpleRestValidator;

/**
 * @author manish goel
 *
 */
public abstract class RestValidator extends SelfRegisteredValidator {

	public static final String TYPE = "Rest";
	public static final String DEFAULT_REST_VALIDATOR = SimpleRestValidator.class.getSimpleName();

	public static RestValidator get(String name) {
		if (!StringUtils.hasText(name)) {
			name = DEFAULT_REST_VALIDATOR;
		}
		return (RestValidator) ValidatorRegistry.getValidator(TYPE, name);
	}

	@Override
	public String type() {
		return TYPE;
	}

	public abstract Class<?> responseEntityClass();

}
