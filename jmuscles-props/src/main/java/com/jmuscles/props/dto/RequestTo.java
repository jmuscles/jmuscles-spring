package com.jmuscles.props.dto;

/**
 * @author manish goel
 *
 */

import com.jmuscles.props.JmusclesConfig;

/**
 * 
 */
public class RequestTo {

	private String requestPath;
	private String versionTag;
	private String versionDescription;
	private Long majorVersion;
	private Long minorVersion;
	private Long tenantId;
	private String tenantName;

	private JmusclesConfig jmuscles;

	public RequestTo() {
		// TODO Auto-generated constructor stub
	}

	public RequestTo(String requestPath, String versionTag, String versionDescription, Long majorVersion,
			Long minorVersion, Long tenantId, String tenantName, JmusclesConfig jmuscles) {
		super();
		this.requestPath = requestPath;
		this.versionTag = versionTag;
		this.versionDescription = versionDescription;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.tenantId = tenantId;
		this.tenantName = tenantName;
		this.jmuscles = jmuscles;
	}

	public static RequestTo of(String requestPath, String versionTag, String versionDescription, Long majorVersion,
			Long minorVersion, Long tenantId, String tenantName, JmusclesConfig jmuscles) {
		return new RequestTo(requestPath, versionTag, versionDescription, majorVersion, minorVersion, tenantId,
				tenantName, jmuscles);
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
	 * @return the versionTag
	 */
	public String getVersionTag() {
		return versionTag;
	}

	/**
	 * @param versionTag the versionTag to set
	 */
	public void setVersionTag(String versionTag) {
		this.versionTag = versionTag;
	}

	/**
	 * @return the versionDescription
	 */
	public String getVersionDescription() {
		return versionDescription;
	}

	/**
	 * @param versionDescription the versionDescription to set
	 */
	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}

	/**
	 * @return the majorVersion
	 */
	public Long getMajorVersion() {
		return majorVersion;
	}

	/**
	 * @param majorVersion the majorVersion to set
	 */
	public void setMajorVersion(Long majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * @return the minorVersion
	 */
	public Long getMinorVersion() {
		return minorVersion;
	}

	/**
	 * @param minorVersion the minorVersion to set
	 */
	public void setMinorVersion(Long minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the tenantName
	 */
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * @param tenantName the tenantName to set
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	/**
	 * @return the jmuscles
	 */
	public JmusclesConfig getJmuscles() {
		return jmuscles;
	}

	/**
	 * @param jmuscles the jmuscles to set
	 */
	public void setJmuscles(JmusclesConfig jmuscles) {
		this.jmuscles = jmuscles;
	}

}
