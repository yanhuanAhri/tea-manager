package com.tea.mservice.portal.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 订单日志
 */
@Entity
@Table(name = "order_log")
public class OrderLog extends IdEntity {

	private Long orderId;//订单Id
	private String orderNum;//订单号
	private Integer orderStatus;//订单状态
	private String remark;
	private Timestamp createTime;
	private Long createUserId;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	
	
	
}
