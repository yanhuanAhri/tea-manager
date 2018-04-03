package com.tea.mservice.manager.department.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.Department;
import com.tea.mservice.manager.entity.DepartmentType;

public interface DepartmentRepository
		extends PagingAndSortingRepository<Department, Long>, JpaSpecificationExecutor<Department> {

	Department findById(Long id);

	Department findByCode(String code);

	List<Department> findByParentId(Long parentId);
	List<Department> findByParentIdAndIdIn(Long parentId, List<Long> ids);
	List<Department> findByParentIdInAndIdIn(List<Long> parentIds, List<Long> ids);

	List<Department> findByIdIn(List<Long> ids);
	List<Department> findByCodeLike(String code);
	@Override
	List<Department> findAll();
	List<Department> findByNameLike(String name);
	List<Department> findByIdInAndType(List<Long> ids, String type);
}
