package com.kb.catalogInventory.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.model.BrandBO;
import com.kb.catalogInventory.model.BrandReviewsBO;
import com.kb.catalogInventory.service.BrandService;
import com.kb.java.utils.RestApiErrorResponse;
import com.kb.java.utils.RestApiSuccessResponse;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@RequestMapping("/brand")
@Validated
@Log4j2
public class BrandController {

	@Autowired
	private BrandService brandService;

	@ApiOperation("It take brandname as a parameter and Will return all brand on basis of contains name like %brandName%")
	@GetMapping(value = "/getBrandInfo", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getBrandInfo(@RequestParam(value = "brandName", required = false) String brandName,
															   @RequestHeader(name = "phonenumber", defaultValue = "0", required = false) String contact) {
		log.info("getting info of brand name {}", brandName);
		return getsuccessResponse(brandService.getBrandByName(brandName, contact), "Brand Fetched Successfully!");
	}


	@ApiOperation("It take brandId as a parameter and Will return all brand on basis of brandId")
	@GetMapping(value = "/getBrandInfo/{brandId}", produces = MediaType.APPLICATION_JSON)
	public Object  getBrandInfoById(@PathVariable(value = "brandId", required = false) String brandId) {
		log.info("getting info of brand brandId {}", brandId);
		return brandService.getBrandInfoById(brandId);
	}


	@ApiOperation("This Will give you All Brands")
	@GetMapping(value = "/getAllBrands", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getAllBrands(@RequestHeader("countrycode") String countryCode) {
		log.info("getting getAllBrands");
		return getsuccessResponse(brandService.getAllBrands(countryCode), "Brands Fetched Successfully!");
	}

	@ApiOperation("This Will give you All Brands")
	@GetMapping(value = "/getAllBrandsForAdmin", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getAllBrandsForAdmin(@RequestParam(value = "pageNum", required = false) Integer pageNum,
																	   @RequestParam(value = "pageSize", required = false) Integer pageSize,
																	   @RequestParam(value = "sortOrder", required = false) String sortOrder) {
		log.info("getting getAllBrandsForAdmin");
		return getsuccessResponse(brandService.getAllBrandsForAdmin(sortOrder, pageNum, pageSize), "Brands Fetched Successfully!");
	}

	@ApiOperation("Will use to save brand.")
	@PostMapping(value = "/saveBrand", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> saveBrand(@RequestBody BrandBO brandBo) {
		log.info("save brand Info invoked");
		try {
			String finalSku = "";
			String[] sku = brandBo.getBestSellingSKU().trim().replaceAll("\n", ",").split(",");
			if (sku.length > 0) {
				for (int i = 0; i < sku.length; i++) {
					if (i < (sku.length - 1)) {
						finalSku = finalSku + sku[i].trim() + ",";
					} else {
						finalSku = finalSku + sku[i].trim();
					}
				}
			} else {
				finalSku = brandBo.getBestSellingSKU().trim();
			}

			brandBo.setBestSellingSKU(finalSku);
		} catch (Exception e) {

		}
		/*List<String> invalidSkus = brandService.isSKUValid(brandBo.getSupplierId(), brandBo.getBestSellingSKU());
		if(invalidSkus.size()>0) {
			return getErrorResponse(invalidSkus.toString(),"Given SKUs belong to a different Supplier");
		}else {
		return getsuccessResponse(brandService.save(brandBo),"Brand Saved Successfully!");
		}
*/
		return getsuccessResponse(brandService.save(brandBo), "Brand Saved Successfully!");
	}

	@ApiOperation("Will use to Update brand.")
	@PatchMapping(value = "/patch", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> patch(@RequestBody BrandBO brandBo) {
		log.info("update brand Info invoked");
		try {
			String finalSku = "";
			String[] sku = brandBo.getBestSellingSKU().trim().replaceAll("\n", ",").split(",");
			if (sku.length > 0) {
				for (int i = 0; i < sku.length; i++) {
					if (i < (sku.length - 1)) {
						finalSku = finalSku + sku[i].trim() + ",";
					} else {
						finalSku = finalSku + sku[i].trim();
					}
				}
			} else {
				finalSku = brandBo.getBestSellingSKU().trim();
			}

			brandBo.setBestSellingSKU(finalSku);
		} catch (Exception e) {

		}

		List<String> invalidSkus = brandService.isSKUValid(brandBo.getSupplierId(), brandBo.getBestSellingSKU());
		if (invalidSkus.size() > 0) {
			return getErrorResponse(invalidSkus.toString(), "Given SKUs belong to a different Supplier");
		} else {
			return getsuccessResponse(brandService.patch(brandBo), "Brand Updated Successfully!");
		}
	}

	@ApiOperation("it take brandId as aparameter and setActive 0  which indicate product is inactive")
	@PostMapping(value = "/deleteBrand", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> deleteBrand(@RequestParam long brandId) {
		log.info("deleteBrand invoked");
		return getsuccessResponse(brandService.changeActiveStatus(brandId, false), "Deleted Successfully.");
	}

	@ApiOperation("it take brandId as aparameter and setActive 1  which indicate product is active")
	@PostMapping(value = "/activateBrand", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> activateBrand(@RequestParam long brandId) {
		log.info("activateBrand invoked");
		return getsuccessResponse(brandService.changeActiveStatus(brandId, true), "Status Changed Successfully !!");
	}

	@ApiOperation("will return all productCombination of brand sku")
	@GetMapping(value = "/getAllPopularSku/{brandId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getAllPopularSku(@PathVariable("brandId") long brandId, @RequestHeader("countrycode") String displayCountryCode) {
		log.info("getAllPopularSku invoked brandId : {}", brandId);
		return getsuccessResponse(brandService.getAllPopularSku(brandId, displayCountryCode));
	}

	@ApiOperation("will return all popular productCombination of brand best selling sku")
	@GetMapping(value = "/getAllPopularSku", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getAllPopularSku() {
		log.info("getAllPopularSku invoked : {}");
		return getsuccessResponse(brandService.getAllBestSellingSku());
	}

	@ApiOperation("Will Return All BrandModelCategory belongs from any supplier")
	@GetMapping(value = "/getAllBrandsForSupplier/{supplierId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getAllSupplierBrand(@PathVariable("supplierId") long supplierId) {
		log.info("getAllSupplierBrand invoked brandId : {}", supplierId);
		return getsuccessResponse(brandService.getAllSupplierBrand(supplierId));
	}

	@ApiOperation("Will Return All ProductCombination belongs from any brand")
	@GetMapping(value = "/getAllProduct/{brandId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getProductCombinationByBrand(@PathVariable("brandId") long brandId, @RequestParam(value = "pageNum", required = false) Integer pageNum,
																			   @RequestParam(value = "pageSize", required = false) Integer pageSize,
																			   @RequestParam(value = "sortOrder", required = false) String sortOrder) {
		log.info("getProductCombinationByBrand invoked brandId : {}", brandId);
		return getsuccessResponse(brandService.getProductCombinationByBrand(brandId, sortOrder, pageNum, pageSize));
	}

	@ApiOperation("Will Return All BrandModelCategory belongs from any brand")
	@GetMapping(value = "/getbrandModelCategoryByBrand/{brandId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getbrandModelCategoryByBrand(@PathVariable("brandId") long brandId) {
		log.info("getbrandModelCategoryByBrand invoked brandId : {}", brandId);
		return getsuccessResponse(brandService.getbrandModelCategoryByBrand(brandId));
	}

	@ApiOperation("it take brandId as paparameter and review object in requestparam and create review for brand")
	@PostMapping(value = "/addReview/{brandId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> addReview(@PathVariable("brandId") long brandId, @RequestBody BrandReviewsBO brandReviewsBO) {
		log.info("addReview invoked {}", brandId);
		return getsuccessResponse(brandService.addReview(brandId, brandReviewsBO));
	}

	@ApiOperation("Will Return All Review belongs from any brand")
	@GetMapping(value = "/getBrandReview/{brandId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getBrandReview(@PathVariable("brandId") long brandId) {
		log.info("getBrandReview invoked brandId : {}", brandId);
		return getsuccessResponse(brandService.getBrandReview(brandId));
	}

	@ApiOperation("It take SupplierId as a parameter and Will return  brand on basis of supplierId")
	@GetMapping(value = "/{supplierId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getBrandInfoBySupplier(@PathVariable("supplierId") Long supplierId) {
		log.info("getting info of brand of supplier {}", supplierId);
		List<BrandBO> bo = null;
		try {
			bo = brandService.getBrandBySupplier(supplierId);
			return getsuccessResponse(bo, "Brand Fetched Successfully!");
		} catch (Exception e) {
			return getsuccessResponse(bo, "supplier don't have brands");
		}

	}

	@ApiOperation("will return Category and subcategory both")
	@GetMapping(value = "/getAllCategory", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> getAllCategory(@RequestHeader("countrycode") String countryCode) {
		log.info("getAllCategory invoked");
		return ResponseEntity.ok(brandService.getAllCategory(countryCode, false));
	}

	@ApiOperation("will return Category and subcategory both with cache")
	@GetMapping(value = "/refreshCategoriesInCache", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> refreshCategoriesInCache(@RequestHeader("countrycode") String countryCode) {
		log.info("getAllCategory invoked");
		return ResponseEntity.ok(brandService.getAllCategory(countryCode, true));
	}

	@ApiOperation("It will return all top level category")
	@GetMapping(value = "/getTopCategory", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getTopCategory() {
		log.info("getAllCategory invoked");
		return getsuccessResponse(brandService.getTopCategory(), "Category Fetched Successfully!");
	}

	@ApiOperation("It take categoryId as a parameter and Will return  all last level of catalogary")
	@GetMapping(value = "/getSubCategoryById/{categoryId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getSubCategoryById(@PathVariable("categoryId") Long categoryId, @RequestHeader("countrycode") String countryCode) {
		log.info("getAllCategory invoked");
		return getsuccessResponse(brandService.getAllCategory(categoryId, countryCode), "Category Fetched Successfully!");
	}

	@GetMapping(value = "/getAllBrandProduct/{brandId}", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getAllBrandProduct(@PathVariable("brandId") Long brandId, @RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) {
		log.info("getAllBrandProduct invoked");
		return getsuccessResponse(brandService.getAllBrandProduct(brandId, displayCountryCode, displayCountryCode), "Product Fetched Successfully!");
	}

	private ResponseEntity<RestApiSuccessResponse> getsuccessResponse(Object obj) {
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(), "Success", obj));
	}

	private ResponseEntity<RestApiSuccessResponse> getsuccessResponse(Object obj, String msg) {
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(), msg, obj));
	}

	private ResponseEntity<RestApiErrorResponse> getErrorResponse(String obj, String msg) {
		return ResponseEntity.ok(new RestApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, obj));
	}


	@ApiOperation("will Flush Categories from Cache ")
	@GetMapping(value = "/flushAllCategory")
	public ResponseEntity<RestApiSuccessResponse> flushAllCategoryFromCache() {
		log.info("flushAllCategory invoked");
		brandService.flushCategoryCache();
		return getsuccessResponse(null, "Category Flushed Successfully!");
	}


	@ApiOperation("add supplier to a brands")
	@PostMapping("adddBrandToSupplier")
	public Object addbrandToSupplier(@RequestParam("supplierEmail") String supplierEmail, @RequestParam("brandId") String brandId) {
		log.info("inside addbrandToSupplier supplierId>>" + supplierEmail + "brandId>>" + brandId);
		Object res = null;
		try {
			res = brandService.addBrandToSupplier(supplierEmail, brandId);
			log.info("res for add supplier" + res);
			return res;
		} catch (Exception e) {
			RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Couldn't add the brand to supplier", null);
			log.error("error occur while adding the brand to the supplier>>", e);
			return new ResponseEntity<>(successResponse, HttpStatus.OK);
		}

	}


	@ApiOperation("remove supplier from a brands")
	@PostMapping("removeBrandToSupplier")
	public Object removeBrandToSupplier(@RequestParam("supplierId") String supplierId, @RequestParam("brandId") String brandId) {
		log.info("inside removeBrandToSupplier supplierId>>" + supplierId + "brandId>>" + brandId);
		Object res = null;
		try {
			res = brandService.removeBrandToSupplier(supplierId, brandId);
			log.info("res for remove supplier" + res);
			return res;
		} catch (Exception e) {
			RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Couldn't add the brand to supplier", null);
			log.error("error occur while removing the brand to the supplier>>", e);
			return new ResponseEntity<>(successResponse, HttpStatus.OK);
		}

	}

	@ApiOperation("this api will be use for update the feature of brands")
	@GetMapping("/updateSortOrderAndFeatured/{brandId}")
	public  Object updateSortOrderAndFeatured(@PathVariable("brandId")String brandId,
											  @RequestParam(value = "sortOrder",required = false)String sortOrder,@RequestParam(value = "isFeatured",required = false) Boolean isFeatured){
		log.info("inside the updateSortOrderAndFeatured");
		Object res=null;
		return brandService.updateSortOrderAndFeatured(brandId,sortOrder,isFeatured);

	}

	@ApiOperation("get All supplier of a brands")
	@GetMapping("/getAllSupplierByBrand/{brandId}")
	public Object getSupplier(@PathVariable("brandId") String brandId,
							  @RequestParam(value = "pageNum", required = false) Integer pageNum,
							  @RequestParam(value = "pageSize", required = false) Integer pageSize,
							  @RequestParam(value = "sortOrder", required = false) String sortOrder) {
		Object res = null;
		try {
			res = brandService.getSupplierByBrand(brandId,sortOrder,pageNum,pageSize);
			log.info("res for get Supplier " + res);
			return res;
		} catch (Exception e) {
			RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Couldn't get supplier ", null);
			log.error("error occur while removing the brand to the supplier>>", e);
			return new ResponseEntity<>(successResponse, HttpStatus.OK);
		}

	}

	@ApiOperation("It take categoryId as a parameter and Will return  all level of catalogaries")
	@GetMapping(value = "/getAllCategoriesAndSubCategory", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<RestApiSuccessResponse> getAllCategoriesAndSubCategories(@RequestHeader("countrycode") String countryCode) {
		log.info("getAllCategory invoked");
		return getsuccessResponse(brandService.getAllCategoriesAndSubCategories(countryCode), "Category Fetched Successfully!");
	}

	@ApiOperation("get brands by brand name")
	@GetMapping(value = "/searchByBrandName/{brandName}")
	public Object searchBrandByBrandName(@PathVariable("brandName") String brandName,
										 @RequestParam(value = "pageNum", required = false) Integer pageNum,
										 @RequestParam(value = "pageSize", required = false) Integer pageSize,
										 @RequestParam(value = "sortOrder", required = false) String sortOrder){
		log.info("searching brand by brand name : {}", brandName);
		Object response = brandService.getBrandByBrandName(brandName, pageNum, pageSize, sortOrder);
		return response;
	}

}