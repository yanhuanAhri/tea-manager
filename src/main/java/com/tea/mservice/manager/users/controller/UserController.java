package com.tea.mservice.manager.users.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.manager.department.service.DepartmentService;
import com.tea.mservice.manager.department.vo.DepartmentVo;
import com.tea.mservice.manager.entity.User;
import com.tea.mservice.manager.users.service.UserService;
import com.tea.mservice.manager.users.vo.UserVo;
import com.tea.mservice.portal.common.ApiData;

@Configuration
@Controller
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	DepartmentService departmentService;

	@RequestMapping(value = "users.html", method = RequestMethod.GET)
	public String goList(ModelMap map) {
		return "users/list";
	}

	@RequestMapping(value = "users/add.html", method = RequestMethod.GET)
	public String goAdd(ModelMap map) {
		return "users/add";
	}

	@RequestMapping(value = "users/resetPassword.html", method = RequestMethod.GET)
	public String toRestPassword(ModelMap map) {
		return "users/reset-password";
	}

	@RequestMapping(value = "users/edit.html", method = RequestMethod.GET)
	public String goEdit(ModelMap map) {
		return "users/edit";
	}

	@RequestMapping(value = "users/confirm.html", method = RequestMethod.GET)
	public String goConfirm(ModelMap map) {
		return "users/confirm";
	}

	@RequestMapping(value = "users/department.html", method = RequestMethod.GET)
	public String goDepartment(ModelMap map) {
		return "users/department";
	}

	@RequestMapping(value = "users/{id}", method = RequestMethod.GET)
	@ResponseBody
	public UserVo getUser(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		User entity = userService.findById(id);
		UserVo vo = new UserVo();
		vo.setId(entity.getId());
		vo.setLoginName(entity.getLoginName());
		vo.setName(entity.getName());
		vo.setEmail(entity.getEmail());
		vo.setMobile(entity.getMobile());
		vo.setStatus(entity.getStatus());
		vo.setDeptId(entity.getDeptId());
		return vo;
	}

	@RequestMapping(value = "users/pages/{pageNumber}", method = RequestMethod.GET)
	@ResponseBody
	public ApiData<UserVo> page(@PathVariable(name = "pageNumber", required = true) Integer pageNumber,
								@RequestParam(name = "pageSize", required = true, defaultValue = "25") Integer pageSize,
								@RequestParam(name = "filter", required = false) String filter,
								@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
								HttpServletResponse response, ModelMap map) {

		try {
			return userService.pages(pageNumber, pageSize, filter, sort);
		} catch (Exception e) {
			LOG.error(e.getMessage());

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}

		return null;
	}

	@RequestMapping(value = "users/{userId}/roles", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listUserRole(@PathVariable(name = "userId", required = true) Long userId,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		try {
			return userService.listUserRoles(userId);
		} catch (Exception e) {
			LOG.error(e.getMessage());

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}

		return null;
	}
	
	@RequestMapping(value = "users/{id}/passwords", method = RequestMethod.PUT)
	@ResponseBody
	public void resetPassword(@RequestBody UserVo vo, HttpServletResponse response, ModelMap map) {

		try {
			userService.resetPassword(vo, false);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "users", method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody UserVo vo, HttpServletResponse response, ModelMap map) {

		try {
			userService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "users/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody UserVo vo, HttpServletResponse response, ModelMap map) {

		try {
			userService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "users/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(name = "id") Long id, HttpServletResponse response, ModelMap map) {

		try {
			userService.delete(id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			response.setStatus(503);

			try {
				response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "users/{id}/department", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findUserDepartment(@PathVariable Long id) {
		return userService.findDepartmentList(id);
	}

	@RequestMapping(value = "users/{id}/department", method = RequestMethod.POST)
	@ResponseBody
	public void saveUserDepartment(@PathVariable Long id,
							   @RequestBody List<Long> departmentIds) {
		userService.saveUserDepartment(id, departmentIds);
	}

	/**
	 * 我的组织
	 * @return
	 */
	@RequestMapping(value = "users/department", method = RequestMethod.GET)
	@ResponseBody
	public List<DepartmentVo> findMyDepartment() {
		return departmentService.findMyUserDepartments();
	}
}
