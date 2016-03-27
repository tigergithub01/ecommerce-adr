package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TOperationLog entity. @author MyEclipse Persistence Tools
 */
public class TOperationLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer userId;
	private Integer moduleId;
	private Integer operationId;
	private Timestamp opDate;
	private String opIpAddr;
	private String opBrowserType;
	private String opUrl;
	private String opDesc;

	// Constructors

	/** default constructor */
	public TOperationLog() {
	}

	/** minimal constructor */
	public TOperationLog(Integer userId, Integer moduleId, Integer operationId,
			Timestamp opDate) {
		this.userId = userId;
		this.moduleId = moduleId;
		this.operationId = operationId;
		this.opDate = opDate;
	}

	/** full constructor */
	public TOperationLog(Integer userId, Integer moduleId, Integer operationId,
			Timestamp opDate, String opIpAddr, String opBrowserType,
			String opUrl, String opDesc) {
		this.userId = userId;
		this.moduleId = moduleId;
		this.operationId = operationId;
		this.opDate = opDate;
		this.opIpAddr = opIpAddr;
		this.opBrowserType = opBrowserType;
		this.opUrl = opUrl;
		this.opDesc = opDesc;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getOperationId() {
		return this.operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public Timestamp getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Timestamp opDate) {
		this.opDate = opDate;
	}

	public String getOpIpAddr() {
		return this.opIpAddr;
	}

	public void setOpIpAddr(String opIpAddr) {
		this.opIpAddr = opIpAddr;
	}

	public String getOpBrowserType() {
		return this.opBrowserType;
	}

	public void setOpBrowserType(String opBrowserType) {
		this.opBrowserType = opBrowserType;
	}

	public String getOpUrl() {
		return this.opUrl;
	}

	public void setOpUrl(String opUrl) {
		this.opUrl = opUrl;
	}

	public String getOpDesc() {
		return this.opDesc;
	}

	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
	}

}