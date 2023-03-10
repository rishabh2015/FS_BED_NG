package com.kb.catalogInventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class CategoryAttributeRS {
    private Integer id;
    private Date updatedOn;
    private Date createdOn;
    private String attributeName;
    private Integer categoryId;
    private Map<String,List<String>> attributesNames;
}
