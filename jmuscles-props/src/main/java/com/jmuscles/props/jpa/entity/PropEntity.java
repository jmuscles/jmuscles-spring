package com.jmuscles.props.jpa.entity;

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

	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private PropEntity parent;

	@Column(name = "MAJOR_VERSION", nullable = false) // This is the join column
	private Long majorVersion;

	@Column(name = "MINOR_VERSION", nullable = false) // This is the join column
	private Long minorVersion;

	@Column(name = "CHILDREN_MINOR_VERSION") // This is the join column
	private Long childrenMinorVersion;

	@ManyToOne
	@JoinColumn(name = "TENANT_ID", nullable = false) // This is the join column
	private TenantEntity tenant;

	@Column(name = "PROP_FULL_KEY", length = 500)
	private String prop_full_key;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	public PropEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropEntity(Long id, String prop_key, String prop_value, byte[] prop_value_blob, PropEntity parent,
			Long majorVersion, Long minorVersion, Long childrenMinorVersion, TenantEntity tenant, String prop_full_key,
			Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		super();
		this.id = id;
		this.prop_key = prop_key;
		this.prop_value = prop_value;
		this.prop_value_blob = prop_value_blob;
		this.parent = parent;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.childrenMinorVersion = childrenMinorVersion;
		this.tenant = tenant;
		this.prop_full_key = prop_full_key;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public static PropEntity of(Long id, String prop_key, String prop_value, byte[] prop_value_blob, PropEntity parent,
			Long majorVersion, Long minorVersion, Long childrenMinorVersion, TenantEntity tenant, String prop_full_key,
			Timestamp createdAt, String createdBy, Timestamp updatedAt, String updatedBy) {
		return new PropEntity(id, prop_key, prop_value, prop_value_blob, parent, majorVersion, minorVersion,
				childrenMinorVersion, tenant, prop_full_key, createdAt, createdBy, updatedAt, updatedBy);

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
	 * @return the parent
	 */
	public PropEntity getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(PropEntity parent) {
		this.parent = parent;
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
	 * @return the childrenMinorVersion
	 */
	public Long getChildrenMinorVersion() {
		return childrenMinorVersion;
	}

	/**
	 * @param childrenMinorVersion the childrenMinorVersion to set
	 */
	public void setChildrenMinorVersion(Long childrenMinorVersion) {
		this.childrenMinorVersion = childrenMinorVersion;
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