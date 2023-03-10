package com.kb.catalogInventory.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.gson.Gson;
import com.kb.catalogInventory.datatable.SupplierBankDetailsDto;
import com.kb.catalogInventory.model.SupplierBankDetailsBo;
import com.kb.catalogInventory.service.admin.SupplierBankDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kb.catalogInventory.exception.ControllerExceptionHandler;
import com.kb.catalogInventory.model.CreateSupplierRQ;
import com.kb.catalogInventory.model.FetchAllSupplierRQ;
import com.kb.catalogInventory.model.UpdateSupplierRQ;
import com.kb.catalogInventory.service.SupplierService;

@RestController
@CrossOrigin
@RequestMapping("/supplier")
public class SupplierController extends ControllerExceptionHandler {
	Logger logger= LoggerFactory.getLogger(SupplierController.class);
	@Autowired
	private SupplierService supplierService;

	private static final Gson gson = new Gson();

	@Autowired
	private SupplierBankDetailsService bankDetailsService;

	@PostMapping("/create")
	public ResponseEntity<?> createSupplier(@RequestBody CreateSupplierRQ request){
		try {
			return ResponseEntity.ok(supplierService.createSupplier(request));
		}catch (Exception e){
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{supplier_id}")
	public ResponseEntity<?> updateSupplier(@PathVariable("supplier_id") Long supplierId,
			@RequestBody UpdateSupplierRQ request) throws Exception{
		return ResponseEntity.ok(supplierService.updateSupplier(supplierId, request));
	}

	@PutMapping("/updatesupplierName")
	public ResponseEntity<?> updatesupplierName() throws Exception{
		return ResponseEntity.ok(supplierService.updateSupplierNameByFirstNameLastName());
	}

	@PostMapping("/getAll")
	public ResponseEntity<?> getAllSupplier(@RequestBody FetchAllSupplierRQ request) throws Exception{
		return ResponseEntity.ok(supplierService.getAllSupplier(request));
	}



	@GetMapping("/searchSupplierByColumn/{searchColumn}/{searchKeyword}")
	public Object searchSupplier(@PathVariable("searchColumn") String searchColumn,
			@PathVariable("searchKeyword") String searchKeyword)  {
		logger.info("Entered searchProductByColumn with searchKeyword : {} and column {}",searchKeyword ,searchColumn);

		Object obj=null;
		try {
			obj= supplierService.getSupplierBySearch(searchColumn,searchKeyword);
		}catch (Exception e){
			logger.error("",e);
		}
		return (obj);
	}

	@PostMapping("/detail/byproductIds")
	public ResponseEntity<?> getSUpplierDetailFromProduct(@RequestBody List<String> productCombinationIds) throws Exception{
		return ResponseEntity.ok(supplierService.getsupplierDetailFromProduct(productCombinationIds));
	}
	
	@PostMapping("/all/detail/byproductIds")
	public ResponseEntity<?> getAllSupplierDetailFromProduct(@RequestBody List<String> productCombinationIds) throws Exception{
		return ResponseEntity.ok(supplierService.getAllSupplierDetailFromProduct(productCombinationIds));
	}


	@GetMapping("/getSupplier/{supplierid}")
	public ResponseEntity<?> getSupplier(@PathVariable("supplierid")String supplierId) throws Exception {

		return ResponseEntity.ok(supplierService.getSupplier(supplierId));
	}

	@PostMapping("/getSupplierList")
	public ResponseEntity<?> getSupplierList(@RequestBody List<String> supplierIdList) throws Exception {

		return ResponseEntity.ok(supplierService.getSupplierList(supplierIdList));
	}


	@GetMapping("/getlegalname/{supplierid}/{name}")
	public ResponseEntity<?> getLegalName(@PathVariable("supplierid")String supplierid,
			@PathVariable("name")String name) throws Exception {

		return ResponseEntity.ok(supplierService.SaveLegalName(supplierid,name));
	}

	@GetMapping("/getSupplierByEmail/{emailId}")
	public ResponseEntity<?> getSupplierByEmail(@PathVariable("emailId")String emailId) throws Exception {

		return ResponseEntity.ok(supplierService.getSupplierByEmail(emailId));
	}


	@GetMapping("/coupon/allSupplier")
	public ResponseEntity<?> getallSupplier() throws Exception {
		return ResponseEntity.ok(supplierService.getAllSupplier());
	}

	@GetMapping("/coupon/supplier/brands/{supplierid}")
	public ResponseEntity<?> getSupplierBrands(@PathVariable("supplierid")Long supplierId) throws Exception {
		return ResponseEntity.ok(supplierService.getBrandsOfSupplier(supplierId));
	}

	@PostMapping("/savebankdetails")
	public Object saveSupplierBankDetails(@RequestBody SupplierBankDetailsBo bankDetailsBo){
		return  bankDetailsService.saveSupplierBankDetails(bankDetailsBo);
	}

	@GetMapping("/getbankdetails/{id}")
	public Object getSupplierBankDetails(@PathVariable("id") Long id){
		return  new ResponseEntity<>(bankDetailsService.getSupplierBankDetails(id), HttpStatus.OK);
	}

	@GetMapping("/deletebankdetails/{id}")
	public Boolean deleteSupplierBankDetailsById(@PathVariable("id") Long id){
		return  ResponseEntity.ok(bankDetailsService.deleteBankAccount(id)).getBody();
	}

	@GetMapping("/supplierEmaildetails/{supplierId}")
	public Object supplierEmaildetailsDetailsById(@PathVariable("supplierId") Long supplierId) throws Exception {
		return  ResponseEntity.ok(supplierService.getEmailOfSupplier(supplierId)).getBody();
	}


	@GetMapping("/updatelegalnamepost")
	public ResponseEntity<?> updatelegalnamePost(){
		Object obj=null;
		try {

			supplierService.UpdateSupplierLegalName();
		}catch (Exception e){
			logger.error("Exception while fetching profile :::::: ",e);

		}
		return ResponseEntity.ok(obj);
	}

}
