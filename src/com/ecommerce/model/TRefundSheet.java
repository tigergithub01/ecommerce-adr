package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TRefundSheet entity. @author MyEclipse Persistence Tools
 */
public class TRefundSheet implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer sheetTypeId;
	private String code;
	private Integer orderId;
	private Integer returnId;
	private Integer userId;
	private Timestamp sheetDate;
	private Double needReturnAmt;
	private Double returnAmt;
	private String memo;
	private Integer status;
	private Integer settleFlag;

	// Constructors

	/** default constructor */
	public TRefundSheet() {
	}

	/** minimal constructor */
	public TRefundSheet(Integer sheetTypeId, String code, Integer orderId,
			Integer userId, Timestamp sheetDate, Integer status,
			Integer settleFlag) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.orderId = orderId;
		this.userId = userId;
		this.sheetDate = sheetDate;
		this.status = status;
		this.settleFlag = settleFlag;
	}

	/** full constructor */
	public TRefundSheet(Integer sheetTypeId, String code, Integer orderId,
			Integer returnId, Integer userId, Timestamp sheetDate,
			Double needReturnAmt, Double returnAmt, String memo,
			Integer status, Integer settleFlag) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.orderId = orderId;
		this.returnId = returnId;
		this.userId = userId;
		this.sheetDate = sheetDate;
		this.needReturnAmt = needReturnAmt;
		this.returnAmt = returnAmt;
		this.memo = memo;
		this.status = status;
		this.settleFlag = settleFlag;
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

	public Integer getReturnId() {
		return this.returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
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

	public Double getNeedReturnAmt() {
		return this.needReturnAmt;
	}

	public void setNeedReturnAmt(Double needReturnAmt) {
		this.needReturnAmt = needReturnAmt;
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

	public Integer getSettleFlag() {
		return this.settleFlag;
	}

	public void setSettleFlag(Integer settleFlag) {
		this.settleFlag = settleFlag;
	}

}