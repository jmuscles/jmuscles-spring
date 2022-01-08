/**
 * 
 */
package com.jmuscles.async.producer.jpa.asyncpayload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author manish goel
 *
 */
@Entity
@Table(name = "ASYNC_PAYLOAD")
public class AsyncPayloadEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ASYNC_PAYLOAD_SEQ")
	@SequenceGenerator(sequenceName = "ASYNC_PAYLOAD_SEQ", name = "ASYNC_PAYLOAD_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;

	@Lob
	@Column(name = "PAYLOAD")
	private byte[] payload;

	@Column(name = "PAYLOAD_TYPE")
	private String payloadType;

	@Column(name = "EXEC_ATTEMPT")
	private String execAttempt;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATED_TS")
	private LocalDateTime createdTS;

	@Column(name = "UPDATED_TS")
	private LocalDateTime updatedTS;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "PROCESSING_LOCK")
	private String processingLock;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ASYNC_PAYLOAD_PROPS", joinColumns = @JoinColumn(name = "ASYNC_PAYLOAD_ID"))
	private List<AsyncPayloadPropEntity> props = new ArrayList<AsyncPayloadPropEntity>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getExecAttempt() {
		return execAttempt;
	}

	public void setExecAttempt(String execAttempt) {
		this.execAttempt = execAttempt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getProcessingLock() {
		return processingLock;
	}

	public void setProcessingLock(String processingLock) {
		this.processingLock = processingLock;
	}

	public List<AsyncPayloadPropEntity> getProps() {
		return props;
	}

	public void setProps(List<AsyncPayloadPropEntity> props) {
		this.props = props;
	}

}
