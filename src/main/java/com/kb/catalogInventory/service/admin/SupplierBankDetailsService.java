package com.kb.catalogInventory.service.admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kb.catalogInventory.datatable.Supplier;
import com.kb.catalogInventory.datatable.SupplierBankDetailsDto;
import com.kb.catalogInventory.model.*;
import com.kb.catalogInventory.repository.SupplierBankDetailsRepository;
import com.kb.catalogInventory.repository.SupplierRepository;
import com.kb.java.utils.KbRestTemplate;
import com.kb.java.utils.RestApiSuccessResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@EnableTransactionManagement
public class SupplierBankDetailsService {

    @Autowired
    private SupplierBankDetailsRepository bankDetailsRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private KbRestTemplate restTemplate;

    @Value("${cashfree.addbenificiary.url}")
    private String cashFreeAddBenificiaryUrl;
    @Value("${verify.bankaccount.url}")
    private String verifyBankAccountUrl;


    @Transactional(readOnly = false)
    public Object saveSupplierBankDetails(SupplierBankDetailsBo bankDetailsBo) {
        SupplierBankDetailsDto bankDetailsDto = null;
        SupplierBankDetailsRQ bankDetailsRQ = null;
        SupplierBankDetailsCashFreeBo bankDetailsCashFreeBo = null;
        RestApiSuccessResponse restApiSuccessResponse = null;
        SupplierBankDetailsRS bankDetailsRS = null;
        SupplierBankAccountVerifyBo bankAccountVerifyBo = null;
        Supplier supplier = null;
        boolean flag = false;

        bankDetailsDto = new SupplierBankDetailsDto();
        bankDetailsRQ = new SupplierBankDetailsRQ();

        supplier = supplierRepository.findByIdAndIsActive(bankDetailsBo.getSupplierId(), true);
        if(supplier.getPhone() != null && supplier.getEmail() != null && supplier.getSupplierAddress() != null && supplier.getPin_code() !=null) {


            bankAccountVerifyBo = SupplierBankAccountVerifyBo.builder().ifsc(bankDetailsBo.getIfscCode()).accountNumber(bankDetailsBo.getBankAccountNo()).userId(bankDetailsBo.getSupplierId().intValue()).accountHolderName(bankDetailsBo.getAccountHolderName()).bankName(bankDetailsBo.getBankBranchName()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);

            HttpEntity entity = new HttpEntity(bankAccountVerifyBo, headers);
            Map<String, Object> response = restTemplate.postForEntity(verifyBankAccountUrl, bankAccountVerifyBo, 60000, Map.class).getBody();
            Map<String, String> verifyResponse = (Map<String, String>) response.get("body");



            if (verifyResponse.get("data").equals("true")) {

           /* if (ObjectUtils.isNotEmpty(bankDetailsBo.getId())) {
                BeanUtils.copyProperties(bankDetailsBo, bankDetailsRQ);
                int updateResponse = bankDetailsRepository.updateSupplierBankDetails(bankDetailsRQ.getId(), bankDetailsRQ.getAccountHolderName(), bankDetailsRQ.getBankAccountNo(), bankDetailsRQ.getIfscCode(), bankDetailsRQ.getBankName(), bankDetailsRQ.getBankBranchName(), bankDetailsRQ.getBankCity(), bankDetailsRQ.getBankState());
                if (updateResponse == 1) {
                    BeanUtils.copyProperties(bankDetailsRQ, bankDetailsDto);
                    bankDetailsRS = SupplierBankDetailsRS.builder().id(bankDetailsDto.getId()).accountHolderName(bankDetailsDto.getAccountHolderName()).bankAccountNo(bankDetailsDto.getBankAccountNo()).ifscCode(bankDetailsDto.getIfscCode()).bankName(bankDetailsDto.getBankName()).bankBranchName(bankDetailsDto.getBankBranchName()).bankCity(bankDetailsDto.getBankCity()).bankState(bankDetailsDto.getBankState()).build();
                    restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "bank details updatetd successfully", bankDetailsRS);
                } else {
                    restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "bank details cannot be updatetd!! Please try again later", bankDetailsRS);
                }
            } else {*/

                restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "bank account verified successfully", bankAccountVerifyBo);
                SupplierBankDetailsDto existingSupplier = bankDetailsRepository.getBankDetails(bankDetailsBo.getBankAccountNo(), bankDetailsBo.getIfscCode());

                if (existingSupplier == null) {
                    bankDetailsDto.setBeneficiaryId(null);
                    bankDetailsDto.setAccountHolderName(bankDetailsBo.getAccountHolderName());
                    bankDetailsDto.setBankAccountNo(bankDetailsBo.getBankAccountNo());
                    bankDetailsDto.setIfscCode(bankDetailsBo.getIfscCode());
                    bankDetailsDto.setBankName(bankDetailsBo.getBankName());
                    bankDetailsDto.setBankBranchName(bankDetailsBo.getBankBranchName());
                    bankDetailsDto.setBankCity(bankDetailsBo.getBankCity());
                    bankDetailsDto.setBankState(bankDetailsBo.getBankState());
                    bankDetailsDto.setBenified(false);
                    bankDetailsDto.setSupplierId(bankDetailsBo.getSupplierId());


                    bankDetailsDto = bankDetailsRepository.save(bankDetailsDto);
                    flag = true;
                } else {
                    restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "bank detail is already registered", "");
                }

                /*}*/
                if (flag) {

                    String beneId = supplier.getId() + "_" + bankDetailsDto.getId();

                    bankDetailsCashFreeBo = SupplierBankDetailsCashFreeBo.builder().beneId(beneId).bankAccount(bankDetailsBo.getBankAccountNo()).phone(supplier.getPhone()).email(supplier.getEmail()).name(bankDetailsBo.getAccountHolderName()).ifsc(bankDetailsBo.getIfscCode()).address1(supplier.getSupplierAddress()).city(bankDetailsBo.getBankCity()).state(bankDetailsBo.getBankState()).pincode(supplier.getPin_code()).build();
                    HttpEntity cashfreeEntity = new HttpEntity(bankDetailsCashFreeBo, headers);
                    ResponseEntity<String> successResponse = restTemplate.postForEntity(cashFreeAddBenificiaryUrl, bankDetailsCashFreeBo, 60_000, String.class);
                    String successResponseString = successResponse.getBody();
                    JsonObject objectString = JsonParser.parseString(successResponseString).getAsJsonObject();
                    if (objectString.get("status").getAsInt() == 200) {
                        int updateValue = bankDetailsRepository.updateBenificiary(true, beneId, bankDetailsDto.getId());
                        restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), objectString.get("data").getAsString(), bankDetailsCashFreeBo);
                    } else if (objectString.get("status").getAsInt() == 409) {
                        int updateValue = bankDetailsRepository.updateBenificiary(true, beneId, bankDetailsDto.getId());
                        restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "bank details saved, account is previously registered with cashfree", bankDetailsCashFreeBo);
                    } else {
                        restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), objectString.get("data").getAsString(), "");
                    }
                }
            } else {
                restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "bank account cannot be verified!! please try again", bankAccountVerifyBo);
            }

        }else {
            restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "supplier details is not completely filled!! please fill supplier details before proceding", "");
        }
        return restApiSuccessResponse;
    }

    @Transactional(readOnly = false)
    public Object getSupplierBankDetails(Long id){
        SupplierBankDetailsRQ bankDetailsRQ = null;
        RestApiSuccessResponse restApiSuccessResponse = null;

        Optional<SupplierBankDetailsDto> bankDetailsDto = bankDetailsRepository.findBySupplierId(id);
        if(bankDetailsDto.isPresent()) {
            bankDetailsRQ = new SupplierBankDetailsRQ();
            bankDetailsRQ.setId(bankDetailsDto.get().getId());
            bankDetailsRQ.setBankAccountNo(bankDetailsDto.get().getBankAccountNo());
            bankDetailsRQ.setIfscCode(bankDetailsDto.get().getIfscCode());
            bankDetailsRQ.setBankName(bankDetailsDto.get().getBankName());
            bankDetailsRQ.setAccountHolderName(bankDetailsDto.get().getAccountHolderName());
            bankDetailsRQ.setBankBranchName(bankDetailsDto.get().getBankBranchName());
            bankDetailsRQ.setBankCity(bankDetailsDto.get().getBankCity());
            bankDetailsRQ.setBankState(bankDetailsDto.get().getBankState());
            bankDetailsRQ.setSupplierId(bankDetailsDto.get().getSupplierId());
            bankDetailsRQ.setBeneficiaryId(bankDetailsDto.get().getBeneficiaryId());
            restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "supplier bank details fetched successfully", bankDetailsRQ);
        }
        else{
            restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "supplier bank details cannot be fetched!! please try again", bankDetailsRQ);
        }
        return restApiSuccessResponse;
    }

    public Boolean deleteBankAccount(Long id){
        bankDetailsRepository.deleteById(id);
        return true;
    }
}
