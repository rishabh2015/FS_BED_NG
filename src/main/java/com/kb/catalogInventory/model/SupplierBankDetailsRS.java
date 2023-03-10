package com.kb.catalogInventory.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierBankDetailsRS {
    private Long id;
    private String accountHolderName;
    private String bankAccountNo;
    private String ifscCode;
    private String bankName;
    private String bankBranchName;
    private String bankCity;
    private String bankState;
}
