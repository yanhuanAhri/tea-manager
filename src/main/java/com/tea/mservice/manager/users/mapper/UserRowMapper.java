package com.tea.mservice.manager.users.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.tea.mservice.manager.users.vo.UserVo;

public class UserRowMapper implements RowMapper<UserVo> {

	@Override
	public UserVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserVo vo = new UserVo();
		vo.setId(rs.getLong("id"));
		vo.setDeptId(rs.getLong("dept_id"));
		vo.setLoginName(rs.getString("login_name"));
		vo.setName(rs.getString("name"));
		vo.setEmail(rs.getString("email"));
		vo.setMobile(rs.getString("mobile"));
		vo.setCreateTime(rs.getTimestamp("create_time"));
		vo.setStatus(rs.getShort("status"));
		return vo;
	}

}
