package com.ecommerce.model;


/**
 * TSysFeedback entity. @author MyEclipse Persistence Tools
 */
public class TSysFeedback implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer vipId;
	private String feedbackDate;
	private String feedbackType;
	private String content;
	private String ipAddress;
	private String osType;
	private String phoneModel;
	private String contactMethod;

	// Constructors

	/** default constructor */
	public TSysFeedback() {
	}

	/** minimal constructor */
	public TSysFeedback(String feedbackDate, String feedbackType,
			String content, String ipAddress) {
		this.feedbackDate = feedbackDate;
		this.feedbackType = feedbackType;
		this.content = content;
		this.ipAddress = ipAddress;
	}

	/** full constructor */
	public TSysFeedback(Integer vipId, String feedbackDate,
			String feedbackType, String content, String ipAddress,
			String osType, String phoneModel) {
		this.vipId = vipId;
		this.feedbackDate = feedbackDate;
		this.feedbackType = feedbackType;
		this.content = content;
		this.ipAddress = ipAddress;
		this.osType = osType;
		this.phoneModel = phoneModel;
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

	public String getFeedbackDate() {
		return this.feedbackDate;
	}

	public void setFeedbackDate(String feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public String getFeedbackType() {
		return this.feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOsType() {
		return this.osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getPhoneModel() {
		return this.phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public String getContactMethod() {
		return contactMethod;
	}

	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}

}