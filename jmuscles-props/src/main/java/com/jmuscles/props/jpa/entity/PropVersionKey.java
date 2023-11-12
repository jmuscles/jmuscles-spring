package com.jmuscles.props.jpa.entity;

/**
 * @author manish goel
 *
 */

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PropVersionKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "MAJOR_VERSION", nullable = false)
	private Long majorVersion;

	@Column(name = "MINOR_VERSION", nullable = false)
	private Long minorVersion;

	@Column(name = "TENANT_ID", nullable = false)
	private Long tenantId;

	public PropVersionKey() {
		// TODO Auto-generated constructor stub
	}

	public PropVersionKey(Long majorVersion, Long minorVersion, Long tenantId) {
		super();
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.tenantId = tenantId;
	}

	public static PropVersionKey of(Long majorVersion, Long minorVersion, Long tenantId) {
		return new PropVersionKey(majorVersion, minorVersion, tenantId);
	}

	public void increaseMajorVersion() {
		this.majorVersion++;
	}

	public void increaseMinorVersion() {
		this.minorVersion++;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PropVersionKey that = (PropVersionKey) o;
		return majorVersion.equals(that.majorVersion) && minorVersion.equals(that.minorVersion)
				&& tenantId.equals(that.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(majorVersion, minorVersion, tenantId);
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

}
