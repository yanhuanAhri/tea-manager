package com.tea.mservice.portal.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 首页推荐
 */
@Entity
@Table(name = "commodity_recommend")
public class Recommend extends IdEntity {

	private Long commodityId;
	private String commodityNum;
	private Long createUserId;
	private Timestamp createTime;
	public Long getCommodityId() {
		return commodityId;
	}
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	public String getCommodityNum() {
		return commodityNum;
	}
	public void setCommodityNum(String commodityNum) {
		this.commodityNum = commodityNum;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
