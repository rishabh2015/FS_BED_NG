package com.kb.catalogInventory.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kb.catalogInventory.model.*;
import com.kb.java.utils.RestApiSuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.datatable.Category;
import com.kb.catalogInventory.datatable.Supplier;
import com.kb.catalogInventory.exception.ControllerExceptionHandler;
import com.kb.catalogInventory.service.BrandService;
import com.kb.catalogInventory.service.ProductDetailService;
import com.kb.catalogInventory.service.admin.CatalogInventoryAdminService;
import com.kb.java.utils.KbRestTemplate;

@RestController
@CrossOrigin
@RequestMapping("productcategory")

@Validated
public class ProductDetailController extends ControllerExceptionHandler{
	@Autowired
	private ProductDetailService productDetailService;

	@Autowired
	KbRestTemplate restTemplate;
	@Autowired 
	private CatalogInventoryAdminService adminService;

	@Value("${CATEGORYERRORMSG}")
	private String CATEGORYERRORMSG;
	@Value("${utility.service.country.get.country.conversiondata.url}")
	private String _GetCountryConversionDataUrl;

	@Value("${utility.service.country.get.country.conversiondata.url.timeout}")
	private int _GetCountryConversionDataUrlTimeout;

	@Value("${utility.service.country.getdefaultcurrency.url}")
	private String _GetDefaultCurrencyUrl;


	@Value("${utility.service.country.getdefaultcurrency.url.timeout}")
	private int _GetDefaultCurrencyUrlTimeout;
	
	@Autowired
	private BrandService brandService;


	private final static Logger _logger = LoggerFactory.getLogger(ProductDetailController.class);

	@GetMapping
	public ResponseEntity<?> getAllProducts(@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode){
		_logger.info("Entered getAllProducts");
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		List<ProductInventoryBO> piBO =productDetailService.getAllProductsDetail(displayCountryCode,displayCurrencyCode);
		_logger.debug("Leaving getAllProducts with properties : {}",piBO);
		return ResponseEntity.ok(piBO);
	}

	@GetMapping("/productdetail/{uniqueidentifier}")
	public ResponseEntity<?> getProductByUniqueIdentifier(
			@PathVariable("uniqueidentifier") @NotBlank @Size(min=32, message="Not a Valid uniqueidentifier") String uniqueIdentifier,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode
			,@RequestParam(value = "serviceCalledFor", required = false) String serviceCalledFor,
			@RequestHeader(name = "contact", defaultValue = "0", required = false) String contact,@RequestParam(name = "callFromOrder", required = false) Boolean callFromOrder){
		_logger.info("Entered getProductByUniqueIdentifier with productUniqueIdentifier : {}",uniqueIdentifier);
		 Long t1 = System.currentTimeMillis();
        
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		ProductInventoryBO piBo =productDetailService.productDetailByUniqueIdentifier(uniqueIdentifier,displayCountryCode,displayCurrencyCode,serviceCalledFor, contact,callFromOrder);
		 Long t2 = System.currentTimeMillis(); 
		_logger.info("time taken in Product detail "+(t2-t1));
		_logger.debug("Leaving getProductByUniqueIdentifier with properties : {}",piBo);
		
		return ResponseEntity.ok(piBo);
	}

	@GetMapping("/productdetail/{supplier_id}/getPremiumDetails/{contact}")
	public ResponseEntity<?> getProductPremiumDetailsBySupplierIdAndContact(
			@PathVariable("supplier_id") Long supplierId,
			@PathVariable("contact") String contact) {
		_logger.info("Entered getProductPremiumDetailsBySupplierIdAndContact with supplier_id : {}", supplierId);
		PremiumBrandDetails premiumBrandDetails = productDetailService.getProductPremiumDetailsBySupplierIdAndContact
				(contact, supplierId);
		_logger.debug("Leaving getProductPremiumDetailsBySupplierIdAndContact with properties : {}", premiumBrandDetails);
		return ResponseEntity.ok(premiumBrandDetails);
	}

	@PostMapping("/productdetail/getPremiumDetailsBySuppliers/{contact}")
	public ResponseEntity<?> getProductPremiumDetailsBySupplierIdsAndContact(
			@RequestBody SupplierListRQ request,
			@PathVariable("contact") String contact) {
		_logger.info("Entered getProductPremiumDetailsBySupplierIdsAndContact with supplier_ids : {}", request.getSupplierIdList());
		Map<Long, PremiumBrandDetails> output = productDetailService.getProductPremiumDetailsBySupplierIdsAndContact
				(contact, request);
		_logger.debug("Leaving getProductPremiumDetailsBySupplierIdsAndContact with properties : {}", output);
		return ResponseEntity.ok(output);
	}

	@GetMapping("/variations")
	public ResponseEntity<?> getVariations(){
		_logger.info("Entered getVariations");
		Map<String, List<String>> map = adminService.variationVariationOptionList();
		_logger.debug("Leaving getVariations with properties : {}",map);
		return ResponseEntity.ok(map);
	}


	@GetMapping("/categories")
	public ResponseEntity<?> getCategories(){
		_logger.info("Entered getCategories");
		List<CategoryBO> category = adminService.categoriesAndSubcategories();
		return ResponseEntity.ok(category);
	}

	@GetMapping("/brands/{supplierId}")
	public ResponseEntity<?> getBrands(@PathVariable("supplierId") Long supplierId){
		_logger.info("Entered getBrands");
		List<String> brands = adminService.getBrandNames(supplierId);
		_logger.debug("Leaving getBrands with properties : {}",brands);
		return ResponseEntity.ok(brands);
	}

	@GetMapping("/searchKeyByCategories")
	public ResponseEntity<?> getSearchKeyByCategories(){
		_logger.info("Entered getSearchKeyByCategories");
		List<String> list = adminService.categoryKeyWords();
		_logger.debug("Leaving getSearchKeyByCategories with properties : {}",list);
		return ResponseEntity.ok(list);
	}


	@GetMapping("/categorySibling/{categoryId}")
	public ResponseEntity<?> getCategorySibling(
			@PathVariable("categoryId") @NotBlank @Size(min=1,message="Not a Valid Category Id") long categoryId){
		_logger.info("Entered getCategorySibling with categoryId :{}",categoryId);
		return adminService.categorySibling(categoryId);
	}

	@GetMapping("/availablity/{uuid}")
	public ResponseEntity<?> checkAvailiblityOfProduct(
			@PathVariable("uuid") @NotBlank @Size(min=32,message="Not a Valid Unique Identifier") String uuid){
		_logger.info("Entered checkAvailiblityOfProduct with uuid : {}",uuid);
		ProductStockBO psBO= productDetailService.getAvailableStockOfProduct(uuid);
		_logger.debug("Leaving checkAvailiblityOfProduct with properties : {}",psBO);
		return ResponseEntity.ok(psBO);
	}

	@PostMapping("/multipleProductAvailablity")
	public ResponseEntity<?> checkAvailiblityOfMultipleProduct(
			@RequestBody @NotNull List<String> uuids){
		_logger.info("Entered checkAvailiblityOfMultipleProduct with uuids : {}",uuids);
		List<ProductStockBO> psBO = productDetailService.getMultipleAvailableStockOfProduct(uuids);
		_logger.debug("Leaving checkAvailiblityOfMultipleProduct with properties : {}",psBO);
		return ResponseEntity.ok(psBO);
	}

	@GetMapping("/productVariations/{productUUID}")
	public ResponseEntity<?> getProductVariationsFromProduct(
			@PathVariable("productUUID") @NotBlank @Size(min=32,message="Not a Valid Product Unique Id") String productUUID){
		_logger.info("Entered getProductVariationsFromProduct with productUUID : {}",productUUID);
		List<ProductVariationBO> pvBO = productDetailService.getProductVariationByProductId(productUUID);
		_logger.debug("Leaving getProductVariationsFromProduct with properties : {}",pvBO);
		return ResponseEntity.ok(pvBO);
	}

	@GetMapping("/supplierDetail/{supplierId}")
	public ResponseEntity<?> getSellerDetail(
			@PathVariable("supplierId") @NotNull Long supplierId,HttpServletResponse response){		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		response.setContentType("appilcation/text");
		_logger.info("Entered getSellerDetail with supplierId : {}",supplierId);
		Supplier supplier = productDetailService.getSupplier(supplierId);
		_logger.debug("Leaving getSellerDetail with properties : {}",supplier);
		return  new ResponseEntity(supplier,headers, HttpStatus.OK);
	}

	@GetMapping("/productDetail/{supplierId}")
	public ResponseEntity<?> getProductDetail(@PathVariable("supplierId") @NotNull Long supplierId, 
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode){
		_logger.info("Entered getProductDetail with supplierId : {}",supplierId);
		List<Long> supplierIdList = new ArrayList<>();
		supplierIdList.add(supplierId);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		List<Map<String, Object>> list = productDetailService.getProductsDetail(supplierIdList, sortOrder, pageNum, pageSize,displayCountryCode,displayCurrencyCode);
		Map map = new HashMap<>();
		map.put("ProductInventoryList", list);
		return ResponseEntity.ok(map); 

	}

	@GetMapping("/AllActiveProductDetail/{supplierId}")
	public ResponseEntity<?> getAllActiveProductDetail(@PathVariable("supplierId") @NotNull Long supplierId, 
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode){
		_logger.info("Entered getProductDetail with supplierId : {}",supplierId);
		List<Long> supplierIdList = new ArrayList<>();
		supplierIdList.add(supplierId);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		List<Map<String, Object>> list = productDetailService.getAllActiveProductsDetail(supplierIdList, sortOrder, pageNum, pageSize,displayCountryCode,displayCurrencyCode);
		Map map = new HashMap<>();
		map.put("ProductInventoryList", list);
		return ResponseEntity.ok(map); 

	}

	@GetMapping("/productDetail")
	public ResponseEntity<?> getProductDetail( 
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode){
		_logger.info("Entered getProductDetail ");
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		List<Map<String, Object>> piBOList = productDetailService.getAllProducts(sortOrder, pageNum, pageSize,displayCountryCode,displayCurrencyCode);
		Map map = new HashMap<>();
		map.put("ProductInventoryList", piBOList);
		return ResponseEntity.ok(map); 

	}

	@GetMapping("/getSupplierAndProductsCount")
	public ResponseEntity<?> getcount(){
		_logger.info("Entered getcount Method");
		return ResponseEntity.ok(productDetailService.getSupplierAndProductsCount());
	}

	@GetMapping("/getInventoryActiveInactiveCount/{supplierId}")
	public ResponseEntity<?> getInventoryCount(@PathVariable("supplierId") long supplierId){

		_logger.info("Entered getInventoryCount with supplierId {}",supplierId);
		return ResponseEntity.ok(productDetailService.getInventoryCount(supplierId));
	}


	@GetMapping("/getSubmittedProducts/{supplierId}")
	public ResponseEntity<?> getSubmittedProducts(@PathVariable("supplierId") long supplierId, 
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode){

		_logger.info("Entered getSubmittedProducts with supplierId {}",supplierId);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		List<Map<String, Object>> piBOList = productDetailService.getInActiveProducts(supplierId, sortOrder, pageNum, pageSize,displayCountryCode,displayCurrencyCode);
		Map map = new HashMap<>();
		map.put("ProductInventoryList", piBOList);
		return ResponseEntity.ok(map);
	}


	@GetMapping("/filterProductOnStock/{supplierId}/{inStock}")
	public ResponseEntity<?> applyfilteronStockQuantity(@PathVariable("supplierId") long supplierId,@PathVariable("inStock") boolean inStock, 
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode){

		_logger.info("Entered applyfilteronStockQuantity with supplierId {}",supplierId);
		List<Long> supplierIdList = new ArrayList<>();
		supplierIdList.add(supplierId);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		List<Map<String, Object>> list = productDetailService.getProductsOnStockFilter(supplierIdList, inStock, sortOrder, pageNum, pageSize,displayCountryCode,displayCurrencyCode);
		Map map = new HashMap<>();
		map.put("ProductInventoryList", list);
		return ResponseEntity.ok(map);
	}

	@GetMapping("/filterProductOnIsActive/{supplierId}/{inStock}")
	public ResponseEntity<?> applyfilteronIsActive(@PathVariable("supplierId") long supplierId,@PathVariable("inStock") boolean inStock, 
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode){

		_logger.debug("Entered applyfilteronIsActive with supplierId {}",supplierId);
		List<Long> supplierIdList = new ArrayList<>();
		supplierIdList.add(supplierId);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		List<Map<String, Object>> list = productDetailService.getProductsOnIsActiveFilter(supplierIdList, inStock, sortOrder, pageNum, pageSize,displayCountryCode,displayCurrencyCode);
		Map map = new HashMap<>();
		map.put("ProductInventoryList", list);
		return ResponseEntity.ok(map);
	}

	@PostMapping("/addCategory")
	public ResponseEntity<?> addCategory(@RequestBody CategoryRQ categoryRQ){
		_logger.info("Entered addCategory with properties : {}",categoryRQ);
		Category addCategory =null;
		Map<String, Object> newResponseList = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		HashMap<String, Object> obj = new HashMap<>();
		List<String> msgList = new ArrayList<>();
		try {
			newResponseList = productDetailService.addCategory(categoryRQ);
			if (newResponseList.get("status").equals("failed")){
				msgList.add("Category name is already present in stage 1");
				obj.put("status", "FAILED");
				obj.put("message", msgList);
				_logger.info("Duplicate category name for stage 1");
				RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Category name is already present in stage 1", obj);
				return ResponseEntity.ok(restApiSuccessResponse);
			}
			
			try {
				brandService.flushCategoryCache();
				}catch(Exception e) {
					_logger.error("Exception in flushing category cache while adding Category ", e);
				}

		} catch (Exception e) {

			msgList.add(CATEGORYERRORMSG);
			obj.put("status", "FAILED");
			obj.put("message", msgList);
			//_logger.info("");
			//return ResponseEntity.ok(obj);
			RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), CATEGORYERRORMSG, obj);
			return ResponseEntity.ok(restApiSuccessResponse);
		}finally{
			try {
			brandService.flushCategoryCache();
			}catch(Exception e) {
				_logger.error("Exception in flushing category cache while adding Category ", e);
			}
		}
		_logger.debug("Leaving addCategory with properties : {}", newResponseList.get("category"));
		RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Category is successfully saved!!", newResponseList.get("category"));
		return ResponseEntity.ok(restApiSuccessResponse);
		//return ResponseEntity.ok(newResponseList.get("category"));
	}

	@GetMapping("/searchProduct/{supplierId}/{searchKeyword}")
	public ResponseEntity<?> searchProduct(@PathVariable("supplierId") long supplierId,
			@PathVariable("searchKeyword") String searchKeyword,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) throws UnsupportedEncodingException{
		_logger.info("Entered searchProduct with searchKeyword : {}",searchKeyword);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		return ResponseEntity.ok(productDetailService.getProductOnSearch(supplierId, searchKeyword,sortOrder,pageNum,pageSize,displayCountryCode,displayCurrencyCode));
	}

	@GetMapping("/setProductStatus/{uuid}/{statusId}")
	public ResponseEntity<?> approveRejectProduct(@PathVariable("statusId") String statusId,@PathVariable("uuid") String uuid){
		_logger.info("Entered approveRejectProduct with searchKeyword : {}",statusId);
		return ResponseEntity.ok(productDetailService.updateProductStatus(statusId, uuid,"",null));
	}

	@GetMapping("/searchProductByColumn/{searchColumn}")
	public ResponseEntity<?> searchProduct(@PathVariable("searchColumn") String searchColumn,
			@RequestParam("searchKeyword") String searchKeyword,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) throws UnsupportedEncodingException{
		_logger.info("Entered searchProductByColumn with searchKeyword : {} and column ",searchKeyword ,searchColumn);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		return productDetailService.getProductOnSearchByColumns( searchKeyword,searchColumn,sortOrder,pageNum,pageSize,displayCountryCode,displayCurrencyCode);
	}

	@PostMapping("/deactivateSupplier/{supplierId}")
	public ResponseEntity<?> deactivateSupplier(@PathVariable("supplierId") Long supplierId) {
		_logger.info("Entered deactivateSupplier with id : {}" , supplierId);
		return ResponseEntity.ok(productDetailService.deactivateSupplierAndSupplierProduct(supplierId));
	}


	@PutMapping("/updateproductbysearchterm")
	public ResponseEntity<?> updateproductcombinationbysearchterm(
			@RequestParam("uuid") String uuid,@RequestParam("searchterms")String searchterms) {
		_logger.info("Entered updateproductcombinationbysearchterm with id : {}" , uuid);

		return ResponseEntity.ok( new RestApiResponse(((System.currentTimeMillis())),"Success","updated successfully",  productDetailService.updateproductbysearchterms(searchterms,uuid)));
	}

	@GetMapping("/updateAverageRating/{uniqueIdentifier}")
	public ResponseEntity<?> updateAverageRating(@PathVariable("uniqueIdentifier") String uniqueIdentifier) {
		_logger.info("Entered updateAverageRating with id : {}" , uniqueIdentifier);
		return ResponseEntity.ok(productDetailService.updateAverageRating(uniqueIdentifier));
	}

	@GetMapping("/getFileUrl/{categoryName}")
	public ResponseEntity<?> getFileUrl(@PathVariable("categoryName") String categoryName) {
		_logger.info("Entered getFileUrl with id : {}" , categoryName);
		return ResponseEntity.ok(productDetailService.getFileUrl(categoryName));
	}


	public String getDefaultCurrency(String countryCode) {
		String defaultCurrencyUrl=_GetDefaultCurrencyUrl+countryCode;
		String defaultCurrency=null;
		try {

			ResponseEntity<Map> res = restTemplate.getForEntity(defaultCurrencyUrl,_GetDefaultCurrencyUrlTimeout, Map.class);
			if(res!=null && res.getBody()!=null ) {
				LinkedHashMap<String, Object> rsMap = (LinkedHashMap) (res.getBody());
				defaultCurrency= ((String) rsMap.get("data")) ;
				_logger.info("exiting  defaultCurrency {}",defaultCurrency);
			}
			if(defaultCurrency.equalsIgnoreCase("")) {
				defaultCurrency="INR";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultCurrency;
	}





	@PostMapping("/getMultiProductDetails")
	public ResponseEntity<?> getMultiProductDetails(@RequestBody List<String> UUIDS,
			@RequestParam(value = "displayCurrencyCode", required = false) String displayCurrencyCode,
			@RequestParam(value = "displayCurrencyId", required = false) Long displayCurrencyId) {
		_logger.info("Entered getMultiProductDetails with pcUUIDS : {}" , UUIDS);
		return ResponseEntity.ok(productDetailService.getMultiProductDetails(UUIDS, String.valueOf(displayCurrencyId),
				displayCurrencyCode));
	}

	@PostMapping("/getProductsBySkuList")
	public ResponseEntity<?> getProductsBySKUList(@RequestBody List<String> skuList){
		_logger.info("Entered getProductsBySKUList with skuList : {}" , skuList);
		return ResponseEntity.ok(productDetailService.getProductsOnSku(skuList));
	}
	
	@PostMapping("/getProductsBySkuListAdmin")
	public ResponseEntity<?> getProductsBySKUListAdmin(@RequestBody List<String> skuList){
		_logger.info("Entered getProductsBySkuListAdmin with skuList : {}" , skuList);
		return ResponseEntity.ok(productDetailService.getProductsOnSkuAdmin(skuList));
	}

	@PostMapping("/updateProducts")
	public ResponseEntity<?> updateProducts(@RequestBody List<ProductQcBo> qcProducts){
		_logger.info("Entered updateProducts with qcProducts : {}",qcProducts);
		return ResponseEntity.ok(productDetailService.updateProductStatusInBulk(qcProducts));
	}
	
	@PostMapping("/updateProductsStockOnCancellationOrRejection")
	public ResponseEntity<?> updateProductsStockOnCancellationOrRejection(@RequestBody List<ProductQcBo> qcProducts){
		_logger.info("Entered updateProducts with qcProducts : {}",qcProducts);
		return ResponseEntity.ok(productDetailService.updateProductStockInBulk(qcProducts));
	}
	
	

	@GetMapping("/searchProductBysupplierAndStatus")
	public ResponseEntity<?> searchProductBysupplierAndStatus(
										   @RequestParam("supplierId") String supplierId,
										   @RequestParam("productStatus") Integer productStatus,
										   @RequestParam(value = "pageNum", required = false) Integer pageNum,
										   @RequestParam(value = "pageSize", required = false) Integer pageSize,
										   @RequestParam(value = "sortOrder", required = false) String sortOrder,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) throws UnsupportedEncodingException{
		_logger.info("Entered searchProductBysupplierAndStatus with supplierId : {} and productStatus ",supplierId ,productStatus);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		return productDetailService.getProductOnSearchBySupplierIdAndStatus( supplierId,productStatus,sortOrder,pageNum,pageSize,displayCountryCode,displayCurrencyCode);
	}

	@GetMapping("/searchAllProductBysupplierAndStatus")
	public ResponseEntity<?> searchProductBysupplierNameAndStatus(
			@RequestParam("searchBy") String searchBy,
			@RequestParam("searchValue") String searchValue,
			@RequestParam("productStatus") Integer productStatus,
			@RequestParam("searchStock") String searchStock,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) throws UnsupportedEncodingException{
		_logger.info("Entered searchProductBysupplierAndStatus with searchBY : {} and productStatus ",searchBy ,productStatus);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		return productDetailService.getAllProductOnSearchBySupplierAndStatus( searchBy,searchValue,productStatus,sortOrder,pageNum,pageSize,displayCountryCode,displayCurrencyCode,searchStock);
	}
	
	@GetMapping("/searchAllProductBysupplierAndStatusWithNoPagination")
	public ResponseEntity<?> searchProductBysupplierNameAndStatus(
			@RequestParam(value = "emailId", required = false) String emailId,
			@RequestParam(value = "processingType", required = false) String processingType,
			@RequestParam("searchBy") String searchBy,
			@RequestParam("searchValue") String searchValue,
			@RequestParam("productStatus") Integer productStatus,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) throws UnsupportedEncodingException{
		_logger.info("Entered searchProductBysupplierAndStatus with searchBY : {} and productStatus ",searchBy ,productStatus);
		String displayCurrencyCode=getDefaultCurrency(displayCountryCode);
		return productDetailService.getAllProductOnSearchBySupplierAndStatusWithOutPagination( emailId,processingType,searchBy,searchValue,productStatus,sortOrder,displayCountryCode,displayCurrencyCode);
	}
}
