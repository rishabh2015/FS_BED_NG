package com.kb.catalogInventory.datatable;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="similar_products")
public class SimilarProductDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    private String productCombinationUniqueId;

    @Column(name = "similar_products_list", columnDefinition = "json")
    private String similarProductData;

    private Date createdOn;

    private Date updatedOn;
}
