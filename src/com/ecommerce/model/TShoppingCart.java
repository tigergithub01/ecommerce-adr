package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TShoppingCart entity. @author MyEclipse Persistence Tools
 */
public class TShoppingCart implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer vipId;
	private Integer productId;
	private Integer quantity;
	private Double price;
	private Double amount;
	private Timestamp createDate;
	private Timestamp updateDate;

	// Constructors

	/** default constructor */
	public TShoppingCart() {
	}

	/** minimal constructor */
	public TShoppingCart(Integer vipId, Integer productId, Integer quantity,
			Timestamp createDate) {
		this.vipId = vipId;
		this.productId = productId;
		this.quantity = quantity;
		this.createDate = createDate;
	}

	/** full constructor */
	public TShoppingCart(Integer vipId, Integer productId, Integer quantity,
			Double price, Double amount, Timestamp createDate,
			Timestamp updateDate) {
		this.vipId = vipId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.amount = amount;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVipId() {
		return this.vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
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

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

}