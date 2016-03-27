package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TVipOperationLog entity. @author MyEclipse Persistence Tools
 */
public class TVipOperationLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer vipId;
	private Integer moduleId;
	private Timestamp opDate;
	private String opIpAddr;
	private String opBrowserType;
	private String opPhoneModel;
	private String opUrl;
	private String opDesc;

	// Constructors

	/** default constructor */
	public TVipOperationLog() {
	}

	/** minimal constructor */
	public TVipOperationLog(Integer vipId, Integer moduleId, Timestamp opDate) {
		this.vipId = vipId;
		this.moduleId = moduleId;
		this.opDate = opDate;
	}

	/** full constructor */
	public TVipOperationLog(Integer vipId, Integer moduleId, Timestamp opDate,
			String opIpAddr, String opBrowserType, String opPhoneModel,
			String opUrl, String opDesc) {
		this.vipId = vipId;
		this.moduleId = moduleId;
		this.opDate = opDate;
		this.opIpAddr = opIpAddr;
		this.opBrowserType = opBrowserType;
		this.opPhoneModel = opPhoneModel;
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

	public Integer getVipId() {
		return this.vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}
	public Integer getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
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

	public String getOpPhoneModel() {
		return this.opPhoneModel;
	}

	public void setOpPhoneModel(String opPhoneModel) {
		this.opPhoneModel = opPhoneModel;
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