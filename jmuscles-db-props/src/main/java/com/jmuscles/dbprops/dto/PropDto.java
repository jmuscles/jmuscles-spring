/**
 * 
 */
package com.jmuscles.dbprops.dto;

import java.sql.Timestamp;

import com.jmuscles.dbprops.jpa.entity.PropEntity;

public class PropDto {

	private Long id;
	private String propKey;
	private String propValue;
	private byte[] propValueBlob;
	private Long parentPropId;
	private Long majorVersion;
	private Long minorVersion;
	private Long propTenantId;
	private String propFullKey;
	private Timestamp createdAt;
	private String createdBy;

	public PropDto() {
		// TODO Auto-generated constructor stub
	}

	public PropDto(Long id, String propKey, String propValue, byte[] propValueBlob, Long parentPropId,
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

	public static PropDto of(Long id, String propKey, String propValue, byte[] propValueBlob, Long parentPropId,
			Long majorVersion, Long minorVersion, Long propTenantId, String propFullKey, Timestamp createdAt,
			String createdBy) {
		return new PropDto(id, propKey, propValue, propValueBlob, parentPropId, majorVersion, minorVersion,
				propTenantId, propFullKey, createdAt, createdBy);

	}

	public static PropDto of(PropEntity propEntity) {
		if (propEntity != null) {
			return of(propEntity.getId(), propEntity.getPropKey(), propEntity.getPropValue(),
					propEntity.getPropValueBlob(), propEntity.getParentPropId(), propEntity.getMajorVersion(),
					propEntity.getMinorVersion(), propEntity.getPropTenantId(), propEntity.getPropFullKey(),
					propEntity.getCreatedAt(), propEntity.getCreatedBy());
		}
		return null;
	}

	public PropEntity toPropEntity() {
		return PropEntity.of(id, propKey, propValue, propValueBlob, parentPropId, majorVersion, minorVersion,
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