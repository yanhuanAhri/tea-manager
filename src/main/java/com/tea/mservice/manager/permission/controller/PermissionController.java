package com.tea.mservice.manager.permission.controller;

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

import com.tea.mservice.manager.permission.service.PermissionService;
import com.tea.mservice.manager.permission.vo.PermissionVo;

@Configuration
@Controller
public class PermissionController {

	private static final Logger LOG = LoggerFactory.getLogger(PermissionController.class);

	@Autowired
	private PermissionService permissionService;

	@RequestMapping(value = "permission.html", method = RequestMethod.GET)
	public String goRole(ModelMap map) {
		return "permission/list";
	}

	@RequestMapping(value = "permission/add.html", method = RequestMethod.GET)
	public String goAdd(ModelMap map) {
		return "permission/add";
	}

	@RequestMapping(value = "permission/edit.html", method = RequestMethod.GET)
	public String goEdit(ModelMap map) {
		return "permission/edit";
	}

	@RequestMapping(value = "permission/confirm.html", method = RequestMethod.GET)
	public String goConfirm(ModelMap map) {
		return "permission/confirm";
	}

	@RequestMapping(value = "permission/pages", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> page(@RequestParam(name = "parentId", required = false) Long parentId,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return permissionService.pages(parentId, filter, sort);
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

	@RequestMapping(value = "permission/buttons/codes", method = RequestMethod.GET)
	@ResponseBody
	public List<String> buttonsPermission(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		String account = SecurityUtils.getSubject().getPrincipal().toString();
		try {
			List<PermissionVo> list = permissionService.findPermissionByUser(account, "button");

			if (list == null || list.size() <= 0) {
				return null;
			}

			List<String> codeList = new ArrayList<String>();
			for (PermissionVo vo : list) {
				String code = vo.getCode();
				if (StringUtils.isEmpty(code)) {
					continue;
				}
				codeList.add(code);
			}

			return codeList;

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

	@RequestMapping(value = "permission/{id}", method = RequestMethod.GET)
	@ResponseBody
	public PermissionVo get(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return permissionService.get(id);
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

	@RequestMapping(value = "permission", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(@RequestBody PermissionVo vo, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			permissionService.save(vo);
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

	@RequestMapping(value = "permission/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody PermissionVo vo, HttpServletRequest request, HttpServletResponse response,
			ModelMap map) {

		try {
			permissionService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "permission/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			permissionService.delete(id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "permission/roles/{roleId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listAll(@PathVariable(name = "roleId", required = true) Long roleId,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return permissionService.listAll(filter, sort, roleId);
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

	@RequestMapping(value = "permission/roles/{roleId}", method = RequestMethod.POST)
	@ResponseBody
	public void rolePermission(@PathVariable(name = "roleId", required = true) Long roleId,
			@RequestBody Long[] permissionIds, HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		try {
			permissionService.saveRolePermissions(roleId, permissionIds);
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
