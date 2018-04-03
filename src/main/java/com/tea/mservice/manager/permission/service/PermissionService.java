package com.tea.mservice.manager.permission.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.tea.mservice.manager.entity.Permission;
import com.tea.mservice.manager.entity.Role;
import com.tea.mservice.manager.entity.RolePermission;
import com.tea.mservice.manager.entity.User;
import com.tea.mservice.manager.permission.mapper.LevelRowMapper;
import com.tea.mservice.manager.permission.repository.PermissionRepository;
import com.tea.mservice.manager.permission.vo.LevelVo;
import com.tea.mservice.manager.permission.vo.PermissionVo;
import com.tea.mservice.manager.roles.repository.RolePermissionRepository;
import com.tea.mservice.manager.roles.repository.RoleRepository;
import com.tea.mservice.manager.users.mapper.PermissionRowMapper;
import com.tea.mservice.manager.users.service.UserService;

import net.sf.json.JSONObject;

@Service
public class PermissionService {

	private static final Logger LOG = LoggerFactory.getLogger(PermissionService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserService userService;

	public void saveRolePermissions(Long roleId, Long[] permissionIds) throws Exception {
		Role roleEntity = roleRepository.findById(roleId);
		if (roleEntity == null) {
			throw new Exception("没有找到此角色 ");
		}
		
		String currentUserLoginName = SecurityUtils.getSubject().getPrincipal().toString();
		if(!"admin".equals(currentUserLoginName) && roleId.longValue() == 1) {
			throw new Exception("此角色权限不可更改 ");
		}

		if (permissionIds == null || permissionIds.length == 0) {
			LOG.error("没有角色授权的权限 , roleId={}, premissionIds = {}", roleId, permissionIds);
			throw new Exception("没有角色授权的权限 ");
		}else {
			List<RolePermission> rpList = rolePermissionRepository.findByRoleId(roleId);
			rolePermissionRepository.delete(rpList);

			List<RolePermission> list = new ArrayList<RolePermission>();
			for (Long id : permissionIds) {
				if (id == null) {
					continue;
				}
				RolePermission entity = new RolePermission();
				entity.setRoleId(roleId);
				entity.setPermissionId(id);
				list.add(entity);
			}

			rolePermissionRepository.save(list);
		}

	}

	public boolean hasPermWithCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return false;
		}
		Long count = rolePermissionRepository.countByCode(code);
		if (count.longValue() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取资源集合
	 * 
	 * @param account
	 * @return
	 */
	public Set<String> findPermissions(String account) {
		Set<String> set = Sets.newHashSet();
		List<PermissionVo> list = findPermissionByUser(account, "all");

		for (PermissionVo info : list) {
			String url = info.getUrl();
			if (StringUtils.isEmpty(url)) {
				continue;
			}
			set.add(info.getUrl());
		}
		return set;
	}
	
	public List<PermissionVo> findPermissionAll() {

		String sql = "SELECT p.id, p.parent_id, p.name, p.type, p.url, p.method, p.code, p.icon, p.sortby "
					+ "FROM t_sys_permission p " + "LEFT JOIN t_sys_role_permission rp ON p.id = rp.PERMISSION_ID "
					+ "LEFT JOIN t_sys_user_role ur ON ur.role_id = rp.role_id "
					+ "WHERE p.type = 1 order by p.sortby, p.name";
		List<PermissionVo> list = jdbcTemplate.query(sql, new Object[] {}, new PermissionRowMapper());
		return list;
	}

	public List<PermissionVo> findPermissionByUser(String account, String type) {

		User user = userService.findByAccount(account);

		String sql = null;
		if ("menu".equalsIgnoreCase(type)) {
			sql = "SELECT distinct p.id, p.parent_id, p.name, p.type, p.url, p.method, p.code, p.icon, p.sortby "
					+ "FROM t_sys_permission p " + "LEFT JOIN t_sys_role_permission rp ON p.id = rp.PERMISSION_ID "
					+ "LEFT JOIN t_sys_user_role ur ON ur.role_id = rp.role_id "
					+ "WHERE ur.user_id = ? AND p.type = 1 ";
		} else {
			sql = "select distinct p.id, p.parent_id, p.name, p.type, p.url, p.method, p.code, p.icon, p.sortby "
					+ "from t_sys_role r " + "left join t_sys_user_role ur on ur.role_id = r.id "
					+ "left join t_sys_role_permission rp on rp.role_id = r.id "
					+ "left join t_sys_permission p on p.id = rp.permission_id "
					+ "where ur.user_id = ? and p.id IS NOT NULL ";
		}

		if ("button".equalsIgnoreCase(type)) {
			sql += " and p.type = 2 ";
		}

		sql += " order by p.sortby, p.name";

		List<PermissionVo> list = jdbcTemplate.query(sql, new Object[] { user.getId() }, new PermissionRowMapper());

		return list;
	}

	public Map<String, Object> listAll(String filter, String sort, Long roleId) throws Exception {

		StringBuffer sql = new StringBuffer(
				"SELECT id, parent_id, NAME, TYPE, url, method, CODE, sortby,icon FROM t_sys_permission ORDER BY parent_id,sortby");

		StringBuffer rolePermSql = new StringBuffer(
				"SELECT permission_id FROM t_sys_role_permission WHERE role_id = ? ORDER BY permission_id");

		List<PermissionVo> list = jdbcTemplate.query(sql.toString(), new Object[] {}, new PermissionRowMapper());

		List<Map<String, Object>> rolePermList = jdbcTemplate.queryForList(rolePermSql.toString(), roleId);

		setLevel(list);
		checkSelect(list, rolePermList);

		// 重排结果
		List<PermissionVo> newList = new LinkedList<PermissionVo>();
		sortList(newList, list);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", newList);
		return map;
	}

	private void checkSelect(List<PermissionVo> list, List<Map<String, Object>> rolePermList) throws Exception {

		for (PermissionVo vo : list) {
			if (hasPerm(vo.getId(), rolePermList)) {
				vo.setCheck(true);
			}
		}
	}

	private boolean hasPerm(Long id, List<Map<String, Object>> rolePermList) {
		for (Map<String, Object> map : rolePermList) {
			Object perIdObj = map.get("permission_id");
			if (perIdObj == null) {
				continue;
			}

			Long perId = Long.valueOf(perIdObj.toString());
			if (id.longValue() == perId.longValue()) {
				return true;
			}
		}

		return false;
	}

	private void sortList(List<PermissionVo> newList, List<PermissionVo> list) {
		for (PermissionVo vo : list) {
			if (vo.getParentId().longValue() == 0) {
				newList.add(vo);
				findChild(vo.getId(), newList, list);
			}
		}
	}

	private void findChild(Long parentId, List<PermissionVo> newList, List<PermissionVo> list) {
		for (PermissionVo vo : list) {
			if (parentId.longValue() == vo.getParentId().longValue()) {
				newList.add(vo);
				findChild(vo.getId(), newList, list);
			}
		}
	}

	public Map<String, Object> pages(Long parentId, String filter, String sort) throws Exception {

		StringBuffer sql = new StringBuffer(
				"select id, parent_id, name, type, url, method, code, icon, sortby from t_sys_permission where parent_id = ?");

		addFilter(sql, filter);
		addSort(sql, sort);

		List<PermissionVo> list = jdbcTemplate.query(sql.toString(), new Object[] { parentId },
				new PermissionRowMapper());
		setLevel(list);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		return map;
	}

	public PermissionVo get(Long id) {
		Permission entity = permissionRepository.findById(id);
		PermissionVo vo = new PermissionVo();
		vo.setId(entity.getId());
		vo.setParentId(entity.getParentId());
		vo.setName(entity.getName());
		vo.setType(entity.getType());
		vo.setCode(entity.getCode());
		vo.setIcon(entity.getIcon());
		vo.setUrl(entity.getUrl());
		vo.setMethod(entity.getMethod());
		vo.setSortby(entity.getSortby());
		return vo;
	}

	public void save(PermissionVo vo) throws Exception {

		Permission entity = null;
		Long id = vo.getId();
		if (id == null) {
			entity = new Permission();
			entity.setParentId(vo.getParentId());
		} else {
			entity = permissionRepository.findById(id);
			if (entity == null) {
				throw new Exception("没有找到此权限的配置");
			}
		}

		if (StringUtils.isEmpty(entity.getName()) || (!vo.getName().equals(entity.getName()))) {
			entity.setName(vo.getName());
		}
		if (entity.getType() == null || vo.getType().shortValue() != entity.getType().shortValue()) {
			entity.setType(vo.getType());
		}
		if (StringUtils.isEmpty(entity.getCode()) || (!vo.getCode().equals(entity.getCode()))) {
			entity.setCode(vo.getCode());
		}
		if (StringUtils.isEmpty(entity.getUrl()) || (!vo.getUrl().equals(entity.getUrl()))) {
			entity.setUrl(vo.getUrl());
		}
		if (StringUtils.isEmpty(entity.getMethod()) || (!vo.getMethod().equals(entity.getMethod()))) {
			entity.setMethod(vo.getMethod());
		}
		if (StringUtils.isEmpty(entity.getIcon()) || (!vo.getIcon().equals(entity.getIcon()))) {
			entity.setIcon(vo.getIcon());
		}
		if (entity.getSortby() == null || (vo.getSortby().intValue() != entity.getSortby().intValue())) {
			entity.setSortby(vo.getSortby());
		}

		permissionRepository.save(entity);
	}

	public void delete(Long id) throws Exception {
		Permission entity = permissionRepository.findById(id);
		if (entity == null) {
			throw new Exception("没有找到此权限的配置");
		}

		//
		List<RolePermission> list = rolePermissionRepository.findByPermissionId(id);
		rolePermissionRepository.delete(list);

		//
		List<Permission> childList = new ArrayList<Permission>();
		findChild(childList, id);

		permissionRepository.delete(childList);
		permissionRepository.delete(id);
	}

	private void findChild(List<Permission> perList, Long parentId) {
		List<Permission> children = permissionRepository.findByParentId(parentId);
		if (children == null || children.size() <= 0) {
			return;
		}

		perList.addAll(children);
		for (Permission p : children) {
			findChild(perList, p.getId());
		}
	}

	private void addFilter(StringBuffer sql, String filter) {

		if (StringUtils.isEmpty(filter)) {
			return;
		}
		JSONObject obj = JSONObject.fromObject(filter);
		@SuppressWarnings("unchecked")
		Iterator<String> it = obj.keySet().iterator();

		while (it.hasNext()) {
			String columnName = it.next();
			String value = obj.getString(columnName);
			if ("name".equalsIgnoreCase(columnName)) {
				sql.append(" and name like ").append("'%").append(value).append("%'");
			} else if ("type".equalsIgnoreCase(columnName) && (StringUtils.isNotEmpty(value) && !"-1".equals(value))) {
				sql.append(" and type = ").append(value);
			} else if ("url".equalsIgnoreCase(columnName)) {
				sql.append(" and url like ").append("'%").append(value).append("%'");
			} else if ("method".equalsIgnoreCase(columnName)
					&& (StringUtils.isNotEmpty(value) && !"-1".equals(value))) {
				sql.append(" and method = ").append(value);
			} else if ("code".equalsIgnoreCase(columnName)) {
				sql.append(" and code like ").append("'%").append(value).append("%'");
			} else if ("icon".equalsIgnoreCase(columnName)) {
				sql.append(" and icon like").append("'%").append(value).append("%'");
			}
		}
	}

	private void addSort(StringBuffer sql, String sort) {
		if (StringUtils.isEmpty(sort)) {
			sql.append(" order by sortby ");
			return;
		}
	}

	private void setLevel(List<PermissionVo> list) throws Exception {

		if (list == null || list.size() <= 0) {
			return;
		}

		List<LevelVo> levelList = findLevel();
		for (PermissionVo pvo : list) {
			for (LevelVo lvo : levelList) {
				if (pvo.getId().longValue() == lvo.getId().longValue()) {
					pvo.setLevel(lvo.getLevel());
				}
			}
		}
	}

	private List<LevelVo> findLevel() throws Exception {
		String sql = "SELECT id AS ID,parent_id ,levels AS 'level', paths AS 'path' FROM ( " + "SELECT id,parent_id, "
				+ "@le:= IF (parent_id = 0 ,0,  " + "  IF( LOCATE( CONCAT('|',parent_id,':'),@pathlevel)  > 0 ,   "
				+ "      SUBSTRING_INDEX( SUBSTRING_INDEX(@pathlevel,CONCAT('|',parent_id,':'),-1),'|',1) +1 "
				+ " ,@le+1) ) levels " + ", @pathlevel:= CONCAT(@pathlevel,'|',id,':', @le ,'|') pathlevel "
				+ ", @pathnodes:= IF( parent_id =0,',0',  " + "   CONCAT_WS(',', "
				+ "   IF( LOCATE( CONCAT('|',parent_id,':'),@pathall) > 0 ,  "
				+ "     SUBSTRING_INDEX( SUBSTRING_INDEX(@pathall,CONCAT('|',parent_id,':'),-1),'|',1) "
				+ "    ,@pathnodes ) ,parent_id ) )paths "
				+ ",@pathall:=CONCAT(@pathall,'|',id,':', @pathnodes ,'|') pathall  " + "  FROM t_sys_permission,  "
				+ "(SELECT @le:=0,@pathlevel:='', @pathall:='',@pathnodes:='') vv " + " ORDER BY parent_id,id "
				+ ") src " + "ORDER BY id";

		return jdbcTemplate.query(sql, new LevelRowMapper());
	}

}
