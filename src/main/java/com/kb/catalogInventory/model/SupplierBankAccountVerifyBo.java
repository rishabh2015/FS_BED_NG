package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class SupplierBankAccountVerifyBo {
    private String ifsc;
    private Integer userId;
    private String accountNumber;
    private String accountHolderName;
    private String bankName;
}
