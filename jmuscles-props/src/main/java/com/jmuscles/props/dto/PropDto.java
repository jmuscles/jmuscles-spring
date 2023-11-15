/**
 * 
 */
package com.jmuscles.props.dto;

import java.sql.Timestamp;

import com.jmuscles.props.jpa.entity.PropEntity;

public class PropDto {

	private Long id;
	private String prop_key;
	private String prop_value;
	private byte[] prop_value_blob;
	private Long parentId;
	private Long majorVersion;
	private Long minorVersion;
	private Long tenantId;
	private String prop_full_key;
	private Timestamp createdAt;
	private String createdBy;

	public PropDto() {
		// TODO Auto-generated constructor stub
	}

	public PropDto(Long id, String prop_key, String prop_value, byte[] prop_value_blob, Long parentId,
			Long majorVersion, Long minorVersion, Long tenantId, String prop_full_key, Timestamp createdAt,
			String createdBy) {
		super();
		this.id = id;
		this.prop_key = prop_key;
		this.prop_value = prop_value;
		this.prop_value_blob = prop_value_blob;
		this.parentId = parentId;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.tenantId = tenantId;
		this.prop_full_key = prop_full_key;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static PropDto of(Long id, String prop_key, String prop_value, byte[] prop_value_blob, Long parentId,
			Long majorVersion, Long minorVersion, Long tenantId, String prop_full_key, Timestamp createdAt,
			String createdBy) {
		return new PropDto(id, prop_key, prop_value, prop_value_blob, parentId, majorVersion, minorVersion, tenantId,
				prop_full_key, createdAt, createdBy);

	}

	public static PropDto of(PropEntity propEntity) {
		if (propEntity != null) {
			return of(propEntity.getId(), propEntity.getProp_key(), propEntity.getProp_value(),
					propEntity.getProp_value_blob(), propEntity.getParentId(), propEntity.getMajorVersion(),
					propEntity.getMinorVersion(), propEntity.getTenantId(), propEntity.getProp_full_key(),
					propEntity.getCreatedAt(), propEntity.getCreatedBy());
		}
		return null;
	}

	public PropEntity toPropEntity() {
		return PropEntity.of(id, prop_key, prop_value, prop_value_blob, parentId, majorVersion, minorVersion, tenantId,
				prop_full_key, createdAt, createdBy);
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
	 * @return the prop_key
	 */
	public String getProp_key() {
		return prop_key;
	}

	/**
	 * @param prop_key the prop_key to set
	 */
	public void setProp_key(String prop_key) {
		this.prop_key = prop_key;
	}

	/**
	 * @return the prop_value
	 */
	public String getProp_value() {
		return prop_value;
	}

	/**
	 * @param prop_value the prop_value to set
	 */
	public void setProp_value(String prop_value) {
		this.prop_value = prop_value;
	}

	/**
	 * @return the prop_value_blob
	 */
	public byte[] getProp_value_blob() {
		return prop_value_blob;
	}

	/**
	 * @param prop_value_blob the prop_value_blob to set
	 */
	public void setProp_value_blob(byte[] prop_value_blob) {
		this.prop_value_blob = prop_value_blob;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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