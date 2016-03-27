package com.ecommerce.model;


/**
 * TModule entity. @author MyEclipse Persistence Tools
 */
public class TModule implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String name;
	private Integer parentId;
	private String url;

	// Constructors

	/** default constructor */
	public TModule() {
	}

	/** minimal constructor */
	public TModule(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/** full constructor */
	public TModule(String code, String name, Integer parentId, String url) {
		this.code = code;
		this.name = name;
		this.parentId = parentId;
		this.url = url;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}