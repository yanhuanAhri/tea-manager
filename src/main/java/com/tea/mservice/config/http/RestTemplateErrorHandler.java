package com.tea.mservice.config.http;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import net.sf.json.JSONObject;

public class RestTemplateErrorHandler implements ResponseErrorHandler {

	private ResponseErrorHandler myErrorHandler = new DefaultResponseErrorHandler();

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return myErrorHandler.hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		String body = IOUtils.toString(response.getBody(), "UTF-8");
		
		String msg = null;
		if(StringUtils.isNotEmpty(body)) {
			JSONObject jsonObj = JSONObject.fromObject(body);
			Object msgObj = jsonObj.get("errMsg");
			if(msgObj == null) {
				msg = body;
			} else {
				msg = msgObj.toString();
			}
		}
		
		RestCustomException ex = new RestCustomException(response.getStatusCode(), body, (msg == null ? "" : msg));
		throw ex;
	}
}