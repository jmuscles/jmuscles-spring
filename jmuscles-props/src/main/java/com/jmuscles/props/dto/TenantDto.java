/**
 * @author manish goel
 *
 */
package com.jmuscles.props.dto;

import java.sql.Timestamp;

import com.jmuscles.props.jpa.entity.TenantEntity;

public class TenantDto {

	private Long id;
	private String name;
	private String desc;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;

	public TenantDto() {
		// TODO Auto-generated constructor stub
	}

	public TenantDto(Long id, String name, String desc, Timestamp createdAt, String createdBy, Timestamp updatedAt,
			String updatedBy) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public TenantEntity toTenantEntity() {
		return TenantEntity.of(id, name, desc, createdAt, createdBy, updatedAt, updatedBy);
	}

	public static TenantDto of(TenantEntity tenantEntity) {
		if (tenantEntity != null) {
			return new TenantDto(tenantEntity.getId(), tenantEntity.getName(), tenantEntity.getDesc(),
					tenantEntity.getCreatedAt(), tenantEntity.getCreatedBy(), tenantEntity.getUpdatedAt(),
					tenantEntity.getUpdatedBy());
		}
		return null;
	}

	public static TenantDto of(Long id, String name, String desc, Timestamp createdAt, String createdBy,
			Timestamp updatedAt, String updatedBy) {
		return new TenantDto(id, name, desc, createdAt, createdBy, updatedAt, updatedBy);
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
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
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
