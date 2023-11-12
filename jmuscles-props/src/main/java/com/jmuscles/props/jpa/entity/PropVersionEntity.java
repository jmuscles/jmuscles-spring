package com.jmuscles.props.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "PropVersionEntity.findMaxMajorVersionByTenant", query = "SELECT MAX(p2.propVersionKey.majorVersion)"
				+ " FROM PropVersionEntity p2 " + " WHERE p2.propVersionKey.tenantId = :tenantId "
				+ " AND p2.propVersionKey.minorVersion = 0"),

		@NamedQuery(name = "PropVersionEntity.findMaxMinorVersionByTenantAndMajorVersion", query = "SELECT MAX(p2.propVersionKey.minorVersion) "
				+ " FROM PropVersionEntity p2 " + " WHERE p2.propVersionKey.tenantId = :tenantId"
				+ " AND p2.propVersionKey.majorVersion = :majorVersion") })

@Entity
@Table(name = "PROP_VERSION")
public class PropVersionEntity {

	@EmbeddedId
	private PropVersionKey propVersionKey;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PROP_FULL_KEY", length = 500)
	private String prop_full_key;

	@ManyToOne
	@JoinColumn(name = "PARENT_PROP_ID")
	private PropEntity parent_prop;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	public PropVersionEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropVersionEntity(PropVersionKey propVersionKey, String name, String description, String prop_full_key,
			PropEntity parent_prop, Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		super();
		this.propVersionKey = propVersionKey;
		this.name = name;
		this.description = description;
		this.prop_full_key = prop_full_key;
		this.parent_prop = parent_prop;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public static PropVersionEntity of(PropVersionKey propVersionKey, String name, String description,
			String prop_full_key, PropEntity parent_prop, Timestamp createdAt, String createdBy, Timestamp updatedAt,
			String updatedBy) {
		return new PropVersionEntity(propVersionKey, name, description, prop_full_key, parent_prop, createdAt,
				createdBy, updatedAt, updatedBy);
	}

	public void resetCreateUpdate(Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the propVersionKey
	 */
	public PropVersionKey getPropVersionKey() {
		return propVersionKey;
	}

	/**
	 * @param propVersionKey the propVersionKey to set
	 */
	public void setPropVersionKey(PropVersionKey propVersionKey) {
		this.propVersionKey = propVersionKey;
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
