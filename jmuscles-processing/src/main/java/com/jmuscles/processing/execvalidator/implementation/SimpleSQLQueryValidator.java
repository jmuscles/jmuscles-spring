/**
 * 
 */
package com.jmuscles.processing.execvalidator.implementation;

import com.jmuscles.processing.execvalidator.SQLQueryValidator;
import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class SimpleSQLQueryValidator extends SQLQueryValidator {

	@Override
	public boolean validateRequest(RequestData requestData, Object config) {
		return false;
	}

	@Override
	public boolean validateResponse(RequestData requestData, Object response, Object config) {
		return response.equals(1);
	}

}
