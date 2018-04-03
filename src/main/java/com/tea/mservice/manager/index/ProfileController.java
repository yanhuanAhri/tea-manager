package com.tea.mservice.manager.index;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.config.shiro.UserRealm.ShiroUser;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.manager.entity.User;
import com.tea.mservice.manager.users.service.UserService;
import com.tea.mservice.manager.users.vo.UserVo;

@Configuration
@Controller
public class ProfileController {
	private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "profile.html", method = RequestMethod.GET)
	public String goProfile(ModelMap map) {
		return "profile/list";
	}
	
	@RequestMapping(value = "profile/pwd.html", method = RequestMethod.GET)
	public String goProfilePwd(ModelMap map) {
		return "profile/pwd";
	}
	
	@RequestMapping(value = "profile/avatar.html", method = RequestMethod.GET)
	public String goProfileAvatar(ModelMap map) {
		return "profile/avatar";
	}

	@RequestMapping(value = "profile", method = RequestMethod.GET)
	@ResponseBody
	public UserVo getUser(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		ShiroUser user = ShiroUserUtil.getUser();
		User entity = userService.findById(user.id);
		UserVo vo = new UserVo();
		vo.setId(entity.getId());
		vo.setLoginName(entity.getLoginName());
		vo.setName(entity.getName());
		vo.setEmail(entity.getEmail());
		vo.setMobile(entity.getMobile());
		vo.setStatus(entity.getStatus());

		return vo;
	}

	@RequestMapping(value = "profile", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody UserVo vo, HttpServletResponse response, ModelMap map) {

		try {
			ShiroUser user = ShiroUserUtil.getUser();
			vo.setId(user.id);
			vo.setStatus((short)1);
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
	
	@RequestMapping(value = "profile/password", method = RequestMethod.PUT)
	@ResponseBody
	public void updatePwd(@RequestBody UserVo vo, HttpServletResponse response, ModelMap map) {

		try {
			ShiroUser user = ShiroUserUtil.getUser();
			vo.setId(user.id);
			userService.resetPassword(vo, true);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}
	
	@RequestMapping(value = "profile/avatar", method = RequestMethod.POST)
	public void saveAvatar (@RequestBody String data, HttpServletResponse response, ModelMap map) {

		try {
			ShiroUser user = ShiroUserUtil.getUser();
			userService.saveAavtar(data, user.id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			try {
				response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e1);
			}
		}
	}
	
	@RequestMapping(value = "profile/avatar", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAvatar (HttpServletResponse response, ModelMap map) {
		
		try {
			ShiroUser user = ShiroUserUtil.getUser();
			return userService.getAavtar(user.id);
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
}
