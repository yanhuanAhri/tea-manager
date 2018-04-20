package com.tea.mservice.portal.recommend.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tea.mservice.config.shiro.UserRealm.ShiroUser;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.portal.entity.Recommend;
import com.tea.mservice.portal.recommend.repository.RecommendRepository;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class RecommendService {
    private static final Logger LOG = LoggerFactory.getLogger(RecommendService.class);

    @Autowired
    private RecommendRepository recommendRepository;
    
    @Value("${img.url}")
	private String imgUrl;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;

    
    /**
     * 查询列表
     * @return
     */
    public Map<String,Object> page(){
      	
      	Map<String,Object> map = new HashMap<>();
      	String sql = "select r.id,c.trade_name as name,i.path from commodity_recommend r "
      			+ " left join commodity c on r.commodity_id = c.id left join commodity_img i on r.commodity_id = i.commodity_id "
      			+ " where i.type=10";

      	try{	
   
  			
  			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
  			map.put("data", list);
  			map.put("total", list.size());
      	}catch(Exception e){
      		LOG.error(e.getMessage(),e);
      		throw e;
      	}
  		
  		return map;
      	
      }
    
    
    
    /**
     * 保存
     * @param  商品数据
     */
    public void save(String param){

        	
    	 JSONObject jsonObj = JSONObject.fromObject(param);
    	 Object obj = jsonObj.get("param");
    	 JSONArray  params = JSONArray.fromObject(obj);
    	 Recommend recommend = null;
    	 ShiroUser user = ShiroUserUtil.getUser();//当前登录用户
    	 Timestamp date = new Timestamp(new Date().getTime());//当前时间
    	 int size = params.size();
    	 for(int i = 0;i<size;i++){
    		 	recommend = new Recommend();
    		 	Object productObj = params.get(i);
    		 	JSONObject product = JSONObject.fromObject(productObj);
    		 	//商品ID
    		 	Long id = Long.parseLong(product.get("id").toString());
    		 	//商品编号
    		 	String commodityNum = product.get("commodityNum").toString();
    		 	recommend.setCommodityId(id);
    		 	recommend.setCommodityNum(commodityNum);
    		 	recommend.setCreateUserId(user.id);
    		 	recommend.setCreateTime(date);
    		 	recommendRepository.save(recommend);
    	 }
    	
    	
    	 
    }
    
    
    
    /**
     * 删除
     * @param id
     */
    public void delete(Long id){
    	
    	try{
    	
	    	Recommend recommend = recommendRepository.findOne(id);
	    	
	    	if(recommend == null){
	    		return;
	    	}
	    	
	    	recommendRepository.delete(recommend);
    	}catch(Exception e){
    		LOG.error(e.getMessage(),e);
      		throw e;
    	}
    	
    }

}
