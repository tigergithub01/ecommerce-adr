package com.ecommerce.model;


/**
 * TEarnGuild entity. @author MyEclipse Persistence Tools
 */
public class TEarnGuild implements java.io.Serializable {

	// Fields

	private Integer id;
	private String title;
	private String content;
	private Integer createUserId;
	private String createDate;
	private Integer updateUserId;
	private String updateDate;

	// Constructors

	/** default constructor */
	public TEarnGuild() {
	}

	/** minimal constructor */
	public TEarnGuild(String title) {
		this.title = title;
	}

	/** full constructor */
	public TEarnGuild(String title, String content, Integer createUserId,
			String createDate, Integer updateUserId, String updateDate) {
		this.title = title;
		this.content = content;
		this.createUserId = createUserId;
		this.createDate = createDate;
		this.updateUserId = updateUserId;
		this.updateDate = updateDate;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

}