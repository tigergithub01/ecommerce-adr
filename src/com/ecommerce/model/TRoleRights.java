package com.ecommerce.model;


/**
 * TRoleRights entity. @author MyEclipse Persistence Tools
 */
public class TRoleRights implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer roleId;
	private Integer moduleId;
	private Integer opId;

	// Constructors

	/** default constructor */
	public TRoleRights() {
	}

	/** full constructor */
	public TRoleRights(Integer roleId, Integer moduleId, Integer opId) {
		this.roleId = roleId;
		this.moduleId = moduleId;
		this.opId = opId;
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

	public Integer getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getOpId() {
		return this.opId;
	}

	public void setOpId(Integer opId) {
		this.opId = opId;
	}

}