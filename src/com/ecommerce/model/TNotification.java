package com.ecommerce.model;


/**
 * TNotificatioin entity. @author MyEclipse Persistence Tools
 */
public class TNotification implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer scopeType;
	private String title;
	private String issueDate;
	private String content;

	// Constructors

	/** default constructor */
	public TNotification() {
	}

	/** minimal constructor */
	public TNotification(Integer scopeType, String title, String issueDate) {
		this.scopeType = scopeType;
		this.title = title;
		this.issueDate = issueDate;
	}

	/** full constructor */
	public TNotification(Integer scopeType, String title, String issueDate,
			String content) {
		this.scopeType = scopeType;
		this.title = title;
		this.issueDate = issueDate;
		this.content = content;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScopeType() {
		return this.scopeType;
	}

	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}