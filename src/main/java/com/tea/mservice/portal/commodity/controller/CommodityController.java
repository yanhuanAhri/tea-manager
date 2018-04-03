package com.tea.mservice.portal.commodity.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tea.mservice.manager.users.vo.UserVo;
import com.tea.mservice.portal.commodity.service.CommodityService;
import com.tea.mservice.portal.commodity.vo.CommodityVo;

import net.sf.json.JSONObject;



/**
 * 商品模块
 * @author Masked
 *
 */
@Configuration
@Controller
public class CommodityController {

	
	@Autowired
	private CommodityService commodityService;

	@GetMapping(value = "commodity.html")
	public String goList(ModelMap map) {
		return getTemplatePath("list");
	}
	
	@GetMapping(value = "commodity/edit.html")
	public String goEdit(ModelMap map) {
		return getTemplatePath("edit");
	}

	private String getTemplatePath(String fileName) {
		return "/commodity/" + fileName;
	}
    
	/**
	 * 查詢
	 * @param pageNumber 页数
	 * @param pageSize 页码
	 * @return 
	 */
    @GetMapping("commodity/list")
    @ResponseBody
    public Map<String,Object> page(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
      @RequestParam(required = false, defaultValue = "25") Integer pageSize,String filter) {
    	return commodityService.page(pageNumber, pageSize, filter);
    }
	
    
    /**
     * 商品详情
     * @param id
     * @return
     */
    @GetMapping("commodity/{id}")
    @ResponseBody
    public Map<String,Object> detail(@PathVariable("id") Long id){
    	return commodityService.detail(id);
    }
    
    /**
     * 更新编辑
     * @param id
     * @param commodity
     */
    @PostMapping("commodity/{id}")
    @ResponseBody
    public void edit(@PathVariable("id") Long id,@RequestBody String commodity) {
    	commodityService.edit(id,commodity);
    }
    
 
    
    /**
     * 商品操作
     * @param id
     * @param type
     */
    @PostMapping("commodity/{id}/operation")
    @ResponseBody
    public void operation(@PathVariable("id") Long id,@RequestBody String type) {
    	commodityService.operation(type, id);
    }
    
    
	
    /**
     * 删除商品
     * @param id
     */
    @DeleteMapping("commodity/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
    	commodityService.delCommodity(id);
    }
   
}
