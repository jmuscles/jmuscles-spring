package com.jmuscles.props.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author manish goel
 *
 */
@Entity
@Table(name = "PROP_TENANT")
public class PropTenantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PROP_TENANT_SEQ")
	@SequenceGenerator(sequenceName = "PROP_TENANT_SEQ", name = "PROP_TENANT_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@Column(name = "NAME", unique = true, nullable = false, length = 100)
	private String name;

	@Column(name = "DESCRIPTION", length = 250)
	private String description;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	public PropTenantEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropTenantEntity(Long id, String name, String description, Timestamp createdAt, String createdBy,
			Timestamp updatedAt, String updatedBy) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public static PropTenantEntity of(Long id, String name, String description, Timestamp createdAt, String createdBy,
			Timestamp updatedAt, String updatedBy) {
		return new PropTenantEntity(id, name, description, createdAt, createdBy, updatedAt, updatedBy);
	}

	public void changeCreateUpdate(Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
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
