package com.tea.mservice.manager.users.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tea.mservice.manager.permission.vo.PermissionVo;

public class PermissionRowMapper implements RowMapper<PermissionVo> {

	@Override
	public PermissionVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		PermissionVo vo = new PermissionVo();
		vo.setId(rs.getLong("id"));
		vo.setParentId(rs.getLong("parent_id"));
		vo.setName(rs.getString("name"));
		vo.setType(rs.getShort("type"));
		vo.setUrl(rs.getString("url"));
		vo.setMethod(rs.getString("method"));
		vo.setCode(rs.getString("code"));
		String icon = rs.getString("icon");
		vo.setIcon(icon == null ? "" : icon);
		vo.setSortby(rs.getInt("sortby"));

		return vo;
	}

}
