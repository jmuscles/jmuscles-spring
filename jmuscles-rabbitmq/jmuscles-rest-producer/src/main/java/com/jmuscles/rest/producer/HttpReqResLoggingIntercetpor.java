/**
 * 
 */
package com.jmuscles.rest.producer;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 */
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import com.jmuscles.async.producer.util.JmusclesProducerConstants;

public class HttpReqResLoggingIntercetpor implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(HttpReqResLoggingIntercetpor.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code (if needed)
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		logger.info("Entering in to HttpReqResLoggingIntercetpor...");
		try {
			// Generate a unique request ID and add it to the MDC
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String jmuscleTraceId = httpRequest.getHeader(JmusclesProducerConstants.JMUSCLE_TRACE_ID);
			if (!StringUtils.hasText(jmuscleTraceId)) {
				jmuscleTraceId = UUID.randomUUID().toString();
			}
			MDC.put(JmusclesProducerConstants.JMUSCLE_TRACE_ID, jmuscleTraceId);
			// Log incoming request details here
			logger.info("Incoming Request: " + httpRequest.getMethod() + " " + httpRequest.getRemoteAddr() + " "
					+ httpRequest.getServletContext().getContextPath() + httpRequest.getRequestURI());

			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.addHeader(JmusclesProducerConstants.JMUSCLE_TRACE_ID, jmuscleTraceId);

			// Continue the filter chain
			chain.doFilter(request, response);

			logger.info(
					"Outgoing Response: " + httpResponse.getContentType() + " " + httpResponse.getCharacterEncoding());

		} finally {
			// Clean up the MDC after the request is processed
			MDC.remove(JmusclesProducerConstants.JMUSCLE_TRACE_ID);
			logger.info("... Request ends");
		}
	}

	@Override
	public void destroy() {
		// Cleanup code (if needed)
	}
}
