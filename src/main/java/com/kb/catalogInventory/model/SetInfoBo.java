package com.kb.catalogInventory.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Data
public class SetInfoBo {
    private Integer id;
    private String color;
    private String size;
    private String  productcombinationidentifier;
}
