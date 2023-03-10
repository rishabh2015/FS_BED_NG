package com.kb.catalogInventory.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierBankDetailsCashFreeBo {
    private String beneId;
    private String name;
    private String email;
    private String phone;
    private String bankAccount;
    private String ifsc;
    private String address1;
    private String city;
    private String state;
    private String pincode;
}
