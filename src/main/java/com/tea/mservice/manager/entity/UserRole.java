package com.tea.mservice.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 用户角色关系表
 */
@Entity
@Table(name = "t_sys_user_role")
public class UserRole extends IdEntity {

	private Long roleId;
	private Long userId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
