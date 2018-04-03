package com.tea.mservice.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 日志表
 */
@Entity
@Table(name = "t_sys_log")
public class Log extends IdEntity {

	private Long userId;
	private String userName;
	private String crTime;
	private String module;
	private String operate;
	private String content;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCrTime() {
		return crTime;
	}

	public void setCrTime(String crTime) {
		this.crTime = crTime;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Log [userId=" + userId + ", userName=" + userName + ", crTime=" + crTime + ", module=" + module
				+ ", operate=" + operate + ", content=" + content + "]";
	}

}
