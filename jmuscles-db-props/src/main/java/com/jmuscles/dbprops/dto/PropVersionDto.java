package com.jmuscles.dbprops.dto;

import java.sql.Timestamp;

import com.jmuscles.dbprops.jpa.entity.PropVersionEntity;
import com.jmuscles.dbprops.jpa.entity.PropVersionKey;

public class PropVersionDto {

	private Long majorVersion;
	private Long minorVersion;
	private Long propTenantId;
	private String name = "";
	private String description = "";
	private String propFullKey;
	private Long parentPropId;
	private Timestamp createdAt;
	private String createdBy;

	public PropVersionDto() {
		// TODO Auto-generated constructor stub
	}

	public PropVersionDto(Long majorVersion, Long minorVersion, Long propTenantId, String name, String description,
			String propFullKey, Long parentPropId, Timestamp createdAt, String createdBy) {
		super();
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.propTenantId = propTenantId;
		this.name = name;
		this.description = description;
		this.propFullKey = propFullKey;
		this.parentPropId = parentPropId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static PropVersionDto of(Long majorVersion, Long minorVersion, Long propTenantId, String name,
			String description, String propFullKey, Long parentPropId, Timestamp createdAt, String createdBy) {
		return new PropVersionDto(majorVersion, minorVersion, propTenantId, name, description, propFullKey,
				parentPropId, createdAt, createdBy);
	}

	public static PropVersionDto of(PropVersionEntity entity) {
		return of(entity.getPropVersionKey().getMajorVersion(), entity.getPropVersionKey().getMinorVersion(),
				entity.getPropVersionKey().getPropTenantId(), entity.getName(), entity.getDescription(),
				entity.getPropFullKey(), entity.getParentPropId(), entity.getCreatedAt(), entity.getCreatedBy());
	}

	public PropVersionEntity toPropVersionEntity() {
		return PropVersionEntity.of(PropVersionKey.of(majorVersion, minorVersion, propTenantId), name, description,
				propFullKey, parentPropId, createdAt, createdBy);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
