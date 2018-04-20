package com.tea.mservice.portal.order.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.portal.order.service.OrderService;



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
	
	@GetMapping(value = "order/audit.html")
	public String goAudit(ModelMap map) {
		return getTemplatePath("audit");
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
	
    /**
     * 发货
     */
    @PutMapping("order/deliveryn/{id}")
    @ResponseBody
    public void deliveryn(@PathVariable("id") Long id){
    	orderService.deliveryn(id);
    }
    
    /**
     * 退款
     */
    @PostMapping("order/refund/{id}")
    @ResponseBody
    public void  openRefund(@PathVariable("id") Long id,@RequestBody String param){
    	orderService.openRefund(id, param);
    }
 
}
