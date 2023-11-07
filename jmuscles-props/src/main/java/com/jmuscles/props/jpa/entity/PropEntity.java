/**
 * 
 */
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

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@ManyToOne
	@JoinColumn(name = "VERSION_ID", nullable = false) // This is the join column
	private PropVersionEntity version;

	@ManyToOne
	@JoinColumn(name = "CHILD_PROP_VERSION_ID") // This is the join column
	private PropVersionEntity childPropVersion;

	public PropEntity() {
		// TODO Auto-generated constructor stub
	}

	public PropEntity of(String prop_key, String prop_value, byte[] prop_value_blob, PropEntity parent,
			Timestamp createdAt, String createdBy, PropVersionEntity version) {
		return new PropEntity(prop_key, prop_value, prop_value_blob, parent, createdAt, createdBy, version);
	}

	public PropEntity(String prop_key, String prop_value, byte[] prop_value_blob, PropEntity parent,
			Timestamp createdAt, String createdBy, PropVersionEntity version) {
		super();
		this.prop_key = prop_key;
		this.prop_value = prop_value;
		this.prop_value_blob = prop_value_blob;
		this.parent = parent;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
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

	/**
	 * @return the childPropVersion
	 */
	public PropVersionEntity getChildPropVersion() {
		return childPropVersion;
	}

	/**
	 * @param childPropVersion the childPropVersion to set
	 */
	public void setChildPropVersion(PropVersionEntity childPropVersion) {
		this.childPropVersion = childPropVersion;
	}

}