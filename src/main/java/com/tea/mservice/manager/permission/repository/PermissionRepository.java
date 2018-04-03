package com.tea.mservice.manager.permission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.Permission;

public interface PermissionRepository
		extends PagingAndSortingRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

	Permission findById(Long id);

	List<Permission> findByParentId(Long parentId);

	
}
