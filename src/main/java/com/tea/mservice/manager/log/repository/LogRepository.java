package com.tea.mservice.manager.log.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.Log;

public interface LogRepository extends PagingAndSortingRepository<Log, Long>, JpaSpecificationExecutor<Log> {

	Log findById(Long id);
	
}
