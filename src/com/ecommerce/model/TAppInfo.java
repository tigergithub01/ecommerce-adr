package com.ecommerce.model;


/**
 * TAppInfo entity. @author MyEclipse Persistence Tools
 */
public class TAppInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String description;
	private Integer releaseId;

	// Constructors

	/** default constructor */
	public TAppInfo() {
	}

	/** minimal constructor */
	public TAppInfo(String name) {
		this.name = name;
	}

	/** full constructor */
	public TAppInfo(String name, String description, Integer releaseId) {
		this.name = name;
		this.description = description;
		this.releaseId = releaseId;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getReleaseId() {
		return this.releaseId;
	}

	public void setReleaseId(Integer releaseId) {
		this.releaseId = releaseId;
	}

}