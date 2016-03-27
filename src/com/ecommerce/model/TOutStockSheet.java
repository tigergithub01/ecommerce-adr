package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TOutStockSheet entity. @author MyEclipse Persistence Tools
 */
public class TOutStockSheet implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer sheetTypeId;
	private String code;
	private Integer orderId;
	private Integer userId;
	private Timestamp sheetDate;
	private Double deliverFee;
	private String memo;
	private Integer status;

	// Constructors

	/** default constructor */
	public TOutStockSheet() {
	}

	/** minimal constructor */
	public TOutStockSheet(Integer sheetTypeId, String code, Integer orderId,
			Integer userId, Timestamp sheetDate, Integer status) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.orderId = orderId;
		this.userId = userId;
		this.sheetDate = sheetDate;
		this.status = status;
	}

	/** full constructor */
	public TOutStockSheet(Integer sheetTypeId, String code, Integer orderId,
			Integer userId, Timestamp sheetDate, Double deliverFee,
			String memo, Integer status) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.orderId = orderId;
		this.userId = userId;
		this.sheetDate = sheetDate;
		this.deliverFee = deliverFee;
		this.memo = memo;
		this.status = status;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSheetTypeId() {
		return this.sheetTypeId;
	}

	public void setSheetTypeId(Integer sheetTypeId) {
		this.sheetTypeId = sheetTypeId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Timestamp getSheetDate() {
		return this.sheetDate;
	}

	public void setSheetDate(Timestamp sheetDate) {
		this.sheetDate = sheetDate;
	}

	public Double getDeliverFee() {
		return this.deliverFee;
	}

	public void setDeliverFee(Double deliverFee) {
		this.deliverFee = deliverFee;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}