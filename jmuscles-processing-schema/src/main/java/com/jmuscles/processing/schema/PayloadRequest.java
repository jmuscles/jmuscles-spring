package com.jmuscles.processing.schema;

import java.io.Serializable;

/**
 * 
 * @author manish goel
 *
 */
public class PayloadRequest implements Serializable {

	private static final long serialVersionUID = 6270895554394469056L;

	private byte[] payload;
	private String payloadType;
	private String routingKey;
	private String exchange;
	private TrackingDetail trackingDetail;

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public TrackingDetail getTrackingDetail() {
		return trackingDetail;
	}

	public void setTrackingDetail(TrackingDetail trackingDetail) {
		this.trackingDetail = trackingDetail;
	}

}
