package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TReturnSheet entity. @author MyEclipse Persistence Tools
 */
public class TReturnSheet implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer sheetTypeId;
	private String code;
	private Integer orderId;
	private Integer outId;
	private Integer userId;
	private Timestamp sheetDate;
	private Double returnAmt;
	private String memo;
	private Integer status;

	// Constructors

	/** default constructor */
	public TReturnSheet() {
	}

	/** minimal constructor */
	public TReturnSheet(Integer sheetTypeId, String code, Integer orderId,
			Integer outId, Integer userId, Timestamp sheetDate, Integer status) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.orderId = orderId;
		this.outId = outId;
		this.userId = userId;
		this.sheetDate = sheetDate;
		this.status = status;
	}

	/** full constructor */
	public TReturnSheet(Integer sheetTypeId, String code, Integer orderId,
			Integer outId, Integer userId, Timestamp sheetDate,
			Double returnAmt, String memo, Integer status) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.orderId = orderId;
		this.outId = outId;
		this.userId = userId;
		this.sheetDate = sheetDate;
		this.returnAmt = returnAmt;
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

	public Integer getOutId() {
		return this.outId;
	}

	public void setOutId(Integer outId) {
		this.outId = outId;
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

	public Double getReturnAmt() {
		return this.returnAmt;
	}

	public void setReturnAmt(Double returnAmt) {
		this.returnAmt = returnAmt;
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