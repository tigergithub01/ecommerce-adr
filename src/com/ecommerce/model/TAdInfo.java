package com.ecommerce.model;

import android.graphics.Bitmap;


/**
 * TAdInfo entity. @author MyEclipse Persistence Tools
 */
public class TAdInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String imageUrl;
	private Integer sequenceId;
	private String redirectUrl;
	
	//transient fields
	private Bitmap image;

	// Constructors

	/** default constructor */
	public TAdInfo() {
	}

	/** minimal constructor */
	public TAdInfo(String imageUrl, Integer sequenceId) {
		this.imageUrl = imageUrl;
		this.sequenceId = sequenceId;
	}

	/** full constructor */
	public TAdInfo(String imageUrl, Integer sequenceId, String redirectUrl) {
		this.imageUrl = imageUrl;
		this.sequenceId = sequenceId;
		this.redirectUrl = redirectUrl;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getSequenceId() {
		return this.sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	
	
}