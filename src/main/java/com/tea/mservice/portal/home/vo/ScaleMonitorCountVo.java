package com.tea.mservice.portal.home.vo;

public class ScaleMonitorCountVo {
    private Long total = 0L;//总设备数
    private Long placeTotal = 0L;//在线设备数
    private Long normalTotal = 0L;//正常设备数
    private Long shutdownTotal = 0L;//关机设备数
    private Long timeoutTotal = 0L;//超时设备数
    private Long alertTotal = 0L;//告警设备数
    private Long breakdownTotal = 0L;//故障设备数

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPlaceTotal() {
        return placeTotal;
    }

    public void setPlaceTotal(Long placeTotal) {
        this.placeTotal = placeTotal;
    }

    public Long getNormalTotal() {
        return normalTotal;
    }

    public void setNormalTotal(Long normalTotal) {
        this.normalTotal = normalTotal;
    }

    public Long getShutdownTotal() {
        return shutdownTotal;
    }

    public void setShutdownTotal(Long shutdownTotal) {
        this.shutdownTotal = shutdownTotal;
    }

    public Long getTimeoutTotal() {
        return timeoutTotal;
    }

    public void setTimeoutTotal(Long timeoutTotal) {
        this.timeoutTotal = timeoutTotal;
    }

    public Long getAlertTotal() {
        return alertTotal;
    }

    public void setAlertTotal(Long alertTotal) {
        this.alertTotal = alertTotal;
    }

    public Long getBreakdownTotal() {
        return breakdownTotal;
    }

    public void setBreakdownTotal(Long breakdownTotal) {
        this.breakdownTotal = breakdownTotal;
    }

	@Override
	public String toString() {
		return "MonitorCountVo [total=" + total + ", placeTotal=" + placeTotal + ", normalTotal=" + normalTotal
				+ ", shutdownTotal=" + shutdownTotal + ", timeoutTotal=" + timeoutTotal + ", alertTotal=" + alertTotal
				+ ", breakdownTotal=" + breakdownTotal + "]";
	}
    
    
}
