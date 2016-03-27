package com.ecommerce.model;


/**
 * TSoSheet entity. @author MyEclipse Persistence Tools
 */
public class TSoSheet implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer sheetTypeId;
	private String code;
	private Integer vipId;
	private Double orderAmt;
	private Integer orderQuantity;
	private Double deliverFee;
	private Integer status;
	private Integer settleFlag;
	private String orderDate;
	private String deliveryDate;
	private Integer deliveryType;
	private String deliveryNo;
	private Integer payTypeId;
	private Double payAmt;
	private String payDate;
	private Double returnAmt;
	private String returnDate;
	private String memo;
	private String message;

	//Transient fields
	private String vipNo;
	private String orderStatusVal;
	private String productName;

	// Constructors

	/** default constructor */
	public TSoSheet() {
	}

	/** minimal constructor */
	public TSoSheet(Integer id, Integer sheetTypeId, String code,
			Integer vipId, Integer status, Integer settleFlag,
			String orderDate) {
		this.id = id;
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.vipId = vipId;
		this.status = status;
		this.settleFlag = settleFlag;
		this.orderDate = orderDate;
	}

	/** full constructor */
	public TSoSheet(Integer id, Integer sheetTypeId, String code,
			Integer vipId, Double orderAmt, Integer orderQuantity,
			Double deliverFee, Integer status, Integer settleFlag,
			String orderDate, String deliveryDate, Integer deliveryType,
			String deliveryNo, Integer payTypeId, Double payAmt,
			String payDate, Double returnAmt, String returnDate,
			String memo, String message) {
		this.id = id;
		this.sheetTypeId = sheetTypeId;
		this.code = code;
		this.vipId = vipId;
		this.orderAmt = orderAmt;
		this.orderQuantity = orderQuantity;
		this.deliverFee = deliverFee;
		this.status = status;
		this.settleFlag = settleFlag;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.deliveryType = deliveryType;
		this.deliveryNo = deliveryNo;
		this.payTypeId = payTypeId;
		this.payAmt = payAmt;
		this.payDate = payDate;
		this.returnAmt = returnAmt;
		this.returnDate = returnDate;
		this.memo = memo;
		this.message = message;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSheetTypeId() {
		return this.sheetTypeId;
	}

	public void setSheetTypeId(Integer sheetTypeId) {
		this.sheetTypeId = sheetTypeId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getVipId() {
		return this.vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public Double getOrderAmt() {
		return this.orderAmt;
	}

	public void setOrderAmt(Double orderAmt) {
		this.orderAmt = orderAmt;
	}

	public Integer getOrderQuantity() {
		return this.orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Double getDeliverFee() {
		return this.deliverFee;
	}

	public void setDeliverFee(Double deliverFee) {
		this.deliverFee = deliverFee;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSettleFlag() {
		return this.settleFlag;
	}

	public void setSettleFlag(Integer settleFlag) {
		this.settleFlag = settleFlag;
	}

	public String getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getDeliveryType() {
		return this.deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getDeliveryNo() {
		return this.deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public Integer getPayTypeId() {
		return this.payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public Double getPayAmt() {
		return this.payAmt;
	}

	public void setPayAmt(Double payAmt) {
		this.payAmt = payAmt;
	}

	public String getPayDate() {
		return this.payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public Double getReturnAmt() {
		return this.returnAmt;
	}

	public void setReturnAmt(Double returnAmt) {
		this.returnAmt = returnAmt;
	}

	public String getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getVipNo() {
		return vipNo;
	}

	public void setVipNo(String vipNo) {
		this.vipNo = vipNo;
	}

	public String getOrderStatusVal() {
		return orderStatusVal;
	}

	public void setOrderStatusVal(String orderStatusVal) {
		this.orderStatusVal = orderStatusVal;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	

}