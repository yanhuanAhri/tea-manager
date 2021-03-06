package com.tea.mservice.manager.permission.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tea.mservice.manager.permission.vo.LevelVo;

public class LevelRowMapper implements RowMapper<LevelVo> {

	@Override
	public LevelVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		LevelVo vo = new LevelVo();
		vo.setId(rs.getLong("id"));
		vo.setParentId(rs.getLong("parent_id"));
		vo.setLevel(rs.getInt("level"));
		vo.setPath(rs.getString("path"));
		return vo;
	}

}
