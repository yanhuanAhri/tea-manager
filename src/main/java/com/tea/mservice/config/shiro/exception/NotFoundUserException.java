package com.tea.mservice.config.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

public class NotFoundUserException extends AuthenticationException {

	private static final long serialVersionUID = 1113899152843258701L;

	public NotFoundUserException() {
		super();
	}

	public NotFoundUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundUserException(String message) {
		super(message);
	}

	public NotFoundUserException(Throwable cause) {
		super(cause);
	}
}