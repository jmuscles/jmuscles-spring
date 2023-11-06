/**
 * 
 */
package com.jmuscles.processing.execvalidator.implementation;

import java.util.Map;

import com.jmuscles.processing.config.properties.SQLProcedureCallConfig;
import com.jmuscles.processing.execvalidator.SQLProcedureValidator;
import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public class SimpleSQLProcedureValidator extends SQLProcedureValidator {

	@Override
	public boolean validateRequest(RequestData requestData, Object config) {
		return false;
	}

	@Override
	public boolean validateResponse(RequestData requestData, Object response, Object config) {
		@SuppressWarnings("unchecked")
		Map<String, Object> responseMap = (Map<String, Object>) response;
		SQLProcedureCallConfig procKeyInfo = (SQLProcedureCallConfig) config;

		return responseMap.containsKey(procKeyInfo.getSuccessKey())
				&& procKeyInfo.getSuccessValue().equals(responseMap.get(procKeyInfo.getSuccessKey()));
	}

}
