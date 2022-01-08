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
public abstract class SQLProcedureValidator extends SelfRegisteredValidator {

	public static final String TYPE = "SQLProcedure";
	public static final String DEFAULT_SQL_PROCEDURE_VALIDATOR = SimpleSQLQueryValidator.class.getSimpleName();

	public static SQLProcedureValidator get(String name) {
		if (!StringUtils.hasText(name)) {
			name = DEFAULT_SQL_PROCEDURE_VALIDATOR;
		}
		return (SQLProcedureValidator) ValidatorRegistry.getValidator(TYPE, name);
	}

	@Override
	public String type() {
		return TYPE;
	}

}
