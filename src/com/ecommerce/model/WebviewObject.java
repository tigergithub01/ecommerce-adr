package com.ecommerce.model;

public class WebviewObject implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1794071528004923542L;
	private String url;
	private Integer type;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
