package com.ecommerce.model;


/**
 * TProductPhoto entity. @author MyEclipse Persistence Tools
 */
public class TProductPhoto implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer productId;
	private String url;
	private Integer primaryFlag;

	// Constructors

	/** default constructor */
	public TProductPhoto() {
	}

	/** full constructor */
	public TProductPhoto(Integer id, Integer productId, String url,
			Integer primaryFlag) {
		this.id = id;
		this.productId = productId;
		this.url = url;
		this.primaryFlag = primaryFlag;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPrimaryFlag() {
		return this.primaryFlag;
	}

	public void setPrimaryFlag(Integer primaryFlag) {
		this.primaryFlag = primaryFlag;
	}

}