package com.tea.mservice.portal.userInfo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.portal.entity.UserInfo;

public interface UserInfoRepository extends PagingAndSortingRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo> {

}
