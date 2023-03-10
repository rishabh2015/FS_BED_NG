package com.kb.catalogInventory.datatable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="dynamic_collection_attribute_master")
@Data
public class DynamicCollectionAttributeMaster {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "attribute")
    private String attribute;
    
    @Column(name = "equal_to_operator")
    private Boolean equalToOperator;
    
    @Column(name = "in_operator")
    private Boolean inOperator;
    
    @Column(name = "not_equal_to_operator")
    private Boolean notEqualToOperator;
    
    @Column(name = "not_in_operator")
    private Boolean notInOperator;
    
    @Column(name = "lesser_than_operator")
    private Boolean lesserThanOperator;
    
    @Column(name = "greater_than_operator")
    private Boolean greaterThanOperator;
    
    @Column(name = "between_operator")
    private Boolean betweenOperator;
    
    @Column(name="column_name")
    private String columnName;
    
    
}
