/**
 * 
 */
package com.jmuscles.async.producer.jpa.asyncpayload;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author manish goel
 *
 */
@Embeddable
public class AsyncPayloadPropEntity {

	@Column(name = "ASYNC_PAYLOAD_KEY")
	private String key;

	@Column(name = "ASYNC_PAYLOAD_VALUE")
	private String value;

	@Column(name = "CREATED_TS")
	private LocalDateTime createdTS;

	@Column(name = "UPDATED_TS")
	private LocalDateTime updatedTS;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LocalDateTime getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(LocalDateTime createdTS) {
		this.createdTS = createdTS;
	}

	public LocalDateTime getUpdatedTS() {
		return updatedTS;
	}

	public void setUpdatedTS(LocalDateTime updatedTS) {
		this.updatedTS = updatedTS;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
