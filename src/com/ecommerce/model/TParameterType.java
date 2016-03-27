package com.ecommerce.model;



/**
 * TParameterType entity. @author MyEclipse Persistence Tools
 */

public class TParameterType  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String name;
     private String description;


    // Constructors

    /** default constructor */
    public TParameterType() {
    }

	/** minimal constructor */
    public TParameterType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /** full constructor */
    public TParameterType(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
   








}