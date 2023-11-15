package com.jmuscles.props.dto;

import java.sql.Timestamp;

import com.jmuscles.props.jpa.entity.PropVersionEntity;
import com.jmuscles.props.jpa.entity.PropVersionKey;

public class PropVersionDto {

	private Long majorVersion;
	private Long minorVersion;
	private Long tenantId;
	private String name = "";
	private String description = "";
	private String prop_full_key;
	private Long parentPropId;
	private Timestamp createdAt;
	private String createdBy;

	public PropVersionDto() {
		// TODO Auto-generated constructor stub
	}

	public PropVersionDto(Long majorVersion, Long minorVersion, Long tenantId, String name, String description,
			String prop_full_key, Long parentPropId, Timestamp createdAt, String createdBy) {
		super();
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.tenantId = tenantId;
		this.name = name;
		this.description = description;
		this.prop_full_key = prop_full_key;
		this.parentPropId = parentPropId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public PropVersionEntity toPropVersionEntity() {
		return PropVersionEntity.of(PropVersionKey.of(majorVersion, minorVersion, tenantId), name, description,
				prop_full_key, parentPropId, createdAt, createdBy);
	}

	public static PropVersionDto of(Long majorVersion, Long minorVersion, Long tenantId, String name,
			String description, String prop_full_key, Long parentPropId, Timestamp createdAt, String createdBy) {
		return new PropVersionDto(majorVersion, minorVersion, tenantId, name, description, prop_full_key, parentPropId,
				createdAt, createdBy);
	}

	public static PropVersionDto of(PropVersionEntity entity) {
		return of(entity.getPropVersionKey().getMajorVersion(), entity.getPropVersionKey().getMinorVersion(),
				entity.getPropVersionKey().getTenantId(), entity.getName(), entity.getDescription(),
				entity.getProp_full_key(), entity.getParentPropId(), entity.getCreatedAt(), entity.getCreatedBy());
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
	 * @return the tenantId
	 */
	public Long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
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
	 * @return the prop_full_key
	 */
	public String getProp_full_key() {
		return prop_full_key;
	}

	/**
	 * @param prop_full_key the prop_full_key to set
	 */
	public void setProp_full_key(String prop_full_key) {
		this.prop_full_key = prop_full_key;
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
