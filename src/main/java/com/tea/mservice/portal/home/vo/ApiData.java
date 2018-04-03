package com.tea.mservice.portal.home.vo;

import java.util.ArrayList;
import java.util.List;

public class ApiData<T> {

    private Long total = 0L;
    private List<T> data = new ArrayList<>();
    private T totalData;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public T getTotalData() {
		return totalData;
	}

	public void setTotalData(T totalData) {
		this.totalData = totalData;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApiData{");
        sb.append("total=").append(total);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
