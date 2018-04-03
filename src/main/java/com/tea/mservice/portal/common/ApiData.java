package com.tea.mservice.portal.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyihao on 2017/10/18.
 */
public class ApiData<T> {

    private Long total = 0L;
    private List<T> data = new ArrayList<>();

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
}
