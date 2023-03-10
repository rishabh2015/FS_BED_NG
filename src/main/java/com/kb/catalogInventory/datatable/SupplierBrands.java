package com.kb.catalogInventory.datatable;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "supplier_brands")
@Data
public class SupplierBrands {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long Id;
    @Column(name = "brand_id")
    String brandId;
    @Column(name = "supplier_id")
    String supplierId;
    @Column(name = "is_active")
    Boolean isActive;
    @Column(name = "created_on")
    Date createdOn;
    @Column(name = "updated_on")
    Date updatedOn;
}
