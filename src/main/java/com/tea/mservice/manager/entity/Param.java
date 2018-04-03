package com.tea.mservice.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 系统参数表
 */
@Entity
@Table(name = "t_sys_param")
public class Param extends IdEntity {

	private String paramName;
	private String paramValue;
	private String description;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
