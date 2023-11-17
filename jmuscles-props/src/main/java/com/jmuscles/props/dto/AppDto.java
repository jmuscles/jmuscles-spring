/**
 * @author manish goel
 *
 */
package com.jmuscles.props.dto;

import java.sql.Timestamp;

import com.jmuscles.props.jpa.entity.AppEntity;

/**
 * 
 */
public class AppDto {

	private Long id;
	private String name;
	private String description;
	private AppGroupDto appGroupDto;
	private Timestamp createdAt;
	private String createdBy;

	public AppDto() {
		// TODO Auto-generated constructor stub
	}

	public AppDto(Long id, String name, String description, AppGroupDto appGroupDto, Timestamp createdAt,
			String createdBy) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.appGroupDto = appGroupDto;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public static AppDto of(Long id, String name, String description, AppGroupDto appGroupDto, Timestamp createdAt,
			String createdBy) {
		return new AppDto(id, name, description, appGroupDto, createdAt, createdBy);
	}

	public static AppDto of(AppEntity entity) {
		if (entity != null) {
			return of(entity.getId(), entity.getName(), entity.getDescription(),
					AppGroupDto.of(entity.getAppGroupEntity()), entity.getCreatedAt(), entity.getCreatedBy());
		}
		return null;
	}

	public AppEntity toEntity() {
		return AppEntity.of(id, name, description, appGroupDto != null ? appGroupDto.toEntity() : null, createdAt,
				createdBy);
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
	 * @return the appGroupDto
	 */
	public AppGroupDto getAppGroupDto() {
		return appGroupDto;
	}

	/**
	 * @param appGroupDto the appGroupDto to set
	 */
	public void setAppGroupDto(AppGroupDto appGroupDto) {
		this.appGroupDto = appGroupDto;
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
