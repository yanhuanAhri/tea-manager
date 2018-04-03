package com.tea.mservice.portal.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;


public class MapData {

    private Long total = 0L;
    
    private List<Map<String,Object>> data = new ArrayList<>();

    
    
    public Map<String,Object> createMap(){
    	return new HashedMap<String,Object>();
    }
    
    
 
    
    
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
}
