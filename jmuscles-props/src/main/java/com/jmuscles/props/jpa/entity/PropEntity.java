package com.jmuscles.props.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jmuscles.props.util.Constants;

@Entity
@Table(name = "PROPERTIES")
public class PropEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PROP_SEQ")
	@SequenceGenerator(sequenceName = "PROP_SEQ", name = "PROP_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@Column(name = "PROP_KEY", length = 250)
	private String propKey;

	@Column(name = "PROP_VALUE", length = Constants.PROP_VALUE_LENGTH)
	private String propValue;

	@Lob
	@Column(name = "PROP_VALUE_BLOB")
	private byte[] propValueBlob;

	@Column(name = "PARENT_ID")
	private Long parentPropId;

	@Column(name = "MAJOR_VERSION", nullable = false) // This is the join column
	private Long majorVersion;

	@Column(name = "MINOR_VERSION", nullable = false) // This is the join column
	private Long minorVersion;

	@Column(name = "TENANT_ID", nullable = false) // This is the join column
	private Long propTenantId;

	@Column(name = "PROP_FULL_KEY", length = 1000)
	private String propFullKey;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	public PropEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropEntity(Long id, String propKey, String propValue, byte[] propValueBlob, Long parentPropId,
			Long majorVersion, Long minorVersion, Long propTenantId, String propFullKey, Timestamp createdAt,
			String createdBy) {
		super();
		this.id = id;
		this.propKey = propKey;
		this.propValue = propValue;
		this.propValueBlob = propValueBlob;
		this.parentPropId = parentPropId;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.propTenantId = propTenantId;
		this.propFullKey = propFullKey;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static PropEntity of(Long id, String propKey, String propValue, byte[] propValueBlob, Long parentPropId,
			Long majorVersion, Long minorVersion, Long propTenantId, String propFullKey, Timestamp createdAt,
			String createdBy) {
		return new PropEntity(id, propKey, propValue, propValueBlob, parentPropId, majorVersion, minorVersion,
				propTenantId, propFullKey, createdAt, createdBy);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the propKey
	 */
	public String getPropKey() {
		return propKey;
	}

	/**
	 * @param propKey the propKey to set
	 */
	public void setPropKey(String propKey) {
		this.propKey = propKey;
	}

	/**
	 * @return the propValue
	 */
	public String getPropValue() {
		return propValue;
	}

	/**
	 * @param propValue the propValue to set
	 */
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	/**
	 * @return the propValueBlob
	 */
	public byte[] getPropValueBlob() {
		return propValueBlob;
	}

	/**
	 * @param propValueBlob the propValueBlob to set
	 */
	public void setPropValueBlob(byte[] propValueBlob) {
		this.propValueBlob = propValueBlob;
	}

	/**
	 * @return the parentPropId
	 */
	public Long getParentPropId() {
		return parentPropId;
	}

	/**
	 * @param parentPropId the parentPropId to set
	 */
	public void setParentPropId(Long parentPropId) {
		this.parentPropId = parentPropId;
	}

	/**
	 * @return the majorVersion
	 */
	public Long getMajorVersion() {
		return majorVersion;
	}

	/**
	 * @param majorVersion the majorVersion to set
	 */
	public void setMajorVersion(Long majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * @return the minorVersion
	 */
	public Long getMinorVersion() {
		return minorVersion;
	}

	/**
	 * @param minorVersion the minorVersion to set
	 */
	public void setMinorVersion(Long minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * @return the propTenantId
	 */
	public Long getPropTenantId() {
		return propTenantId;
	}

	/**
	 * @param propTenantId the propTenantId to set
	 */
	public void setPropTenantId(Long propTenantId) {
		this.propTenantId = propTenantId;
	}

	/**
	 * @return the propFullKey
	 */
	public String getPropFullKey() {
		return propFullKey;
	}

	/**
	 * @param propFullKey the propFullKey to set
	 */
	public void setPropFullKey(String propFullKey) {
		this.propFullKey = propFullKey;
	}

	/**
	 * @return the createdAt
	 */
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}