package com.kb.catalogInventory.datatable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="supplier_bank_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierBankDetailsDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    @Column(name="beneficiary_id")
    private String beneficiaryId;
    @Column(name="account_holder_name")
    private String accountHolderName;
    @Column(name="bank_account_no")
    private String bankAccountNo;
    @Column(name="ifsc_code")
    private String ifscCode;
    @Column(name="bank_name")
    private String bankName;
    @Column(name="bank_branch_name")
    private String bankBranchName;
    @Column(name="bank_city")
    private String bankCity;
    @Column(name="bank_state")
    private String bankState;
    @Column(name="is_benified")
    private boolean isBenified;
    @Column(name="supplier_id")
    private Long supplierId;
}
