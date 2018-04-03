package com.tea.mservice.manager.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.UserRole;

public interface UserRoleRepository
		extends PagingAndSortingRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

	UserRole findById(Long id);
	
	List<UserRole> findByUserId(Long userId);
	List<UserRole> findByRoleId(Long roleId);
}
