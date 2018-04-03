package com.tea.mservice.portal.commodity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.portal.entity.CommodityImg;

public interface CommodityImgRepository extends PagingAndSortingRepository<CommodityImg, Long>, JpaSpecificationExecutor<CommodityImg> {

	
	//编号查询
	List<CommodityImg> findByCommodityNum(String commodityNum);
}
