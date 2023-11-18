/**
 * @author manish goel
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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 */
@Entity
@Table(name = "APPS", uniqueConstraints = @UniqueConstraint(name = "APPS_UNIQUE_CONSTRAINT", columnNames = { "NAME",
		"APP_GROUP_ID" }))
public class AppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "APP_SEQ")
	@SequenceGenerator(sequenceName = "APP_SEQ", name = "APP_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToOne
	@JoinColumn(nullable = false, name = "APP_GROUP_ID")
	private AppGroupEntity appGroupEntity;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	public AppEntity() {
		// TODO Auto-generated constructor stub
	}

	public AppEntity(Long id, String name, String description, AppGroupEntity appGroupEntity, Timestamp createdAt,
			String createdBy) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.appGroupEntity = appGroupEntity;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static AppEntity of(Long id, String name, String description, AppGroupEntity appGroupEntity,
			Timestamp createdAt, String createdBy) {
		return new AppEntity(id, name, description, appGroupEntity, createdAt, createdBy);
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
	 * @return the appGroupEntity
	 */
	public AppGroupEntity getAppGroupEntity() {
		return appGroupEntity;
	}

	/**
	 * @param appGroupEntity the appGroupEntity to set
	 */
	public void setAppGroupEntity(AppGroupEntity appGroupEntity) {
		this.appGroupEntity = appGroupEntity;
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
