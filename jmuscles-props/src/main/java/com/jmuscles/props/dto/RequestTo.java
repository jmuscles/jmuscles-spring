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
	private JmusclesConfig jmusclesConfig;
	private PropVersionDto propVersionDto;
	private TenantDto tenantDto;

	public RequestTo() {
		// TODO Auto-generated constructor stub
	}

	public RequestTo(String requestPath, JmusclesConfig jmusclesConfig, PropVersionDto propVersionDto,
			TenantDto tenantDto) {
		super();
		this.requestPath = requestPath;
		this.jmusclesConfig = jmusclesConfig;
		this.propVersionDto = propVersionDto;
		this.tenantDto = tenantDto;
	}

	public static RequestTo of(String requestPath, JmusclesConfig jmusclesConfig, PropVersionDto propVersionDto,
			TenantDto tenantDto) {
		return new RequestTo(requestPath, jmusclesConfig, propVersionDto, tenantDto);
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
	 * @return the jmusclesConfig
	 */
	public JmusclesConfig getJmusclesConfig() {
		return jmusclesConfig;
	}

	/**
	 * @param jmusclesConfig the jmusclesConfig to set
	 */
	public void setJmusclesConfig(JmusclesConfig jmusclesConfig) {
		this.jmusclesConfig = jmusclesConfig;
	}

	/**
	 * @return the propVersionDto
	 */
	public PropVersionDto getPropVersionDto() {
		return propVersionDto;
	}

	/**
	 * @param propVersionDto the propVersionDto to set
	 */
	public void setPropVersionDto(PropVersionDto propVersionDto) {
		this.propVersionDto = propVersionDto;
	}

	/**
	 * @return the tenantDto
	 */
	public TenantDto getTenantDto() {
		return tenantDto;
	}

	/**
	 * @param tenantDto the tenantDto to set
	 */
	public void setTenantDto(TenantDto tenantDto) {
		this.tenantDto = tenantDto;
	}

}
