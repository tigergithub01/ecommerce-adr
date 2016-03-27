package com.ecommerce.model;


/**
 * TVipWithdrawFlow entity. @author MyEclipse Persistence Tools
 */
public class TVipWithdrawFlow implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer sheetTypeId;
	private String code;
	private String applyDate;
	private Integer vipId;
	private Double amount;
	private Double settledAmt;
	private String settledDate;
	private Integer status;

	// Constructors

	/** default constructor */
	public TVipWithdrawFlow() {
	}

	/** minimal constructor */
	public TVipWithdrawFlow(Integer sheetTypeId, String code,
			String applyDate, Integer vipId, Integer status) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.applyDate = applyDate;
		this.vipId = vipId;
		this.status = status;
	}

	/** full constructor */
	public TVipWithdrawFlow(Integer sheetTypeId, String code,
			String applyDate, Integer vipId, Double amount,
			Double settledAmt, String settledDate, Integer status) {
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.applyDate = applyDate;
		this.vipId = vipId;
		this.amount = amount;
		this.settledAmt = settledAmt;
		this.settledDate = settledDate;
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

	public String getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public Integer getVipId() {
		return this.vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getSettledAmt() {
		return this.settledAmt;
	}

	public void setSettledAmt(Double settledAmt) {
		this.settledAmt = settledAmt;
	}

	public String getSettledDate() {
		return this.settledDate;
	}

	public void setSettledDate(String settledDate) {
		this.settledDate = settledDate;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}