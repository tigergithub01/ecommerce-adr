package com.ecommerce.model;


/**
 * TSoContactPerson entity. @author MyEclipse Persistence Tools
 */
public class TSoContactPerson implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private String name;
	private String phoneNumber;
	private Integer provinceId;
	private Integer cityId;
	private Integer districtId;
	private String detailAddress;

	// Constructors

	/** default constructor */
	public TSoContactPerson() {
	}

	/** minimal constructor */
	public TSoContactPerson(Integer orderId, String name, String phoneNumber,
			Integer provinceId, Integer cityId, Integer districtId) {
		this.orderId = orderId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.districtId = districtId;
	}

	/** full constructor */
	public TSoContactPerson(Integer orderId, String name, String phoneNumber,
			Integer provinceId, Integer cityId, Integer districtId,
			String detailAddress) {
		this.orderId = orderId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.districtId = districtId;
		this.detailAddress = detailAddress;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getDetailAddress() {
		return this.detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

}