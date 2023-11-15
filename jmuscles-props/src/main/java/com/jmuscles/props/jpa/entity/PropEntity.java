package com.jmuscles.props.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jmuscles.props.util.Constants;

@Entity
@Table(name = "PROPERTIES")
public class PropEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PROP_SEQ")
	@SequenceGenerator(sequenceName = "PROP_SEQ", name = "PROP_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@Column(name = "PROP_KEY", length = 250)
	private String prop_key;

	@Column(name = "PROP_VALUE", length = Constants.PROP_VALUE_LENGTH)
	private String prop_value;

	@Lob
	@Column(name = "PROP_VALUE_BLOB")
	private byte[] prop_value_blob;

	@Column(name = "PARENT_ID")
	private Long parentId;

	@Column(name = "MAJOR_VERSION", nullable = false) // This is the join column
	private Long majorVersion;

	@Column(name = "MINOR_VERSION", nullable = false) // This is the join column
	private Long minorVersion;

	@Column(name = "TENANT_ID", nullable = false) // This is the join column
	private Long tenantId;

	@Column(name = "PROP_FULL_KEY", length = 1000)
	private String prop_full_key;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	public PropEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropEntity(Long id, String prop_key, String prop_value, byte[] prop_value_blob, Long parentId,
			Long majorVersion, Long minorVersion, Long tenantId, String prop_full_key, Timestamp createdAt,
			String createdBy) {
		super();
		this.id = id;
		this.prop_key = prop_key;
		this.prop_value = prop_value;
		this.prop_value_blob = prop_value_blob;
		this.parentId = parentId;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.tenantId = tenantId;
		this.prop_full_key = prop_full_key;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static PropEntity of(Long id, String prop_key, String prop_value, byte[] prop_value_blob, Long parentId,
			Long majorVersion, Long minorVersion, Long tenantId, String prop_full_key, Timestamp createdAt,
			String createdBy) {
		return new PropEntity(id, prop_key, prop_value, prop_value_blob, parentId, majorVersion, minorVersion, tenantId,
				prop_full_key, createdAt, createdBy);
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
	 * @return the prop_key
	 */
	public String getProp_key() {
		return prop_key;
	}

	/**
	 * @param prop_key the prop_key to set
	 */
	public void setProp_key(String prop_key) {
		this.prop_key = prop_key;
	}

	/**
	 * @return the prop_value
	 */
	public String getProp_value() {
		return prop_value;
	}

	/**
	 * @param prop_value the prop_value to set
	 */
	public void setProp_value(String prop_value) {
		this.prop_value = prop_value;
	}

	/**
	 * @return the prop_value_blob
	 */
	public byte[] getProp_value_blob() {
		return prop_value_blob;
	}

	/**
	 * @param prop_value_blob the prop_value_blob to set
	 */
	public void setProp_value_blob(byte[] prop_value_blob) {
		this.prop_value_blob = prop_value_blob;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	 * @return the prop_full_key
	 */
	public String getProp_full_key() {
		return prop_full_key;
	}

	/**
	 * @param prop_full_key the prop_full_key to set
	 */
	public void setProp_full_key(String prop_full_key) {
		this.prop_full_key = prop_full_key;
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

}