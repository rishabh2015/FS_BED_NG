package com.kb.catalogInventory.controller.admin;

import java.util.List;
import java.util.Map;

import com.kb.catalogInventory.datatable.CategoryAttribute;
import com.kb.catalogInventory.model.*;
import com.kb.catalogInventory.service.CategoryAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kb.catalogInventory.exception.ControllerExceptionHandler;
import com.kb.catalogInventory.service.admin.CatalogInventoryAdminService;

@RestController
@CrossOrigin
@RequestMapping("/admincategory")
public class AdminCategoryController extends ControllerExceptionHandler{
	@Autowired 
	private CatalogInventoryAdminService adminService;

	@Autowired
	private CategoryAttributeService categoryAttributeService;
	
	
	private final  Logger _logger = LoggerFactory.getLogger(AdminCategoryController.class);
	
	@GetMapping("/categoryvariations/{categoryName}")
	public ResponseEntity<?> getVariations(@PathVariable("categoryName") String categoryName){
		_logger.info("Entered getVariations");
		Map<String, List<String>> map = adminService.categoryVariationVariationOptionList(categoryName);
		_logger.debug("Leaving getVariations with properties : {}",map);
		return ResponseEntity.ok(map);
	}

	@GetMapping("/categoryattributes/{categoryId}")
	public ResponseEntity<?> getAttributes(@PathVariable("categoryId") Integer categoryId){
		_logger.info("Entered getVariations");
		List<CategoryAttributeRS> listMap = adminService.categoryAttributeList(categoryId);
		_logger.debug("Leaving getVariations with properties : {}",listMap);
		return ResponseEntity.ok(listMap);
	}

	@GetMapping("/categoryattributeOptions/{categoryId}")
	public Object getAttributeOptions(@PathVariable("categoryId") Integer categoryId){
		_logger.info("Entered get categoryattribute Options");
		//List<CategoryAttributeOptionsRS> listMap = adminService.categoryAttributeOptionList(categoryId);
		//_logger.debug("Leaving getVariations with properties : {}",listMap);
		//return ResponseEntity.ok(listMap);
		return new ResponseEntity<>(adminService.categoryAttributeOptionList(categoryId), HttpStatus.OK);
	}

	@PostMapping("/addCtaegoryVariation")
	public ResponseEntity<?> addCategoryVariations(@RequestBody Map<String, Map<String,List<String>>> categoryVariationRQ){
		_logger.info("Entered addCtaegoryVariation with RQ categoryVariationRQ {}",categoryVariationRQ);
		return ResponseEntity.ok( adminService.addCategoryVariation(categoryVariationRQ));
	}

	@PostMapping("/saveCategoryAttribute")
	public Object saveCategoryAttribute(@RequestBody CategoryAttributeBo categoryAttributeBo){
		return categoryAttributeService.saveCategoryAttributue(categoryAttributeBo);
	}

	@GetMapping("/getCategoryAttributeByCategoryId/{categoryId}")
	public Object getCategoryAttributeByCategoryId(@PathVariable("categoryId") Integer categoryId){
		return categoryAttributeService.getCategoryAttribute(categoryId);
	}

	@PostMapping("/saveCategoryAttributeOptions")
	public Object saveCategoryAttributeOptions(@RequestBody List<CategoryAttributeOptionsBO> categoryAttributeOptionsBO){
		return adminService.saveCategoryAttributueOptions(categoryAttributeOptionsBO);
	}

	@PostMapping("/saveCategoryAttributeWithOptions")
	public Object saveCategoryAttributeWithOptions(@RequestBody List<CategoryAttributeWithOptionsBO> categoryAttributeWithOptionsBO){
		return adminService.saveCategoryAttributueWithOptions(categoryAttributeWithOptionsBO);
	}


	@GetMapping("/deleteCategoryAttributeWithOptions/{categoryId}")
	public Object deleteCategoryAttributeWithOptions(@PathVariable("categoryId") Integer categoryId){
		return adminService.deleteCategoryAttributeWithOption(categoryId);
	}

	@GetMapping("/deleteCategoryAttributeOption/{categoryId}")
	public Object deleteCategoryAttributeOption(@PathVariable("categoryId") Integer categoryId){
		return adminService.deleteCategoryAttributeOption(categoryId);
	}

	@GetMapping("/getCategoryAttributeByCategoryName")
	public Object getCategoryAttributeByCategoryName(){
		return categoryAttributeService.getCategoryAttributeName();
	}

}
