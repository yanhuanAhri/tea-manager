package com.tea.mservice.manager.users.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.UserDepartment;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDepartmentRepository
		extends PagingAndSortingRepository<UserDepartment, Long>, JpaSpecificationExecutor<UserDepartment> {

	@Transactional
	long deleteByUserId(Long userId);
	List<UserDepartment> findByUserId(Long userId);
	List<UserDepartment> findByDepId(Long depId);
}
