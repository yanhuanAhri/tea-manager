package com.tea.mservice.portal.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.portal.home.service.HomeService;
import com.tea.mservice.portal.home.vo.ApiData;
import com.tea.mservice.portal.home.vo.AppDayVo;
import com.tea.mservice.portal.home.vo.ScaleAddFansVo;

import java.util.Map;


@Controller
@RequestMapping("/home")
public class HomeController {

	@Autowired
	HomeService homeService;

	/**
	 * 模块页
	 */
	@GetMapping(value = "dashboard.html")
	public String goList(ModelMap map) {
		return getTemplatePath("list");
	}

	private String getTemplatePath(String fileName) {
		return "/dashboard/" + fileName;
	}
    
    /**
	 * 公众号加粉情况图显示
	 */
    @GetMapping("/homePage/officialAddSituation")
    @ResponseBody
    public Map<String, Object> officialAddSituation(){
		return homeService.officialAddSituation();
    }
    
    /**
	 * 设备加粉情况图显示
	 */
    @GetMapping("/homePage/scaleAddSituation")
    @ResponseBody
    public Map<String, Object> scaleAddSituation(){
		return homeService.scaleAddSituation();
    	
    }
    
	/**
	 * 公众号加粉情况列表 
	 */
    @GetMapping("/homePage/appFans")
    @ResponseBody
	public ApiData<AppDayVo> appFans(String staTime,
									String endTime,
									Integer pageNumber,
									Integer pageSize,
									@RequestParam(required = false)String sort){
    	
    	return homeService.appFans(staTime, endTime, pageNumber, pageSize, sort);
    }
    
    /**
     * 设备加粉情况列表 
     */
    @GetMapping("/homePage/scaleFans")
    @ResponseBody
    public ApiData<ScaleAddFansVo> scaleFans(String staTime,
						    		String endTime,
						    		Integer pageNumber,
						    		Integer pageSize,
						    		@RequestParam(required = false)String sort){
    	
    	return homeService.scaleFans(staTime, endTime, pageNumber, pageSize, sort);
    }
}
