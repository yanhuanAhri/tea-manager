package com.tea.mservice.portal.home.vo;


/**
 * 设备投放状态统计
 */
public class ScaleStatisticsCountVo {
	private Long hasBeenShipped = 0L;//已发货
	private Long waitForTheDelivery = 0L;//等待投放
	private Long hasBeenPutOn = 0L;//已投放
	private Long hasRecycled = 0L;//已回收
	private Long toBeRecycled = 0L;//待回收
	private Long hasBeenDamaged = 0L;//已经损坏
	private Long temporaryShutdown = 0L;//临时歇业
	private Long inTheMaintenance = 0L;//维修中
	private Long theLost = 0L;//遗失
	private Long brandDisplay = 0L;//品牌展示
	
	
	public Long getHasBeenShipped() {
		return hasBeenShipped;
	}
	public void setHasBeenShipped(Long hasBeenShipped) {
		this.hasBeenShipped = hasBeenShipped;
	}
	public Long getWaitForTheDelivery() {
		return waitForTheDelivery;
	}
	public void setWaitForTheDelivery(Long waitForTheDelivery) {
		this.waitForTheDelivery = waitForTheDelivery;
	}
	public Long getHasBeenPutOn() {
		return hasBeenPutOn;
	}
	public void setHasBeenPutOn(Long hasBeenPutOn) {
		this.hasBeenPutOn = hasBeenPutOn;
	}
	public Long getHasRecycled() {
		return hasRecycled;
	}
	public void setHasRecycled(Long hasRecycled) {
		this.hasRecycled = hasRecycled;
	}
	public Long getHasBeenDamaged() {
		return hasBeenDamaged;
	}
	public void setHasBeenDamaged(Long hasBeenDamaged) {
		this.hasBeenDamaged = hasBeenDamaged;
	}
	public Long getTemporaryShutdown() {
		return temporaryShutdown;
	}
	public void setTemporaryShutdown(Long temporaryShutdown) {
		this.temporaryShutdown = temporaryShutdown;
	}
	public Long getInTheMaintenance() {
		return inTheMaintenance;
	}
	public void setInTheMaintenance(Long inTheMaintenance) {
		this.inTheMaintenance = inTheMaintenance;
	}
	public Long getTheLost() {
		return theLost;
	}
	public void setTheLost(Long theLost) {
		this.theLost = theLost;
	}
	public Long getToBeRecycled() {
		return toBeRecycled;
	}
	public void setToBeRecycled(Long toBeRecycled) {
		this.toBeRecycled = toBeRecycled;
	}
	public Long getBrandDisplay() {
		return brandDisplay;
	}
	public void setBrandDisplay(Long brandDisplay) {
		this.brandDisplay = brandDisplay;
	}
	
	
	@Override
	public String toString() {
		return "ScaleStatisticsCountVo [hasBeenShipped=" + hasBeenShipped + ", waitForTheDelivery=" + waitForTheDelivery
				+ ", hasBeenPutOn=" + hasBeenPutOn + ", hasRecycled=" + hasRecycled + ", toBeRecycled=" + toBeRecycled
				+ ", hasBeenDamaged=" + hasBeenDamaged + ", temporaryShutdown=" + temporaryShutdown
				+ ", inTheMaintenance=" + inTheMaintenance + ", theLost=" + theLost + ", brandDisplay=" + brandDisplay
				+ "]";
	}

	
}
