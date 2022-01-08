/**
 * 
 */
package com.jmuscles.processing.execvalidator;

import org.springframework.util.StringUtils;

import com.jmuscles.processing.execvalidator.implementation.SimpleSQLQueryValidator;

/**
 * @author manish goel
 *
 */
public abstract class SQLQueryValidator extends SelfRegisteredValidator {

	public static final String TYPE = "SQLQuery";
	public static final String DEFAULT_SQL_QUERY_VALIDATOR = SimpleSQLQueryValidator.class.getSimpleName();

	public static SQLQueryValidator get(String name) {
		if (!StringUtils.hasText(name)) {
			name = DEFAULT_SQL_QUERY_VALIDATOR;
		}
		return (SQLQueryValidator) ValidatorRegistry.getValidator(TYPE, name);
	}

	@Override
	public String type() {
		return TYPE;
	}

}
