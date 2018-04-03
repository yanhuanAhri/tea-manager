package com.tea.mservice.portal.order.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.manager.users.vo.UserVo;
import com.tea.mservice.portal.commodity.service.CommodityService;
import com.tea.mservice.portal.commodity.vo.CommodityVo;
import com.tea.mservice.portal.order.service.OrderService;
import com.tea.mservice.portal.userInfo.service.UserInfoService;

import net.sf.json.JSONObject;



/**
 * 订单模块
 * 
 *
 */
@Configuration
@Controller
public class OrderController {

	
	@Autowired
	private OrderService orderService;

	@GetMapping(value = "order.html")
	public String goList(ModelMap map) {
		return getTemplatePath("list");
	}

	private String getTemplatePath(String fileName) {
		return "/order/" + fileName;
	}
    
	/**
	 * 查詢
	 * @param pageNumber 页数
	 * @param pageSize 页码
	 * @return 
	 */
    @GetMapping("order/list")
    @ResponseBody
    public Map<String,Object> page(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
      @RequestParam(required = false, defaultValue = "25") Integer pageSize,String filter) {
    	return orderService.page(pageNumber, pageSize, filter);
    }
	
    

 
}
