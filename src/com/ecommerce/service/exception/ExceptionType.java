package com.ecommerce.service.exception;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

public class ExceptionType {
	public Throwable exception;
	public int errorNo;

	public ExceptionType(Throwable exception) {
		super();
		this.exception = exception;
	}

	public int getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		String msg = this.exception.getMessage();
		Throwable error = (exception.getCause()!=null?exception.getCause():exception);
		if (error.getClass().equals(SocketException.class)) {
			msg = "网络连接异常，请检查网络连接";
		}else if (error.getClass().equals(HttpHostConnectException.class)) {
			msg = "服务器连接异常，请检查网络连接";
		}else if (error.getClass().equals(UnknownHostException.class)) {
			msg = "服务器连接异常，请检查网络连接";
		}else if (error.getClass().equals(SocketTimeoutException.class)) {
			msg = "连接超时，请检查网络连接";
		}else if (error.getClass().equals(ConnectTimeoutException.class)) {
			msg = "连接超时，请检查网络连接";
		}else{
			msg="系统异常";
		}
		return msg;
	}
}
