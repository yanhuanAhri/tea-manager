package com.tea.mservice.manager.area.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.Area;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AreaRepository
		extends PagingAndSortingRepository<Area, Long>, JpaSpecificationExecutor<Area> {
	List<Area> findById(Collection<Long> ids);
}
