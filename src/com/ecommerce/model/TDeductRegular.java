package com.ecommerce.model;


/**
 * TDeductRegular entity. @author MyEclipse Persistence Tools
 */
public class TDeductRegular implements java.io.Serializable {

	// Fields

	private Integer id;
	private Double deductLevel1;
	private Double deductLevel2;
	private Double deductLevel3;
	private Double deductLevel4;

	// Constructors

	/** default constructor */
	public TDeductRegular() {
	}

	/** full constructor */
	public TDeductRegular(Double deductLevel1, Double deductLevel2,
			Double deductLevel3, Double deductLevel4) {
		this.deductLevel1 = deductLevel1;
		this.deductLevel2 = deductLevel2;
		this.deductLevel3 = deductLevel3;
		this.deductLevel4 = deductLevel4;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getDeductLevel1() {
		return this.deductLevel1;
	}

	public void setDeductLevel1(Double deductLevel1) {
		this.deductLevel1 = deductLevel1;
	}

	public Double getDeductLevel2() {
		return this.deductLevel2;
	}

	public void setDeductLevel2(Double deductLevel2) {
		this.deductLevel2 = deductLevel2;
	}

	public Double getDeductLevel3() {
		return this.deductLevel3;
	}

	public void setDeductLevel3(Double deductLevel3) {
		this.deductLevel3 = deductLevel3;
	}

	public Double getDeductLevel4() {
		return this.deductLevel4;
	}

	public void setDeductLevel4(Double deductLevel4) {
		this.deductLevel4 = deductLevel4;
	}

}