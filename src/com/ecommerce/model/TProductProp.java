package com.ecommerce.model;


/**
 * TProductProp entity. @author MyEclipse Persistence Tools
 */
public class TProductProp implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer productTypeId;
	private String propName;
	private Integer propType;

	// Constructors

	/** default constructor */
	public TProductProp() {
	}

	/** full constructor */
	public TProductProp(Integer id, Integer productTypeId, String propName,
			Integer propType) {
		this.id = id;
		this.productTypeId = productTypeId;
		this.propName = propName;
		this.propType = propType;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductTypeId() {
		return this.productTypeId;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getPropName() {
		return this.propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public Integer getPropType() {
		return this.propType;
	}

	public void setPropType(Integer propType) {
		this.propType = propType;
	}

}