package com.tea.mservice.portal.home.vo;

public class AppDayVo {
	
	private String appName;//公众号名称
	private Integer putScaleNum; //已投放设备
	private Integer avgScaleFans;//平均每台加粉
	private Integer subscribeNum;//新增关注数
	private Integer noSubscribeNum;//取消关注数
	private Integer newFans;//新增粉丝
	private Integer scanNum;//二维码生成数
	private Integer phoneScanNum;//二维码扫码数
	private Integer longPressNum;//长按二维码关注数
	private Integer subscribedScanNum;//已关注扫码数
	private Integer scanDistanctNum;//重复扫码数
	
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getPutScaleNum() {
		return putScaleNum;
	}
	public void setPutScaleNum(Integer putScaleNum) {
		this.putScaleNum = putScaleNum;
	}
	public Integer getAvgScaleFans() {
		return avgScaleFans;
	}
	public void setAvgScaleFans(Integer avgScaleFans) {
		this.avgScaleFans = avgScaleFans;
	}
	public Integer getSubscribeNum() {
		return subscribeNum;
	}
	public void setSubscribeNum(Integer subscribeNum) {
		this.subscribeNum = subscribeNum;
	}
	public Integer getNoSubscribeNum() {
		return noSubscribeNum;
	}
	public void setNoSubscribeNum(Integer noSubscribeNum) {
		this.noSubscribeNum = noSubscribeNum;
	}
	public Integer getNewFans() {
		return newFans;
	}
	public void setNewFans(Integer newFans) {
		this.newFans = newFans;
	}
	public Integer getScanNum() {
		return scanNum;
	}
	public void setScanNum(Integer scanNum) {
		this.scanNum = scanNum;
	}
	public Integer getPhoneScanNum() {
		return phoneScanNum;
	}
	public void setPhoneScanNum(Integer phoneScanNum) {
		this.phoneScanNum = phoneScanNum;
	}
	public Integer getLongPressNum() {
		return longPressNum;
	}
	public void setLongPressNum(Integer longPressNum) {
		this.longPressNum = longPressNum;
	}
	public Integer getSubscribedScanNum() {
		return subscribedScanNum;
	}
	public void setSubscribedScanNum(Integer subscribedScanNum) {
		this.subscribedScanNum = subscribedScanNum;
	}
	public Integer getScanDistanctNum() {
		return scanDistanctNum;
	}
	public void setScanDistanctNum(Integer scanDistanctNum) {
		this.scanDistanctNum = scanDistanctNum;
	}
	
	
}