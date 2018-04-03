package com.tea.mservice.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 权限表
 */
@Entity
@Table(name = "t_sys_permission")
public class Permission extends IdEntity {
	private Long parentId;
	private String name;
	private Short type;
	private String code;
	private String url;
	private String method;
	private String icon;
	private Integer sortby;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSortby() {
		return sortby;
	}

	public void setSortby(Integer sortby) {
		this.sortby = sortby;
	}

}
