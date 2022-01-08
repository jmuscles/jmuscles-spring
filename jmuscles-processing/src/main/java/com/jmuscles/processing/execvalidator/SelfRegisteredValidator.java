/**
 * 
 */
package com.jmuscles.processing.execvalidator;

/**
 * @author manish goel
 *
 */
public abstract class SelfRegisteredValidator implements Validator {

	public SelfRegisteredValidator() {
		ValidatorRegistry.register(type(), this);
	}

	public abstract String type();

}
