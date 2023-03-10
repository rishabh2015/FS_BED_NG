package com.kb.catalogInventory.datatable;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="category_attribute_options")
@Getter
@Setter
public class CategoryAttributeOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "attribute_id")
    private Integer attributeId;
}
