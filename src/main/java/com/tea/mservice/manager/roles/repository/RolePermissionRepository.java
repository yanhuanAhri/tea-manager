package com.tea.mservice.manager.roles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.RolePermission;

public interface RolePermissionRepository extends PagingAndSortingRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {

	RolePermission findById(Long id);
	
	List<RolePermission> findByRoleId(Long roleId);
	
	List<RolePermission> findByPermissionId(Long permissionId);
	
	@Query("select count(1) from Permission p , RolePermission r where r.permissionId = p.id and p.code = ?1")
//	@Query(value="select count(1) from t_sys_permission p , t_sys_role_permission r where r.permission_id = p.id and p.code = ?",nativeQuery=true)
	Long countByCode(String code);
	
}
