package com.tea.mservice.manager.roles.service;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tea.mservice.core.util.PageBean;
import com.tea.mservice.manager.entity.Role;
import com.tea.mservice.manager.entity.RolePermission;
import com.tea.mservice.manager.entity.UserRole;
import com.tea.mservice.manager.roles.mapper.RoleRowMapper;
import com.tea.mservice.manager.roles.repository.RolePermissionRepository;
import com.tea.mservice.manager.roles.repository.RoleRepository;
import com.tea.mservice.manager.roles.vo.RoleVo;
import com.tea.mservice.manager.users.repository.UserRoleRepository;

import net.sf.json.JSONObject;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Role get(Long id) {
		return roleRepository.findById(id);
	}
	
	public void save(RoleVo vo) throws Exception {
		
		if(vo == null) {
			throw new Exception("数据格式不正确");
		}
		
		if(StringUtils.isEmpty(vo.getCode())) {
			throw new Exception("角色编码不能为空");
		}
		
		Role entity = null;
		if(vo.getId() == null) {
			entity = new Role();
			
			List<Role> list = roleRepository.findByName(vo.getName());
			if(list.size() >= 1) {
				throw new Exception("存在相同名称的角色");
			}
			
		} else {
			entity = roleRepository.findById(vo.getId());
		}
		
		if(StringUtils.isNotEmpty(vo.getName()) && !vo.getName().equals(entity.getName())) {
			entity.setName(vo.getName());
		}
		if(StringUtils.isNotEmpty(vo.getCode()) && !vo.getCode().equals(entity.getCode())) {
			entity.setCode(vo.getCode());
		}
		if(StringUtils.isNotEmpty(vo.getDescription()) && !vo.getDescription().equals(entity.getDescription())) {
			entity.setDescription(vo.getDescription());
		}
		
		roleRepository.save(entity);
		
	}
	
	public void delete(Long id) throws Exception {
		Role entity = roleRepository.findById(id);
		if(entity == null) {
			throw new Exception("没有找到此角色");
		}
		
		if(id.longValue() == 1) {
			throw new Exception("此角色不能删除");
		}
		
		List<RolePermission> list = rolePermissionRepository.findByRoleId(id);
		rolePermissionRepository.delete(list);
		
		List<UserRole> uRoleList = userRoleRepository.findByRoleId(id);
		if(uRoleList != null && uRoleList.size() > 0) {
			userRoleRepository.delete(uRoleList);
		}
		
		roleRepository.delete(id);
	}
	
	public Map<String, Object> pages(Integer pageNumber, Integer pageSize, String filter, String sort) throws Exception{
		
		String currentUserLoginName = SecurityUtils.getSubject().getPrincipal().toString();
		
		StringBuffer totalSql = new StringBuffer("select count(1) as total from t_sys_role where 1=1 ");
		StringBuffer sql = new StringBuffer("select id, name, code, description from t_sys_role where 1=1 ");
		
		if (!"admin".equalsIgnoreCase(currentUserLoginName)) {
			sql.append(" and id != 1 ");
			totalSql.append(" and id != 1 ");
		}
		
		addFilter(totalSql, filter);
		Map<String, Object> countMap = jdbcTemplate.queryForMap(totalSql.toString());
		Object totalObj = countMap.get("total");
		if(totalObj == null) {
			return null;
		}
		
		Integer total = Integer.parseInt(totalObj.toString());
		PageBean pBean = new PageBean(pageSize, (pageNumber + 1), total);
		
		addFilter(sql, filter);
		addSort(sql, sort);
		sql.append(" limit ").append(pBean.getStartNum()).append(",").append(pBean.getPageSize());
		
		List<RoleVo> list = jdbcTemplate.query(sql.toString(), new RoleRowMapper());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("total", total);
		
		return map;
	}
	
	public List<Map<String, Object>> listAll() throws Exception{
		
		String sql = "select id, name from t_sys_role";
		return jdbcTemplate.queryForList(sql);
	}
	
	public Set<String> findByUserId(Long userId) {
		
		if(userId == null) {
			return null;
		}
		
		StringBuffer sql = new StringBuffer("select distinct r.code from t_sys_user_role ur left join t_sys_role r on r.id = ur.role_id where ur.user_id = ?");
		List<Map<String, Object>> restList = jdbcTemplate.queryForList(sql.toString(), userId);
		Set<String> list = new LinkedHashSet<String>();
		
		for(Map<String, Object> map : restList) {
			Object obj = map.get("code");
			if(obj == null) {
				continue;
			}
			list.add(obj.toString());
		}
		
		return list;
	}
	
	private void addSort(StringBuffer sql, String sort) {
		if(StringUtils.isNotEmpty(sort)) {
			sql.append(" order by ").append(sort).append(" ");
			return;
		}
		
		sql.append(" order by id ");
	}
	
	private void addFilter(StringBuffer sql, String filter) {
		if(StringUtils.isEmpty(filter)) {
			return;
		}
		
		JSONObject obj = JSONObject.fromObject(filter);
		Object nameObj = obj.get("name");
		if(nameObj != null) {
			sql.append(" and name like '%").append(nameObj.toString()).append("%'");
		}
		Object desObj = obj.get("description");
		if(desObj != null) {
			sql.append(" and description like '%").append(desObj.toString()).append("%'");
		}
	}
}
