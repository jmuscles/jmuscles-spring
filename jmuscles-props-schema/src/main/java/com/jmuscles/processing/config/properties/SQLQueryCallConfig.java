/**
 * 
 */
package com.jmuscles.processing.config.properties;

/**
 * @author manish goel
 *
 */
public class SQLQueryCallConfig {
	private String dskey;
	private String query;
	private String validator;

	public SQLQueryCallConfig() {
		// TODO Auto-generated constructor stub
	}

	public SQLQueryCallConfig(String dskey, String query, String validator) {
		super();
		this.dskey = dskey;
		this.query = query;
		this.validator = validator;
	}

	public String getDskey() {
		return dskey;
	}

	public void setDskey(String dskey) {
		this.dskey = dskey;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}
}
