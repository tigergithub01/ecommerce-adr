package com.ecommerce.model;


/**
 * TDeliveryType entity. @author MyEclipse Persistence Tools
 */
public class TDeliveryType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String name;
	private String printTpl;
	private String description;
	private Integer status;

	// Constructors

	/** default constructor */
	public TDeliveryType() {
	}

	/** minimal constructor */
	public TDeliveryType(String code, String name, Integer status) {
		this.code = code;
		this.name = name;
		this.status = status;
	}

	/** full constructor */
	public TDeliveryType(String code, String name, String printTpl,
			String description, Integer status) {
		this.code = code;
		this.name = name;
		this.printTpl = printTpl;
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

	public String getPrintTpl() {
		return this.printTpl;
	}

	public void setPrintTpl(String printTpl) {
		this.printTpl = printTpl;
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