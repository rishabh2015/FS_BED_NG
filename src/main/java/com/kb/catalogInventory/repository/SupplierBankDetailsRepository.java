package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.SupplierBankDetailsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierBankDetailsRepository extends JpaRepository<SupplierBankDetailsDto, Long> {

    @Modifying
    @Query(value = "update supplier_bank_details sbd set sbd.account_holder_name=:newaccountHolderName, sbd.bank_account_no=:newbankAccountNo, sbd.ifsc_code=:newifscCode, sbd.bank_name=:bankName, sbd.bank_branch_name=:newbankBranchName, sbd.bank_city=:newbankCity, sbd.bank_state=:newbankState where sbd.id=:id", nativeQuery = true)
    int updateSupplierBankDetails(@Param("id") Long id,@Param("newaccountHolderName") String accountHolderName,@Param("newbankAccountNo") String accountNo,@Param("newifscCode") String ifscCode,@Param("bankName") String bankName,@Param("newbankBranchName") String bankBranch,@Param("newbankCity") String bankCity,@Param("newbankState") String bankState);

    @Modifying
    //@Query("update SupplierBankDetailsDto sbd set sbd.isBenified=:isBenified, sbd.beneficiaryId=:beneficiaryId where sbd.id=:id")
    @Query(value = "update supplier_bank_details sbd set sbd.is_benified=:isBenified, sbd.beneficiary_id=:beneficiaryId where sbd.id=:id", nativeQuery = true)
    int updateBenificiary(@Param("isBenified") Boolean isBenified,@Param("beneficiaryId") String beneficiaryId,@Param("id") Long id);

//    @Query(value = "select * from supplier_bank_details sbd where sbd.id=:id", nativeQuery = true)
//    SupplierBankDetailsDto getBankDetailsById(@Param("id") Long id);

    Optional<SupplierBankDetailsDto> findBySupplierId(Long id);

    @Query(value = "select * from supplier_bank_details sbd where sbd.bank_account_no = :bankAccountNo and ifsc_code = :bankIfscCode", nativeQuery = true)
    SupplierBankDetailsDto getBankDetails(@Param("bankAccountNo") String bankAccountNo,@Param("bankIfscCode") String bankIfscCode);

}
