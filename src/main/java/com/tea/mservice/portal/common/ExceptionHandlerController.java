package com.tea.mservice.portal.common;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import com.tea.mservice.config.http.RestCustomException;


@RestControllerAdvice
public class ExceptionHandlerController {

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);


	@ExceptionHandler(value = { ValidatingFormException.class})
	public void validatingFormException(ValidatingFormException ex, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		LOG.error(ex.getMessage(), ex);
	}

	@ExceptionHandler(value = { Exception.class})
	public void defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception ex) throws IOException {
		String ms = "网站维护中，请稍后重试或联系管理员";
		if (ex instanceof ResourceAccessException) {
			if (ex.getCause() instanceof RestCustomException) {
				RestCustomException r = (RestCustomException) ex.getCause();
				int httpCode = r.getStatusCode().value();
				if (httpCode >= 500) {
					ms = "服务器维护中，请稍后重试或联系管理员";
				} else {
					ms = r.getMessage();
				}
			} else if (ex.getCause() instanceof SocketTimeoutException) {
				ms = "服务器繁忙，请稍后再试";
			}

		}
		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ms);
		LOG.error(ex.getMessage(), ex);
	}
}
