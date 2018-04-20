package com.tea.mservice.portal.order.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.portal.entity.OrderLog;

public interface OrderLogRepository extends PagingAndSortingRepository<OrderLog, Long>, JpaSpecificationExecutor<OrderLog> {

	
	
}
