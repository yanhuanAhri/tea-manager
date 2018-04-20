package com.tea.mservice.portal.recommend.web;

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
import com.tea.mservice.portal.recommend.service.RecommendService;
import com.tea.mservice.portal.userInfo.service.UserInfoService;

import net.sf.json.JSONObject;



/**
 * 首页推荐
 * 
 *
 */
@Configuration
@Controller
public class RecommendController {

	
	@Autowired
	private RecommendService recommendService;

	@GetMapping(value = "recommend.html")
	public String goList(ModelMap map) {
		return getTemplatePath("list");
	}
	
	
	@GetMapping(value = "recommend/commodity.html")
	public String goCommodity(ModelMap map) {
		return getTemplatePath("commodity");
	}

	private String getTemplatePath(String fileName) {
		return "/recommend/" + fileName;
	}
    
	/**
	 * 首页列表
	 * @return
	 */
    @GetMapping("recommend/list")
    @ResponseBody
    public Map<String,Object> page() {
    	return recommendService.page();
    }
	
    /**
     * 保存
     * @param param
     */
    @PostMapping("recommend")
    @ResponseBody
    public void save(@RequestBody String param){
    	 recommendService.save(param);
    }
    
	
    /**
     * 删除首页商品
     * @param id
     */
    @DeleteMapping("recommend/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
    	recommendService.delete(id);
    }
 
}
