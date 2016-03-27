package com.ecommerce.model;

public class JsonObj {
	private int status;
	private String msg;
	private Object value;

	
	
	public JsonObj() {
		super();
	}

	public JsonObj(int status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
