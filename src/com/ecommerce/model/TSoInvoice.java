package com.ecommerce.model;


/**
 * TSoInvoice entity. @author MyEclipse Persistence Tools
 */
public class TSoInvoice implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private Integer invoiceType;
	private String invoiceHeader;

	// Constructors

	/** default constructor */
	public TSoInvoice() {
	}

	/** full constructor */
	public TSoInvoice(Integer orderId, Integer invoiceType, String invoiceHeader) {
		this.orderId = orderId;
		this.invoiceType = invoiceType;
		this.invoiceHeader = invoiceHeader;
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

	public Integer getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceHeader() {
		return this.invoiceHeader;
	}

	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

}