package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQcBo {
    private String productUUID;

    private String status;

    private String rejectReason;

    private Long adminOfQc;
    
    private int increaseStockBy;
}
