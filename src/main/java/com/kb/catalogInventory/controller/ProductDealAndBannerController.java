package com.kb.catalogInventory.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kb.catalogInventory.exception.ControllerExceptionHandler;
import com.kb.catalogInventory.model.ProductBannerBO;
import com.kb.catalogInventory.service.ProductDealsService;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@RequestMapping("/deals")
@Validated
@Log4j2
public class ProductDealAndBannerController extends ControllerExceptionHandler{

	@Autowired
	private ProductDealsService dealsService;
	
	@GetMapping("/banners")
	public ResponseEntity<?> getBanners() throws Exception{
		log.info("Entered getBanners");
		String res = dealsService.getAllActiveProductBanners();
		log.debug("Leaving getBanners {}",res.toString());
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("/categoryBanners")
	public ResponseEntity<?> getcategoryBanners() throws Exception{
		log.info("Entered getBanners");
		Set<ProductBannerBO> res = dealsService.getCategoryBanner();
		log.debug("Leaving getBanners {}",res.toString());
		return ResponseEntity.ok(res);
	}


	@PostMapping("/addupdateCategoryBanners")
	public  Object AddOrupdateCategoryBanner(@RequestBody ProductBannerBO bo ){
		Object res=null;
		try{
			res=dealsService.AddOrUpdateCategoryBanners(bo);
		}catch (Exception e){

		}

      return res;
	}


	@PostMapping("/addupdateBanners")
	public  Object AddOrupdateBanner(@RequestBody ProductBannerBO bo ){
		Object res=null;
		try{
			res=dealsService.AddOrUpdateBanners(bo);
		}catch (Exception e){

		}

		return res;
	}
	@GetMapping("/refreshHomeBanners")
	public ResponseEntity<?> refreshHomeBanners() throws Exception{
		log.info("Entered getBanners");
		String res = dealsService.refreshProductHomeBanners();
		log.debug("Leaving getBanners {}",res.toString());
		return ResponseEntity.ok(res);
	}





}
