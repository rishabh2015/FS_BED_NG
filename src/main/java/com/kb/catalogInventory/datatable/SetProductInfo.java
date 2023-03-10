package com.kb.catalogInventory.datatable;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name="set_info")
public class SetProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "color")
    private String color;
    @Column(name = "size")
    private String size;
    @Column(name = "qty_per_set")
    private Integer qtyPerSet;
    @Column(name = "productcombinationidentifier")
    private String  productcombinationidentifier;
}
