package com.ecommerce.model;


/**
 * TSheetType entity. @author MyEclipse Persistence Tools
 */
public class TSheetType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String name;
	private String prefix;
	private String dateFormat;
	private String sep;
	private Integer seqLength;
	private Integer curSeq;

	// Constructors

	/** default constructor */
	public TSheetType() {
	}

	/** minimal constructor */
	public TSheetType(Integer id, String code, String name, String prefix,
			String dateFormat, Integer seqLength, Integer curSeq) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.prefix = prefix;
		this.dateFormat = dateFormat;
		this.seqLength = seqLength;
		this.curSeq = curSeq;
	}

	/** full constructor */
	public TSheetType(Integer id, String code, String name, String prefix,
			String dateFormat, String sep, Integer seqLength, Integer curSeq) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.prefix = prefix;
		this.dateFormat = dateFormat;
		this.sep = sep;
		this.seqLength = seqLength;
		this.curSeq = curSeq;
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

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getDateFormat() {
		return this.dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getSep() {
		return this.sep;
	}

	public void setSep(String sep) {
		this.sep = sep;
	}

	public Integer getSeqLength() {
		return this.seqLength;
	}

	public void setSeqLength(Integer seqLength) {
		this.seqLength = seqLength;
	}

	public Integer getCurSeq() {
		return this.curSeq;
	}

	public void setCurSeq(Integer curSeq) {
		this.curSeq = curSeq;
	}

}