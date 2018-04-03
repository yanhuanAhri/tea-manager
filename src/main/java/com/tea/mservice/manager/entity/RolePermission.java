package com.tea.mservice.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 角色权限关系表
 */
@Entity
@Table(name = "t_sys_role_permission")
public class RolePermission extends IdEntity {

	private Long permissionId;
	private Long roleId;

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
