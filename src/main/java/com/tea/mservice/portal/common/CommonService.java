package com.tea.mservice.portal.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tea.mservice.config.shiro.UserRealm;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.manager.department.repository.DepartmentRepository;
import com.tea.mservice.manager.department.service.DepartmentService;
import com.tea.mservice.manager.department.vo.DepartmentVo;
import com.tea.mservice.manager.entity.Department;
import com.tea.mservice.manager.entity.DepartmentType;
import com.tea.mservice.manager.entity.User;
import com.tea.mservice.manager.permission.vo.LevelVo;
import com.tea.mservice.manager.users.repository.UserRepository;

import static java.util.stream.Collectors.*;

/**
 *
 * @author liuyihao
 * @date 2017/10/20
 */
@Component
public class CommonService {
	private static final Logger LOG = LoggerFactory.getLogger(CommonService.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void putCreators(List<? extends CreatorVo> creatorVos) throws Exception {
		if (creatorVos == null) {
			return;
		}
		List<Long> ids = new ArrayList<>();
		creatorVos.stream().filter(creatorVo -> creatorVo.getCreatorId() != null)
				.forEach(creatorVo -> ids.add(creatorVo.getCreatorId()));

		List<User> users = userRepository.findByIdIn(ids);
		creatorVos.forEach(creatorVo -> users.stream().filter(user -> user.getId().equals(creatorVo.getCreatorId()))
				.forEach(user -> creatorVo.setCreatorName(user.getName())));
	}

	public Map<Long, String> findUserNames(Collection<Long> userIds) {
		Map<Long, String> map = new HashMap<>(userIds.size());
		findUsers(userIds).forEach(user -> map.put(user.getId(), user.getName()));
		return map;
	}

	public List<User> findUsers(Collection<Long> userIds) {
		if (userIds.isEmpty()){
			return Collections.emptyList();
		}
		return userRepository.findByIdIn(userIds);
	}
	public Map<Long, String> findOrgNames(Collection<Long> orgIds) {
		Map<Long, String> map = new HashMap<>(orgIds.size());
		findDepartments(orgIds).forEach(departmentVo -> map.put(departmentVo.getId(), departmentVo.getName()));
		return map;
	}

	

	private String getNewName(String filename) {
		int index = filename.lastIndexOf(".");
		String suffix = StringUtils.substring(filename, index);
		return (UUID.randomUUID() + suffix);
	}

	/**
	 * 查询机构及其子机构的集合
	 * @param orgId 目标机构
	 * @param userOrgIds 拥有的组织机构
	 * @return
	 */
	public List<Long> getAllOrgs(Long orgId, List<Long> userOrgIds) {
		if (orgId == null) {
			return userOrgIds;
		}
		List<Long> result = new ArrayList<>();
		result.add(orgId);
		result.addAll(getSubOrgs(result, userOrgIds));
		return result;
	}

	private List<Long> getSubOrgs(List<Long> orgIds, List<Long> userOrgIds) {
		List<Long> sub = departmentRepository
				.findByParentIdInAndIdIn(orgIds, userOrgIds)
				.stream()
				.map(Department::getId)
				.collect(toList());
		if (sub.isEmpty()) {
			return sub;
		}
		sub.addAll(getSubOrgs(sub, userOrgIds));
		return sub;
	}

	/**
	 *  查询部门员工
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> findUserByDeptId(Long id) {
		try {
			return userRepository
					.findByDeptId(id)
					.stream()
					.map(user -> {
						Map<String, Object> map = new HashMap<>(2);
						map.put("id", user.getId());
						map.put("name", user.getName());
						return map;
					})
					.collect(toList());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 查找用户拥有权的类型部门
	 * @param type
	 * @return
	 */
	public List<DepartmentVo> findDeptsForUser(DepartmentType type) {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		return findDepartments(type == null ? user.orgIdList : departmentRepository.findByIdInAndType(user.orgIdList, type.name()).stream().map(Department::getId).collect(toList()));

	}

	public List<DepartmentVo> findDepartments(Collection<Long> orgIds) {
		try {
			if (orgIds != null && orgIds.isEmpty()) {
				return Collections.emptyList();
			}
			List<Department> departments = departmentRepository.findAll();
			List<LevelVo> levelVos = findDepartmentLevel(orgIds);
			return levelVos
					.stream()
					.map(levelVo -> {
						DepartmentVo vo = new DepartmentVo();
						StringBuilder nameB = new StringBuilder();
						String fillPath = levelVo.getPath() + "," + levelVo.getId();
						String[] children = fillPath.split(",");
						int iMax = children.length - 1;

						for (int i = 0;; i++) {
							String child = children[i];
							if (StringUtils.equals(child, "0")) {
								continue;
							}
							for (Department department : departments) {
								if (Objects.equals(child, String.valueOf(department.getId()))) {
									nameB.append(department.getName());
									break;
								}
							}
							if (i == iMax) {
								break;
							}
							nameB.append("-");
						}

						vo.setId(levelVo.getId());
						vo.setCode(levelVo.getCode());
						vo.setName(nameB.toString());
						return vo;
					})
					.collect(toList());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}

	private static final String SQL_DEPT_LEVEL = "SELECT id AS ID,code AS CODE,parent_id ,levels AS level, paths AS path\n" +
			"FROM ( SELECT\n" +
			"id,code,\n" +
			"parent_id,\n" +
			"@le:= IF (parent_id = 0 ,0,    IF( LOCATE( CONCAT('|',parent_id,':'),@pathlevel)  > 0 ,         SUBSTRING_INDEX( SUBSTRING_INDEX(@pathlevel,CONCAT('|',parent_id,':'),-1),'|',1) +1  ,@le+1) ) levels ,\n" +
			"@pathlevel:= CONCAT(@pathlevel,'|',id,':', @le ,'|') pathlevel ,\n" +
			"@pathnodes:= IF( parent_id =0,'0',     CONCAT_WS(',',    IF( LOCATE( CONCAT('|',parent_id,':'),@pathall) > 0 ,       SUBSTRING_INDEX( SUBSTRING_INDEX(@pathall,CONCAT('|',parent_id,':'),-1),'|',1)     ,@pathnodes ) ,parent_id ) )paths ,\n" +
			"@pathall:=CONCAT(@pathall,'|',id,':', @pathnodes ,'|') pathall\n" +
			"FROM t_sys_department, (SELECT @le:=0,@pathlevel:='', @pathall:='',@pathnodes:='') vv ORDER BY CODE) src\n";

	private List<LevelVo> findDepartmentLevel(Collection<Long> orgIds) {
		String sql = SQL_DEPT_LEVEL;
		if (orgIds != null) {
			sql += "	WHERE id IN " + orgIds.stream().map(Object::toString).collect(joining(",", "(", ")"));
		}
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LevelVo.class));
	}
}
