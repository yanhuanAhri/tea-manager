package com.tea.mservice.core.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfig {

	//@Autowired
	//private StringRedisTemplate redisTemplate;

	public  String  getValue(String key) {

		if(key == null || "".equals(key)) {
			return null;
		}
		//HashOperations<String,String,String> hashOperation=redisTemplate.opsForHash();
		//String value=hashOperation.get("YPJJ_SYS_CONFIG", key);
		return null;
	}

	public String get(String key) {
		
		if(key == null || "".equals(key)) {
			return null;
		}
		
		//Map<Object, Object> hashCache = redisTemplate.opsForHash().entries("YPJJ_SYS_CONFIG");
		
//		for (Map.Entry<Object, Object> entry : hashCache.entrySet()) {
//			
//			if(!key.equals(entry.getKey())) {
//				continue;
//			}
//			
//			return entry.getValue() == null ? null : entry.getValue().toString();
//		}

		return null;
	}
}
