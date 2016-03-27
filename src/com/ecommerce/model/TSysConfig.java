package com.ecommerce.model;


/**
 * TSysConfig entity. @author MyEclipse Persistence Tools
 */
public class TSysConfig implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String value;
	private String description;

	// Constructors

	/** default constructor */
	public TSysConfig() {
	}

	/** minimal constructor */
	public TSysConfig(String code, String value) {
		this.code = code;
		this.value = value;
	}

	/** full constructor */
	public TSysConfig(String code, String value, String description) {
		this.code = code;
		this.value = value;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}