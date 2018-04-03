package com.tea.mservice.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 部门表
 */
@Entity
@Table(name = "t_sys_department")
public class Department extends IdEntity {
	private Long parentId;
	private String code;
	private String name;
	private String description;
	private Integer sortby;
	private String type;
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSortby() {
		return sortby;
	}
	public void setSortby(Integer sortby) {
		this.sortby = sortby;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
