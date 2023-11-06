package com.jmuscles.props.jpa;

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
@Table(name = "APP_PROPS_AUDIT")
public class AppPropsAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "APP_PROPS_AUDIT_SEQ")
	@SequenceGenerator(sequenceName = "APP_PROPS_AUDIT_SEQ", name = "APP_PROPS_AUDIT_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "APP_PROPS_ID")
	private AppPropsEntity appPropsEntity;

	@Column(name = "OLD_STATUS")
	private String oldStatus;

	@Column(name = "NEW_STATUS")
	private String newStatus;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@ManyToOne
	@JoinColumn(name = "TENANT_ID") // This is the join column
	private TenantEntity tenant;

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
	 * @return the appPropsEntity
	 */
	public AppPropsEntity getAppPropsEntity() {
		return appPropsEntity;
	}

	/**
	 * @param appPropsEntity the appPropsEntity to set
	 */
	public void setAppPropsEntity(AppPropsEntity appPropsEntity) {
		this.appPropsEntity = appPropsEntity;
	}

	/**
	 * @return the oldStatus
	 */
	public String getOldStatus() {
		return oldStatus;
	}

	/**
	 * @param oldStatus the oldStatus to set
	 */
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	/**
	 * @return the newStatus
	 */
	public String getNewStatus() {
		return newStatus;
	}

	/**
	 * @param newStatus the newStatus to set
	 */
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
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
