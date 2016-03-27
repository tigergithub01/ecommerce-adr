package com.ecommerce.model;



/**
 * TSaleAgreement entity. @author MyEclipse Persistence Tools
 */

public class TSaleAgreement  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String content;


    // Constructors

    /** default constructor */
    public TSaleAgreement() {
    }

    
    /** full constructor */
    public TSaleAgreement(String content) {
        this.content = content;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    

    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
   








}