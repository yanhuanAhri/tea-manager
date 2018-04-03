package com.tea.mservice.manager.department.vo;

/**
 * 部门
 */
public class DepartmentVo {

	private Long id;
	private Long parentId;
	private String code;
	private String name;
	private String description;
	private Integer sortby;
	private Integer level;
	private Boolean check;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

}
