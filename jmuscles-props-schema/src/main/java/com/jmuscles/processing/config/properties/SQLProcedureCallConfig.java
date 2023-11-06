/**
 * 
 */
package com.jmuscles.processing.config.properties;

/**
 * @author manish goel
 *
 */
public class SQLProcedureCallConfig {

	private String dskey;
	private String procedure;
	private String validator;
	private String successKey;
	private String successValue;

	public SQLProcedureCallConfig() {
		// TODO Auto-generated constructor stub
	}

	public SQLProcedureCallConfig(String dskey, String procedure, String validator, String successKey,
			String successValue) {
		super();
		this.dskey = dskey;
		this.procedure = procedure;
		this.validator = validator;
		this.successKey = successKey;
		this.successValue = successValue;
	}

	public String getDskey() {
		return dskey;
	}

	public void setDskey(String dskey) {
		this.dskey = dskey;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getSuccessKey() {
		return successKey;
	}

	public void setSuccessKey(String successKey) {
		this.successKey = successKey;
	}

	public String getSuccessValue() {
		return successValue;
	}

	public void setSuccessValue(String successValue) {
		this.successValue = successValue;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

}
