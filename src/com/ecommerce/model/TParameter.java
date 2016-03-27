package com.ecommerce.model;


/**
 * TParameter entity. @author MyEclipse Persistence Tools
 */
public class TParameter implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer typeId;
	private String paVal;
	private String description;

	// Constructors

	/** default constructor */
	public TParameter() {
	}
	
	
	

	public TParameter(Integer id, String paVal) {
		super();
		this.id = id;
		this.paVal = paVal;
	}




	/** minimal constructor */
	public TParameter(Integer id, Integer typeId, String paVal) {
		this.id = id;
		this.typeId = typeId;
		this.paVal = paVal;
	}

	/** full constructor */
	public TParameter(Integer id, Integer typeId, String paVal,
			String description) {
		this.id = id;
		this.typeId = typeId;
		this.paVal = paVal;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getPaVal() {
		return this.paVal;
	}

	public void setPaVal(String paVal) {
		this.paVal = paVal;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}