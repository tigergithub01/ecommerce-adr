package com.ecommerce.model;


/**
 * TVipAddress entity. @author MyEclipse Persistence Tools
 */
public class TVipAddress implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer vipId;
	private String name;
	private String phoneNumber;
	private Integer provinceId;
	private Integer cityId;
	private Integer districtId;
	private String detailAddress;
	private Integer defaultFlag;

	// Constructors

	/** default constructor */
	public TVipAddress() {
	}

	/** minimal constructor */
	public TVipAddress(Integer vipId, String name, String phoneNumber,
			Integer provinceId, Integer cityId, Integer districtId,
			Integer defaultFlag) {
		this.vipId = vipId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.districtId = districtId;
		this.defaultFlag = defaultFlag;
	}

	/** full constructor */
	public TVipAddress(Integer vipId, String name, String phoneNumber,
			Integer provinceId, Integer cityId, Integer districtId,
			String detailAddress, Integer defaultFlag) {
		this.vipId = vipId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.districtId = districtId;
		this.detailAddress = detailAddress;
		this.defaultFlag = defaultFlag;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVipId() {
		return this.vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
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

	public Integer getDefaultFlag() {
		return this.defaultFlag;
	}

	public void setDefaultFlag(Integer defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

}