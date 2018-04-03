package com.tea.mservice.manager.index;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(ModelMap map) {
		return "index";
	}
}
