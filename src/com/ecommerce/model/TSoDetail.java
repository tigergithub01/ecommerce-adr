package com.ecommerce.model;


/**
 * TSoDetail entity. @author MyEclipse Persistence Tools
 */
public class TSoDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private Integer productId;
	private Integer quantity;
	private Double price;
	private Double amount;

	// Constructors

	/** default constructor */
	public TSoDetail() {
	}

	/** minimal constructor */
	public TSoDetail(Integer orderId, Integer productId) {
		this.orderId = orderId;
		this.productId = productId;
	}

	/** full constructor */
	public TSoDetail(Integer orderId, Integer productId, Integer quantity,
			Double price, Double amount) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.amount = amount;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}