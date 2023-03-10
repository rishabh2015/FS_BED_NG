package com.kb.catalogInventory.datatable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="category_attribute")
@Getter
@Setter
public class CategoryAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "updated_on")
    private Date updatedOn;
    @Column(name = "created_on")
    private Date createdOn;
    @Column(name = "attribute_name")
    private String attributeName;
    @Column(name = "category_id")
    private Integer categoryId;
}
