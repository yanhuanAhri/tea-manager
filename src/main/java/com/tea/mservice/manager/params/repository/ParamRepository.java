package com.tea.mservice.manager.params.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.Param;

public interface ParamRepository extends PagingAndSortingRepository<Param, Long>, JpaSpecificationExecutor<Param> {

	Param findById(Long id);
	
	Long countByParamName(String paramName);
	
	List<Param> findAll();
}
