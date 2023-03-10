package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class PremiumBrandDetails {
    private Boolean isUserLoginRequired;
    private Boolean isPremiumBrand;
    private Boolean isUserBelongToBrand;
}
