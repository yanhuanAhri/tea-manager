package com.tea.mservice.manager.log.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tea.mservice.manager.entity.Log;
import com.tea.mservice.manager.log.repository.LogRepository;

import net.sf.json.JSONObject;

@Service
public class LogService {
	private static final Logger LOG = LoggerFactory.getLogger(LogService.class);
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 日志列表
	 * @param pageNumber
	 * @param pageSize
	 * @param filter
	 * @param sort
	 * @return
	 */
	public Map<String, Object> findLogList(Integer pageNumber, Integer pageSize, String filter, String sort) {
		LOG.info("sys_log pages by pageNumber={}, pageSize={}, filter={}, sort={}", pageNumber, pageSize, filter, sort);
		StringBuffer sql = new StringBuffer();
		String listSql = "SELECT l.id,l.user_id AS userId,l.user_name AS userName,DATE_FORMAT(l.cr_time, '%Y-%m-%d %h:%i:%s') AS crTime,l.module,l.operate,l.content FROM t_sys_log l where 1=1";
		String countSql = "select count(1) count from t_sys_log l where 1=1";

		Map<String, Object> resMap = new HashMap<String, Object>();
		if (filter != null && !"".equals(filter)) {
			JSONObject jsonObj = JSONObject.fromObject(filter);
			if (jsonObj != null) {

				Object userNameObj = jsonObj.get("userName");
				Object startCrTimeObj = jsonObj.get("startCrTime");
				Object endCrTimeObj = jsonObj.get("endCrTime");
				Object moduleObj = jsonObj.get("module");
				Object operateObj = jsonObj.get("operate");
				Object contentObj = jsonObj.get("content");

				if (userNameObj != null) {
					sql.append(" AND l.user_name like '%" + userNameObj.toString() + "%'");
				}

				if (startCrTimeObj != null && endCrTimeObj != null) {
					sql.append(" AND DATE_FORMAT(l.cr_time, '%Y-%m-%d %h:%i:%s') >= DATE_FORMAT('"
							+ startCrTimeObj.toString()
							+ "', '%Y-%m-%d %h:%i:%s') AND DATE_FORMAT(l.cr_time, '%Y-%m-%d %h:%i:%s') <= DATE_FORMAT('"
							+ endCrTimeObj + "', '%Y-%m-%d %h:%i:%s')");
				}

				if (moduleObj != null) {
					sql.append(" AND l.module like '%" + moduleObj.toString() + "%'");
				}

				if (operateObj != null) {
					sql.append(" AND l.operate like '%" + operateObj.toString() + "%'");
				}

				if (contentObj != null) {
					sql.append(" AND l.content like '%" + contentObj.toString() + "%'");
				}
			}
		}

		List<Map<String, Object>> allList = jdbcTemplate.queryForList(countSql + sql.toString());

		if (sort != null && !"".equals(sort)) {
			sql.append(" order by l." + sort);
		} else {
			sql.append(" order by l.cr_time desc");
		}

		if (pageNumber != null && pageSize != null) {
			sql.append(" limit " + (pageNumber * pageSize) + "," + pageSize);
		}
		List<Map<String, Object>> pageList = jdbcTemplate.queryForList(listSql + sql.toString());

		resMap.put("total", allList.get(0).get("count"));
		resMap.put("data", pageList);
		return resMap;
	}

	/**
	 * 删除日志
	 * @param id
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception {
		Log entity = logRepository.findById(id);
		if (entity == null) {
			throw new Exception("日志未找到");
		}

		logRepository.delete(entity);
	}
}
