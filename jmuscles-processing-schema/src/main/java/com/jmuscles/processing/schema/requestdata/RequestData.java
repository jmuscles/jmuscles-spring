/**
 * 
 */
package com.jmuscles.processing.schema.requestdata;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author manish goel
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = DemoRequestData.class, name = "demo"),
		@JsonSubTypes.Type(value = CustomRequestData.class, name = "custom"),
		@JsonSubTypes.Type(value = RestRequestData.class, name = "rest"),
		@JsonSubTypes.Type(value = SequentialRequestData.class, name = "sequential"),
		@JsonSubTypes.Type(value = SQLProcedureRequestData.class, name = "sql-procedure"),
		@JsonSubTypes.Type(value = SQLQueryRequestData.class, name = "sql-query") })
public interface RequestData extends Serializable {

}
