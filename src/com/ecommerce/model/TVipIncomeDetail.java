package com.ecommerce.model;

/**
 * VVipIncomeDetail entity. @author MyEclipse Persistence Tools
 */
public class TVipIncomeDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private Integer productId;
	private Integer vipId;
	private Integer subVipId;
	private Double amount;

	//transient fields
	public String subVipNo;
	public String orderCode;
	public String productName;

	// Constructors

	/** default constructor */
	public TVipIncomeDetail() {
	}

	/** minimal constructor */
	public TVipIncomeDetail(Integer orderId, Integer productId, Integer vipId,
			Integer subVipId) {
		this.orderId = orderId;
		this.productId = productId;
		this.vipId = vipId;
		this.subVipId = subVipId;
	}

	/** full constructor */
	public TVipIncomeDetail(Integer orderId, Integer productId, Integer vipId,
			Integer subVipId, Double amount) {
		this.orderId = orderId;
		this.productId = productId;
		this.vipId = vipId;
		this.subVipId = subVipId;
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

	public Integer getVipId() {
		return this.vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public Integer getSubVipId() {
		return this.subVipId;
	}

	public void setSubVipId(Integer subVipId) {
		this.subVipId = subVipId;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getSubVipNo() {
		return subVipNo;
	}

	public void setSubVipNo(String subVipNo) {
		this.subVipNo = subVipNo;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}