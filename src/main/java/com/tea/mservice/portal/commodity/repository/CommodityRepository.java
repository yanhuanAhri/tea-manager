package com.tea.mservice.portal.commodity.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.portal.entity.Commodity;

public interface CommodityRepository extends PagingAndSortingRepository<Commodity, Long>, JpaSpecificationExecutor<Commodity> {

}
