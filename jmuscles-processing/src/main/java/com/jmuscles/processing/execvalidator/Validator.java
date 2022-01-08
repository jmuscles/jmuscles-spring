/**
 * 
 */
package com.jmuscles.processing.execvalidator;

import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
public interface Validator {

	public boolean validateRequest(RequestData requestData, Object config);

	public boolean validateResponse(RequestData requestData, Object response, Object config);

}
