package com.kb.catalogInventory.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.kb.catalogInventory.datatable.*;
import com.kb.catalogInventory.model.SupplierBo;
import com.kb.catalogInventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.kb.catalogInventory.constant.AppConstant;
import com.kb.catalogInventory.exception.DataNotFoundException;
import com.kb.catalogInventory.model.BrandBO;
import com.kb.catalogInventory.model.BrandReviewsBO;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;

@Component
@Log4j2
public class BrandMapper {
	@Autowired 
	private BrandsRepository brandsRepository;
	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private BrandModelCategoryRepository  brandModelCategoryRepository;

	@Autowired
	private BrandModelsRepository  brandModelsRepository;

	@Autowired
	private BrandReviewsRepository brandReviewsRepository;

	@Autowired
	private BrandReviewsMapper reviewMapper;

	@Autowired
	private SupplierBrandsRespository  supplierBrandsRespository;
	@Autowired
	private  SupplierMapper supplierMapper;

	@Value("${s3.base.context.url}")
	private String s3ImageUrl;

	public Brands convertBoToEntity(BrandBO brandBO) {
		Brands brands=new Brands();

		if(brandBO.getId()!=0) {
			Optional<Brands> brandsOpt=brandsRepository.findById(brandBO.getId());
			if(!brandsOpt.isPresent()) {
				throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(),"Invalid Brand Id", "Invalid Brand Id");
			}
			brands=brandsOpt.get();
		}
		convertBoToEntity(brandBO,brands);
		return brands;
	}

	private Brands convertBoToEntity(BrandBO brandBO, Brands brands) {
		brands.setName(brandBO.getName());
		if(brandBO.getIsActive()!=null) {
			brands.setIsActive(brandBO.getIsActive());
		}else {
			brands.setIsActive(true);
		}
		brands.setThumbnails(brandBO.getThumbnails());
		if(brandBO.getAddedOn()!=null) {
			brands.setAddedOn(brandBO.getAddedOn());
		}
		brands.setAddedOn( new Date(System.currentTimeMillis()));
		brands.setBrandLogo(brandBO.getBrandLogo());
		brands.setBrandCoverPic(brandBO.getBrandCoverPic());
		brands.setLongDescription(brandBO.getLongDescription());
		brands.setOneLiner(brandBO.getOneLiner());
		brands.setShipsFrom(brandBO.getShipsFrom());
		brands.setShortStory(brandBO.getShortStory());
		brands.setTags(brandBO.getTags());
		brands.setBasedIn(brandBO.getBasedIn());
		brands.setShipsFrom(brandBO.getShipsFrom());
		brands.setUpdatedOn( new Date(System.currentTimeMillis()));
		brands.setAvgRating(brandBO.getAvgRating());
		brands.setBestSellingSku(brandBO.getBestSellingSKU());
		brands.setAdminId(brandBO.getAdminId());
		brands.setIsFeatured(brandBO.getIsFeatured());
		brands.setSortOrder(brandBO.getSortOrder());
		brands.setSameManufacturerAs(brandBO.getSameManufacturerAs());
		
		/*if(brandBO.getSupplierId()!=null) {
			Optional<Supplier> supplierOpt= supplierRepository.findById(brandBO.getSupplierId());
			if(supplierOpt.isPresent()) {
				brands.setSupplier(supplierOpt.get());
			}
		}*/
		final List<BrandReviews> branreviewDtosExisting= new ArrayList<>();
		
		if(brandBO.getId()!=null && brandBO.getId() !=0) {
			branreviewDtosExisting.addAll(brandReviewsRepository.findByBrands(brandBO.getId()));
		}
		log.info("size of existing reviews -- "+branreviewDtosExisting.size());
		List<BrandReviews> branreviewDtos= new ArrayList<>();
		if (null != brandBO.getBrandReviews()) {
			brandBO.getBrandReviews().forEach(br->{
				BrandReviews reviews= reviewMapper.convertBoToEntity(br);
				if (branreviewDtosExisting != null && branreviewDtosExisting.size() > 0) {
					branreviewDtosExisting.removeIf(e -> e.getName().equalsIgnoreCase( reviews.getName()));
				}
				reviews.setBrands(brands);
				branreviewDtos.add(reviews);
			});
		}
		/**
		 * Deactivate non matching brand reviews
		 */
		log.info("size of existing reviews after removal -- "+branreviewDtosExisting.size());
		if(branreviewDtosExisting!=null && branreviewDtosExisting.size()>0) {
			branreviewDtosExisting.stream().forEach(br->br.setIsActive(false));
			branreviewDtos.addAll(branreviewDtosExisting);
		}
		branreviewDtos.stream().forEach(br->log.info("id and is active -- "+br.getId()+br.getIsActive()));
		
		brands.getBrandReviews().addAll(branreviewDtos);
		return brands;
	}
	public BrandBO convertEntityToBo(Brands brands) {
		return convertEntityToBo(new BrandBO(), brands, new HashMap<>(), new HashMap<>(), "0");
	}

	public BrandBO convertEntityToBo(Brands brands, Map<Long, Supplier> supplierMap, Map<Long, Groups> groupsHashMap,
									 String contact) {
		return convertEntityToBo(new BrandBO(), brands, supplierMap, groupsHashMap, contact);
	}

	public BrandBO convertEntityToBo(BrandBO brandBO, Brands brands, Map<Long, Supplier> supplierMap,
									 Map<Long, Groups> groupsHashMap, String contact) {
		brandBO.setId(brands.getId());
		brandBO.setName(brands.getName());
		brandBO.setIsActive(brands.getIsActive());
		brandBO.setThumbnails(StringUtils.isNotBlank(brands.getThumbnails())?(AppConstant.IMAGE_URL+brands.getThumbnails()):null);
		log.info("added on date for brands"+brands.getAddedOn()+" for brandsId"+brands.getId());
		brandBO.setAddedOn(brands.getAddedOn());
		brandBO.setBrandLogo(StringUtils.isNotBlank(brands.getBrandLogo())?(s3ImageUrl+brands.getBrandLogo()):null);
		brandBO.setBrandCoverPic(StringUtils.isNotBlank(brands.getBrandCoverPic())?(s3ImageUrl+brands.getBrandCoverPic()):null);
		brandBO.setLongDescription(brands.getLongDescription());
		brandBO.setOneLiner(brands.getOneLiner());
		brandBO.setShipsFrom(brands.getShipsFrom());
		brandBO.setShortStory(brands.getShortStory());
		brandBO.setTags(brands.getTags());
		brandBO.setBasedIn(brands.getBasedIn());
		brandBO.setShipsFrom(brands.getShipsFrom());
		log.info("updated on date for brands"+brands.getUpdatedOn() + " for brandsId"+brands.getId());
		brandBO.setUpdatedOn(brands.getUpdatedOn());
		brandBO.setAvgRating(brands.getAvgRating());
		brandBO.setAdminId(brands.getAdminId());
		brandBO.setBestSellingSKU(brands.getBestSellingSku());
		brandBO.setIsFeatured(brands.getIsFeatured());
		brandBO.setSortOrder(brands.getSortOrder());
		brandBO.setSameManufacturerAs(brands.getSameManufacturerAs());

		if(brands.getSupplier()!=null) {
			brandBO.setSupplierId(brands.getSupplier().getId());
		}
		List<BrandReviews> review=brandReviewsRepository.findByBrands(brands.getId());
		List<BrandReviewsBO> boList=new ArrayList<>();
		review.forEach(br->{
			if (br.getIsActive()!=null &&br.getIsActive()) {
				BrandReviewsBO bo = reviewMapper.convertEntityToBo(br);
				boList.add(bo);
			}
		});
		brandBO.setBrandReviews(boList);
		try {

		   //supplier
			try {
				List<SupplierBrands> supplierBrandsList = supplierBrandsRespository.findByBrandIdAndIsActiveTrue(String.valueOf(brandBO.getId()));
				List<Long> supplierIds = new ArrayList<>();
				 List<SupplierBo> list =new ArrayList<>();
				supplierBrandsList.stream().forEach(sb -> {
					supplierIds.add(Long.valueOf(sb.getSupplierId()));
				});
				List<Supplier> supplierList = supplierRepository.findAllByIdList(supplierIds);
				 supplierList.stream().forEach(bd->{
					 list.add(supplierMapper.convertSupplierToBo(bd));
				 });
				brandBO.setSupplierList(list);
				brandBO.setNoOfSupplier(list.size());

			}catch (Exception e){

			}


			BrandModels brandModel=brandModelsRepository.findByBrands(brands);
			List<String> categoryNameList = brandModelCategoryRepository
					.findByBrandModels(brandModel).stream()
					.map(BrandModelCategory::getCategoryName).collect(Collectors.toList());
			brandBO.setCategoryNameList(categoryNameList);
			if (supplierMap.containsKey(brands.getSupplier().getId())){
				Supplier supplier = supplierMap.get(brands.getSupplier().getId());
				if (!ObjectUtils.isEmpty(supplier.getGroupString())){
					brandBO.setIsUserLoginRequired(true);
					brandBO.setIsPremiumBrand(true);
					AtomicReference<Boolean> isUserBelongToBrand = new AtomicReference<>(false);
					if (!contact.equalsIgnoreCase("0")){
						List<Long> groupIds = Arrays.stream(supplier.getGroupString().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
						groupIds.forEach(groupId ->{
							if (groupsHashMap.containsKey(groupId)){
								Groups groups = groupsHashMap.get(groupId);
								if (groups.getPhoneString().contains(contact)){
									isUserBelongToBrand.set(true);
								}
							}

						});
					}
					brandBO.setIsUserBelongToBrand(isUserBelongToBrand.get());

				} else {
					brandBO.setIsUserLoginRequired(false);
					brandBO.setIsPremiumBrand(false);
					brandBO.setIsUserBelongToBrand(true);
				}
			} else {
				brandBO.setIsUserLoginRequired(false);
				brandBO.setIsPremiumBrand(false);
				brandBO.setIsUserBelongToBrand(true);
			}
		}catch (Exception e){
		 log.error("error while create for brandId>" +brands,e);
		}

		return brandBO;
	}
}
