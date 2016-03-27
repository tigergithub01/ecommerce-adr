package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TMsgPushHistory entity. @author MyEclipse Persistence Tools
 */
public class TMsgPushHistory implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer vipId;
	private String title;
	private String content;
	private Timestamp pushDate;

	// Constructors

	/** default constructor */
	public TMsgPushHistory() {
	}

	/** minimal constructor */
	public TMsgPushHistory(Integer id, Integer vipId, String title,
			Timestamp pushDate) {
		this.id = id;
		this.vipId = vipId;
		this.title = title;
		this.pushDate = pushDate;
	}

	/** full constructor */
	public TMsgPushHistory(Integer id, Integer vipId, String title,
			String content, Timestamp pushDate) {
		this.id = id;
		this.vipId = vipId;
		this.title = title;
		this.content = content;
		this.pushDate = pushDate;
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

	public Timestamp getPushDate() {
		return this.pushDate;
	}

	public void setPushDate(Timestamp pushDate) {
		this.pushDate = pushDate;
	}

}