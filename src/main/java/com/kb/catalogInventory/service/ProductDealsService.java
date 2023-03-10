package com.kb.catalogInventory.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.kb.catalogInventory.mapper.ProductBannerMapper;
import com.kb.java.utils.RestApiErrorResponse;
import com.kb.java.utils.RestApiSuccessResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kb.catalogInventory.datatable.Deals;
import com.kb.catalogInventory.datatable.ProductBanner;
import com.kb.catalogInventory.datatable.ProductDeals;
import com.kb.catalogInventory.model.ProductBannerBO;
import com.kb.catalogInventory.model.ProductDealsBO;
import com.kb.catalogInventory.repository.DealsRepository;
import com.kb.catalogInventory.repository.ProductBannerRepository;
import com.kb.catalogInventory.repository.ProductDealsRepository;
import com.kb.catalogInventory.util.LocalCache;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ProductDealsService {
	@Autowired
	ProductBannerMapper mapper;

	@Autowired
	private DealsRepository dealsRepository;
	
	@Autowired
	private ProductDealsRepository productDealsRepository;
	
	@Autowired
	private ProductBannerRepository productBannerRepository;
	
	@Autowired
	private LocalCache cache;
	
	public List<ProductDealsBO> getAllActiveProductDeals() {
		log.info("Inside getAllActiveProductDeals");
		List<ProductDealsBO> pdBOList = new ArrayList<>();
		List<Long> activeDealsIdList = new ArrayList<>(); 
		List<Deals> activeDealsList = dealsRepository.findByIsActive(true);
		for(Deals deal:activeDealsList) {
			activeDealsIdList.add(deal.getId());
		}
		List<ProductDeals> productDealsList = productDealsRepository.findByDealsIdIn(activeDealsIdList);
		pdBOList = productDTOMapper(productDealsList);
				
		return pdBOList;
	}
	
	public List<ProductDealsBO> productDTOMapper(List<ProductDeals> productDealsList) {
		log.debug("Inside productDTOMapper with productDealsList "+new Gson().toJson(productDealsList));
		List<ProductDealsBO> pdBOList = new ArrayList<>();
		for(ProductDeals pd:productDealsList) {
			ProductDealsBO pdBO = new ProductDealsBO();
			pdBO.setId(pd.getId());
			pdBO.setName(pd.getName());
			pdBO.setPriceVariationPercent(pd.getPriceVariationPercent());
			pdBO.setEndDate(pd.getEndDate());
			pdBO.setStartDate(pdBO.getStartDate());
			pdBO.setDealsId(pdBO.getDealsId());
			pdBOList.add(pdBO);
		}
		return pdBOList;
	}
	
	
	public String getAllActiveProductBanners() throws Exception {
		String response = "";
		List<List<ProductBannerBO>> prdBannerBOList = new ArrayList<>();
		response = cache.get("HomeBanner");
		if ((StringUtils.isBlank(response))) {
			ObjectMapper mapper= new ObjectMapper();
				List<ProductBanner> prdBannerList = productBannerRepository
						.findByIsActiveTrueAndIsCategoryBannerFalseOrderBySortOrderDesc();
				prdBannerBOList = productBannerDTOMapper(prdBannerList);

				response =mapper.writeValueAsString(prdBannerBOList);
				cache.put("HomeBanner", response);
		}

		return response;
	}
	
	
	public List<List<ProductBannerBO>> productBannerDTOMapper(List<ProductBanner> prdBannerList) {

		log.info("Inside productBannerDTOMapper with prdBannerList ");
		List<List<ProductBannerBO>> prdBannerBOList = new ArrayList<>();
		Map<String, List<ProductBannerBO>> bannerStageMap = new HashMap<>();
		prdBannerList.stream().forEach(pb -> {
			String bannerCallBackUrl = "";
			bannerCallBackUrl = pb.getBannerUrl();
			ProductBannerBO pbBO = new ProductBannerBO(pb.getId(), pb.getImageURL(), pb.isActive(),
					pb.getSearchKeyword(), pb.getHeading(), pb.getShortTitle(), pb.getDescription(), pb.getSortOrder(),
					pb.getBannerStage(),bannerCallBackUrl,pb.getBannerUrl(),pb.getIsCategoryBanner(),pb.getCreatedOn(),pb.getUpdatedOn());
			if (bannerStageMap.containsKey(pb.getBannerStage()) && bannerStageMap.get(pb.getBannerStage()).size() > 0) {
				bannerStageMap.get(pb.getBannerStage()).add(pbBO);
			} else {
				List<ProductBannerBO> boLIst = new ArrayList<>();
				boLIst.add(pbBO);
				bannerStageMap.put(pbBO.getBannerStage(), boLIst);
			}
		});
		bannerStageMap.forEach((k, v) -> {
			prdBannerBOList.add(v);
		});

		return prdBannerBOList;
	}
	
	
	public Set<ProductBannerBO> productBannerDTOMapperForCatBanner(List<ProductBanner> prdBannerList) {

		log.info("Inside productBannerDTOMapper with prdBannerList ");
		Set<ProductBannerBO> prdBannerBOList = new TreeSet<ProductBannerBO>();
		prdBannerList.stream().forEach(pb -> {
			try {
				String bannerCallBackUrl = "";
				if(pb.getBannerUrl().startsWith("/categories") || pb.getBannerUrl().startsWith("categories")) {
					bannerCallBackUrl = "/home/products/"+pb.getBannerUrl();
				}else {
					bannerCallBackUrl = pb.getBannerUrl();
				}

				ProductBannerBO pbBO = new ProductBannerBO(pb.getId(), pb.getImageURL(), pb.isActive(),
						pb.getSearchKeyword(), pb.getHeading(), pb.getShortTitle(), pb.getDescription(), pb.getSortOrder(),
						pb.getBannerStage(),bannerCallBackUrl,pb.getBannerUrl(),pb.getIsCategoryBanner(),pb.getCreatedOn(),pb.getUpdatedOn());
				prdBannerBOList.add(pbBO);

			}catch(Exception e) {
				log.error("Exception in productBannerDTOMapperForCatBanner for Product Banner id "+pb.getId());
				log.error("Exception in productBannerDTOMapperForCatBanner ",e);
			}
		});
		

		return prdBannerBOList;
	}
	
	public Set<ProductBannerBO> getCategoryBanner() throws Exception {
		log.info("Inside getAllActiveProductBanners ");
		Set<ProductBannerBO> prdBannerBOList = new TreeSet<>();
		try {
			List<ProductBanner> prdBannerList = productBannerRepository.findByIsActiveTrueAndIsCategoryBannerTrueOrderBySortOrderDesc();
			prdBannerBOList = productBannerDTOMapperForCatBanner(prdBannerList);
		}catch (Exception e){
			log.error("error occur while fetching category banner",e);
		}

		//cache.set("categoryBanner", prdBannerBOList);
		return prdBannerBOList;
	}
	
	public String refreshProductHomeBanners() throws Exception {
		String response = "";
		
		ObjectMapper mapper= new ObjectMapper();
		List<List<ProductBannerBO>> prdBannerBOList = new ArrayList<>();
		List<ProductBanner> prdBannerList = productBannerRepository
				.findByIsActiveTrueAndIsCategoryBannerFalseOrderByUpdatedOnDesc();
		prdBannerBOList = productBannerDTOMapper(prdBannerList);
		response =mapper.writeValueAsString(prdBannerBOList);
		cache.put("HomeBanner", response);
		return response;
	}

	public ResponseEntity<?> AddOrUpdateCategoryBanners(ProductBannerBO bo) {
          log.info("inside the add or update category banner" +bo.getHeading());
		    ProductBanner banner=null;
		   try {
			   banner=mapper.map(bo);
		   }catch (Exception e){

		   }

	      try{
		      if (banner.getId()!=null )
			  {
				 
				  banner=productBannerRepository.save(banner);

				  RestApiSuccessResponse successResponse=new RestApiSuccessResponse(HttpStatus.OK.value(),"category Banner Updated successfully" ,bo);
                 return  new ResponseEntity<>(successResponse,HttpStatus.OK);
			  }else {
				
				  try {
					  List<ProductBanner> pb=productBannerRepository.findByIsActiveTrueAndIsCategoryBannerTrueOrderBySortOrderDesc();
					  banner.setSortOrder(pb.get(0).getSortOrder()+1);

				  }catch (Exception e){
					  banner.setSortOrder(1L);
				  }
				 banner=productBannerRepository.save(banner);
				 bo=mapper.map(banner);
				  RestApiSuccessResponse successResponse=new RestApiSuccessResponse(HttpStatus.OK.value(),"category Banner added successfully" ,bo);
				  return  new ResponseEntity<>(successResponse,HttpStatus.OK);
			  }


		  }catch (Exception e){
			  log.error("error while adding the category banner",e);
			  RestApiErrorResponse response=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),"something went wrong","'");
			  return  new ResponseEntity<>(response,HttpStatus.OK);

		  }


	}
	public ResponseEntity<?> AddOrUpdateBanners(ProductBannerBO bo) {
          log.info("inside the add or update category banner" +bo.getHeading());
		    ProductBanner banner=null;
		   try {
			  banner=mapper.map(bo);
		   }catch (Exception e){
             log.error("can't map ProductBannerBO");
		   }

	      try{
		      if (banner.getId()!=null )
			  {
				 
				  banner=productBannerRepository.save(banner);
				  refreshProductHomeBanners();
				  RestApiSuccessResponse successResponse=new RestApiSuccessResponse(HttpStatus.OK.value(),"category Banner Updated successfully" ,bo);
                 return  new ResponseEntity<>(successResponse,HttpStatus.OK);
			  }else {

				 
				  try {
					  List<ProductBanner> pb=productBannerRepository.findByIsActiveTrueAndIsCategoryBannerFalseOrderBySortOrderDesc();
					  banner.setSortOrder(pb.get(0).getSortOrder()+1);
				  }catch (Exception e){
					banner.setSortOrder(1L);
				  }

				  banner=productBannerRepository.save(banner);
				 refreshProductHomeBanners();
				  bo=mapper.map(banner);
				  RestApiSuccessResponse successResponse=new RestApiSuccessResponse(HttpStatus.OK.value(),"category Banner added successfully" ,bo);
				  return  new ResponseEntity<>(successResponse,HttpStatus.OK);
			  }


		  }catch (Exception e){
			  RestApiErrorResponse response=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),"something went wrong","'");
			  return  new ResponseEntity<>(response,HttpStatus.OK);

		  }


	}
}
