package com.tea.mservice.manager.roles.controller;

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

import com.tea.mservice.manager.entity.Role;
import com.tea.mservice.manager.roles.service.RoleService;
import com.tea.mservice.manager.roles.vo.RoleVo;

@Configuration
@Controller
public class RoleController {

	private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "roles.html", method = RequestMethod.GET)
	public String goRole(ModelMap map) {
		return "roles/list";
	}

	@RequestMapping(value = "roles/add.html", method = RequestMethod.GET)
	public String goAdd(ModelMap map) {
		return "roles/add";
	}

	@RequestMapping(value = "roles/edit.html", method = RequestMethod.GET)
	public String goEdit(ModelMap map) {
		return "roles/edit";
	}

	@RequestMapping(value = "roles/confirm.html", method = RequestMethod.GET)
	public String goConfirm(ModelMap map) {
		return "roles/confirm";
	}

	@RequestMapping(value = "roles/permission.html", method = RequestMethod.GET)
	public String goPermission(ModelMap map) {
		return "roles/permission";
	}

	/*@RequestMapping(value = "roles/department.html", method = RequestMethod.GET)
	public String goDepartment(ModelMap map) {
		return "roles/department";
	}*/

	@RequestMapping(value = "roles/pages/{pageNumber}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> page(@PathVariable(name = "pageNumber", required = true) Integer pageNumber,
			@RequestParam(name = "pageSize", required = true, defaultValue = "25") Integer pageSize,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return roleService.pages(pageNumber, pageSize, filter, sort);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
		return null;
	}

	@RequestMapping(value = "roles", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listAll(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		try {
			return roleService.listAll();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
		return null;
	}

	@RequestMapping(value = "roles/{id}", method = RequestMethod.GET)
	@ResponseBody
	public RoleVo get(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			Role entity = roleService.get(id);
			RoleVo vo = new RoleVo();
			vo.setId(entity.getId());
			vo.setName(entity.getName());
			vo.setCode(entity.getCode());
			vo.setDescription(entity.getDescription());
			return vo;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}

		return null;
	}

	@RequestMapping(value = "roles", method = RequestMethod.POST)
	@ResponseBody
	public void create(@RequestBody RoleVo vo, HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		try {
			roleService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "roles/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody RoleVo vo, HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		try {
			roleService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "roles/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			roleService.delete(id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}
}
