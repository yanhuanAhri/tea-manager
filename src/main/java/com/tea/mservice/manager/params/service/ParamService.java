package com.tea.mservice.manager.params.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tea.mservice.core.util.PageBean;
import com.tea.mservice.manager.entity.Param;
import com.tea.mservice.manager.params.mapper.ParamRowMapper;
import com.tea.mservice.manager.params.repository.ParamRepository;
import com.tea.mservice.manager.params.vo.ParamVo;

import net.sf.json.JSONObject;

@Service
public class ParamService {
	
	private static final String MZJ_SYS_PARAMS="MZJ_SYS_PARAMS";
	@Autowired
	private ParamRepository paramRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//@Autowired
	//private StringRedisTemplate redisTemplate;

	public Param get(Long id) {
		return paramRepository.findById(id);
	}

	public void save(ParamVo vo) throws Exception {

		if (vo == null) {
			throw new Exception("数据格式不正确");
		}

		Param entity = null;
		if (vo.getId() == null) {
			entity = new Param();

			Long total = paramRepository.countByParamName(vo.getParamName());
			if (total >= 1) {
				throw new Exception("存在相同名称的参数");
			}

		} else {
			entity = paramRepository.findById(vo.getId());
		}

		if (StringUtils.isNotEmpty(vo.getParamName()) && !vo.getParamName().equals(entity.getParamName())) {
			entity.setParamName(vo.getParamName());
		}
		if (StringUtils.isNotEmpty(vo.getParamValue()) && !vo.getParamValue().equals(entity.getParamValue())) {
			entity.setParamValue(vo.getParamValue());
		}
		if (StringUtils.isNotEmpty(vo.getDescription()) && !vo.getDescription().equals(entity.getDescription())) {
			entity.setDescription(vo.getDescription());
		}

		paramRepository.save(entity);

	}

	public void delete(Long id) throws Exception {
		Param entity = paramRepository.findById(id);
		if (entity == null) {
			throw new Exception("没有找到此参数");
		}

		paramRepository.delete(entity);
	}

	public Map<String, Object> pages(Integer pageNumber, Integer pageSize, String filter, String sort)
			throws Exception {

		StringBuffer totalSql = new StringBuffer("select count(1) as total from t_sys_param where 1=1 ");
		StringBuffer sql = new StringBuffer(
				"select id, param_name, param_value, description from t_sys_param where 1=1 ");

		addFilter(totalSql, filter);
		Map<String, Object> countMap = jdbcTemplate.queryForMap(totalSql.toString());
		Object totalObj = countMap.get("total");
		if (totalObj == null) {
			return null;
		}

		Integer total = Integer.parseInt(totalObj.toString());
		PageBean pBean = new PageBean(pageSize, (pageNumber + 1), total);

		addFilter(sql, filter);
		addSort(sql, sort);
		sql.append(" limit ").append(pBean.getStartNum()).append(",").append(pBean.getPageSize());

		List<ParamVo> list = jdbcTemplate.query(sql.toString(), new ParamRowMapper());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("total", total);

		return map;
	}

	private void addSort(StringBuffer sql, String sort) {
		if (StringUtils.isNotEmpty(sort)) {
			sql.append(" order by ").append(sort).append(" ");
			return;
		}

		sql.append(" order by id ");
	}

	private void addFilter(StringBuffer sql, String filter) {
		if (StringUtils.isEmpty(filter)) {
			return;
		}

		JSONObject obj = JSONObject.fromObject(filter);
		Object nameObj = obj.get("paramName");
		if (nameObj != null) {
			sql.append(" and param_name like '%").append(nameObj.toString()).append("%'");
		}
		Object valueObj = obj.get("paramValue");
		if (valueObj != null) {
			sql.append(" and param_value like '%").append(valueObj.toString()).append("%'");
		}
		Object desObj = obj.get("description");
		if (desObj != null) {
			sql.append(" and description like '%").append(desObj.toString()).append("%'");
		}
	}

	public void synch() throws Exception {
		List<Param> params = paramRepository.findAll();
		if (params == null || params.isEmpty()) {
			return;
		}
		//HashOperations<String, Object, Object> hashOperation = redisTemplate.opsForHash();
		Map<String,String> paramMap=new HashMap<>();
		// 添加redis数据yy
		for (Param entity : params) {
			paramMap.put(entity.getParamName(), entity.getParamValue());
		}
		//hashOperation.putAll(MZJ_SYS_PARAMS, paramMap);
	}
}
