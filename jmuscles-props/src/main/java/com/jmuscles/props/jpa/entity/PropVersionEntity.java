package com.jmuscles.props.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PROP_VERSION")
public class PropVersionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PROP_VERSION_SEQ")
	@SequenceGenerator(sequenceName = "PROP_VERSION_SEQ", name = "PROP_VERSION_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@JoinColumn(name = "PARENT_PROP")
	private PropEntity parentProp;

	@Column(name = "REQUEST_PATH")
	private String requestPath;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@ManyToOne
	@JoinColumn(name = "TENANT_ID", nullable = false) // This is the join column
	private TenantEntity tenant;

	public static PropVersionEntity of(PropEntity parentProp, String requestPath, String description,
			Timestamp createdAt, String createdBy, TenantEntity tenant) {
		return new PropVersionEntity(parentProp, requestPath, description, createdAt, createdBy, tenant);
	}

	public PropVersionEntity(PropEntity parentProp, String requestPath, String description, Timestamp createdAt,
			String createdBy, TenantEntity tenant) {
		super();
		this.parentProp = parentProp;
		this.requestPath = requestPath;
		this.description = description;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.tenant = tenant;
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
	 * @return the parentProp
	 */
	public PropEntity getParentProp() {
		return parentProp;
	}

	/**
	 * @param parentProp the parentProp to set
	 */
	public void setParentProp(PropEntity parentProp) {
		this.parentProp = parentProp;
	}

	/**
	 * @return the requestPath
	 */
	public String getRequestPath() {
		return requestPath;
	}

	/**
	 * @param requestPath the requestPath to set
	 */
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
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
