package com.tea.mservice.core.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.tea.mservice.manager.log.service.LogTasks;
import com.tea.mservice.manager.permission.service.PermissionService;
import com.tea.mservice.manager.permission.vo.PermissionVo;

public class URLPermissionFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(URLPermissionFilter.class);

	private static final List<String> FILE_TYPE = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".ico", ".html",
			".css", ".map", ".js", ".woff", ".woff2", ".ttf", ".xls", ".xlsx", ".doc", ".docx", ".txt", ".ppt",
			".pptx");
	private static final List<String> PASS_PATH = Arrays.asList("/", "/login", "/menus");

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private LogTasks logTasks;

	@Value("${spring.profiles.active}")
	private String profile;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		String servletPath = req.getServletPath();
		String method = req.getMethod();

		if (isPass(servletPath, method) || isStaticResource(servletPath, method)) {
			chain.doFilter(request, response);
			return;
		}

		if (isAuth(req, servletPath, method)) {
			chain.doFilter(request, response);
			return;
		}

		throw new UnauthorizedException("此操作未授权," + method + "-" + servletPath);
	}

	@Override
	public void destroy() {

	}

	@SuppressWarnings("unchecked")
	private boolean isAuth(HttpServletRequest req, String path, String method) {

		Object accountObj = SecurityUtils.getSubject().getPrincipal();
		if (accountObj == null) {
			return true;
		}

		if ("get".equalsIgnoreCase(method) && "/permission/buttons/codes".equalsIgnoreCase(path)) {
			return true;
		}

		List<PermissionVo> list = null;
		List<PermissionVo> listAll = null;
		Object perList = req.getSession().getAttribute("current_user_permission_list");
		Object perListAll = req.getSession().getAttribute("current_user_permission_list_all");
		if (("dev".equals(profile) || "test".equals(profile)) || perList == null) {
			String account = accountObj.toString();
			list = permissionService.findPermissionByUser(account, "all");
			listAll = permissionService.findPermissionAll();
			req.getSession().setAttribute("current_user_permission_list", list);
			req.getSession().setAttribute("current_user_permission_list_all", listAll);
		} else {
			list = (List<PermissionVo>) perList;
			listAll = (List<PermissionVo>) perListAll;
		}

		//
		for (PermissionVo vo : list) {
			if (!method.equalsIgnoreCase(vo.getMethod())) {
				continue;
			}

			if (StringUtils.isEmpty(vo.getUrl())) {
				continue;
			}

			String urlPattern = replace(vo.getUrl());
			boolean isMatch = Pattern.matches(urlPattern, path);
			//LOG.debug("------> permUrl = {}, urlPattern={}, reqPath ={}, isMatch={}", vo.getUrl(), urlPattern, path, isMatch);
			if (isMatch) {
				try {
					logTasks.recordFilter(listAll, vo, path, method);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
				return true;
			}
		}

		return false;
	}

	private String replace(String url) {
		if (StringUtils.isEmpty(url)) {
			return url;
		}

		int start = url.indexOf("{", 0);
		if (start <= 0) {
			return url;
		}

		int end = url.indexOf("}", start);
		try {
			String repStr = url.substring(start, end + 1);
			url = url.replace(repStr, "\\w+");
			return replace(url);
		} catch (Exception e) {
			LOG.error("URL format invalid {}", url);
			LOG.error(e.getMessage(), e);
		}

		return url;
	}

	private boolean isPass(String path, String method) {
		if (PASS_PATH.contains(path)) {
			return true;
		}

		return false;
	}

	private boolean isStaticResource(String path, String method) {
		if (StringUtils.isEmpty(path)) {
			return false;
		}

		int start = path.lastIndexOf(".");
		if (start <= -1) {
			return false;
		}
		String suffix = path.substring(start, path.length()).toLowerCase();

		if (FILE_TYPE.contains(suffix) && "get".equalsIgnoreCase(method)) {
			return true;
		}

		return false;
	}
}
