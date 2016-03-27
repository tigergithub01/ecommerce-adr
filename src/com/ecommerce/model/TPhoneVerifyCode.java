package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TPhoneVerifyCode entity. @author MyEclipse Persistence Tools
 */
public class TPhoneVerifyCode implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer codeType;
	private Timestamp sentTime;
	private Timestamp expirationTime;
	private String verifyCode;
	private String smsContent;

	// Constructors

	/** default constructor */
	public TPhoneVerifyCode() {
	}

	/** minimal constructor */
	public TPhoneVerifyCode(Timestamp sentTime, Timestamp expirationTime,
			String verifyCode) {
		this.sentTime = sentTime;
		this.expirationTime = expirationTime;
		this.verifyCode = verifyCode;
	}

	/** full constructor */
	public TPhoneVerifyCode(Integer codeType, Timestamp sentTime,
			Timestamp expirationTime, String verifyCode, String smsContent) {
		this.codeType = codeType;
		this.sentTime = sentTime;
		this.expirationTime = expirationTime;
		this.verifyCode = verifyCode;
		this.smsContent = smsContent;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodeType() {
		return this.codeType;
	}

	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}

	public Timestamp getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(Timestamp sentTime) {
		this.sentTime = sentTime;
	}

	public Timestamp getExpirationTime() {
		return this.expirationTime;
	}

	public void setExpirationTime(Timestamp expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getVerifyCode() {
		return this.verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getSmsContent() {
		return this.smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

}