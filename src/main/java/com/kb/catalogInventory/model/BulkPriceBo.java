package com.kb.catalogInventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkPriceBo {

    @JsonIgnore
    private String deliveryCountryCode="IN";

    private Integer quantity;

    private String productCombinationId;

    private String pickUpPincode;

    private String deliveryPincode;

    private Long loggedInUserId;

    private String couponCode;

    @JsonIgnore
    private String loggedInCountryCode="IN";

    private String lowestProductCategoryName;

    private Boolean isSupplierPrice=false;

}
