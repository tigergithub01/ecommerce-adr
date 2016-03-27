package com.ecommerce.model;


/**
 * TReturnDetail entity. @author MyEclipse Persistence Tools
 */
public class TReturnDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer returnId;
	private Integer productId;
	private Integer outQuantity;
	private Integer returnQuantity;

	// Constructors

	/** default constructor */
	public TReturnDetail() {
	}

	/** minimal constructor */
	public TReturnDetail(Integer returnId, Integer productId) {
		this.returnId = returnId;
		this.productId = productId;
	}

	/** full constructor */
	public TReturnDetail(Integer returnId, Integer productId,
			Integer outQuantity, Integer returnQuantity) {
		this.returnId = returnId;
		this.productId = productId;
		this.outQuantity = outQuantity;
		this.returnQuantity = returnQuantity;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReturnId() {
		return this.returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getOutQuantity() {
		return this.outQuantity;
	}

	public void setOutQuantity(Integer outQuantity) {
		this.outQuantity = outQuantity;
	}

	public Integer getReturnQuantity() {
		return this.returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

}