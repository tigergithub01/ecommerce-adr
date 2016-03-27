package com.ecommerce.model;

import java.io.InputStream;
import java.util.Date;

/**
 * TProductVersion entity. @author MyEclipse Persistence Tools
 */
public class TProductVersion extends AbstractEntity implements
		java.io.Serializable {

	// Fields    

	/**
	* @Fields serialVersionUID : 
	*/
	private static final long serialVersionUID = 180119661777347463L;
	private Long id;
	private Integer verCode;
	private String verName;
	private String fileUrl;
	private Date createDate;
	private Date updateDate;
	private String description;
	private byte[] fileBuffer;
	private InputStream inputStream;
	private long contentLength;

	// Constructors

	// Property accessors
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVerName() {
		return this.verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getVerCode() {
		return verCode;
	}

	public void setVerCode(Integer verCode) {
		this.verCode = verCode;
	}

	public byte[] getFileBuffer() {
		return fileBuffer;
	}

	public void setFileBuffer(byte[] fileBuffer) {
		this.fileBuffer = fileBuffer;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}