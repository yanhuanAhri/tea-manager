package com.tea.mservice.portal.home.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tea.mservice.config.shiro.UserRealm;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.portal.common.URLBuilder;
import com.tea.mservice.portal.home.controller.HomeController;
import com.tea.mservice.portal.home.vo.ApiData;
import com.tea.mservice.portal.home.vo.AppDayVo;
import com.tea.mservice.portal.home.vo.ScaleAddFansVo;
import com.tea.mservice.portal.home.vo.ScaleMonitorCountVo;
import com.tea.mservice.portal.home.vo.ScaleStatisticsCountVo;

import net.sf.json.JSON;
import net.sf.json.JSONArray;

@Service
public class HomeService {
	@Value("${apigw.host}")
	private String apigwHost;

	@Autowired
	private RestTemplate restTemplate;
	
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * 设备投放状态统计 
	 */
	public ScaleStatisticsCountVo scaleStatisticsCount() {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		URLBuilder api =new URLBuilder(apigwHost + "scale/machine/count");
		api.paramArray("orgIds", user.orgIdList);
		return restTemplate.getForObject(api.toString(), ScaleStatisticsCountVo.class);
	}
	
	/**
	 * 已投放设备 
	 */
	public ScaleMonitorCountVo scaleMonitorCount() {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		URLBuilder api =new URLBuilder(apigwHost + "scale/monitor/count");
		api.paramArray("orgIds", user.orgIdList);
		return restTemplate.getForObject(api.toString(), ScaleMonitorCountVo.class);
	}

	/**
	 * 关注数据量折线图
	 */
	public Map<String, Map<String,Integer>> officialCount() {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		URLBuilder api =new URLBuilder(apigwHost + "els/appDay/FansAll");
		api.paramArray("orgIds", user.orgIdList);
		return restTemplate.getForObject(api.toString(), Map.class);
	}

	/**
	 * 订阅号加粉取关率柱状图 
	 */
	public Map<String, Double> officialAddRaTeCount() {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		URLBuilder api =new URLBuilder(apigwHost + "els/appDay/noSubscribeRate");
		api.paramArray("orgIds", user.orgIdList);
		return restTemplate.getForObject(api.toString(), Map.class);
	}

	/**
	 * 订阅号加粉排行柱状图 
	 */
	public Map<String, Integer> officialAddRankCount() {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		URLBuilder api =new URLBuilder(apigwHost + "els/appDay/appFansYesterday");
		api.paramArray("orgIds", user.orgIdList);
		return restTemplate.getForObject(api.toString(), Map.class);
	}
	
	
	/**
	 * 公众号加粉情况图显示
	 */
	public Map<String, Object> officialAddSituation(){
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Double> officialAddRaTeCount = officialAddRaTeCount();//订阅号加粉取关率柱状图 
		Map<String, Integer> officialAddRankCount = officialAddRankCount();//订阅号加粉排行柱状图 
		Map<String, Map<String, Integer>> officialCount = officialCount();//关注数据量折线图
		
		map.put("officialAddRaTeCount", officialAddRaTeCount);
		map.put("officialAddRankCount", officialAddRankCount);
		map.put("officialCount", officialCount);
		
		return map;
		
	}
	
	
	/**
	 * 设备加粉情况图显示
	 */
	public Map<String, Object> scaleAddSituation(){
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Object> scaleStatisticsMap = new LinkedHashMap<>();
		Map<String, Object> scaleMonitorMap = new LinkedHashMap<>();
		ScaleStatisticsCountVo scaleStatisticsCount = scaleStatisticsCount();//设备投放状态统计 
		ScaleMonitorCountVo scaleMonitorCount = scaleMonitorCount();//已投放设备 
		
		
		scaleStatisticsMap.put("已投放",scaleStatisticsCount.getHasBeenPutOn());
		scaleStatisticsMap.put("已回收",scaleStatisticsCount.getHasRecycled());
		scaleStatisticsMap.put("待回收",scaleStatisticsCount.getToBeRecycled());
		scaleStatisticsMap.put("维修中",scaleStatisticsCount.getInTheMaintenance());
		scaleStatisticsMap.put("遗失",scaleStatisticsCount.getTheLost());
		scaleStatisticsMap.put("等待投放",scaleStatisticsCount.getWaitForTheDelivery());
		scaleStatisticsMap.put("已经损坏",scaleStatisticsCount.getHasBeenDamaged());
		scaleStatisticsMap.put("品牌展示",scaleStatisticsCount.getBrandDisplay());
		scaleStatisticsMap.put("临时歇业",scaleStatisticsCount.getTemporaryShutdown());
		scaleStatisticsMap.put("已发货",scaleStatisticsCount.getHasBeenShipped());

		scaleMonitorMap.put("投放", scaleMonitorCount.getTotal());
		scaleMonitorMap.put("已投放", scaleMonitorCount.getPlaceTotal());
		scaleMonitorMap.put("在线", scaleMonitorCount.getNormalTotal());
		scaleMonitorMap.put("关机", scaleMonitorCount.getShutdownTotal());
		scaleMonitorMap.put("故障", scaleMonitorCount.getBreakdownTotal());
		
		map.put("scaleStatisticsCount", scaleStatisticsMap);
		map.put("scaleMonitorCount", scaleMonitorMap);
		
		return map;
		
	}
	
	/**
	 * 公众号加粉情况列表 
	 */
	public ApiData<AppDayVo> appFans(String staTime,
									String endTime,
									Integer pageNumber,
									Integer pageSize,
									String sort){
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		URLBuilder api = new URLBuilder(apigwHost + "els/appDay/appFans");

		api.param("pageNumber", pageNumber)
		   .param("pageSize", pageSize)
		   .param("staTime", staTime.replace("-", ""))
		   .param("endTime", endTime.replace("-", ""))
		   .param("sort", sort)
		   .paramArray("orgIds", user.orgIdList);

		ResponseEntity<ApiData<AppDayVo>> responseEntity = restTemplate.exchange(
				api.toString(),
				HttpMethod.GET, 
				null, 
				new ParameterizedTypeReference<ApiData<AppDayVo>>() {
				});
		return responseEntity.getBody();
		
	}
	
	/**
	 * 设备加粉情况列表
	 */
	public ApiData<ScaleAddFansVo> scaleFans(String staTime, String endTime, Integer pageNumber, Integer pageSize, String sort) {
		UserRealm.ShiroUser user = ShiroUserUtil.getUser();
		URLBuilder api = new URLBuilder(apigwHost + "els/scaleFans/scaleFans");

		api.param("pageNumber", pageNumber)
		   .param("pageSize", pageSize)
		   .param("staTime", staTime.replace("-", ""))
		   .param("endTime", endTime.replace("-", ""))
		   .param("sort", sort)
		   .paramArray("orgIds", user.orgIdList);

		ResponseEntity<ApiData<ScaleAddFansVo>> responseEntity = restTemplate.exchange(
				api.toString(), 
				HttpMethod.GET,
				null, 
				new ParameterizedTypeReference<ApiData<ScaleAddFansVo>>() {
				});
		return responseEntity.getBody();

}
	

}
