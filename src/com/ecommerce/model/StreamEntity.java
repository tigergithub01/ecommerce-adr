package com.ecommerce.model;

import java.io.InputStream;

public class StreamEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte[] fileBuffer;
	private InputStream inputStream;
	private long contentLength;

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

}
