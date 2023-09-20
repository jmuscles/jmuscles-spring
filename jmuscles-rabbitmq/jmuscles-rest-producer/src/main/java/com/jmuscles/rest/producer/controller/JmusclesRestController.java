/**
 * 
 */
package com.jmuscles.rest.producer.controller;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;
import com.jmuscles.rest.producer.helper.JmusclesRestControllerBean;

/**
 * 
 */

@RestController
@RequestMapping("/event/")
@DependsOn("jmusclesRestControllerBean")
public class JmusclesRestController {

	private static final Logger logger = LoggerFactory.getLogger(JmusclesRestController.class);

	@Autowired
	private JmusclesRestControllerBean jmusclesRestControllerBean;

	@RequestMapping("/process")
	public ResponseEntity<?> queuePayload(Payload payload, TrackingDetail trackingDetail)
			throws JsonProcessingException {
		return jmusclesRestControllerBean.queuePayload(payload, trackingDetail);
	}

	@RequestMapping("/rest/{configKey}/**")
	public ResponseEntity<?> processRestStringPayload(@RequestHeader Map<String, String> headers,
			@RequestBody(required = false) String requestBody, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		return jmusclesRestControllerBean.processRestStringPayload(headers, requestBody, request, configKey);
	}

	@RequestMapping("/restByteArrayPayload/{configKey}/**")
	public ResponseEntity<?> processRestByteArrayPayload(@RequestHeader Map<String, String> headers,
			@RequestBody(required = false) Serializable requestBody, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		return jmusclesRestControllerBean.processRestByteArrayPayload(headers, requestBody, request, configKey);
	}

}
