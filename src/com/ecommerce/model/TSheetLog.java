package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TSheetLog entity. @author MyEclipse Persistence Tools
 */
public class TSheetLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer sheetTypeId;
	private Integer refSheetId;
	private Integer userId;
	private Integer vipId;
	private Timestamp operateDate;
	private String memo;

	// Constructors

	/** default constructor */
	public TSheetLog() {
	}

	/** minimal constructor */
	public TSheetLog(Integer sheetTypeId, Integer refSheetId,
			Timestamp operateDate) {
		this.sheetTypeId = sheetTypeId;
		this.refSheetId = refSheetId;
		this.operateDate = operateDate;
	}

	/** full constructor */
	public TSheetLog(Integer sheetTypeId, Integer refSheetId, Integer userId,
			Integer vipId, Timestamp operateDate, String memo) {
		this.sheetTypeId = sheetTypeId;
		this.refSheetId = refSheetId;
		this.userId = userId;
		this.vipId = vipId;
		this.operateDate = operateDate;
		this.memo = memo;
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

	public Integer getRefSheetId() {
		return this.refSheetId;
	}

	public void setRefSheetId(Integer refSheetId) {
		this.refSheetId = refSheetId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getVipId() {
		return this.vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public Timestamp getOperateDate() {
		return this.operateDate;
	}

	public void setOperateDate(Timestamp operateDate) {
		this.operateDate = operateDate;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}