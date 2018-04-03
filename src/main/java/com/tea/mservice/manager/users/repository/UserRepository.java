package com.tea.mservice.manager.users.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tea.mservice.manager.entity.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

	User findById(Long id);

	@Query("from User where loginName=? and status != 99")
	User findByLoginName(String loginName);

	@Query("from User where mobile=? and status != 99")
	User findByMobile(String mobile);

	List<User> findByIdIn(Collection<Long> ids);
	List<User> findByNameLike(String name);
	List<User> findByDeptId(Long id);
}
