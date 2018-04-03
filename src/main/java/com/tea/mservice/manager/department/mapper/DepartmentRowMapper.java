package com.tea.mservice.manager.department.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tea.mservice.manager.department.vo.DepartmentVo;

public class DepartmentRowMapper implements RowMapper<DepartmentVo> {

	@Override
	public DepartmentVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		DepartmentVo vo = new DepartmentVo();
		vo.setId(rs.getLong("id"));
		vo.setParentId(rs.getLong("parent_id"));
		vo.setCode(rs.getString("code"));
		vo.setName(rs.getString("name"));
		vo.setDescription(rs.getString("description"));
		vo.setSortby(rs.getInt("sortby"));
		return vo;
	}

}
