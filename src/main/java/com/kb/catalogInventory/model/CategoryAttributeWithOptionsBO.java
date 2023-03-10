package com.kb.catalogInventory.model;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryAttributeWithOptionsBO {

    private Integer id;
    private Date updatedOn;
    private Date createdOn;
    private String attributeName;
    private Integer categoryId;

    private Integer attributeOptionsId;
    private String name;
    private String value;
    private Integer attributeId;

}
