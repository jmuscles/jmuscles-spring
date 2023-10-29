/**
 * 
 */
package com.jmuscles.processing.schema.requestdata;

/**
 * @author manish goel
 *
 */
public class DemoRequestData implements RequestData {

	private static final long serialVersionUID = 7939017308755635054L;

	private Integer successAttempt;
	private String data;

	public DemoRequestData() {
	}

	public DemoRequestData(Integer successAttempt, String data) {
		super();
		this.successAttempt = successAttempt;
		this.data = data;
	}

	public Integer getSuccessAttempt() {
		return successAttempt;
	}

	public void setSuccessAttempt(Integer successAttempt) {
		this.successAttempt = successAttempt;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
