package com.ecommerce.model;


/**
 * TRoleUser entity. @author MyEclipse Persistence Tools
 */
public class TRoleUser implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer roleId;
	private Integer userId;

	// Constructors

	/** default constructor */
	public TRoleUser() {
	}

	/** full constructor */
	public TRoleUser(Integer roleId, Integer userId) {
		this.roleId = roleId;
		this.userId = userId;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}