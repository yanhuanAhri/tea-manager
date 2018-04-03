package com.tea.mservice.manager.users.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tea.mservice.config.shiro.UserRealm;
import com.tea.mservice.core.util.Digests;
import com.tea.mservice.core.util.Encodes;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.manager.department.repository.DepartmentRepository;
import com.tea.mservice.manager.department.service.DepartmentService;
import com.tea.mservice.manager.department.vo.DepartmentVo;
import com.tea.mservice.manager.entity.Department;
import com.tea.mservice.manager.entity.User;
import com.tea.mservice.manager.entity.UserDepartment;
import com.tea.mservice.manager.entity.UserRole;
import com.tea.mservice.manager.users.repository.UserDepartmentRepository;
import com.tea.mservice.manager.users.repository.UserRepository;
import com.tea.mservice.manager.users.repository.UserRoleRepository;
import com.tea.mservice.manager.users.vo.UserRoleVo;
import com.tea.mservice.manager.users.vo.UserVo;
import com.tea.mservice.portal.common.ApiData;
import com.tea.mservice.portal.common.CommonService;
import com.tea.mservice.portal.common.SQLUtils;

import net.sf.json.JSONObject;

@Service
public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserDepartmentRepository userDepartmentRepository;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private CommonService commonService;

	public User findById(Long id) {
		return userRepository.findById(id);
	}

	public User findByAccount(String account) {
		User user = null;
		try {
			user = userRepository.findByLoginName(account);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		if (user == null) {
			return null;
		}

		try {
			user.setRoleList(findUserRoles(user.getId()));
			user.setOrgIds(findUserOrgIds(user.getId()));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return user;
	}

	public User findByMobile(String mobile) {
		User user = userRepository.findByMobile(mobile);
		if (user == null) {
			return null;
		}

		try {
			user.setRoleList(findUserRoles(user.getId()));
			user.setOrgIds(findUserOrgIds(user.getId()));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return user;
	}

	public void save(UserVo vo) throws Exception {
		User userEntity = userRepository.findByLoginName(vo.getLoginName());
		if (userEntity != null && !Objects.equals(vo.getId(), userEntity.getId())) {
			throw new Exception("存在相同登录名的用户");
		}
		if (vo.getId() == null) {
			User entity = new User();
			entity.setLoginName(vo.getLoginName());
			entity.setName(vo.getName());
			entity.setMobile(vo.getMobile());
			entity.setEmail(vo.getEmail());
			entity.setStatus((short) 1);
			entity.setWrongNumber(0);
			entity.setDeptId(vo.getDeptId());

			String createTime = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
			entity.setCreateTime(createTime);

			byte[] salt = Digests.generateSalt(SALT_SIZE);
			entity.setSalt(Encodes.encodeHex(salt));

			byte[] hashPassword = Digests.sha1(vo.getConfirmPassword().getBytes(), salt, HASH_INTERATIONS);
			entity.setPassword(Encodes.encodeHex(hashPassword));
			userRepository.save(entity);
			
			saveUserRoleRef(vo, entity.getId());
			return;
		}

		User entity = userRepository.findById(vo.getId());

		if (StringUtils.isNotEmpty(vo.getLoginName())) {
			if ("admin".equalsIgnoreCase(entity.getLoginName()) && !StringUtils.equals(vo.getLoginName(), entity.getLoginName())) {
				throw new Exception("不能修改admin的登录名");
			}
			entity.setLoginName(vo.getLoginName());
		}
		if (StringUtils.isNotEmpty(vo.getName())) {
			entity.setName(vo.getName());
		}
		if (StringUtils.isNotEmpty(vo.getEmail())) {
			entity.setEmail(vo.getEmail());
		}
		if (StringUtils.isNotEmpty(vo.getMobile())) {
			entity.setMobile(vo.getMobile());
		}
		if (vo.getStatus() != null) {
			entity.setStatus(vo.getStatus());
		}
		if (vo.getDeptId() != null) {
			entity.setDeptId(vo.getDeptId());
		}

		userRepository.save(entity);
		
		saveUserRoleRef(vo, vo.getId());
	}
	
	private void saveUserRoleRef(UserVo vo, Long userId) {
		Long[] roleIds = vo.getRoles();
		if(userId == null || roleIds == null || roleIds.length <=0 ) {
			return;
		}
		
		List<UserRole> uRoleList = userRoleRepository.findByUserId(userId);
		if(uRoleList != null && uRoleList.size() > 0) {
			userRoleRepository.delete(uRoleList);
		}
		
		List<UserRole> urList = new ArrayList<UserRole>();
		for(Long roleId : roleIds) {
			UserRole ur = new UserRole();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			urList.add(ur);
		}
		
		userRoleRepository.save(urList);
	}

	public void resetPassword(UserVo vo, boolean checked) throws Exception {

		User entity = userRepository.findById(vo.getId());
		if (entity == null) {
			throw new Exception("没有找到此用户 ");
		}

		if (checked) {
			byte[] inputHashPwd = Digests.sha1(vo.getPassword().getBytes(), Encodes.decodeHex(entity.getSalt()), HASH_INTERATIONS);
			String inputPwd = Encodes.encodeHex(inputHashPwd);
			
			if(!StringUtils.equals(inputPwd, entity.getPassword())) {
				throw new Exception("原密码输入不正确，请重新输入");
			}
		}

		byte[] salt = Digests.generateSalt(SALT_SIZE);
		entity.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(vo.getConfirmPassword().getBytes(), salt, HASH_INTERATIONS);
		entity.setPassword(Encodes.encodeHex(hashPassword));

		userRepository.save(entity);
	}

	public void delete(Long id) throws Exception {
		User entity = userRepository.findById(id);
		if (entity == null) {
			throw new Exception("没有找到此用户");
		}

		if ("admin".equalsIgnoreCase(entity.getLoginName())) {
			throw new Exception("此用户不允许删除");
		}

		List<UserRole> uRoleList = userRoleRepository.findByUserId(entity.getId());
		if(uRoleList != null && uRoleList.size() > 0) {
			userRoleRepository.delete(uRoleList);
		}
		
		userRepository.delete(entity);
	}

	private UserVo convert(String filter) {
		if (StringUtils.isEmpty(filter))
			return null;
		try {
			return (UserVo) JSONObject.toBean(JSONObject.fromObject(filter), UserVo.class);
		} catch (Exception e) {
			return null;
		}
	}

	public ApiData<UserVo> pages(Integer pageNumber, Integer pageSize, String filter, String sorts) {
		UserVo voFilter = convert(filter);
		ApiData<UserVo> map = new ApiData<>();
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		String columns = "select tsu.id, tsu.dept_id, tsu.login_name, tsu.name, tsu.email, tsu.mobile, tsu.create_time, tsu.status\n" +
				"FROM t_sys_user tsu\n";
		String count = "select count(1) FROM t_sys_user tsu\n";
		String offsets = " LIMIT ? OFFSET ?";
		StringBuilder where = new StringBuilder("where status !=99\n");
		List<Object> args = new ArrayList<>();
		if (voFilter != null) {
			if (StringUtils.isNotEmpty(voFilter.getLoginName())) {
				where.append(" AND tsu.login_name LIKE ?\n");
				args.add("%" + voFilter.getLoginName() + "%");
			}
			if (StringUtils.isNotEmpty(voFilter.getName())) {
				where.append(" AND tsu.name LIKE ?\n");
				args.add("%" + voFilter.getName() + "%");
			}
			if (StringUtils.isNotEmpty(voFilter.getEmail())) {
				where.append(" AND tsu.email LIKE ?\n");
				args.add("%" + voFilter.getEmail() + "%");
			}
			if (StringUtils.isNotEmpty(voFilter.getMobile())) {
				where.append(" AND tsu.mobile LIKE ?\n");
				args.add("%" + voFilter.getMobile() + "%");
			}
			if (voFilter.getStatus() != null) {
				where.append(" AND tsu.status = ?\n");
				args.add(voFilter.getStatus());
			}
			if (voFilter.getDeptId() != null) {
				List<Long> subOrgs = commonService.getAllOrgs(voFilter.getDeptId(), user.orgIdList);
				if (subOrgs.isEmpty()) {
					return map;
				}
				where
						.append(" AND dept_id IN ")
						.append(SQLUtils.foreachIn(subOrgs.size()));
				args.addAll(subOrgs);
			}
			if (voFilter.getRoleId() != null) {
				List<UserRole> userRoles = userRoleRepository.findByRoleId(voFilter.getRoleId());
				if (userRoles.isEmpty()) {
					return map;
				}
				where.append(" AND tsu.id IN ").append(SQLUtils.foreachIn(userRoles.size()));
				args.addAll(userRoles.stream().map(UserRole::getUserId).collect(Collectors.toList()));
			}
		}
		Long total  = jdbcTemplate.queryForObject(count + where, Long.class, args.toArray());
		if (total == 0) {
			return map;
		}
		where.append("ORDER BY ").append(StringUtils.isNotEmpty(sorts) ? sorts : "tsu.name").append("\n").append(offsets);
		args.add(pageSize);
		args.add(pageNumber * pageSize);
		List<UserVo> data = jdbcTemplate.query(columns + where, new BeanPropertyRowMapper<>(UserVo.class), args.toArray());

		List<DepartmentVo> departmentVos = departmentService.findMyUserDepartments();
		List<Long> userIds = data.stream().map(UserVo::getId).collect(Collectors.toList());
		String roleSql = "SELECT tsur.USER_ID AS userId, tsur.ROLE_ID AS roleId, tsr.NAME AS roleName FROM t_sys_user_role tsur\n"
				+ "INNER JOIN t_sys_role tsr ON tsur.ROLE_ID = tsr.ID WHERE tsur.USER_ID IN \n"
				+ SQLUtils.foreachIn(userIds.size());
		List<UserRoleVo> userRoleVos = jdbcTemplate.query(roleSql, new BeanPropertyRowMapper<>(UserRoleVo.class), userIds.toArray());
		data.forEach(userVo -> {
			for (UserRoleVo userRoleVo : userRoleVos) {
				if (Objects.equals(userRoleVo.getUserId(), userVo.getId())) {
					userVo.getRoleNames().add(userRoleVo.getRoleName());
				}
			}

			for (DepartmentVo departmentVo : departmentVos) {
				if (Objects.equals(departmentVo.getId(), userVo.getDeptId())) {
					userVo.setDeptName(departmentVo.getName());
					break;
				}
			}
		});
		map.setData(data);
		map.setTotal(total);
		return map;
	}

	public List<Map<String, Object>> listUserRoles(Long userId) {
		String sql = "select  r.id, r.name from t_sys_user u left join t_sys_user_role ref on ref.user_id = u.id left join t_sys_role r on ref.role_id = r.id where u.id = ?";
		return jdbcTemplate.queryForList(sql, userId);
	}

	public void saveAavtar(String data, Long userId) {
		String sql = "update t_sys_user set avatar = ? where id = ?";
		JSONObject obj = JSONObject.fromObject(data);
		String imgBase64 = obj.getString("data");
		jdbcTemplate.update(sql, imgBase64, userId);
	}

	public Map<String, Object> getAavtar(Long userId) {
		String sql = "select avatar as data from t_sys_user where id = ?";
		return jdbcTemplate.queryForMap(sql, userId);
	}

	private List<String> findUserRoles(Long id) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select r.code from t_sys_user_role u left join t_sys_role r on r.id = u.role_id where u.user_id = ?");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), id);
		if (list == null || list.size() <= 0) {
			return null;
		}

		List<String> codeList = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			Object codeObj = map.get("code");
			if (codeObj == null) {
				continue;
			}
			codeList.add(codeObj.toString());
		}
		return codeList;
	}

	private List<Long> findUserOrgIds(Long id) throws Exception {
		return jdbcTemplate.queryForList("SELECT tsud.dep_id FROM t_sys_user_department tsud WHERE tsud.user_id = ?", Long.class, id);
	}

	/**
	 * 查看用户的数据权限
	 * @param id
	 * @return
	 */
	public Map<String, Object> findDepartmentList(Long id) {
		User user = userRepository.findOne(id);
		if (user == null)
			throw new RuntimeException("请选择用户");
		List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(id);
		List<Long> selectedDepartmentIds = userDepartments
				.stream()
				.map(UserDepartment::getDepId)
				.collect(Collectors.toList());
		return departmentService.findDepartmentSelectTree(selectedDepartmentIds);
	}

	/**
	 * 保存用户数据权限
	 * @param id
	 * @param departmentIds
	 */
	public void saveUserDepartment(Long id, List<Long> departmentIds) {
		User user = userRepository.findOne(id);
		if (user == null) {
			return;
		}
		userDepartmentRepository.deleteByUserId(id);
		jdbcTemplate.batchUpdate("INSERT INTO t_sys_user_department (dep_id, user_id) VALUES (?,?)",
				departmentIds,
				departmentIds.size(),
				(preparedStatement, aLong) -> {
					preparedStatement.setLong(1, aLong);
					preparedStatement.setLong(2, id);
		});

	}

	/**
	 * 我的组织
	 * @return
	 */
	public List findMyDepartment() {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		Department myDept = departmentRepository.findById(user.deptId);
		List<Department> myDepts = departmentRepository.findByCodeLike(myDept.getCode() + "%");
		return myDepts
				.stream()
				.map(department -> {
					Map<String, Object> vo = new HashMap<>(2);
					vo.put("id", department.getId());
					vo.put("name", department.getName());
					vo.put("code", department.getCode());
					return vo;
				})
				.collect(Collectors.toList());

	}
}