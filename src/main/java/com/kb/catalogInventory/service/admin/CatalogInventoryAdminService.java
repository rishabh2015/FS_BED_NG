package com.kb.catalogInventory.service.admin;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.kb.catalogInventory.datatable.*;
import com.kb.catalogInventory.model.*;
import com.kb.catalogInventory.repository.*;
import com.kb.java.utils.RestApiSuccessResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.kb.catalogInventory.scheduler.UnLockInventoryStockScheduler;

import javax.transaction.Transactional;

@Component
public class CatalogInventoryAdminService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private VariationOptionsRepository variationOptionsRepository;
	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private VariationRepository variationRepository;
	@Autowired
	private BrandModelsRepository brandModelsRepository;
	@Autowired
	private CategoryVariationRepository categoryVariationRepo;

	@Autowired
	private CategoryAttributeRepository categoryAttributeRepositoryRepo;

	@Autowired
	private CategoryAttributeOptionsRepository categoryAttributeOptionsRepository;
	@Autowired
	private VariationOptionsRepository variationOptionRepository;
	@Autowired
	private BrandsRepository brandRepository;
	
	@Autowired
	private ProductCombinationsRepository pcRepo;

	public Map<String, Map<String, List<String>>> productCategoryDropDowns() {

		logger.info("Inside productCategoryDropDowns ");
		Map<String, Map<String, List<String>>> dropdownMap = new HashMap<String, Map<String, List<String>>>();
		final Map<String, List<String>> variationMap = new HashMap<>();
		List<Variation> variations = variationRepository.findAll();
		variations.stream().forEach(var -> {
			variationMap.put(var.getVariationName(), variationOptionsRepository.findByVariationId(var.getId()));
		});
		dropdownMap.put("variation-variationOptionDropdown", variationMap);
		final Map<String, List<String>> brandModelMap = new HashMap<>();
		List<BrandModels> brandModels = brandModelsRepository.findAll();
		List<String> names = brandModels.stream().map(BrandModels::getName).collect(Collectors.toList());
		brandModelMap.put("brand-models", names);
		dropdownMap.put("brand-models-Dropdown", brandModelMap);
		final Map<String, List<String>> suuplierMap = new HashMap<>();
		List<Supplier> supliers = supplierRepository.findAll();
		List<String> suppliernames = supliers.stream().map(Supplier::getSupplierName).collect(Collectors.toList());
		suuplierMap.put("suppliers", suppliernames);
		dropdownMap.put("supplier-Dropdown", suuplierMap);
		List<Category> categories = categoryRepository.findAll();
		final Map<String, List<String>> categoryMap = new HashMap<>();
		List<String> categoriesSet = new ArrayList<>();
		categories.stream().forEach(cat -> {
			if (cat.getParent()==null) {
				List<String> childCategories = cat.getChildrenCategories().stream().map(Category::getCategoryName)
						.collect(Collectors.toList());
				childCategories.add(cat.getCategoryName());
				for (int i = 0; i < childCategories.size(); i++) {
					createCombination(childCategories, i, i, categoriesSet, cat.getCategoryName() + "-", "-");
				}

			}
		});

		categoryMap.put("categories", categoriesSet);
		dropdownMap.put("categories-Dropdown", categoryMap);
		logger.debug("returning  productCategoryDropDowns "+new Gson().toJson(dropdownMap));
		return dropdownMap;
	}

	private void combinationUtil(List<String> strnList, String data[], int start, int end, int index, int r,
			List<String> outputList, String finalData, String seperator) {

		logger.info("Inside combinationUtil ");
		if (index == r) {
			for (int j = 0; j < r; j++) {
				finalData = finalData + data[j] + seperator;
			}
			outputList.add(finalData);
			return;
		}
		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = strnList.get(i);
			combinationUtil(strnList, data, i + 1, end, index + 1, r, outputList, finalData, seperator);
		}
	}

	private void createCombination(List<String> strnList, int n, int r, List<String> outputList, String finalData,
			String seperator) {

		logger.info("Inside createCombination");
		String data[] = new String[r];
		combinationUtil(strnList, data, 0, n - 1, 0, r, outputList, finalData, seperator);
	}

	public Map<String, List<String>> variationVariationOptionList() {
		logger.info("Inside variationVariationOptionList");

		final Map<String, List<String>> variationMap = new HashMap<>();
		List<Variation> variations = variationRepository.findAll();
		variations.stream().forEach(var -> {
			variationMap.put(var.getVariationName(), variationOptionsRepository.findByVariationId(var.getId()));
		});
		logger.debug("returning variationVariationOptionList"+new Gson().toJson(variationMap));
		return variationMap;

	}
	


	public List<CategoryBO> categoriesAndSubcategories() {
		logger.info("Inside categoriesAndSubcategories");
		return categoryRepository.findAll().stream().map(mapToCategoryDTO).collect(Collectors.toList());
	}
	
	public List<String> getBrandNames(Long supplierId){
		logger.info("Inside getBrandNames");
		List<String> brandNames= new ArrayList<>();
		List<Brands> brands = brandRepository.findBrandBySupplierId(supplierId);
		brands.stream().forEach(bd->{
			brandNames.add(bd.getName());
		});
		//brandNames.add(brandRepository.findBrandBySupplierId(supplierId).getName());
		return brandNames;
	}
	
	public ResponseEntity<Set<CategoryBO>> categorySibling(long categoryId) {

		logger.info("Inside categorySibling with categoryId: {}",categoryId);
		return categoryRepository.findById(categoryId).map(findSiblings).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
	}

	private Function<Category, CategoryBO> mapToCategoryDTO = c -> CategoryBO.builder().id(c.getId())
			.categoryName(c.getCategoryName()).parent(c.getParent()).childrenCategories(c.getChildrenCategories()).categoryStage(c.getCategoryStage())
			.build();
	
	  private Function<Category, Set<CategoryBO>> findSiblings = category -> category.getParent().getChildrenCategories().stream()
	            .map(c -> CategoryBO.builder().id(c.getId()).categoryName(c.getCategoryName()).build()).collect(Collectors.toSet());

	public List<String> categoryKeyWords() {

		logger.info("Inside categoryKeyWords ");
		List<Category> categories = categoryRepository.findAll();
		List<String> categoriesSet = new ArrayList<>();
		categories.stream().forEach(cat -> {
			if (cat.getParent().getId() == 0) {
				List<String> childCategories = cat.getChildrenCategories().stream().map(Category::getCategoryName)
						.collect(Collectors.toList());
				childCategories.add(cat.getCategoryName());
				for (int i = 0; i < childCategories.size(); i++) {
					createCombination(childCategories, i, i, categoriesSet, cat.getCategoryName() + " ", " ");
				}

			}
		});

		return categoriesSet;
	}

	public Map<String, List<String>> categoryVariationVariationOptionList(String categoryName) {

		List<CategoryVariation> categoryVariationList = categoryVariationRepo.findByCategoryName(categoryName);
		List<Variation> variations = categoryVariationList.stream().map(CategoryVariation::getVariation)
				.collect(Collectors.toList());
		final Map<String, List<String>> variationMap = new HashMap<>();
		variations.stream().forEach(var -> {
			variationMap.put(var.getVariationName(), variationOptionsRepository.findByVariationId(var.getId()));
		});
		return variationMap;

	}

	public Object categoryAttributeOptionList(Integer categoryId) {
		logger.info("Inside get category Attribute Option List ");
		RestApiSuccessResponse restApiSuccessResponse = null;
		List<CategoryAttribute> categoryAttributeList =  categoryAttributeRepositoryRepo.findByCategoryIdList(categoryId);

		if (ObjectUtils.isNotEmpty(categoryAttributeList)) {

			ArrayList<Integer> categoryIdList = new ArrayList<>();
			categoryAttributeList.stream().forEach(lst->{
				categoryIdList.add(lst.getId());
			});

			List<CategoryAttributeOptions> categoryAttributeOptionsList = categoryAttributeOptionsRepository.findOptionsByAttributeIds(categoryIdList);

			ArrayList<CategoryAttributeOptionsRS> list = new ArrayList<>();

			for (CategoryAttributeOptions categoryAttributeOptions : categoryAttributeOptionsList) {
				CategoryAttributeOptionsRS categoryAttributeOptionsRS = new CategoryAttributeOptionsRS();
				categoryAttributeOptionsRS.setName(categoryAttributeOptions.getName());
				categoryAttributeOptionsRS.setValue(categoryAttributeOptions.getValue());
				categoryAttributeOptionsRS.setId(categoryAttributeOptions.getId());
				list.add(categoryAttributeOptionsRS);
			}
			restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), " category Attribute Options Data fetched successfully", list);
			logger.info("response of getting category Attribute Option List {} ", list);
		}else {
			restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "category Attribute Options can not be fetched", "");
		}
		return restApiSuccessResponse;
	}

	public List<CategoryAttributeRS> categoryAttributeList(Integer categoryId) {
		logger.info("Inside get category Attribute List ");
		List<CategoryAttribute> categoryAttributeList =  categoryAttributeRepositoryRepo.findByCategory(categoryId);

		ArrayList<CategoryAttributeRS> list = new ArrayList<>();

		for(CategoryAttribute categoryAttribute : categoryAttributeList)
		{

			CategoryAttributeRS categoryAttributeRS = new CategoryAttributeRS();
			categoryAttributeRS.setId(categoryAttribute.getId());
			categoryAttributeRS.setAttributeName(categoryAttribute.getAttributeName());
			categoryAttributeRS.setCategoryId(categoryAttribute.getCategoryId());
			categoryAttributeRS.setUpdatedOn(categoryAttribute.getUpdatedOn());
			categoryAttributeRS.setCreatedOn(categoryAttribute.getCreatedOn());
			list.add(categoryAttributeRS);
		}

		return list;
	}

	public Object saveCategoryAttributueOptions(List<CategoryAttributeOptionsBO> categoryAttributeOptionsBOList){
		logger.info("Inside save Category Attribute Options ");
		RestApiSuccessResponse restApiSuccessResponse = null;
		AtomicInteger count = new AtomicInteger();
		try {

			categoryAttributeOptionsBOList.stream().forEach(catAO -> {
				if (catAO.getCategoryId() != null) {
					Optional<CategoryAttribute> categoryAttributeList = categoryAttributeRepositoryRepo.findByCategoryId(catAO.getCategoryId());
					catAO.setAttributeId(categoryAttributeList.get().getId());

					CategoryAttributeOptions categoryAttributeOptions = null;
					if (ObjectUtils.isNotEmpty(catAO.getAttributeId())) {
						categoryAttributeOptions = new CategoryAttributeOptions();
						BeanUtils.copyProperties(catAO, categoryAttributeOptions);
						categoryAttributeOptions = categoryAttributeOptionsRepository.save(categoryAttributeOptions);
						count.getAndIncrement();
					}

				}
			});

		}catch (Exception e){
			logger.error("");

		}
			restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), " Total category attribute saved successfully", count);

		return  restApiSuccessResponse;

	}

	public Object saveCategoryAttributueWithOptions(List<CategoryAttributeWithOptionsBO> categoryAttributeWithOptionsBOList){

		logger.info("Inside save Category Attributue and Attribute Options ");

		RestApiSuccessResponse response = null;


			List<CategoryAttribute> categoryAttributeList = new ArrayList<>();
			List<CategoryAttributeWithOptionsBO> categoryAttributeWithOptionsBOArrayList = new ArrayList<>();
			categoryAttributeWithOptionsBOList.stream().forEach(categoryAttributeWithOptionsForEach -> {

				CategoryAttribute categoryAttribute = new CategoryAttribute();
				CategoryAttributeOptions categoryAttributeOptions = new CategoryAttributeOptions();
				CategoryAttribute existingAttribute = categoryAttributeRepositoryRepo.findByCategoryAndName(categoryAttributeWithOptionsForEach.getCategoryId(),
																											categoryAttributeWithOptionsForEach.getAttributeName());
				int attributeId =0;
				if (existingAttribute == null) {

					categoryAttribute.setCategoryId(categoryAttributeWithOptionsForEach.getCategoryId());
					categoryAttribute.setAttributeName(categoryAttributeWithOptionsForEach.getAttributeName());
					categoryAttribute.setCreatedOn(categoryAttributeWithOptionsForEach.getCreatedOn());
					categoryAttribute.setUpdatedOn(categoryAttributeWithOptionsForEach.getUpdatedOn());
					categoryAttribute = categoryAttributeRepositoryRepo.save(categoryAttribute);
					attributeId = categoryAttribute.getId();
				} else {
					attributeId = existingAttribute.getId();
					categoryAttribute.setAttributeName(existingAttribute.getAttributeName());
					categoryAttribute.setCategoryId(existingAttribute.getCategoryId());
				}
				if (ObjectUtils.isNotEmpty(attributeId)) {

					categoryAttributeOptions.setAttributeId(attributeId);
					categoryAttributeOptions.setName(categoryAttributeWithOptionsForEach.getName());
					categoryAttributeOptions.setValue(categoryAttributeWithOptionsForEach.getValue());
					categoryAttributeOptions.setCreatedOn(categoryAttributeWithOptionsForEach.getCreatedOn());
					categoryAttributeOptions.setUpdatedOn(categoryAttributeWithOptionsForEach.getUpdatedOn());

					CategoryAttributeOptions existingAttributeOptions = categoryAttributeOptionsRepository.findByAttributeIdAndName(attributeId,
																											categoryAttributeWithOptionsForEach.getName());
					int attributeOptionId =0;
					if (existingAttributeOptions ==null) {
						categoryAttributeOptions = categoryAttributeOptionsRepository.save(categoryAttributeOptions);
						attributeOptionId = categoryAttributeOptions.getId();
					} else {
						String toAppendValue = existingAttributeOptions.getValue();
						String appebdedValue = toAppendValue+","+categoryAttributeWithOptionsForEach.getValue();
						int categoryAttributeOptionsSave = categoryAttributeOptionsRepository.updateValueByAttributeId(appebdedValue,existingAttributeOptions.getAttributeId());
						attributeOptionId = existingAttributeOptions.getId();
					}

					if (ObjectUtils.isNotEmpty(attributeOptionId)) {
						CategoryAttributeWithOptionsRS categoryAttributeWithOptionsRS = new CategoryAttributeWithOptionsRS();
						categoryAttributeWithOptionsRS.setId(attributeOptionId);
						categoryAttributeWithOptionsRS.setAttributeName(categoryAttribute.getAttributeName());
						categoryAttributeWithOptionsRS.setCategoryId(categoryAttribute.getCategoryId());
						categoryAttributeWithOptionsRS.setCreatedOn(categoryAttributeOptions.getCreatedOn());
						categoryAttributeWithOptionsRS.setUpdatedOn(categoryAttributeOptions.getUpdatedOn());
						categoryAttributeWithOptionsRS.setAttributeOptionsId(attributeOptionId);
						categoryAttributeWithOptionsRS.setName(categoryAttributeOptions.getName());
						categoryAttributeWithOptionsRS.setValue(categoryAttributeOptions.getValue());
						categoryAttributeWithOptionsRS.setAttributeId(categoryAttributeOptions.getAttributeId());

						BeanUtils.copyProperties(categoryAttributeWithOptionsRS, categoryAttributeWithOptionsForEach);
						categoryAttributeWithOptionsBOArrayList.add(categoryAttributeWithOptionsForEach);
					}

				}

			});
			if (categoryAttributeWithOptionsBOArrayList != null) {
				response = new RestApiSuccessResponse(HttpStatus.OK.value(), "category attribute with options saved successfully", categoryAttributeWithOptionsBOArrayList);
			}
			 else {
				response = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "category Attribute can not saved", "");

			}


		return response;
	}



	public Object deleteCategoryAttributeOption(Integer optionId) {
		logger.info("Inside delete category Attribute Option ");
		RestApiSuccessResponse response = null;
		if (ObjectUtils.isNotEmpty(optionId)) {
			int categoryAttributeOptionsListDelete = categoryAttributeOptionsRepository.
					deleteOptionsByAttributeId(optionId);
			if (categoryAttributeOptionsListDelete != 0){
			response = new RestApiSuccessResponse(HttpStatus.OK.value(), "category attribute has been deleted successfully",null);
			} else {
				response = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "category Attribute already deleted", null);
			}
		}
		else {
			response = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "category Attribute can not be deleted", null);

		}

		return response;
	}


	public Object deleteCategoryAttributeWithOption(Integer categoryId) {
		logger.info("Inside delete CategoryAttribute and category Attribute Option ");
		RestApiSuccessResponse response = null;
		Optional<CategoryAttribute> categoryAttributeList =  categoryAttributeRepositoryRepo.findByCategoryId(categoryId);

		if (ObjectUtils.isNotEmpty(categoryAttributeList.get().getId())) {
			int categoryAttributeOptionsListDelete = categoryAttributeOptionsRepository.
					deleteOptionsByAttributeId(categoryAttributeList.get().getId());

			int categoryAttributeDelete =  categoryAttributeRepositoryRepo.deleteAttributeAfterOptions(categoryId);


			response = new RestApiSuccessResponse(HttpStatus.OK.value(), "category attribute has been deleted successfully",null);
		}
		else {
			response = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "category Attribute can not be deleted", null);

		}

		return response;
	}


	public List<CategoryVariation> addCategoryVariation(Map<String, Map<String, List<String>>> categoryVariationRQ) {
		logger.info("Inside addCategoryVariation ");

		List<CategoryVariation> response = new ArrayList<>();
		categoryVariationRQ.forEach((k, v) -> {
			v.forEach((k1, v1) -> {

				Variation variation = variationRepository.findByVariationName(k1.trim());
				if (null == variation) {
					variation = new Variation();
					variation.setVariationName(k1.trim());
					variation = variationRepository.save(variation);
				}
				for (String op : v1) {
					//VariationOptions options = variationOptionRepository.findByVariationOptionName(op);
					VariationOptions options = variationOptionRepository.findByVariationOptionNameAndVariationId(op.trim(), variation.getId());
					if (null == options) {
						options = new VariationOptions();
						options.setVariationOptionName(op.trim());
						options.setVariation(variation);
						variationOptionRepository.save(options);
					}
				}
				//CategoryVariation categoryVariation = categoryVariationRepo.findByVariation(variation);//pass category name
				CategoryVariation categoryVariation = categoryVariationRepo.findByVariationIdAndCategoryName(variation.getId(), k.trim());
				if (null == categoryVariation) {
					categoryVariation = new CategoryVariation();
				}
				categoryVariation.setCategoryName(k.trim());
				categoryVariation.setVariation(variation);
				categoryVariationRepo.save(categoryVariation);
				response.add(categoryVariation);
			});

		});
		return response;
	}

	public Object updateProductCountryRule(String pcUUID, String crId) {
	int isUpdated=	pcRepo.updateProductCountryRule(pcUUID,Long.valueOf(crId));
		return isUpdated==1?"Product Country Rule Updated Successfully":"ProductCountryRule Not Updated. Please try again";
	}
	
	@Autowired
	LockItemRepository lockItemRepository; 
	
	
	@Autowired
	UnLockInventoryStockScheduler unLockInventoryStock;
	
	public Object unlockInventory(LockItemsRQ rq) {
		 List<LockedItems> lockedIteams=lockItemRepository.findByBookingId(rq.getBookingId());
		 boolean isUnlockDone=false;
		 if(null!=lockedIteams) {
			 for(LockedItems lockedItems : lockedIteams) {
				 if(!lockedItems.getStatus().equalsIgnoreCase("UNLOCKED")) {
					 unLockInventoryStock.unlockedLockedIteam(lockedItems,"-from direct unlock from admin");
					 isUnlockDone=true;
					 return "Unlock Done for Booking Id "+rq.getBookingId();
				 }else {
					 return "Already unlocked  for Booking Id "+rq.getBookingId()+ "on date "+lockedItems.getLastUpdateTime();
				 }
				 
			 }
			 
		 }
		return isUnlockDone? "Unlock Done for Booking  Id "+rq.getBookingId():"Unlock inventory failed for Booking Id "+rq.getBookingId();
	}

	public Object unlockInventoryByOrderAttempt(Map rq) {
		logger.warn("Inside unlockInventoryByOrderAttempt with input->"+rq.get("orderAttemptId"));
		String orderAttemptId=(String)rq.get("orderAttemptId");
		List<LockedItems> lockedIteams=lockItemRepository.findByOrderAttemptId(orderAttemptId);
		boolean isUnlockDone=false;
		if(null!=lockedIteams) {
			logger.warn("Inside unlockInventoryByOrderAttempt and got locked items > "+lockedIteams.size());
			
			for(LockedItems lockedItems : lockedIteams) {
				if(!lockedItems.getStatus().equalsIgnoreCase("UNLOCKED")) {
					unLockInventoryStock.unlockedLockedIteam(lockedItems,"-from direct unlock");
					isUnlockDone=true;
					return "Unlock Done for OrderAttempt Id "+orderAttemptId;
				}else {
					return "Already unlocked  for OrderAttempt Id "+orderAttemptId+ "on date "+lockedItems.getLastUpdateTime();
				}

			}

		}
		logger.warn(isUnlockDone? "Unlock Done for OrderAttempt Id "+orderAttemptId:"Unlock inventory failed for OrderAttempt Id "+orderAttemptId);
		return isUnlockDone? "Unlock Done for OrderAttempt Id "+orderAttemptId:"Unlock inventory failed for OrderAttempt Id "+orderAttemptId;
	}

	public Map<String,BigInteger> getSupplierIdsFromProducts(List<String> productUuid){
		Map<String,BigInteger> productToSupplierMap = new HashMap<>();
		List<Object[]> resultSet=pcRepo.findAllSupplierByUniqueIdentifiers(productUuid);
		resultSet.stream().forEach(rs->{
			productToSupplierMap.put((String)rs[0],(BigInteger)rs[1]);
		});

		return productToSupplierMap;
	}
	public List<Map<String,Object>>	getSupplierEmailsFromIds(List<Long> supplierIds){
		List<Map<String,Object>> res= new ArrayList<>();

		List<Object []> resultSet= supplierRepository.getSupplierEmailToIdMap(supplierIds);
		resultSet.stream().forEach(r->{
			Map<String,Object> supplierEmailMap= new HashMap<>();
			supplierEmailMap.put("id",r[0]);
			supplierEmailMap.put("email",r[1]);
			res.add(supplierEmailMap);
		});
		return res;
	}


	public Map<String,String>	getHsnOfProducts(List<String> productIds){
		Map<String,String> res= new HashMap<>();
		List<Object []> pchsnMap=pcRepo.findHsnOfProducts( productIds);
		pchsnMap.stream().forEach(r->{
			res.put((String)r[1],(String)r[0]+"-"+(String)r[2]);
		});
		return res;
	}

}
