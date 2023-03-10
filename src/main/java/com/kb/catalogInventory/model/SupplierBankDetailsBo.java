package com.kb.catalogInventory.model;

import lombok.Data;

@Data
public class SupplierBankDetailsBo {
    private Long id;
    private Boolean beneficiaryId;
    private String accountHolderName;
    private String bankAccountNo;
    private String ifscCode;
    private String bankName;
    private String bankBranchName;
    private String bankCity;
    private String bankState;
    private Long supplierId;
}
