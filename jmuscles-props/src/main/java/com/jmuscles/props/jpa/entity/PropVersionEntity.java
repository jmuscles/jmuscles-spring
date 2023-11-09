package com.jmuscles.props.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PROP_VERSION")
public class PropVersionEntity {

	@Column(name = "MAJOR_VERSION", nullable = false) // This is the join column
	private Integer majorVersion;

	@Column(name = "MINOR_VERSION", nullable = false) // This is the join column
	private Integer minorVersion;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "PROP_FULL_KEY", length = 500)
	private String prop_full_key;

	@ManyToOne
	@JoinColumn(name = "PARENT_PROP_ID")
	private PropEntity parent_prop;

	@ManyToOne
	@JoinColumn(name = "TENANT_ID", nullable = false) // This is the join column
	private TenantEntity tenant;

	public PropVersionEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropVersionEntity(Integer majorVersion, Integer minorVersion, String name, String description,
			Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy, String prop_full_key,
			PropEntity parent_prop, TenantEntity tenant) {
		super();
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
		this.prop_full_key = prop_full_key;
		this.parent_prop = parent_prop;
		this.tenant = tenant;
	}

	public static PropVersionEntity of(Integer majorVersion, Integer minorVersion, String name, String description,
			Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy, String prop_full_key,
			PropEntity parent_prop, TenantEntity tenant) {
		return new PropVersionEntity(majorVersion, minorVersion, name, description, createdAt, createdBy, updatedAt,
				updatedBy, prop_full_key, parent_prop, tenant);
	}

	public void changeCreateUpdate(Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the majorVersion
	 */
	public Integer getMajorVersion() {
		return majorVersion;
	}

	/**
	 * @param majorVersion the majorVersion to set
	 */
	public void setMajorVersion(Integer majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * @return the minorVersion
	 */
	public Integer getMinorVersion() {
		return minorVersion;
	}

	/**
	 * @param minorVersion the minorVersion to set
	 */
	public void setMinorVersion(Integer minorVersion) {
		this.minorVersion = minorVersion;
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
	public PropEntity getParent_prop() {
		return parent_prop;
	}

	/**
	 * @param parent_prop the parent_prop to set
	 */
	public void setParent_prop(PropEntity parent_prop) {
		this.parent_prop = parent_prop;
	}

	/**
	 * @return the tenant
	 */
	public TenantEntity getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(TenantEntity tenant) {
		this.tenant = tenant;
	}

}
