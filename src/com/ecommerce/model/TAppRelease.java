package com.ecommerce.model;

/**
 * TAppRelease entity. @author MyEclipse Persistence Tools
 */
public class TAppRelease implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String upgradeDesc;
	private Integer verNo;
	private Integer forceUpgrade;
	private String issueDate;
	private Integer issueUserId;
	private String appPath;

	//Transient fields
		private long contentLength;

	// Constructors

	/** default constructor */
	public TAppRelease() {
	}

	/** minimal constructor */
	public TAppRelease(String name, Integer verNo, Integer forceUpgrade,
			String issueDate, Integer issueUserId) {
		this.name = name;
		this.verNo = verNo;
		this.forceUpgrade = forceUpgrade;
		this.issueDate = issueDate;
		this.issueUserId = issueUserId;
	}

	/** full constructor */
	public TAppRelease(String name, String upgradeDesc, Integer verNo,
			Integer forceUpgrade, String issueDate, Integer issueUserId,
			String appPath) {
		this.name = name;
		this.upgradeDesc = upgradeDesc;
		this.verNo = verNo;
		this.forceUpgrade = forceUpgrade;
		this.issueDate = issueDate;
		this.issueUserId = issueUserId;
		this.appPath = appPath;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpgradeDesc() {
		return this.upgradeDesc;
	}

	public void setUpgradeDesc(String upgradeDesc) {
		this.upgradeDesc = upgradeDesc;
	}

	public Integer getVerNo() {
		return this.verNo;
	}

	public void setVerNo(Integer verNo) {
		this.verNo = verNo;
	}

	public Integer getForceUpgrade() {
		return this.forceUpgrade;
	}

	public void setForceUpgrade(Integer forceUpgrade) {
		this.forceUpgrade = forceUpgrade;
	}

	public String getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getIssueUserId() {
		return this.issueUserId;
	}

	public void setIssueUserId(Integer issueUserId) {
		this.issueUserId = issueUserId;
	}

	public String getAppPath() {
		return this.appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

}