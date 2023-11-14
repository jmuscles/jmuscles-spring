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
	private PropDto parent;
	private Long majorVersion;
	private Long minorVersion;
	private Long childrenMinorVersion;
	private TenantDto tenant;
	private String prop_full_key;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;

	public PropDto() {
		// TODO Auto-generated constructor stub
	}

	public PropDto(Long id, String prop_key, String prop_value, byte[] prop_value_blob, PropDto parent,
			Long majorVersion, Long minorVersion, Long childrenMinorVersion, TenantDto tenant, String prop_full_key,
			Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		super();
		this.id = id;
		this.prop_key = prop_key;
		this.prop_value = prop_value;
		this.prop_value_blob = prop_value_blob;
		this.parent = parent;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.childrenMinorVersion = childrenMinorVersion;
		this.tenant = tenant;
		this.prop_full_key = prop_full_key;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public static PropDto of(Long id, String prop_key, String prop_value, byte[] prop_value_blob, PropDto parent,
			Long majorVersion, Long minorVersion, Long childrenMinorVersion, TenantDto tenant, String prop_full_key,
			Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		return new PropDto(id, prop_key, prop_value, prop_value_blob, parent, majorVersion, minorVersion,
				childrenMinorVersion, tenant, prop_full_key, createdAt, createdBy, updatedAt, updatedBy);

	}

	public static PropDto of(PropEntity propEntity) {
		if (propEntity != null) {
			return of(propEntity.getId(), propEntity.getProp_key(), propEntity.getProp_value(),
					propEntity.getProp_value_blob(), of(propEntity.getParent()), propEntity.getMajorVersion(),
					propEntity.getMinorVersion(), propEntity.getChildrenMinorVersion(),
					TenantDto.of(propEntity.getTenant()), propEntity.getProp_full_key(), propEntity.getCreatedAt(),
					propEntity.getCreatedBy(), propEntity.getUpdatedAt(), propEntity.getUpdatedBy());
		}
		return null;
	}

	public PropEntity toPropEntity() {
		return PropEntity.of(id, prop_key, prop_value, prop_value_blob, parent != null ? parent.toPropEntity() : null,
				majorVersion, minorVersion, childrenMinorVersion, tenant != null ? tenant.toTenantEntity() : null,
				prop_full_key, createdAt, createdBy, updatedAt, updatedBy);
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
	 * @return the parent
	 */
	public PropDto getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(PropDto parent) {
		this.parent = parent;
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
	 * @return the childrenMinorVersion
	 */
	public Long getChildrenMinorVersion() {
		return childrenMinorVersion;
	}

	/**
	 * @param childrenMinorVersion the childrenMinorVersion to set
	 */
	public void setChildrenMinorVersion(Long childrenMinorVersion) {
		this.childrenMinorVersion = childrenMinorVersion;
	}

	/**
	 * @return the tenant
	 */
	public TenantDto getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(TenantDto tenant) {
		this.tenant = tenant;
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

}