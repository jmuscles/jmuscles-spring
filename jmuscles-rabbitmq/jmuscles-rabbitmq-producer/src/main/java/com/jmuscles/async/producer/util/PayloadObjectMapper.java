/**
 * 
 */
package com.jmuscles.async.producer.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmuscles.processing.schema.Payload;

/**
 * @author manish goel
 *
 */
public class PayloadObjectMapper {

	private static ObjectMapper mapper = null;

	static {
		mapper = new ObjectMapper();
	}

	public static Payload deserialize(byte[] payload) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(payload, Payload.class);
	}

	public static byte[] serialize(Payload payload) throws JsonProcessingException {
		return mapper.writeValueAsBytes(payload);
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}
	
}
