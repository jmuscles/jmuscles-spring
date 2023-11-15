/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa.entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 
 */
public class AppPropsSelectionEntity {

	@Column(nullable = false, name = "APP_NAME")
	private String appName;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "MAJOR_VERSION", nullable = false) // This is the join column
	private Integer majorVersion;

	@Column(name = "MINOR_VERSION", nullable = false) // This is the join column
	private Integer minorVersion;

	@Column(name = "PROP_FULL_KEY_LIST", length = 1000)
	private String prop_full_key_list;

	@ManyToOne
	@JoinColumn(name = "TENANT_ID", nullable = false) // This is the join column
	private TenantEntity tenant;

	public AppPropsSelectionEntity() {
		// TODO Auto-generated constructor stub
	}

	public AppPropsSelectionEntity(String appName, String status, Integer majorVersion, Integer minorVersion,
			String prop_full_key_list, TenantEntity tenant) {
		super();
		this.appName = appName;
		this.status = status;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.prop_full_key_list = prop_full_key_list;
		this.tenant = tenant;
	}

	public static AppPropsSelectionEntity of(String appName, String status, Integer majorVersion, Integer minorVersion,
			String prop_full_key_list, TenantEntity tenant) {
		return new AppPropsSelectionEntity(appName, status, majorVersion, minorVersion, prop_full_key_list, tenant);
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