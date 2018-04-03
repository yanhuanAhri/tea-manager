package com.tea.mservice.config.http;

import java.io.IOException;

import org.springframework.http.HttpStatus;

public class RestCustomException extends IOException {

	private static final long serialVersionUID = 1L;

	private HttpStatus statusCode;

    private String body;

    public RestCustomException(String msg) {
        super(msg);
    }

    public RestCustomException(HttpStatus statusCode, String body, String msg) {
        super(msg);
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}