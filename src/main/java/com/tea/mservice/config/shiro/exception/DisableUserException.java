package com.tea.mservice.config.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

public class DisableUserException extends AuthenticationException {

	private static final long serialVersionUID = 1113899152843258701L;

	public DisableUserException() {
		super();
	}

	public DisableUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public DisableUserException(String message) {
		super(message);
	}

	public DisableUserException(Throwable cause) {
		super(cause);
	}
}