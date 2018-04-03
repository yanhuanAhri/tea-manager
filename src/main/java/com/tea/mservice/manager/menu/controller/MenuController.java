package com.tea.mservice.manager.menu.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.manager.menu.service.MenuService;

@Configuration
@Controller
public class MenuController {

	private static final Logger LOG = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuService menuService;

	@RequestMapping("/menus")
	@ResponseBody
	public List<Map<String, Object>> list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		try {
			Subject subject = SecurityUtils.getSubject();
			String account = subject.getPrincipal().toString();
			LOG.info("user {} is login", account);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}

		List<Map<String, Object>> list = menuService.createMenus();
		return list;
	}
}
