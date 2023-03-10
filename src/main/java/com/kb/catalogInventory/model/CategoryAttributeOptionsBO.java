package com.kb.catalogInventory.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder

public class CategoryAttributeOptionsBO {

    private Integer id;
    private Date updatedOn;
    private Date createdOn;
    private String name;
    private String value;
    private Integer attributeId;
    private Integer categoryId;


}
