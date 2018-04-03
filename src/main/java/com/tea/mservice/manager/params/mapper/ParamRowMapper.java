package com.tea.mservice.manager.params.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tea.mservice.manager.params.vo.ParamVo;

public class ParamRowMapper implements RowMapper<ParamVo> {

	@Override
	public ParamVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		ParamVo vo = new ParamVo();
		vo.setId(rs.getLong("id"));
		vo.setParamName(rs.getString("param_name"));
		vo.setParamValue(rs.getString("param_value"));
		vo.setDescription(rs.getString("description"));
		return vo;
	}

}
