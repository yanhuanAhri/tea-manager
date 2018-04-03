package com.tea.mservice;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import com.tea.mservice.core.filter.URLPermissionFilter;

@Configurable
@SpringBootApplication
public class ManagerServiceApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ManagerServiceApplication.class).web(true).run(args);
	}
	
	@Bean
	public URLPermissionFilter urlPermssionFilter() {
		return new URLPermissionFilter();
	}
	
	/*@Bean
	public HttpSessionStrategy cookieHttpSessionStrategy() {
		CookieHttpSessionStrategy strategy = new CookieHttpSessionStrategy();
		DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
		cookieSerializer.setCookieName("MYSESSIONID");// cookies名称
		cookieSerializer.setCookieMaxAge(1800);// 过期时间(秒)
		strategy.setCookieSerializer(cookieSerializer);

		return strategy;
	}*/

//	@Bean
//	public static ConfigureRedisAction configureRedisAction() {
//		return ConfigureRedisAction.NO_OP;
//	}
}