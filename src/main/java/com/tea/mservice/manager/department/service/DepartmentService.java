package com.tea.mservice.manager.department.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tea.mservice.config.shiro.UserRealm;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.manager.department.mapper.DepartmentRowMapper;
import com.tea.mservice.manager.department.repository.DepartmentRepository;
import com.tea.mservice.manager.department.vo.DepartmentVo;
import com.tea.mservice.manager.entity.Department;
import com.tea.mservice.manager.entity.User;
import com.tea.mservice.manager.entity.UserDepartment;
import com.tea.mservice.manager.permission.mapper.LevelRowMapper;
import com.tea.mservice.manager.permission.vo.LevelVo;
import com.tea.mservice.manager.users.service.UserService;

import net.sf.json.JSONObject;

@Service
public class DepartmentService {

	private static final Logger LOG = LoggerFactory.getLogger(DepartmentService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DepartmentRepository departmentRepository;

//	TODO:优化
	public List<DepartmentVo> findMyUserDepartments() {
		try {
			UserRealm.ShiroUser user = ShiroUserUtil.getUser();

			List<Department> departments = departmentRepository.findAll();
			List<LevelVo> levelVos = findLevel();
			List<DepartmentVo> data = levelVos
					.stream()
					.filter(levelVo -> user.orgIdList.contains(levelVo.getId()))
					.map(levelVo -> {
						DepartmentVo vo = new DepartmentVo();
						StringBuilder nameB = new StringBuilder();
						String fillPath = levelVo.getPath() + "," + levelVo.getId();
						String[] children = fillPath.split(",");
						int iMax = children.length - 1;

						for (int i = 0;; i++) {
							String child = children[i];
							if (StringUtils.equals(child, "0"))
								continue;
							for (Department department : departments) {
								if (StringUtils.equals(child, department.getId().toString())) {
									nameB.append(department.getName());
									break;
								}
							}
							if (i == iMax)
								break;
							nameB.append("-");
						}

						vo.setId(levelVo.getId());
						vo.setName(nameB.toString());
						vo.setParentId(levelVo.getParentId());
						return vo;
					})
					.collect(Collectors.toList());
			return data;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 查找【权限下】可勾选的树结构数据权限
	 * @param selectedDepartmentIds
	 * @return
	 */
	public Map<String, Object> findDepartmentSelectTree(List<Long> selectedDepartmentIds) {
		try {
			UserRealm.ShiroUser user = ShiroUserUtil.getUser();

//			admin管理员出生就有权限
			List<Department> departments = StringUtils.equals(user.loginName, "admin") ?
					departmentRepository.findAll(null, new Sort("parentId", "sortby")) :
					departmentRepository.findByIdIn(user.orgIdList);

			List<DepartmentVo> departmentVos =departments
                    .stream()
                    .map(department -> {
                        DepartmentVo vo = new DepartmentVo();
                        vo.setId(department.getId());
                        vo.setParentId(department.getParentId());
                        vo.setName(department.getName());
                        vo.setCode(department.getCode());
                        vo.setSortby(department.getSortby());
                        vo.setDescription(department.getDescription());
                        return vo;
                    })
                    .collect(Collectors.toList());

			setLevel(departmentVos);
			checkSelect(departmentVos, selectedDepartmentIds);

			List<DepartmentVo> newList = new LinkedList<>();
			sortList(newList, departmentVos);

			Map<String, Object> map = new HashMap<>();
			map.put("data", newList);
			return map;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}

	private void checkSelect(List<DepartmentVo> list, List<Long> selectedDepartmentIds){
		list
				.forEach(departmentVo ->
						selectedDepartmentIds
								.forEach(aLong -> {
									if (departmentVo.getId().longValue() == aLong.longValue())
										departmentVo.setCheck(true);
								})
				);
	}

	private void sortList(List<DepartmentVo> newList, List<DepartmentVo> list) {
		for (DepartmentVo vo : list) {
			if (vo.getParentId() == 0) {
				newList.add(vo);
				findChild(vo.getId(), newList, list);
			}
		}
	}

	private void findChild(Long parentId, List<DepartmentVo> newList, List<DepartmentVo> list) {
		for (DepartmentVo vo : list) {
			if (parentId.longValue() == vo.getParentId().longValue()) {
				newList.add(vo);
				findChild(vo.getId(), newList, list);
			}
		}
	}

	public Map<String, Object> pages(Long parentId, String filter, String sort) throws Exception {

		StringBuffer sql = new StringBuffer(
				"SELECT id, parent_id, name, code,description, sortby FROM t_sys_department WHERE parent_id = ?");

		addFilter(sql, filter);
		addSort(sql, sort);

		List<DepartmentVo> list = jdbcTemplate.query(sql.toString(), new Object[] { parentId },
				new DepartmentRowMapper());
		setLevel(list);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		return map;
	}

	public DepartmentVo get(Long id) {
		Department entity = departmentRepository.findById(id);
		DepartmentVo vo = new DepartmentVo();
		vo.setId(entity.getId());
		vo.setParentId(entity.getParentId());
		vo.setName(entity.getName());
		vo.setCode(entity.getCode());
		vo.setSortby(entity.getSortby());
		return vo;
	}

	public void save(DepartmentVo vo) throws Exception {

		Department entity = null;
		Long id = vo.getId();
		if (id == null) {
			Department parentEntity = departmentRepository.findById(vo.getParentId());
			if(parentEntity == null){
				throw new Exception("没有找到此父部门");
			}

			String code = parentEntity.getCode() + vo.getCode();
			if (departmentRepository.findByCode(code) != null) {
				throw new Exception("部门编号已存在");
			}

			entity = new Department();
			entity.setParentId(vo.getParentId());
			entity.setCode(code);

		} else {
			entity = departmentRepository.findById(id);
			if (entity == null) {
				throw new Exception("没有找到此部门");
			}
		}

		if (StringUtils.isEmpty(entity.getName()) || (!vo.getName().equals(entity.getName()))) {
			entity.setName(vo.getName());
		}
		/*if (StringUtils.isEmpty(entity.getCode()) || (!vo.getCode().equals(entity.getCode()))) {
			entity.setCode(vo.getCode());
		}*/
		if (entity.getDescription() == null || (!vo.getDescription().equals(entity.getDescription()))) {
			entity.setDescription(vo.getDescription());
		}
		if (entity.getSortby() == null || (vo.getSortby().intValue() != entity.getSortby().intValue())) {
			entity.setSortby(vo.getSortby());
		}

		departmentRepository.save(entity);
	}

	public void delete(Long id) throws Exception {
		Department entity = departmentRepository.findById(id);
		if (entity == null) {
			throw new Exception("没有找到此部门");
		}

		//
//		List<RoleDepartment> list = roleDepartmentRepository.findByDepartmentId(id);
//		roleDepartmentRepository.delete(list);

		//
		List<Department> childList = new ArrayList<Department>();
		findChild(childList, id);

		departmentRepository.delete(childList);
		departmentRepository.delete(id);
	}

	private void findChild(List<Department> deptList, Long parentId) {
		List<Department> children = departmentRepository.findByParentId(parentId);
		if (children == null || children.size() <= 0) {
			return;
		}

		deptList.addAll(children);
		for (Department p : children) {
			findChild(deptList, p.getId());
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
			if ("name".equalsIgnoreCase(columnName) && StringUtils.isNotEmpty(value)) {
				sql.append(" and name like ").append("'%").append(value).append("%'");
			} else if ("description".equalsIgnoreCase(columnName) && StringUtils.isNotEmpty(value)) {
				sql.append(" and description like ").append("'%").append(value).append("%'");
			} else if ("code".equalsIgnoreCase(columnName)) {
				sql.append(" and code like ").append("'").append(value).append("%'");
			} 
		}
	}

	private void addSort(StringBuffer sql, String sort) {
		if (StringUtils.isEmpty(sort)) {
			sql.append(" order by sortby ");
			return;
		}
	}

	private void setLevel(List<DepartmentVo> list) {

		if (list == null || list.size() <= 0) {
			return;
		}

		List<LevelVo> levelList = findLevel();
		for (DepartmentVo pvo : list) {
			for (LevelVo lvo : levelList) {
				if (pvo.getId().longValue() == lvo.getId().longValue()) {
					pvo.setLevel(lvo.getLevel());
				}
			}
		}
	}

	private List<LevelVo> findLevel() {
		String sql = "SELECT id AS ID,parent_id ,levels AS level, paths AS path\n" +
				"FROM ( SELECT\n" +
				"         id,\n" +
				"         parent_id,\n" +
				"         @le:= IF (parent_id = 0 ,0,    IF( LOCATE( CONCAT('|',parent_id,':'),@pathlevel)  > 0 ,         SUBSTRING_INDEX( SUBSTRING_INDEX(@pathlevel,CONCAT('|',parent_id,':'),-1),'|',1) +1  ,@le+1) ) levels ,\n" +
				"         @pathlevel:= CONCAT(@pathlevel,'|',id,':', @le ,'|') pathlevel ,\n" +
				"         @pathnodes:= IF( parent_id =0,'0',     CONCAT_WS(',',    IF( LOCATE( CONCAT('|',parent_id,':'),@pathall) > 0 ,       SUBSTRING_INDEX( SUBSTRING_INDEX(@pathall,CONCAT('|',parent_id,':'),-1),'|',1)     ,@pathnodes ) ,parent_id ) )paths ,\n" +
				"         @pathall:=CONCAT(@pathall,'|',id,':', @pathnodes ,'|') pathall\n" +
				"       FROM t_sys_department, (SELECT @le:=0,@pathlevel:='', @pathall:='',@pathnodes:='') vv\n" +
				"       ORDER BY CODE ) src";

		return jdbcTemplate.query(sql, new LevelRowMapper());
	}

}
