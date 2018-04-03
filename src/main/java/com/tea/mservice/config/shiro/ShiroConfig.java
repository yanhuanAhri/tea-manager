package com.tea.mservice.config.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

@Configuration
public class ShiroConfig {

	/**
	 * FilterRegistrationBean
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		filterRegistration.setFilter(proxy);
		
		return filterRegistration;
	}

	/**
	 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
	 * @return
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl("/login");
		bean.setSuccessUrl("/");
		bean.setUnauthorizedUrl("/403");
		
		/*Map<String, Filter>filters =new LinkedHashMap<String, Filter>();
		filters.put("authc", urlPermissionsFilter());
		filters.put("anon", new AnonymousFilter());
		bean.setFilters(filters);*/

		Map<String, String> chains = new LinkedHashMap<String, String>();
		chains.put("/login", "authc");
		chains.put("/logout", "logout");
//		chains.put("/", "user");

		chains.put("/demo/**", "anon");
		chains.put("/bootstrap**", "anon");
		chains.put("/bootstrap/**", "anon");
		chains.put("/daterangepicker/**", "anon");
		chains.put("/angularjs/**", "anon");
		chains.put("/dist/**", "anon");
		chains.put("/documentation/**", "anon");
		chains.put("/pages/**", "anon");
		chains.put("/plugins/**", "anon");
		chains.put("/js/**", "anon");
		chains.put("/images**", "anon");
		chains.put("/images/**", "anon");
		chains.put("/css/**", "anon");
		chains.put("/favicon**", "anon");

		chains.put("/**", "authc");

		bean.setFilterChainDefinitionMap(chains);
		return bean;
	}

	/**
	 * shiro AOP支持， 支持权限控制，不加shiro注解则无效
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager);
		return aasa;
	}

	/**
	 * @see org.apache.shiro.mgt.SecurityManager
	 * @return
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager(UserRealm userRealm) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setRealm(userRealm);
		manager.setCacheManager(cacheManager());
		manager.setSessionManager(defaultWebSessionManager());
		manager.setRememberMeManager(rememberMeManager());
		return manager;
	}

	/**
	 * @see DefaultWebSessionManager
	 * @return
	 */
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(cacheManager());
		sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdUrlRewritingEnabled(false);

		Collection<SessionListener> listeners = new ArrayList<SessionListener>();
	    listeners.add(new MyShiroSessionListener());
	    sessionManager.setSessionListeners(listeners);
	        
		return sessionManager;
	}

	/**
	 * @see UserRealm--->AuthorizingRealm
	 * @return
	 */
	@Bean
	@DependsOn(value = "lifecycleBeanPostProcessor")
	public UserRealm userRealm(EhCacheManager ehCacheManager) {
		UserRealm userRealm = new UserRealm();
		userRealm.setCacheManager(ehCacheManager);
		return userRealm;
	}

	@Bean
	public EhCacheManager cacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		return cacheManager;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
	
	 /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能 * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //Cookie rememberMe
	@Bean
	public SimpleCookie rememberMeCookie() {

		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		//记住我cookie生效时间30天 ,单位秒;
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	/**
	 * cookie管理对象;
	 * 
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {

		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}
	
	/**
	 * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
	 * @return
	 */
	@Bean
    public ShiroDialect shiroDialect(){
		return new ShiroDialect();
	}
}