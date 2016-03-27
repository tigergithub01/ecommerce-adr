package com.ecommerce.model;


/**
 * TDistrict entity. @author MyEclipse Persistence Tools
 */
public class TDistrict implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Integer cityId;

	// Constructors

	/** default constructor */
	public TDistrict() {
	}

	/** full constructor */
	public TDistrict(String name, Integer cityId) {
		this.name = name;
		this.cityId = cityId;
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

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

}