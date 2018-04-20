package com.tea.mservice.portal.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 订单
 */
@Entity
@Table(name = "order")
public class Order extends IdEntity {

	private String orderNum; //订单编号
	private Timestamp createTime;//生成时间
	private Long   createUserId;
	private BigDecimal paymentAmount; //支付总价
	private BigDecimal totalAmount; //商品总价
	private Integer status; //订单状态 
	private Long receivingId;//收货
	private Timestamp updateTime;
	private Timestamp putawayTime;
	private String logisticsMode;//物流方式
	private String paymentMode;//支付方式
	private String remark;//留言
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getReceivingId() {
		return receivingId;
	}
	public void setReceivingId(Long receivingId) {
		this.receivingId = receivingId;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Timestamp getPutawayTime() {
		return putawayTime;
	}
	public void setPutawayTime(Timestamp putawayTime) {
		this.putawayTime = putawayTime;
	}
	public String getLogisticsMode() {
		return logisticsMode;
	}
	public void setLogisticsMode(String logisticsMode) {
		this.logisticsMode = logisticsMode;
	}

	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
