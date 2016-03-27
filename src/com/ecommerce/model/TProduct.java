package com.ecommerce.model;

import android.graphics.Bitmap;


/**
 * TProduct entity. @author MyEclipse Persistence Tools
 */
public class TProduct implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String name;
	private Integer typeId;
	private Double price;
	private String description;
	private Integer status;
	private Double stockQuantity;
	private Double safetyQuantity;
	private Integer createUserId;
	private String createDate;
	private Integer updateUserId;
	private String updateDate;
	private Integer canReturnFlag;
	private Integer returnDays;
	private String returnDesc;
	private Integer regularTypeId;
	private Double deductPrice;
	private Integer specialDeductFlag;
	private Double deductLevel1;
	private Double deductLevel2;
	private Double deductLevel3;
	private Double deductLevel4;
	private Integer showInHomepage;
	
	//transient fields
	private String primaryPhotoUrl;
	private Bitmap primaryPhotoBitmap;

	// Constructors

	/** default constructor */
	public TProduct() {
	}

	/** minimal constructor */
	public TProduct(Integer id, String code, String name, Integer typeId,
			Integer status, Integer canReturnFlag) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.typeId = typeId;
		this.status = status;
		this.canReturnFlag = canReturnFlag;
	}

	/** full constructor */
	public TProduct(Integer id, String code, String name, Integer typeId,
			Double price, String description, Integer status,
			Double stockQuantity, Double safetyQuantity, Integer createUserId,
			String createDate, Integer updateUserId, String updateDate,
			Integer canReturnFlag, Integer returnDays, String returnDesc,
			Integer regularTypeId, Double deductPrice,
			Integer specialDeductFlag, Double deductLevel1,
			Double deductLevel2, Double deductLevel3, Double deductLevel4) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.typeId = typeId;
		this.price = price;
		this.description = description;
		this.status = status;
		this.stockQuantity = stockQuantity;
		this.safetyQuantity = safetyQuantity;
		this.createUserId = createUserId;
		this.createDate = createDate;
		this.updateUserId = updateUserId;
		this.updateDate = updateDate;
		this.canReturnFlag = canReturnFlag;
		this.returnDays = returnDays;
		this.returnDesc = returnDesc;
		this.regularTypeId = regularTypeId;
		this.deductPrice = deductPrice;
		this.specialDeductFlag = specialDeductFlag;
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

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Double getStockQuantity() {
		return this.stockQuantity;
	}

	public void setStockQuantity(Double stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Double getSafetyQuantity() {
		return this.safetyQuantity;
	}

	public void setSafetyQuantity(Double safetyQuantity) {
		this.safetyQuantity = safetyQuantity;
	}

	public Integer getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getCanReturnFlag() {
		return this.canReturnFlag;
	}

	public void setCanReturnFlag(Integer canReturnFlag) {
		this.canReturnFlag = canReturnFlag;
	}

	public Integer getReturnDays() {
		return this.returnDays;
	}

	public void setReturnDays(Integer returnDays) {
		this.returnDays = returnDays;
	}

	public String getReturnDesc() {
		return this.returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public Integer getRegularTypeId() {
		return this.regularTypeId;
	}

	public void setRegularTypeId(Integer regularTypeId) {
		this.regularTypeId = regularTypeId;
	}

	public Double getDeductPrice() {
		return this.deductPrice;
	}

	public void setDeductPrice(Double deductPrice) {
		this.deductPrice = deductPrice;
	}

	public Integer getSpecialDeductFlag() {
		return this.specialDeductFlag;
	}

	public void setSpecialDeductFlag(Integer specialDeductFlag) {
		this.specialDeductFlag = specialDeductFlag;
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

	public String getPrimaryPhotoUrl() {
		return primaryPhotoUrl;
	}

	public void setPrimaryPhotoUrl(String primaryPhotoUrl) {
		this.primaryPhotoUrl = primaryPhotoUrl;
	}

	public Bitmap getPrimaryPhotoBitmap() {
		return primaryPhotoBitmap;
	}

	public void setPrimaryPhotoBitmap(Bitmap primaryPhotoBitmap) {
		this.primaryPhotoBitmap = primaryPhotoBitmap;
	}

	public Integer getShowInHomepage() {
		return showInHomepage;
	}

	public void setShowInHomepage(Integer showInHomepage) {
		this.showInHomepage = showInHomepage;
	}
	
	

}