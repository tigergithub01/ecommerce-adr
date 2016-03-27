package com.ecommerce.model;

/**
 * TVip entity. @author MyEclipse Persistence Tools
 */
public class TVip implements java.io.Serializable {

	// Fields

	private Integer id;
	private String vip_no;
	private String name;
	private String id_card;
	private String last_login_date;
	private String password;
	private Integer parent_id;
	private String email;
	private Integer email_verify_flag;
	private Integer status;
	private String register_date;
	private String parent_vip_no;
	public String verifyCode;
	
	//transite fields
	public Integer direct_flag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVip_no() {
		return vip_no;
	}

	public void setVip_no(String vip_no) {
		this.vip_no = vip_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public String getLast_login_date() {
		return last_login_date;
	}

	public void setLast_login_date(String last_login_date) {
		this.last_login_date = last_login_date;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEmail_verify_flag() {
		return email_verify_flag;
	}

	public void setEmail_verify_flag(Integer email_verify_flag) {
		this.email_verify_flag = email_verify_flag;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRegister_date() {
		return register_date;
	}

	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}

	public String getParent_vip_no() {
		return parent_vip_no;
	}

	public void setParent_vip_no(String parent_vip_no) {
		this.parent_vip_no = parent_vip_no;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getDirect_flag() {
		return direct_flag;
	}

	public void setDirect_flag(Integer direct_flag) {
		this.direct_flag = direct_flag;
	}
	
	

}