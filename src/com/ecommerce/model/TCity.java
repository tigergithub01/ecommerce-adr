package com.ecommerce.model;


/**
 * TCity entity. @author MyEclipse Persistence Tools
 */
public class TCity implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Integer provinceId;

	// Constructors

	/** default constructor */
	public TCity() {
	}

	/** full constructor */
	public TCity(String name, Integer provinceId) {
		this.name = name;
		this.provinceId = provinceId;
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

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

}