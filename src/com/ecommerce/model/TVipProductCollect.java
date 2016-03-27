package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * TVipProductCollect entity. @author MyEclipse Persistence Tools
 */
public class TVipProductCollect implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer vipId;
	private Integer productId;
	private Timestamp collectDate;

	// Constructors

	/** default constructor */
	public TVipProductCollect() {
	}

	/** full constructor */
	public TVipProductCollect(Integer vipId, Integer productId,
			Timestamp collectDate) {
		this.vipId = vipId;
		this.productId = productId;
		this.collectDate = collectDate;
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

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Timestamp getCollectDate() {
		return this.collectDate;
	}

	public void setCollectDate(Timestamp collectDate) {
		this.collectDate = collectDate;
	}

}