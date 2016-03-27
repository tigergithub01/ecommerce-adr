package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
public class TUser implements java.io.Serializable {

	// Fields

	private Integer id;
	private String userId;
	private String userName;
	private String password;
	private Integer status;
	private Timestamp lastLoginDate;
	private Integer createUserId;
	private Timestamp createDate;
	private Integer updateUserId;
	private Timestamp updateDate;

	// Constructors

	/** default constructor */
	public TUser() {
	}

	/** minimal constructor */
	public TUser(String userId, String password, Integer status) {
		this.userId = userId;
		this.password = password;
		this.status = status;
	}

	/** full constructor */
	public TUser(String userId, String userName, String password,
			Integer status, Timestamp lastLoginDate, Integer createUserId,
			Timestamp createDate, Integer updateUserId, Timestamp updateDate) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.status = status;
		this.lastLoginDate = lastLoginDate;
		this.createUserId = createUserId;
		this.createDate = createDate;
		this.updateUserId = updateUserId;
		this.updateDate = updateDate;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Integer getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

}