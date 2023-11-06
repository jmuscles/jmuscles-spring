/**
 * 
 */
package com.jmuscles.props.jpa;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jmuscles.props.util.Constants;

@Entity
@Table(name = "APP_PROPS")
public class AppPropsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "APP_PROPS_SEQ")
	@SequenceGenerator(sequenceName = "APP_PROPS_SEQ", name = "APP_PROPS_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@Column(name = "PROP_KEY", length = 250)
	private String prop_key;

	@Column(name = "PROP_VALUE", length = Constants.PROP_VALUE_LENGTH)
	private String prop_value;

	@Column(name = "STATUS", length = 25)
	private String status;

	@Lob
	@Column(name = "PROP_VALUE_BLOB")
	private byte[] prop_value_blob;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private AppPropsEntity parent;

	@ManyToOne
	@JoinColumn(name = "TENANT_ID") // This is the join column
	private TenantEntity tenant;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProp_key() {
		return prop_key;
	}

	public void setProp_key(String prop_key) {
		this.prop_key = prop_key;
	}

	public String getProp_value() {
		return prop_value;
	}

	public void setProp_value(String prop_value) {
		this.prop_value = prop_value;
	}

	public byte[] getProp_value_blob() {
		return prop_value_blob;
	}

	public void setProp_value_blob(byte[] prop_value_blob) {
		this.prop_value_blob = prop_value_blob;
	}

	public AppPropsEntity getParent() {
		return parent;
	}

	public void setParent(AppPropsEntity parent) {
		this.parent = parent;
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

}