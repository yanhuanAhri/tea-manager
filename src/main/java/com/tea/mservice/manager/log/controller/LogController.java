package com.tea.mservice.manager.log.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.manager.log.service.LogService;

@Configuration
@Controller
public class LogController {
	private static final Logger LOG = LoggerFactory.getLogger(LogController.class);
	@Autowired
	private LogService logService;

	@RequestMapping(value = "log.html", method = RequestMethod.GET)
	public String goList(ModelMap map) {
		return "log/list";
	}

	@RequestMapping(value = "log/confirm.html", method = RequestMethod.GET)
	public String goConfirm(ModelMap map) {
		return "log/confirm";
	}

	@RequestMapping(value = "log/pages/{pageNumber}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> page(@PathVariable(name = "pageNumber", required = true) Integer pageNumber,
			@RequestParam(name = "pageSize", required = true, defaultValue = "25") Integer pageSize,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		return logService.findLogList(pageNumber, pageSize, filter, sort);
	}

	@RequestMapping(value = "log/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(name = "id") Long id, HttpServletResponse response, ModelMap map) {
		try {
			logService.delete(id);
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
}
