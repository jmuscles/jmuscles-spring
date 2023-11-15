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
	private PropDto parent_prop;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;

	public PropVersionDto() {
		// TODO Auto-generated constructor stub
	}

	public PropVersionDto(Long majorVersion, Long minorVersion, Long tenantId, String name, String description,
			String prop_full_key, PropDto parent_prop, Timestamp createdAt, String createdBy, Timestamp updatedAt,
			String updatedBy) {
		super();
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.tenantId = tenantId;
		this.name = name;
		this.description = description;
		this.prop_full_key = prop_full_key;
		this.parent_prop = parent_prop;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public PropVersionEntity toPropVersionEntity() {
		return PropVersionEntity.of(PropVersionKey.of(majorVersion, minorVersion, tenantId), name, description,
				prop_full_key, (parent_prop != null) ? parent_prop.toPropEntity() : null, createdAt, createdBy,
				updatedAt, updatedBy);
	}

	public static PropVersionDto of(Long majorVersion, Long minorVersion, Long tenantId, String name,
			String description, String prop_full_key, PropDto parent_prop, Timestamp createdAt, String createdBy,
			Timestamp updatedAt, String updatedBy) {
		return new PropVersionDto(majorVersion, minorVersion, tenantId, name, description, prop_full_key, parent_prop,
				createdAt, createdBy, updatedAt, updatedBy);
	}

	public static PropVersionDto of(PropVersionEntity propVersionEntity) {
		if (propVersionEntity != null) {
			PropVersionKey localPropVersionKey = propVersionEntity.getPropVersionKey();
			return new PropVersionDto(localPropVersionKey != null ? localPropVersionKey.getMajorVersion() : null,
					localPropVersionKey != null ? localPropVersionKey.getMinorVersion() : null,
					localPropVersionKey != null ? localPropVersionKey.getTenantId() : null, propVersionEntity.getName(),
					propVersionEntity.getDescription(), propVersionEntity.getProp_full_key(),
					PropDto.of(propVersionEntity.getParent_prop()), propVersionEntity.getCreatedAt(),
					propVersionEntity.getCreatedBy(), propVersionEntity.getUpdatedAt(),
					propVersionEntity.getUpdatedBy());
		}
		return null;
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
	 * @return the parent_prop
	 */
	public PropDto getParent_prop() {
		return parent_prop;
	}

	/**
	 * @param parent_prop the parent_prop to set
	 */
	public void setParent_prop(PropDto parent_prop) {
		this.parent_prop = parent_prop;
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

	/**
	 * @return the updatedAt
	 */
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/*
	 * public void set(PropVersionEntity propVersionEntity) { if (propVersionEntity
	 * != null) { set(propVersionEntity.getMajorVersion(),
	 * propVersionEntity.getMinorVersion(), propVersionEntity.getName(),
	 * propVersionEntity.getDescription(), propVersionEntity.getCreatedAt(),
	 * propVersionEntity.getCreatedBy(), propVersionEntity.getUpdatedAt(),
	 * propVersionEntity.getUpdatedBy(), propVersionEntity.getProp_full_key(),
	 * PropDto.of(propVersionEntity.getParent_prop()),
	 * TenantDto.of(propVersionEntity.getTenant())); } }
	 * 
	 * public void set(Integer majorVersion, Integer minorVersion, String name,
	 * String description, Timestamp createdAt, String createdBy, Timestamp
	 * updatedAt, String updatedBy, String prop_full_key, PropDto parent_prop,
	 * TenantDto tenant) { this.majorVersion = majorVersion; this.minorVersion =
	 * minorVersion; this.name = name; this.description = description;
	 * this.createdAt = createdAt; this.createdBy = createdBy; this.updatedAt =
	 * updatedAt; this.updatedBy = updatedBy; this.prop_full_key = prop_full_key;
	 * this.parent_prop = parent_prop; this.tenant = tenant; }
	 */

}
