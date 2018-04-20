package com.tea.mservice;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
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
	
	 @Bean
	 public EmbeddedServletContainerCustomizer containerCustomizer(){
	        return new EmbeddedServletContainerCustomizer() {
	            @Override
	            public void customize(ConfigurableEmbeddedServletContainer container) {
	                 container.setSessionTimeout(1800);//单位为秒
	           }
	     };
	 }
	
}