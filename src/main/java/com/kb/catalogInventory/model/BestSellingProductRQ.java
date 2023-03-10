package com.kb.catalogInventory.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestSellingProductRQ {
    private Long bestSellingProductDetailId;
    private String productId;
    private Long sellingCount;
    private Date updatedDate;



/*

    public void setBestSellingProductDetailId(String bestSellingProductDetailId) {
        this.bestSellingProductDetailId = bestSellingProductDetailId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSellingCount(Integer sellingCount) {
        this.sellingCount = sellingCount;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getBestSellingProductDetailId() {
        return this.bestSellingProductDetailId;
    }

//    public String getProductId() {
//        return this.productId;
//    }

    public Integer getSellingCount() {
        return this.sellingCount;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }*/
}
