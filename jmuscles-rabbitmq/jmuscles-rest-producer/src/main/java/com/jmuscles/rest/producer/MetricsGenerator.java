/**
 * 
 */
package com.jmuscles.rest.producer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;

/**
 * 
 */
public class MetricsGenerator {

	public static final String METRIC_PREFIX = "custom.metric.";

	public static final String NUMBER_OF_EXEC_NAME = METRIC_PREFIX + "number.of.exec";
	public static final String NUMBER_OF_EXEC_DESCRIPTION = "Count the number of executions.";

	public static final String HEAP_MEMORY_NAME = METRIC_PREFIX + "heap.memory";
	public static final String HEAP_MEMORY_DESCRIPTION = "Reports heap memory utilization.";

	private static final Logger log = LoggerFactory.getLogger(MetricsGenerator.class);

	@Value("otel-api-trace.version")
	private String tracesApiVersion;

	@Value("otel-api-metrics.version")
	private String metricsApiVersion;
	
	@Value("spring.application.name")
	private String applicationName;

	

	private final Meter meter = GlobalOpenTelemetry.meterBuilder("telemetry.metrics."+applicationName)
			.setInstrumentationVersion(metricsApiVersion).build();

	private LongCounter numberOfExecutions;

	@PostConstruct
	public void createMetrics() {

		numberOfExecutions = meter.counterBuilder(NUMBER_OF_EXEC_NAME).setDescription(NUMBER_OF_EXEC_DESCRIPTION)
				.setUnit("int").build();

		meter.gaugeBuilder(HEAP_MEMORY_NAME).setDescription(HEAP_MEMORY_DESCRIPTION).setUnit("byte")
				.buildWithCallback(r -> {
					r.record(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
				});

	}

}
