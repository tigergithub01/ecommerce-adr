package com.ecommerce.model;

import java.sql.Timestamp;


/**
 * TProductComment entity. @author MyEclipse Persistence Tools
 */

public class TProductComment  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Integer productId;
     private Integer vipId;
     private Integer resultTypeId;
     private String content;
     private Timestamp commentDate;
     private String ipAddr;


    // Constructors

    /** default constructor */
    public TProductComment() {
    }

	/** minimal constructor */
    public TProductComment(Integer productId, Timestamp commentDate, String ipAddr) {
        this.productId = productId;
        this.commentDate = commentDate;
        this.ipAddr = ipAddr;
    }
    
    /** full constructor */
    public TProductComment(Integer productId, Integer vipId, Integer resultTypeId, String content, Timestamp commentDate, String ipAddr) {
        this.productId = productId;
        this.vipId = vipId;
        this.resultTypeId = resultTypeId;
        this.content = content;
        this.commentDate = commentDate;
        this.ipAddr = ipAddr;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    

    public Integer getProductId() {
        return this.productId;
    }
    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    

    public Integer getVipId() {
        return this.vipId;
    }
    
    public void setVipId(Integer vipId) {
        this.vipId = vipId;
    }
    

    public Integer getResultTypeId() {
        return this.resultTypeId;
    }
    
    public void setResultTypeId(Integer resultTypeId) {
        this.resultTypeId = resultTypeId;
    }
    

    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    

    public Timestamp getCommentDate() {
        return this.commentDate;
    }
    
    public void setCommentDate(Timestamp commentDate) {
        this.commentDate = commentDate;
    }
    

    public String getIpAddr() {
        return this.ipAddr;
    }
    
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
   








}