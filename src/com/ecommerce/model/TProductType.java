package com.ecommerce.model;


/**
 * TProductType entity. @author MyEclipse Persistence Tools
 */
public class TProductType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Integer parentId;
	private String description;

	// Constructors

	/** default constructor */
	public TProductType() {
	}

	/** minimal constructor */
	public TProductType(String name) {
		this.name = name;
	}
	

	public TProductType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public TProductType(String name, Integer parentId, String description) {
		this.name = name;
		this.parentId = parentId;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}