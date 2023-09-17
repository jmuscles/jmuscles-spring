/**
 * 
 */
package com.jmuscles.processing.execvalidator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.jmuscles.processing.execvalidator.implementation.SimpleRestValidator;
import com.jmuscles.processing.execvalidator.implementation.SimpleSQLProcedureValidator;
import com.jmuscles.processing.execvalidator.implementation.SimpleSQLQueryValidator;
import com.jmuscles.processing.execvalidator.implementation.SimpleSoapCallValidator;

/**
 * @author manish goel
 *
 */
public class ValidatorRegistry {

	private static final Map<String, Validator> validatorRegistryMap = new HashMap<>();

	static {
		new SimpleRestValidator();
		new SimpleSoapCallValidator();
		new SimpleSQLProcedureValidator();
		new SimpleSQLQueryValidator();
	}

	public static void register(String type, Validator validator) {
		validatorRegistryMap.put(formValidatorName(type, validator), validator);
	}

	public static Validator getValidator(String type, String name) {
		Validator validator = null;
		if (StringUtils.hasText(type) && StringUtils.hasText(name)) {
			validator = validatorRegistryMap.get(formValidatorName(type, name));
		}
		return validator;
	}

	private static String formValidatorName(String type, Validator validator) {
		return formValidatorName(type, validator.getClass().getSimpleName());
	}

	private static String formValidatorName(String type, String name) {
		return type.toLowerCase() + "#" + name.toLowerCase();
	}

}
