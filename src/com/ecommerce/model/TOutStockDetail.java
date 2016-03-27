package com.ecommerce.model;


/**
 * TOutStockDetail entity. @author MyEclipse Persistence Tools
 */
public class TOutStockDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer outId;
	private Integer productId;
	private Integer orderQuantity;
	private Integer outQuantity;

	// Constructors

	/** default constructor */
	public TOutStockDetail() {
	}

	/** minimal constructor */
	public TOutStockDetail(Integer outId, Integer productId) {
		this.outId = outId;
		this.productId = productId;
	}

	/** full constructor */
	public TOutStockDetail(Integer outId, Integer productId,
			Integer orderQuantity, Integer outQuantity) {
		this.outId = outId;
		this.productId = productId;
		this.orderQuantity = orderQuantity;
		this.outQuantity = outQuantity;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOutId() {
		return this.outId;
	}

	public void setOutId(Integer outId) {
		this.outId = outId;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getOrderQuantity() {
		return this.orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Integer getOutQuantity() {
		return this.outQuantity;
	}

	public void setOutQuantity(Integer outQuantity) {
		this.outQuantity = outQuantity;
	}

}