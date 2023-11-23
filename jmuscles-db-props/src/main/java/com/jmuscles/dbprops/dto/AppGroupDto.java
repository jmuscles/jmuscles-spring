package com.jmuscles.dbprops.dto;

import java.sql.Timestamp;

import com.jmuscles.dbprops.jpa.entity.AppGroupEntity;

/**
 * @author manish goel
 *
 */
public class AppGroupDto {

	private Long id;
	private String name;
	private String description;
	private Timestamp createdAt;
	private String createdBy;

	public AppGroupDto() {
		// TODO Auto-generated constructor stub
	}

	public AppGroupDto(Long id, String name, String description, Timestamp createdAt, String createdBy) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static AppGroupDto of(Long id, String name, String description, Timestamp createdAt, String createdBy) {
		return new AppGroupDto(id, name, description, createdAt, createdBy);
	}

	public static AppGroupDto of(AppGroupEntity entity) {
		if (entity != null) {
			return of(entity.getId(), entity.getName(), entity.getDescription(), entity.getCreatedAt(),
					entity.getCreatedBy());
		}
		return null;
	}

	public AppGroupEntity toEntity() {
		return AppGroupEntity.of(id, name, description, createdAt, createdBy);
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
