package com.tea.mservice.manager.params.controller;

import java.io.IOException;
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

import com.tea.mservice.manager.entity.Param;
import com.tea.mservice.manager.params.service.ParamService;
import com.tea.mservice.manager.params.vo.ParamVo;

@Configuration
@Controller
public class ParamController {

	private static final Logger LOG = LoggerFactory.getLogger(ParamController.class);

	@Autowired
	private ParamService paramService;

	@RequestMapping(value = "param.html", method = RequestMethod.GET)
	public String goParam(ModelMap map) {
		return "param/list";
	}

	@RequestMapping(value = "param/edit.html", method = RequestMethod.GET)
	public String goEdit(ModelMap map) {
		return "param/edit";
	}
	
	@RequestMapping(value = "param/confirm.html", method = RequestMethod.GET)
	public String goConfirm(ModelMap map) {
		return "param/confirm";
	}

	@RequestMapping(value = "param/pages/{pageNumber}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> page(@PathVariable(name = "pageNumber", required = true) Integer pageNumber,
			@RequestParam(name = "pageSize", required = true, defaultValue = "25") Integer pageSize,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return paramService.pages(pageNumber, pageSize, filter, sort);
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

	@RequestMapping(value = "param/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Param get(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			return paramService.get(id);
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

	@RequestMapping(value = "param", method = RequestMethod.POST)
	@ResponseBody
	public void create(@RequestBody ParamVo vo, HttpServletRequest request, HttpServletResponse response,
			ModelMap map) {

		try {
			paramService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "param/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody ParamVo vo, HttpServletRequest request, HttpServletResponse response,
			ModelMap map) {

		try {
			paramService.save(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}

	@RequestMapping(value = "param/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(name = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			paramService.delete(id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}
	
	@RequestMapping(value = "param/synch", method = RequestMethod.GET)
	@ResponseBody
	public void synch( HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		try {
			paramService.synch();
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
