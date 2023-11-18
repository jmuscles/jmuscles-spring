package com.jmuscles.props.dto;

import java.sql.Timestamp;

import com.jmuscles.props.jpa.entity.AppPropsProvisionEntity;

/**
 * @author manish goel
 */
public class AppPropsProvisionDto {

	private Long id;
	private AppDto appDto;
	private String env;
	private String status;
	private Long propMajorVersion;
	private Long propMinorVersion;
	private Long propTenantId;
	private String propFullKeyList;
	private String actuatorUrl;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;

	public AppPropsProvisionDto() {
		// TODO Auto-generated constructor stub
	}

	public AppPropsProvisionDto(Long id, AppDto appDto, String env, String status, Long propMajorVersion,
			Long propMinorVersion, Long propTenantId, String propFullKeyList, String actuatorUrl, Timestamp createdAt,
			String createdBy, Timestamp updatedAt, String updatedBy) {
		super();
		this.appDto = appDto;
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

	public static AppPropsProvisionDto of(Long id, AppDto appDto, String env, String status, Long propMajorVersion,
			Long propMinorVersion, Long propTenantId, String propFullKeyList, String actuatorUrl, Timestamp createdAt,
			String createdBy, Timestamp updatedAt, String updatedBy) {
		return new AppPropsProvisionDto(id, appDto, env, status, propMajorVersion, propMinorVersion, propTenantId,
				propFullKeyList, actuatorUrl, createdAt, createdBy, updatedAt, updatedBy);
	}

	public static AppPropsProvisionDto of(AppPropsProvisionEntity entity) {
		if (entity != null) {
			return of(entity.getId(), AppDto.of(entity.getAppEntity()), entity.getEnv(), entity.getStatus(),
					entity.getPropMajorVersion(), entity.getPropMinorVersion(), entity.getPropTenantId(),
					entity.getPropFullKeyList(), entity.getActuatorUrl(), entity.getCreatedAt(), entity.getCreatedBy(),
					entity.getUpdatedAt(), entity.getUpdatedBy());
		}
		return null;
	}

	public AppPropsProvisionEntity toEntity() {
		return AppPropsProvisionEntity.of(id, appDto != null ? appDto.toEntity() : null, env, status, propMajorVersion,
				propMinorVersion, propTenantId, propFullKeyList, actuatorUrl, createdAt, createdBy, updatedAt,
				updatedBy);
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
	 * @return the appDto
	 */
	public AppDto getAppDto() {
		return appDto;
	}

	/**
	 * @param appDto the appDto to set
	 */
	public void setAppDto(AppDto appDto) {
		this.appDto = appDto;
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
