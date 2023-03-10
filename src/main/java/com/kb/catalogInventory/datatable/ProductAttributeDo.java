package com.kb.catalogInventory.datatable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_attribute")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String productCombinationId;
    private String pattern;
    private String sleeveLength;
    private String chestSize;
    private String hemline;
    private String lenghtSize;
    private String wavePattern;
    private String sleeveStyling;
    private String fitOrShape;
    private String printOrPatternType;
    private String length;
    private String closure;
    private String noOfPocket;
    private String fabric;
    private String neck;
    private String design;


}
