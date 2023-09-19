/**
 * 
 */
package com.jmuscles.async.producer.util;

import org.springframework.http.HttpStatus;

/**
 * @author manish goel
 *
 */
public class JmusclesRestResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public final String errorDesc;
	public final HttpStatus httpStatus;
	public final Exception ex;

	public JmusclesRestResponseException(String errorDesc, HttpStatus httpStatus, Exception ex) {
		super();
		this.errorDesc = errorDesc;
		this.httpStatus = httpStatus;
		this.ex = ex;
	}

	public static JmusclesRestResponseException of(String errorDesc, HttpStatus httpStatus, Exception ex) {
		return new JmusclesRestResponseException(errorDesc, httpStatus, ex);
	}

	public static JmusclesRestResponseException of(String errorDesc, HttpStatus httpStatus) {
		return of(errorDesc, httpStatus, null);
	}

}
