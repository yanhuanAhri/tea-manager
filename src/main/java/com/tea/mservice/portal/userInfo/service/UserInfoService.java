package com.tea.mservice.portal.userInfo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class UserInfoService {
    private static final Logger LOG = LoggerFactory.getLogger(UserInfoService.class);


    
    
    @Value("${img.url}")
	private String imgUrl;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;

  
  /**
   * 查询列表
   * @param pageNumber
   * @param pageSize
   * @param filter
   * @return
   */
  public Map<String,Object> page(Integer pageNumber,Integer pageSize,String filter){
    	
    	Map<String,Object> map = new HashMap<>();
    	String sql = "SELECT id,user_name AS userName,nick_name AS nickName,email,phone,sex,birthday,integral,create_time AS "
    			+ "createTime FROM user_info  ";
    	
    	String countSql  = "select count(1) as total from user_info ";
    	StringBuilder where = new StringBuilder(" where 1=1 ");
    	String offsets = " LIMIT ? OFFSET ?";
    	List<Object> args = new ArrayList<>();
    	try{	
    		
    		
	    	//过滤条件
	    	if(filter != null){
	    		
	    		JSONObject jsonObject=JSONObject.fromObject(filter);
	    	
	    		
	    		if(jsonObject.get("userName") != null && !jsonObject.get("userName").toString().equals("")){
	
	    			where.append(" AND user_name LIKE ?\n");
					args.add("%" + jsonObject.get("userName") + "%");
		
	    		}
	    		
	    		if(jsonObject.get("phone") != null && !jsonObject.get("phone").toString().equals("")){
	 
	    			where.append(" AND phone LIKE ?\n");
					args.add("%" + jsonObject.get("phone") + "%");
	    		}
	    	
	    		if(jsonObject.get("sex") != null && !jsonObject.get("sex").toString().equals("")){
	    			where.append(" AND sex = ?\n");
					args.add(jsonObject.get("sex"));
	    		}
	    	
	    	
	    	}
	    	
	    	//总数
	    	Long total  = jdbcTemplate.queryForObject(countSql + where, Long.class, args.toArray());
			
			where.append(" ORDER BY create_time DESC ").append(offsets);
			args.add(pageSize);
			args.add(pageNumber * pageSize);
	    	
			
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql+ where,args.toArray());
	    	
	    	
			map.put("data", list);
			map.put("total", total);
    	}catch(Exception e){
    		LOG.error(e.getMessage(),e);
    		throw e;
    	}
		
		return map;
    	
    }
    

}
