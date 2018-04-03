package com.tea.mservice.portal.home.vo;


public class ScaleAddFansVo {
	private String scaleId;//设备号
	private String scaleSerialNumber;//设备编号
	private String scaleName;//设备名称
	private Long addFansCount = 0L;// 新增关注
	private Long delFansCount = 0L;// 取消关注
	private Integer newFans;//新增粉丝数
	private Long sendCodeCount = 0L;// 随机码生成次数
	private Long useCodeCount = 0L;// 随机码使用次数
	
	
	public String getScaleId() {
		return scaleId;
	}
	public void setScaleId(String scaleId) {
		this.scaleId = scaleId;
	}
	public String getScaleSerialNumber() {
		return scaleSerialNumber;
	}
	public void setScaleSerialNumber(String scaleSerialNumber) {
		this.scaleSerialNumber = scaleSerialNumber;
	}
	public String getScaleName() {
		return scaleName;
	}
	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}
	public Long getAddFansCount() {
		return addFansCount;
	}
	public void setAddFansCount(Long addFansCount) {
		this.addFansCount = addFansCount;
	}
	public Long getDelFansCount() {
		return delFansCount;
	}
	public void setDelFansCount(Long delFansCount) {
		this.delFansCount = delFansCount;
	}
	public Integer getNewFans() {
		return newFans;
	}
	public void setNewFans(Integer newFans) {
		this.newFans = newFans;
	}
	public Long getSendCodeCount() {
		return sendCodeCount;
	}
	public void setSendCodeCount(Long sendCodeCount) {
		this.sendCodeCount = sendCodeCount;
	}
	public Long getUseCodeCount() {
		return useCodeCount;
	}
	public void setUseCodeCount(Long useCodeCount) {
		this.useCodeCount = useCodeCount;
	}
	

}
