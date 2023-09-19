/**
 * 
 */
package com.jmuscles.rest.producer.controller.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jmuscles.async.producer.util.JmusclesRestResponseException;

/**
 * @author manish goel
 *
 */
@ControllerAdvice
public class JmusclesRestExceptionAdvice extends ResponseEntityExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(JmusclesRestExceptionAdvice.class);

	@ExceptionHandler(JmusclesRestResponseException.class)
	public ResponseEntity<?> handleRKPException(JmusclesRestResponseException responseException) {
		return new ResponseEntity<>(responseException.errorDesc, responseException.httpStatus);
	}

	@ExceptionHandler(Throwable.class)
	public void handleThrowable(Throwable ex) throws Throwable {
		logger.error("", ex);
		throw ex;
	}

}