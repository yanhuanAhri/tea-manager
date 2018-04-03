package com.tea.mservice.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class JsonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	// 将json字符串转换为java对象
	public static Object JSON2Object(String jsonStr, Class obj) {
		try {
			// 将json字符串转换为json对象
			JSONObject jsonObj = new JSONObject().fromObject(jsonStr);
			// 将建json对象转换为Object对象
			return JSONObject.toBean(jsonObj, obj);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static String Object2Json(Object obj) {
		try {
			// 将java对象转换为json对象
			JSONObject json = JSONObject.fromObject(obj);
			// 将json对象转换为字符串
			return json.toString();
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
