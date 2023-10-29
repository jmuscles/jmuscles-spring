/**
 * 
 */
package com.jmuscles.processing.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author manish goel
 *
 */
public class TrackingDetail implements Serializable {

	private static final long serialVersionUID = -8088619828255504818L;

	private Map<String, String> attributes = new HashMap<String, String>();

	public static TrackingDetail of() {
		return new TrackingDetail();
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "TrackingDetail [attributes=" + attributes.toString() + "]";
	}

}
