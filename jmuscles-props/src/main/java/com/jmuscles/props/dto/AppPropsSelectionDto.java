package com.jmuscles.props.dto;

import com.jmuscles.props.jpa.entity.AppPropsSelectionEntity;

/**
 * @author manish goel
 *
 */
public class AppPropsSelectionDto {

	private String appName;
	private String status;
	private Integer majorVersion;
	private Integer minorVersion;
	private String prop_full_key_list;
	private TenantDto tenant;

	public AppPropsSelectionDto() {
		// TODO Auto-generated constructor stub
	}

	public AppPropsSelectionDto(String appName, String status, Integer majorVersion, Integer minorVersion,
			String prop_full_key_list, TenantDto tenant) {
		super();
		this.appName = appName;
		this.status = status;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.prop_full_key_list = prop_full_key_list;
		this.tenant = tenant;
	}

	public static AppPropsSelectionDto of(String appName, String status, Integer majorVersion, Integer minorVersion,
			String prop_full_key_list, TenantDto tenant) {
		return new AppPropsSelectionDto(appName, status, majorVersion, minorVersion, prop_full_key_list, tenant);
	}

	public static AppPropsSelectionDto of(AppPropsSelectionEntity entity) {
		return new AppPropsSelectionDto(entity.getAppName(), entity.getStatus(), entity.getMajorVersion(),
				entity.getMinorVersion(), entity.getProp_full_key_list(), TenantDto.of(entity.getTenant()));
	}

	public AppPropsSelectionEntity getAppPropsSelectionEntity() {
		return AppPropsSelectionEntity.of(appName, status, majorVersion, minorVersion, prop_full_key_list,
				tenant != null ? tenant.getTenantEntity() : null);
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
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
	 * @return the prop_full_key_list
	 */
	public String getProp_full_key_list() {
		return prop_full_key_list;
	}

	/**
	 * @param prop_full_key_list the prop_full_key_list to set
	 */
	public void setProp_full_key_list(String prop_full_key_list) {
		this.prop_full_key_list = prop_full_key_list;
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

}
