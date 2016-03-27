package com.ecommerce.model;


/**
 * TPayType entity. @author MyEclipse Persistence Tools
 */
public class TPayType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String name;
	private Double rate;
	private String description;
	private Integer status;

	// Constructors

	/** default constructor */
	public TPayType() {
	}

	/** minimal constructor */
	public TPayType(String code, String name, Integer status) {
		this.code = code;
		this.name = name;
		this.status = status;
	}

	/** full constructor */
	public TPayType(String code, String name, Double rate, String description,
			Integer status) {
		this.code = code;
		this.name = name;
		this.rate = rate;
		this.description = description;
		this.status = status;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRate() {
		return this.rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}