package com.ecommerce.service.exception;


public class ExceptionHandler {
	public static ExceptionType getMessage(Throwable e) {
		return new ExceptionType(e);
	}
}
