package com.ecommerce.model;

/**
 * TVipBankcard entity. @author MyEclipse Persistence Tools
 */

public class TVipBankcard implements java.io.Serializable {

	// Fields    

	private Integer id;
	private Integer vipId;
	private String cardNo;
	private Integer bankId;
	private String branchName;
	private String openAddr;
	
	//transite fields
	private String bankName;

	// Constructors

	/** default constructor */
	public TVipBankcard() {
	}

	/** full constructor */
	public TVipBankcard(Integer vipId, String cardNo, Integer bankId,
			String branchName, String openAddr) {
		this.vipId = vipId;
		this.cardNo = cardNo;
		this.bankId = bankId;
		this.branchName = branchName;
		this.openAddr = openAddr;
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

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getBankId() {
		return this.bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getOpenAddr() {
		return this.openAddr;
	}

	public void setOpenAddr(String openAddr) {
		this.openAddr = openAddr;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	

}