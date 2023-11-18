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

/**
 * @author manish goel
 */
@Entity
@Table(name = "APP_PROPS_PROVISION")
public class AppPropsProvisionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "APP_PROPS_PROVISION_SEQ")
	@SequenceGenerator(sequenceName = "APP_PROPS_PROVISION_SEQ", name = "APP_PROPS_PROVISION_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false, name = "APP_ID")
	private AppEntity appEntity;

	@Column(nullable = false, name = "ENV")
	private String env;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "PROP_MAJOR_VERSION", nullable = false) // This is the join column
	private Long propMajorVersion;

	@Column(name = "PROP_MINOR_VERSION", nullable = false) // This is the join column
	private Long propMinorVersion;

	@Column(name = "PROP_TENANT_ID", nullable = false) // This is the join column
	private Long propTenantId;

	@Column(name = "PROP_FULL_KEY_LIST", length = 1000)
	private String propFullKeyList;

	@Column(name = "ACTUATOR_URL")
	private String actuatorUrl;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	public AppPropsProvisionEntity() {
		// TODO Auto-generated constructor stub
	}

	public AppPropsProvisionEntity(Long id, AppEntity appEntity, String env, String status, Long propMajorVersion,
			Long propMinorVersion, Long propTenantId, String propFullKeyList, String actuatorUrl, Timestamp createdAt,
			String createdBy, Timestamp updatedAt, String updatedBy) {
		super();
		this.id = id;
		this.appEntity = appEntity;
		this.env = env;
		this.status = status;
		this.propMajorVersion = propMajorVersion;
		this.propMinorVersion = propMinorVersion;
		this.propTenantId = propTenantId;
		this.propFullKeyList = propFullKeyList;
		this.actuatorUrl = actuatorUrl;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public static AppPropsProvisionEntity of(Long id, AppEntity appEntity, String env, String status,
			Long propMajorVersion, Long propMinorVersion, Long propTenantId, String propFullKeyList, String actuatorUrl,
			Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		return new AppPropsProvisionEntity(id, appEntity, env, status, propMajorVersion, propMinorVersion, propTenantId,
				propFullKeyList, actuatorUrl, createdAt, createdBy, updatedAt, updatedBy);
	}

	public void resetCreateUpdate(Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
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
	 * @return the appEntity
	 */
	public AppEntity getAppEntity() {
		return appEntity;
	}

	/**
	 * @param appEntity the appEntity to set
	 */
	public void setAppEntity(AppEntity appEntity) {
		this.appEntity = appEntity;
	}

	/**
	 * @return the env
	 */
	public String getEnv() {
		return env;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(String env) {
		this.env = env;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the propMajorVersion
	 */
	public Long getPropMajorVersion() {
		return propMajorVersion;
	}

	/**
	 * @param propMajorVersion the propMajorVersion to set
	 */
	public void setPropMajorVersion(Long propMajorVersion) {
		this.propMajorVersion = propMajorVersion;
	}

	/**
	 * @return the propMinorVersion
	 */
	public Long getPropMinorVersion() {
		return propMinorVersion;
	}

	/**
	 * @param propMinorVersion the propMinorVersion to set
	 */
	public void setPropMinorVersion(Long propMinorVersion) {
		this.propMinorVersion = propMinorVersion;
	}

	/**
	 * @return the propTenantId
	 */
	public Long getPropTenantId() {
		return propTenantId;
	}

	/**
	 * @param propTenantId the propTenantId to set
	 */
	public void setPropTenantId(Long propTenantId) {
		this.propTenantId = propTenantId;
	}

	/**
	 * @return the propFullKeyList
	 */
	public String getPropFullKeyList() {
		return propFullKeyList;
	}

	/**
	 * @param propFullKeyList the propFullKeyList to set
	 */
	public void setPropFullKeyList(String propFullKeyList) {
		this.propFullKeyList = propFullKeyList;
	}

	/**
	 * @return the actuatorUrl
	 */
	public String getActuatorUrl() {
		return actuatorUrl;
	}

	/**
	 * @param actuatorUrl the actuatorUrl to set
	 */
	public void setActuatorUrl(String actuatorUrl) {
		this.actuatorUrl = actuatorUrl;
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
