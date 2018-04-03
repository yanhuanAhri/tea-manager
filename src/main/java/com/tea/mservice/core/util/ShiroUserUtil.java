package com.tea.mservice.core.util;

import org.apache.shiro.SecurityUtils;

import com.tea.mservice.config.shiro.UserRealm.ShiroUser;

public class ShiroUserUtil {
	public static ShiroUser getUser() {
		return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	}
}
