package com.tea.mservice.manager.roles.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.tea.mservice.manager.roles.vo.RoleVo;

public class RoleRowMapper implements RowMapper<RoleVo> {

	@Override
	public RoleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		RoleVo vo = new RoleVo();
		vo.setId(rs.getLong("id"));
		vo.setName(rs.getString("name"));
		
		String code = rs.getString("code");
		vo.setCode(StringUtils.isEmpty(code)? "" : code);
		vo.setDescription(rs.getString("description"));
		return vo;
	}

}
