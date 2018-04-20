package com.tea.mservice.portal.recommend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.portal.entity.Recommend;

public interface RecommendRepository extends PagingAndSortingRepository<Recommend, Long>, JpaSpecificationExecutor<Recommend> {

	
	List<Recommend> findByCommodityId(Long commodityId);
}
