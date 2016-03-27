package com.ecommerce.model;


/**
 * VVipIncome entity. @author MyEclipse Persistence Tools
 */
public class TVipIncome implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer vipId;
	private Double amount;
	private Double canSettleAmt;
	private Double settledAmt;
	private Double canWithdrawAmt;

	// Constructors

	/** default constructor */
	public TVipIncome() {
	}

	/** minimal constructor */
	public TVipIncome(Integer vipId) {
		this.vipId = vipId;
	}

	/** full constructor */
	public TVipIncome(Integer vipId, Double amount, Double canSettleAmt,
			Double settledAmt) {
		this.vipId = vipId;
		this.amount = amount;
		this.canSettleAmt = canSettleAmt;
		this.settledAmt = settledAmt;
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

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCanSettleAmt() {
		return this.canSettleAmt;
	}

	public void setCanSettleAmt(Double canSettleAmt) {
		this.canSettleAmt = canSettleAmt;
	}

	public Double getSettledAmt() {
		return this.settledAmt;
	}

	public void setSettledAmt(Double settledAmt) {
		this.settledAmt = settledAmt;
	}

	public Double getCanWithdrawAmt() {
		return canWithdrawAmt;
	}

	public void setCanWithdrawAmt(Double canWithdrawAmt) {
		this.canWithdrawAmt = canWithdrawAmt;
	}
	
	

}