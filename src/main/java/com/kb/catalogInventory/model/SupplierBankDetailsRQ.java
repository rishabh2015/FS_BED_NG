package com.kb.catalogInventory.model;

import lombok.Data;

@Data
public class SupplierBankDetailsRQ {
    private Long id;
    private String accountHolderName;
    private String bankAccountNo;
    private String ifscCode;
    private String bankName;
    private String bankBranchName;
    private String bankCity;
    private String bankState;
    private Long supplierId;
    private String beneficiaryId;
}
