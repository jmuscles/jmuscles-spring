/**
 * 
 */
package com.jmuscles.processing.config.properties;

import java.util.HashMap;
import java.util.Map;

import com.jmuscles.util.Util;

/**
 * @author manish goel
 *
 */
public class RestConfig {

	private Integer connectionTimeout;
	private Integer readTimeout;
	private Map<String, String> commonHeaders;

	public RestConfig() {
		// TODO Auto-generated constructor stub
	}

	public RestConfig(Integer connectionTimeout, Integer readTimeout, Map<String, String> commonHeaders) {
		super();
		this.connectionTimeout = connectionTimeout;
		this.readTimeout = readTimeout;
		this.commonHeaders = commonHeaders;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Map<String, String> getCommonHeaders() {
		return commonHeaders;
	}

	public void setCommonHeaders(Map<String, String> httpHeader) {
		this.commonHeaders = httpHeader;
	}

	public static RestConfig mapToObject(Map<String, Object> map) {
		return map != null
				? new RestConfig(Util.getInt(map, "connectionTimeout"), Util.getInt(map, "readTimeout"),
						(Map) map.get("commonHeaders"))
				: null;

	}

	public Map<String, Object> objectToMap() {
		Map<String, Object> map = new HashMap<>();
		if (this.getConnectionTimeout() != null) {
			map.put("connectionTimeout", this.getConnectionTimeout().toString());
		}
		if (this.getReadTimeout() != null) {
			map.put("readTimeout", this.getReadTimeout().toString());
		}
		if (this.getCommonHeaders() != null) {
			map.put("commonHeaders", this.getCommonHeaders());
		}

		return map;
	}

}
