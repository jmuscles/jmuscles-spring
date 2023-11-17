package com.jmuscles.props.dto;

import java.sql.Timestamp;

import com.jmuscles.props.jpa.entity.PropTenantEntity;

/**
 * @author manish goel
 *
 */
public class PropTenantDto {

	private Long id;
	private String name;
	private String description;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;

	public PropTenantDto() {
		// TODO Auto-generated constructor stub
	}

	public PropTenantDto(Long id, String name, String description, Timestamp createdAt, String createdBy,
			Timestamp updatedAt, String updatedBy) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public PropTenantEntity toTenantEntity() {
		return PropTenantEntity.of(id, name, description, createdAt, createdBy, updatedAt, updatedBy);
	}

	public static PropTenantDto of(PropTenantEntity tenantEntity) {
		if (tenantEntity != null) {
			return new PropTenantDto(tenantEntity.getId(), tenantEntity.getName(), tenantEntity.getDescription(),
					tenantEntity.getCreatedAt(), tenantEntity.getCreatedBy(), tenantEntity.getUpdatedAt(),
					tenantEntity.getUpdatedBy());
		}
		return null;
	}

	public static PropTenantDto of(Long id, String name, String description, Timestamp createdAt, String createdBy,
			Timestamp updatedAt, String updatedBy) {
		return new PropTenantDto(id, name, description, createdAt, createdBy, updatedAt, updatedBy);
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
