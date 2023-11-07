/**
 * @author manish goel
 *
 */
package com.jmuscles.props.jpa.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * 
 */
public class AppPropsSelectionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PROP_SEQ")
	@SequenceGenerator(sequenceName = "PROP_SEQ", name = "PROP_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@Column(unique = true, nullable = false, name = "APP_NAME")
	private TenantEntity appName;

	@ManyToOne
	@JoinColumn(name = "TENANT_ID", nullable = false) // This is the join column
	private TenantEntity tenant;

	@ManyToOne
	@JoinColumn(name = "VERSION_ID", nullable = false) // This is the join column
	private PropVersionEntity version;

	public AppPropsSelectionEntity() {
		// TODO Auto-generated constructor stub
	}

	public AppPropsSelectionEntity(TenantEntity appName, TenantEntity tenant, PropVersionEntity version) {
		super();
		this.appName = appName;
		this.tenant = tenant;
		this.version = version;
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
	 * @return the appName
	 */
	public TenantEntity getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(TenantEntity appName) {
		this.appName = appName;
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

	/**
	 * @return the version
	 */
	public PropVersionEntity getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(PropVersionEntity version) {
		this.version = version;
	}

}
