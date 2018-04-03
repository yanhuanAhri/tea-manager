package com.tea.mservice.manager.index;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tea.mservice.config.shiro.UserRealm.ShiroUser;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.manager.entity.Log;
import com.tea.mservice.manager.log.service.LogTasks;

@Controller
public class LoginController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	/**
	 * Go login.jsp
	 * 
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	/**
	 * Go login
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, RedirectAttributes rediect) {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		String addr = request.getRemoteAddr();
		String ua = request.getHeader("User-Agent");
		
		UsernamePasswordToken upt = new UsernamePasswordToken(account, password);
		
		if("on".equals(rememberMe)) {
			upt.setRememberMe(true);
		}
		
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(upt);
			addLog("登录", "登录系统", "/login, RemoteAddr=" + addr + ", User Agent=" + ua);
			ShiroUser user = ShiroUserUtil.getUser();
			request.getSession().setAttribute("SHIRO_USER_OBJ", user);
		} catch(IncorrectCredentialsException e) {
			LOG.error(e.getMessage(), e);
			
			rediect.addFlashAttribute("errorText", "用户名或者密码错误");
			return "redirect:/login";
		} catch (AuthenticationException e) {
			LOG.error(e.getMessage(), e);
			
			rediect.addFlashAttribute("errorText", e.getMessage());
			return "redirect:/login";
		}
		return "redirect:/";
	}

	/**
	 * Exit
	 * 
	 * @return
	 */
	@RequestMapping("logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		addLog("登出", "登出系统", "/logout");
		return "redirect:/";
	}
	
	private void addLog(String module, String operate, String content) {
		try {
			ShiroUser user = ShiroUserUtil.getUser();
			if(user == null) {
				return;
			}
			Log log = new Log();
			log.setUserId(user.id);
			log.setUserName(user.name);
			log.setCrTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
			log.setModule(module);
			log.setOperate(operate);
			log.setContent(content);
			LogTasks.add(log);

			LOG.info("id={}, login name={}, name={}, module={}, operate={}, content={}", user.id, user.loginName,
					user.name, module, operate, content);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
