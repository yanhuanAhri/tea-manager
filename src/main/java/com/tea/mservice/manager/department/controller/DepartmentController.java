package com.tea.mservice.manager.department.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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


@Configuration
@Controller
public class DepartmentController {

	private static final Logger LOG = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping(value = "department.html", method = RequestMethod.GET)
	public String goDepartment(ModelMap map) {
		return "department/list";
	}

	@RequestMapping(value = "department/add.html", method = RequestMethod.GET)
	public String goAdd(ModelMap map) {
		return "department/add";
	}

	@RequestMapping(value = "department/edit.html", method = RequestMethod.GET)
	public String goEdit(ModelMap map) {
		return "department/edit";
	}

	@RequestMapping(value = "department/confirm.html", method = RequestMethod.GET)
	public String goConfirm(ModelMap map) {
		return "department/confirm";
	}

	@RequestMapping(value = "department/pages", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> page(@RequestParam(name = "parentId", required = false) Long parentId,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return departmentService.pages(parentId, filter, sort);
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

	@RequestMapping(value = "department/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DepartmentVo get(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return departmentService.get(id);
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

	@RequestMapping(value = "department", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(@RequestBody DepartmentVo vo, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			departmentService.save(vo);
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

	@RequestMapping(value = "department/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody DepartmentVo vo, HttpServletRequest request, HttpServletResponse response,
			ModelMap map) {

		try {
			departmentService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "department/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			departmentService.delete(id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	/*@RequestMapping(value = "department/roles/{roleId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listAll(@PathVariable(name = "roleId", required = true) Long roleId,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return departmentService.listAll(filter, sort, roleId);
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

	@RequestMapping(value = "department/roles/{roleId}", method = RequestMethod.POST)
	@ResponseBody
	public void roleDepartment(@PathVariable(name = "roleId", required = true) Long roleId,
			@RequestBody Long[] departmentIds, HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		try {
			departmentService.saveRoleDepartments(roleId, departmentIds);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}*/
}
