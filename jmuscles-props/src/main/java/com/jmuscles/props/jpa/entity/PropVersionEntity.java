package com.jmuscles.props.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "PropVersionEntity.findMaxMajorVersionByTenant", query = "SELECT MAX(p2.propVersionKey.majorVersion)"
				+ " FROM PropVersionEntity p2 " + " WHERE p2.propVersionKey.propTenantId = :propTenantId "
				+ " AND p2.propVersionKey.minorVersion = 0"),

		@NamedQuery(name = "PropVersionEntity.findMaxMinorVersionByTenantAndMajorVersion", query = "SELECT MAX(p2.propVersionKey.minorVersion) "
				+ " FROM PropVersionEntity p2 " + " WHERE p2.propVersionKey.propTenantId = :propTenantId"
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
	private String propFullKey;

	@Column(name = "PARENT_PROP_ID")
	private Long parentPropId;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	public PropVersionEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropVersionEntity(PropVersionKey propVersionKey, String name, String description, String propFullKey,
			Long parentPropId, Timestamp createdAt, String createdBy) {
		super();
		this.propVersionKey = propVersionKey;
		this.name = name;
		this.description = description;
		this.propFullKey = propFullKey;
		this.parentPropId = parentPropId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static PropVersionEntity of(PropVersionKey propVersionKey, String name, String description,
			String propFullKey, Long parentPropId, Timestamp createdAt, String createdBy) {
		return new PropVersionEntity(propVersionKey, name, description, propFullKey, parentPropId, createdAt,
				createdBy);
	}

	public void resetCreate(Timestamp createdAt2, String createdBy2) {
		this.createdAt = createdAt2;
		this.createdBy = createdBy2;
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
