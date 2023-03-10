package com.kb.catalogInventory.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kb.catalogInventory.exception.ControllerExceptionHandler;
import com.kb.catalogInventory.exception.InventoryException;
import com.kb.catalogInventory.model.ProductDetailUpdateBO;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.ProductInventoryRQ;
import com.kb.catalogInventory.model.ProductInventoryRQ1;
import com.kb.catalogInventory.model.ValidateSKURQ;
import com.kb.catalogInventory.service.ProductUploadService;
import com.kb.java.utils.RestApiSuccessResponse;

@RestController
@CrossOrigin
@RequestMapping(path = "/upload")
@Validated
public class UploadController extends ControllerExceptionHandler{

	@Autowired
	ProductUploadService productUploadService;

	private static final  Logger _logger = LoggerFactory.getLogger(UploadController.class);

	@PostMapping("/csv")
	public ResponseEntity<?> uploadProductFromCsv(@RequestParam("file") @NotNull MultipartFile file) throws InventoryException{
		_logger.debug("Entered uploadProductFromCsv with MultipartFile : {}",file);
		try {
			productUploadService.csvToDataTables(file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			_logger.info("Exception in uploadProductFromCsv : {}",e.getMessage());
			return ResponseEntity.ok(HttpStatus.SC_EXPECTATION_FAILED);
		}
		_logger.info("Leaving uploadProductFromCsv with Status :"+HttpStatus.SC_CREATED);
		return ResponseEntity.ok(HttpStatus.SC_CREATED);
	}

	/*@PostMapping
	public ResponseEntity<?> uploadProduct(@Valid @RequestBody @NotNull List<ProductInventoryRQ> inventories){
		_logger.info("Entered uploadProduct with ProductInventoryRQ List : {}",inventories.toString());
		List<String> responseList = new ArrayList<String>();
		List<Map<String,List<Map<String,String>>>> relativePathRequestList = new ArrayList<>();
			//for(ProductInventoryRQ pirq:inventories) {
				inventories.parallelStream().forEach(pirq -> {
					long t1 = System.currentTimeMillis();
					try {
					ProductInventoryBO pibo= productUploadService.populateProductCategory(pirq.getCategoryString(), pirq.getBrandModelString(),
							pirq.getVariationVariationOptionString(), pirq.getSupplierString(), pirq.getSupplierDetail(),
							pirq.getTotalStock(), pirq.getImageNameString(), pirq.getProductString(),
							pirq.getProductNameString(), pirq.getSKU(), pirq.getSupplierAmount(),pirq.getLegalDisclaimerDescription(),pirq.getManufacturerContactNumber()
							,pirq.getMinOrderQuantity(),pirq.getMaxOrderQuantity(),pirq.getMrp(),pirq.getProductDescription(),pirq.getSearchTerms(),
							pirq.getHsnCode(),pirq.getWeight(),pirq.getLength(),pirq.getWidth(),pirq.getHeight(),Status.QC_Pending,pirq.getOldStock(),pirq.getPriceMatrix(),
							pirq.getBrandName(),pirq.getSaleStartDate(),pirq.getSaleEndDate(),pirq.getManufacturerPartNumber(),
							pirq.getGender(),pirq.getTargertGender(),pirq.getProductTaxCode(),pirq.getHandlingTime(),pirq.getCountryOfOrigion(),pirq.getBulletPoints(),
							pirq.getTargetAudienceKeywords(),pirq.getOccasion(),pirq.getOccasionLifeStyle(),pirq.getItemName(),pirq.getProductCombinationUUID(),pirq.getIsPremium(),pirq.getProductSKU(),pirq.getSupplierCurrencyId(),false,pirq.getSupplierId());

					Map<String,List<Map<String,String>>> relativePathRequest = productUploadService.constructRelativePathRequest(pibo.getProductClubingId(), pirq.getImage1(), pirq.getImage2(), pirq.getImage3(), pirq.getImage4(),
							pirq.getImage5());
					relativePathRequestList.add(relativePathRequest);
					//responseList.add(responseMessage);
				} catch (Exception e) {
					responseList.add(e.getMessage());
					//return ResponseEntity.ok(responseList);

						_logger.error("Exception in uploadProduct ",e);
				}
					long t2 = System.currentTimeMillis();
					_logger.info("Time taken by one single listing --"+(t2-t1));
				});
				try {
				String responseMessage =productUploadService.saveProductImageinBulk(relativePathRequestList);
				responseList.add(responseMessage);
				}catch(Exception e) {
					responseList.add(e.getMessage());
					_logger.error("Exception in uploadProduct ",e);

				}
			//}
			_logger.info("Leaving uploadProduct with status : "+HttpStatus.SC_CREATED);	
		return ResponseEntity.ok(responseList);
	}*/

	@PostMapping
	public ResponseEntity<?> uploadProduct(@Valid @RequestBody @NotNull List<ProductInventoryRQ1> productInventoryRQ1List){
		_logger.info("-----------------------Entered bulkproductUploaduploadProduct : ------------");
		List<ProductInventoryRQ> inventories = new ArrayList<ProductInventoryRQ>();
		productInventoryRQ1List.stream().forEach(productInventoryRQ1->{
			inventories.add(productUploadService.mapProductInventoryObj(productInventoryRQ1));
		});
		List<String> responseList  = productUploadService.populateProductInventoryBulk(inventories);
		_logger.info("-----------------------exiting bulkproductUploaduploadProduct : ------------");

		return ResponseEntity.ok(responseList);
	}

	@PostMapping("/addInventory")
	public ResponseEntity<?> uploadProduct(@Valid @RequestBody @NotNull ProductInventoryRQ1 inventory) throws Exception{
		_logger.info("Entered uploadProduct with ProductInventoryRQ : {}",inventory);
		List<ProductInventoryBO> piBO = productUploadService.populateProductCategoryWrapper(inventory.getCategoryString(), inventory.getBrandModelString(),
				inventory.getVariationVariationOptionString(), inventory.getSupplierString(), inventory.getSupplierDetail(),
				inventory.getTotalStock(), inventory.getImageNameString(), inventory.getProductString(),
				inventory.getProductNameString(), inventory.getSKU(), inventory.getSupplierAmount(),inventory.getLegalDisclaimerDescription(),inventory.getManufacturerContactNumber()
				,inventory.getMinOrderQuantity(),inventory.getMaxOrderQuantity(),inventory.getMrp(),inventory.getProductDescription(),
				inventory.getSearchTerms(),inventory.getHsnCode(),inventory.getWeight(),inventory.getLength(),inventory.getWidth(),
				inventory.getHeight(),inventory.getOldStock(),inventory.getPriceMatrix(),inventory.getBrandName(),
				inventory.getSaleStartDate(),inventory.getSaleEndDate(),inventory.getManufacturerPartNumber(),
				inventory.getGender(),inventory.getTargertGender(),inventory.getProductTaxCode(),inventory.getHandlingTime(),inventory.getCountryOfOrigion(),inventory.getBulletPoints(),
				inventory.getTargetAudienceKeywords(),inventory.getOccasion(),inventory.getOccasionLifeStyle(),inventory.getItemName(),inventory.getProductCombinationUUID(),
				inventory.getIsPremium(),inventory.getProductSKU(),inventory.getSupplierCurrencyId(),true,inventory.getSupplierId(),inventory.getProductContryRuleId(),inventory.getSetPeices(),inventory.getColorSizeCombinationList(),inventory.getProductAttribute());


		_logger.debug("Leaving uploadProduct with product inventory BO : {}",piBO);		

		return ResponseEntity.ok(piBO);
	}

	@PostMapping("/validateProductSKU")
	public ResponseEntity<?> validateProductSKU(@RequestBody ValidateSKURQ validateSKURQ){
		_logger.info("Entered validateProductSKU with category Name : {}  and products SKUs {}",validateSKURQ.getCategoryName(),validateSKURQ.getProductSKUs()
				+" and supplier Name "+validateSKURQ.getSupplierName());
		Map<String,Object> respMap = productUploadService.validateProductSKUForBulkListing(validateSKURQ.getSupplierName(), validateSKURQ.getCategoryName(), validateSKURQ.getProductSKUs());
		return ResponseEntity.ok(respMap);
	}
	
	
	@PostMapping("/updateProductDetail")
	public ResponseEntity<?> bulkUpdateProductDetail(@RequestBody List<ProductDetailUpdateBO> productDetailUpdateBOList){
		_logger.info("Entered bulkUpdateProductDetail with ProductDetailUpdateBO : {}",productDetailUpdateBOList);
		List<String> responseList = productUploadService.bulkUpdateProductDetail(productDetailUpdateBOList);
		_logger.debug("Leaving bulkUpdateProductDetail with response : {}",responseList);		
		return ResponseEntity.ok(responseList);
	}
	
	@PostMapping("/validateSKuAndSupplierForProductUpdate")
	public ResponseEntity<?> validateSKuAndSupplier(@RequestBody List<ProductDetailUpdateBO> productDetailUpdateBOList ){
		_logger.info("Entered validateSKuAndSupplier method");
		List<Map<String,Object>> mapList = new ArrayList<>();
		for(ProductDetailUpdateBO bo:productDetailUpdateBOList) {
			Map<String,Object> map = productUploadService.validateSkuAndSupplier(bo.getSku(),bo.getSupplierId(),bo.getRowId());
			if(!(Boolean)map.get("isValid")) {
				mapList.add(map);
			}
		}
		_logger.debug("Leaving validateSKuAndSupplier with response : {}",mapList);
		RestApiSuccessResponse successResponse=new RestApiSuccessResponse(HttpStatus.SC_OK, "SKU And Supplier Id validated",mapList);		
		return ResponseEntity.ok(successResponse);
	}
	
	@GetMapping("/inactivateProduct/{uuid}")
	public ResponseEntity<?> inactivateProduct(@PathVariable("uuid") String uuid){
		_logger.info("Entered inactivateProduct with uuid : {}",uuid);
		boolean isUpdated = productUploadService.inactivateProduct(uuid);
		_logger.info("Leaving bulkUpdateProductDetail with isUpdated : {}",isUpdated);
		return ResponseEntity.ok(isUpdated);
	}
	
	
}
