package com.kb.catalogInventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeBo implements Serializable {

    private String pattern;
    private String sleevelength;
    private String chestsize;
    private String hemline;
    private String lengthsize;
    private String wavepattern;
    private String sleevestyling;
    private String fitorshape;
    private String printorpatterntype;
    private String length;
    private String closure;
    private String noofpocket;
    private String fabric;
    private String neck;
    private String design;
    @JsonIgnore
    private String productCombinationId;


}