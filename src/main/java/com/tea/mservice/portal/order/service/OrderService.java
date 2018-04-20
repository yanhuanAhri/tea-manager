package com.tea.mservice.portal.order.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.portal.entity.OrderLog;
import com.tea.mservice.portal.order.repository.OrderLogRepository;

import net.sf.json.JSONObject;

@Service
public class OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private OrderLogRepository orderLogRepository;
  /**
   * 查询列表
   * @param pageNumber
   * @param pageSize
   * @param filter
   * @return
   */
  public Map<String,Object> page(Integer pageNumber,Integer pageSize,String filter){
    	
    	Map<String,Object> map = new HashMap<>();
    	String sql = "select o.id,o.order_num as orderNum,c.trade_name as tradeName,o.payment_amount as paymentAmount,"
    			+ "cro.buy_num as buyNum,o.total_amount as totalAmount,i.consignee,i.consignee_phone as phone,"
    			+ "i.receipt_address as  address,o.`status`,o.create_time as creatTime,o.logistics_mode as logisticsMode,"
    			+ "o.payment_mode as paymentMode,o.remark from `order` o left join commodity_ref_order cro on o.order_num = cro.order_num "
    			+ "LEFT JOIN commodity c on cro.commodity_num  = c.commodity_num LEFT JOIN receiving_info i on o.receiving_id = i.id ";
    	
    	String countSql = " select count(1) as total from `order` o left join commodity_ref_order cro on o.order_num = cro.order_num "
    			+ "LEFT JOIN commodity c on cro.commodity_num  = c.commodity_num LEFT JOIN receiving_info i on o.receiving_id = i.id ";
    	StringBuilder where = new StringBuilder(" where 1=1 ");
    	String offsets = " LIMIT ? OFFSET ?";
    	List<Object> args = new ArrayList<>();
    	try{	
    		
    		
    		
	    	//过滤条件
	    	if(filter != null){
	    		
	    		JSONObject jsonObject=JSONObject.fromObject(filter);
	    	
	    		
	    		if(jsonObject.get("orderNum") != null && !jsonObject.get("orderNum").toString().equals("")){
	
	    			where.append(" AND o.order_num LIKE ?\n");
					args.add("%" + jsonObject.get("orderNum") + "%");
		
	    		}
	    		
	    		if(jsonObject.get("tradeName") != null && !jsonObject.get("tradeName").toString().equals("")){
	 
	    			where.append(" AND c.trade_name LIKE ?\n");
					args.add("%" + jsonObject.get("tradeName") + "%");
	    		}
	    		
	    		if(jsonObject.get("consignee") != null && !jsonObject.get("consignee").toString().equals("")){
	    			 
	    			where.append(" AND i.consignee LIKE ?\n");
					args.add("%" + jsonObject.get("consignee") + "%");
	    		}
	    		
	    		
	    		if(jsonObject.get("phone") != null && !jsonObject.get("phone").toString().equals("")){
	    			 
	    			where.append(" AND i.consignee_phone LIKE ?\n");
					args.add("%" + jsonObject.get("phone") + "%");
	    		}
	    		
	    		if(jsonObject.get("status") != null && !jsonObject.get("status").toString().equals("")){
	    			 
	    			where.append(" AND o.status = ?\n");
					args.add( jsonObject.get("status") );
	    		}
	    	
	    	
	    	
	    	}
	    	
	    	//总数
	    	Long total  = jdbcTemplate.queryForObject(countSql + where, Long.class, args.toArray());
			
			where.append(" order by o.create_time desc ").append(offsets);
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
  
  	/**
  	 * 发货
  	 * @param id
  	 */
  	public void deliveryn(Long id){
  		try{
  			
  			
  			String sql = "select status from `order` where id = ?";
  			
  			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, id);
  			
  			if(list.isEmpty()){
  				return;
  			}
  			
  			Integer status = Integer.parseInt(list.get(0).get("status").toString());
  			
  			//订单状态必须是2,待发货
  			if(status != 2){
  				return;
  			}
  			
  			String update = "update `order` set status=3 where id =?";
  			//更新状态
  			jdbcTemplate.execute(update.replaceAll("\\?",id.toString()));
  			
  		}catch(Exception e){
  			LOG.error(e.getMessage(),e);
    		throw e;
  		}
  	}
    
  	/**
  	 * 退款
  	 * @param vo
  	 */
  	public void openRefund(Long id,String param){
  		try{
	  		String sql = "select status,order_num as orderNum from `order` where id = ?";
				
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, id);
				
			if(list.isEmpty()){
					return;
			}
			JSONObject jsonObject=JSONObject.fromObject(param);
			//商品编号
			String orderNum = (String) list.get(0).get("orderNum");
			//通过1 ，不通过0
			int type = Integer.parseInt(jsonObject.getString("type"));
			String remark = "";
			int status = 10;
			if(type == 0){
				//获取不通过的理由
				remark = jsonObject.getString("remark");
				status = Integer.parseInt(list.get(0).get("status").toString());
			}
			
			
			//更新订单状态
			String update = "update `order` set status=?1 where id =?2";
				
			jdbcTemplate.execute(update.replaceAll("\\?1",String.valueOf(status)).replaceAll("\\?2",id.toString()));
			
			//保存到订单日志
			OrderLog log = new OrderLog();
			log.setOrderId(id);
			log.setOrderNum(orderNum);
			log.setOrderStatus(status);
			log.setRemark(remark);
			log.setCreateUserId(ShiroUserUtil.getUser().id);
			Timestamp date = new Timestamp(new Date().getTime());
			log.setCreateTime(date);
			
			orderLogRepository.save(log);
  		}catch(Exception e){
  			LOG.error(e.getMessage(),e);
    		throw e;
  		}
  	}
  	

}
