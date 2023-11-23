/**
 * @author manish goel
 *
 */
package com.jmuscles.dbprops.jpa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 */
@Entity
@Table(name = "APP_GROUP")
public class AppGroupEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "APP_GROUP_SEQ")
	@SequenceGenerator(sequenceName = "APP_GROUP_SEQ", name = "APP_GROUP_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@Column(nullable = false, unique = true, name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	public AppGroupEntity() {
		// TODO Auto-generated constructor stub
	}

	public AppGroupEntity(Long id, String name, String description, Timestamp createdAt, String createdBy) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static AppGroupEntity of(Long id, String name, String description, Timestamp createdAt, String createdBy) {
		return new AppGroupEntity(id, name, description, createdAt, createdBy);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
