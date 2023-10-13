/**
 * 
 */
package com.jmuscles.processing.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

	public static SQLProcedureCallConfig mapToObject(Map<String, Object> map) {
		return new SQLProcedureCallConfig((String) map.get("dskey"), (String) map.get("procedure"),
				(String) map.get("validator"), (String) map.get("successKey"), (String) map.get("successValue"));
	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("dskey", this.getDskey());
		map.put("procedure", this.getProcedure());
		map.put("validator", this.getValidator());
		map.put("successKey", this.getSuccessKey());
		map.put("successValue", this.getSuccessValue());

		return map;
	}

	public static Map<String, SQLProcedureCallConfig> mapToObject2(Map<String, Object> map) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> mapToObject((Map) e.getValue())));
	}

	public static Map<String, Object> objectToMap2(Map<String, SQLProcedureCallConfig> objectsMap) {
		return objectsMap.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().objectToMap()));
	}

}
