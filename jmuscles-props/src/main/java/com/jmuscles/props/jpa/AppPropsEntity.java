/**
 * 
 */
package com.jmuscles.props.jpa;

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
@Table(name = "APPLICATION_PROPERTIES")
public class AppPropsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "APPLICATION_PROPERTY_SEQ")
	@SequenceGenerator(sequenceName = "APPLICATION_PROPERTY_SEQ", name = "APPLICATION_PROPERTY_SEQ", allocationSize = 1)
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
	private AppPropsEntity parent;

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

}