package com.kb.catalogInventory.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.kb.catalogInventory.datatable.*;
import com.kb.catalogInventory.datatable.Collection;
import com.kb.catalogInventory.model.*;
import com.kb.catalogInventory.repository.*;
import com.kb.java.utils.KbRestTemplate;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.constant.InventoryConstants;
import com.kb.catalogInventory.scheduler.PushProductToKafkaScheduler;
import com.kb.catalogInventory.util.LocalCache;
import com.kb.java.utils.RestApiSuccessResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

@Component
@Log4j2
public class CollectionService {
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ProductUploadService uploadService;

	@Autowired
	private ProductViewRepository prodViewRepo;
	
	@Autowired
	private PageTypeStatusRepository pageTypeStatusRepo;
	
	@Autowired
	private ProductCountryRuleRepository productCountryRuleRepo;
	
	@Autowired
	private ProductCombinationsRepository productCombinationsRepo;
	
	@Autowired
	private PushProductToKafkaScheduler pushProductToKafkaScheduler;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private LocalCache cache;
	
	@Autowired
	private ProductBulkUpdateExceptionRepo exceptionRepo;
	
	@Autowired
	private DynamicCollectionAttributeMasterRepo dynamicCollectionAttributeMasterRepo;
	
	@Autowired
	private SchedulerConfigRepo schedulerConfigRepo;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private BrandsRepository brandsRepository;
	
	@Value("${catalog.collection.search.service.url}")
	private String catalogCollectionSearchUrl;

	@Value("${order.service.bestselling.getsellingcountdetail.url}")
	private String getSellingCountDetailsUrl;

	@Value("${order.service.bestselling.getsellingcountdetail.url.timeout}")
	private int getSellingCountDetailsUrlTimeout;

	@Value("${order.service.bestselling.getbestsellingproductdetail.url}")
	private String getBestSellingProductDetailsUrl;


	@Autowired
	KbRestTemplate restTemplate;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private BrandService brandService;
	
	public PagedDataResponse getAllCollections(String sortOrder, Integer pageNum, Integer pageSize){
		List<CollectionBO> collectionBOList = new ArrayList<CollectionBO>();
		List<Integer> statusList = new ArrayList<>();
		statusList.add(1);
		statusList.add(0);
		List<Collection> collectionList = collectionRepository.findByStatusIn(statusList,getPagable(pageNum, pageSize, sortOrder,null));
		int collectionListTotal = collectionRepository.countByStatusIn(statusList);
		for(Collection collection:collectionList) {
			CollectionBO collectionBO = mapCollectionDOtoBODuplicate(collection);
			collectionBO.setProductInventoryBo(null);
			collectionBOList.add(collectionBO);
		}
		Integer totalPages = collectionListTotal/pageSize;
		Integer remainder = collectionListTotal%pageSize;
		if(remainder!=0) {
			remainder=1;
		}
		PagedDataResponse pdr = new PagedDataResponse(collectionRepository.countByStatusIn(statusList), collectionBOList,
				collectionBOList.size(), getPagedStartIndex(pageNum, pageSize),
				getPagedEndIndex(pageNum, pageSize, collectionBOList.size()),totalPages+remainder);

		return pdr;
	}
	
	public List<CollectionBO> getAllCollections(String countrycode){
		log.info("Inside getAllCollections");
		List<CollectionBO> collectionBOList = new LinkedList<>();
		log.info("Going to fetch AllCollections");
		List<Collection> collectionList = collectionRepository.findByStatusAndIsBannerAndIsFeaturedOrderByAddedOnDesc(1,false,false);
		log.info("Total collections fetched: ",collectionList.size());
		boolean isInternationalLoggin=false;
		if(countrycode!=null && !countrycode.equalsIgnoreCase("IN")) {
			isInternationalLoggin=true;
		}
		boolean isInternational=isInternationalLoggin;
		for(Collection collection:collectionList) {
			log.info("Looping Inside collectionList:");
			log.info("Looping Inside collectionList: ",collection.getTitle());
			long totalCountdisplayAll = prodViewRepo.countByCollectionIdsAndStatusNameAndPcrDa(collection.getId().toString(), "Active");
			long totalCountdisplayInter = isInternational
					? prodViewRepo.countByCollectionIdsAndStatusNameAndPcrInterTrue(collection.getId().toString(), "Active")
					: 0;
			long totalCountdisplayWlc = prodViewRepo.countByCollectionIdsAndStatusNameAndPcrWlc(collection.getId().toString(),
					"Active", countrycode);
			long countStockGreaterThanZero = prodViewRepo.countByCollectionIdsAndAvailableStockGreaterThan(collection.getId().toString());
			
			long totalCount = totalCountdisplayAll + totalCountdisplayInter + totalCountdisplayWlc;
			if (totalCount > 0 && countStockGreaterThanZero>0) {
				if(validateDifferentailVisibiltyOfCollection(isInternational,collection.getProductCountryRule().getZone())) {
				collectionBOList.add(mapCollectionDOtoBODuplicate(collection));
				}
			}
		}
		
		return collectionBOList;
	}
	
	
	public Json getAllCollectionsNew(String countrycode){
		log.error("Inside getAllCollections");
		String jsonString ="";
		ObjectMapper mapper = new ObjectMapper();
		List<CollectionBO> collectionBOList = new LinkedList<>();
		jsonString = cache.get(InventoryConstants.COLLECTION_CACHE_KEY);
		log.error("Json string for collections from cache New "+jsonString+"<<<");
		if(StringUtils.isNotBlank(jsonString) && !jsonString.trim().equalsIgnoreCase("[]")) {
			log.error("Inside IF Condition Getting Data from Cache for getAllCollectionsNew");
			return new Json(jsonString);
		} else {
			
		
		log.error("Going to fetch AllCollections");
		List<Collection> collectionList = collectionRepository.findByStatusAndIsBannerAndIsFeaturedOrderByAddedOnDesc(1,false,false);
		log.error("Total collections fetched: ",collectionList.size());
		boolean isInternationalLoggin=false;
		if(countrycode!=null && !countrycode.equalsIgnoreCase("IN")) {
			isInternationalLoggin=true;
		}
		boolean isInternational=isInternationalLoggin;
		for(Collection collection:collectionList) {
		
			log.error("Looping Inside collectionList with collection Id {}",collection.getId());
			List<ProductView> pvList = prodViewRepo.findByCollectionIdAndStatus(collection.getId().toString(), "Active");
			
			//long totalCountdisplayAll = prodViewRepo.countByCollectionIdsAndStatusNameAndPcrDa(collection.getId().toString(), "Active");
			List<ProductView> totalCountdisplayAllList = pvList.stream().filter(ac -> ac.getPcrDa()).collect(Collectors.toList());
			long totalCountdisplayAll = totalCountdisplayAllList.size();
			log.error("totalCountdisplayAll >>> {}",totalCountdisplayAll);
			//long totalCountdisplayInter = isInternational
				//	? prodViewRepo.countByCollectionIdsAndStatusNameAndPcrInterTrue(collection.getId().toString(), "Active")
					//: 0;
			List<ProductView> totalCountdisplayInterList =  pvList.stream().filter(ac -> ac.getPcrInter()).collect(Collectors.toList());
			long totalCountdisplayInter = totalCountdisplayInterList.size();
			log.error("totalCountdisplayInter >>> {}",totalCountdisplayInter);
			List<ProductView> totalCountdisplayWlcList= pvList.stream().filter(ac -> ac.getPcrWlc()!=null && ac.getPcrWlc().equalsIgnoreCase(countrycode)).collect(Collectors.toList());
			long totalCountdisplayWlc = totalCountdisplayWlcList.size();
			log.error("totalCountdisplayWlc >>> {}",totalCountdisplayWlc);
			//long totalCountdisplayWlc = prodViewRepo.countByCollectionIdsAndStatusNameAndPcrWlc(collection.getId().toString(),
				//	"Active", countrycode);
			List<ProductView> countStockGreaterThanZeroList =  pvList.stream().filter(ac -> ac.getAvailableStock()>0).collect(Collectors.toList());
			long countStockGreaterThanZero = countStockGreaterThanZeroList.size();
			log.error("countStockGreaterThanZero >>> {}",countStockGreaterThanZero);
			//long countStockGreaterThanZero = prodViewRepo.countByCollectionIdsAndAvailableStockGreaterThan(collection.getId().toString());
			
			long totalCount = totalCountdisplayAll + totalCountdisplayInter + totalCountdisplayWlc;
			log.error("totalCount >>> {}",totalCount);
			if (totalCount > 0 && countStockGreaterThanZero>0) {
				if(validateDifferentailVisibiltyOfCollection(isInternational,collection.getProductCountryRule().getZone())) {
				collectionBOList.add(mapCollectionDOtoBODuplicate(collection));
				}
			}
		}
		try {
			jsonString = mapper.writeValueAsString(collectionBOList);
		} catch (JsonProcessingException e) {
			 log.error("exception while converting to json string in getAllCollections ",e);
			
		}
	    log.error("jsonString - "+jsonString);
		cache.put(InventoryConstants.COLLECTION_CACHE_KEY, jsonString);
		return new Json(jsonString);
		}
		
	}
	
	
	
	public boolean validateDifferentailVisibiltyOfCollection(Boolean isInternational,String zone) {
		Boolean isVisible= false;
		if(isInternational) {
			if(!(zone.equalsIgnoreCase("India") || zone.equalsIgnoreCase("Zone_IN"))) {
				isVisible = true;
			}
		}else {
			if(zone.equalsIgnoreCase("EveryWhere") || zone.equalsIgnoreCase("India") || zone.equalsIgnoreCase("Zone_IN")) {
				isVisible = true;
			}
		}
		return isVisible;
	}
	
	
	public PagedDataResponse getSingleCollectionProductsInfo(String pageTypeName,String sortOrder, Integer pageNum, Integer pageSize){
		PageTypeStatus pts = pageTypeStatusRepo.findByName(pageTypeName);
		List<CollectionBO> collectionBOList = new ArrayList<CollectionBO>();
		List<Collection> collectionList = collectionRepository.findByStatusAndPageTypeIdAndIsFeatured(1, pts.getId().intValue(),getPagable(pageNum, pageSize, sortOrder,null),true);
		List<Collection> collectionListTotal = collectionRepository.findByStatusAndPageTypeIdAndIsFeatured(1, pts.getId().intValue(),true);
		for(Collection collection:collectionList) {
			CollectionBO collectionBO = mapCollectionDOtoBO(collection);
			collectionBOList.add(collectionBO);
		}
		Integer totalPages = collectionListTotal.size()%pageSize;
		Integer remainder = collectionListTotal.size()%pageSize;
		if(remainder!=0) {
			remainder=1;
		}
		PagedDataResponse pdr = new PagedDataResponse(collectionRepository.countByStatusAndPageTypeId(1, pts.getId().intValue()), collectionBOList,
				collectionBOList.size(), getPagedStartIndex(pageNum, pageSize),
				getPagedEndIndex(pageNum, pageSize, collectionBOList.size()),totalPages+remainder);
		return pdr;
	}
	
	public PagedDataResponse getCollectionsAllProducts(Long collectionId,String sortOrder, Integer pageNum, Integer pageSize){
		List<ProductInventoryBO> productCollectionList = new ArrayList<ProductInventoryBO>();
		Optional<Collection> collection = collectionRepository.findById(collectionId);
		String[] skuList = collection.get().getProductSkus().split(",");
		Set<String> skuset = new HashSet<String>();
		for(String skus:skuList) {
			skuset.add(skus);
		}
		List<String> skuArrayList = new ArrayList<String>();
		skuArrayList.addAll(skuset);
		List<ProductView> viewList = prodViewRepo.findBySkuInAndStatusId(skuArrayList, Long.valueOf(0),getPagable(pageNum, pageSize, sortOrder,"pcId"));
		List<String> productUUIDList = viewList.stream().map(ProductView :: getPcUuid).collect(Collectors.toList());
		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
		viewList.forEach(view -> {
			productCollectionList.add(uploadService.populateProductInventoryObj(view, "IN", "INR", bestSellingProductCountDetailMap));
		});
		Integer totalPages = skuset.size()/pageSize;
		Integer remainder = skuset.size()%pageSize;
		if(remainder!=0) {
			remainder=1;
		}
		PagedDataResponse pdr = new PagedDataResponse(prodViewRepo.countBySkuInAndStatusId(Arrays.asList(skuList), Long.valueOf(0)), productCollectionList,
				productCollectionList.size(), getPagedStartIndex(pageNum, pageSize),
				getPagedEndIndex(pageNum, pageSize, productCollectionList.size()),totalPages+remainder);
		return pdr;
	}

	public Map<String, Long> fetchBestSellingProductDetailCount(List<String> productIdList) {
		Map<String, Long> outputMap = new HashMap<>();
		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<BestSellingRQ> entity = new HttpEntity<>(BestSellingRQ.builder().productIds(productIdList).build(), headers);
//			ResponseEntity<String> result = restTemplate.exchange(
//					getSellingCountDetailsUrl, HttpMethod.POST, entity, getSellingCountDetailsUrlTimeout, String.class);
//			if (result.getStatusCode() == HttpStatus.OK) {
//				JSONObject response = new JSONObject(Objects.requireNonNull(result.getBody()));
//				JSONArray jsonArray = response.optJSONArray("data");
//				if (jsonArray != null && jsonArray.length() > 0) {
//					for (int index = 0; index < jsonArray.length(); index++) {
//						JSONObject object = jsonArray.optJSONObject(index);
//						outputMap.put(object.optString("productId"), object.optLong("sellingCount"));
//					}
//				}
//			}
			productIdList.forEach(productId -> outputMap.put(productId, 0L));
		} catch (Exception e) {
			log.error("Exception occurred while data parsing of bestSellingProductDetails", e);
		}
		return outputMap;
	}



	public Map<String,Object> isValidSKU(String[] skus){
		log.info("Inside isValidSKU method");
		Map<String,Object> validateSKUMap = new HashMap<String, Object>();
		try {
		List<String> invalidSKUs = new ArrayList<String>();
		/*for(String sku:skus) {
			List<String> skuList = new ArrayList<String>();
			skuList.add(sku.trim());
			List<Product> productList = productRepository.findByProductSkuIn(skuList);
			if(productList.size()==0) {
				invalidSKUs.add(sku.trim());
			}
		}*/
		if(invalidSKUs.size()>0) {
			validateSKUMap.put("isValid", false);
			validateSKUMap.put("inValidSKUs", invalidSKUs);
		}else {
			validateSKUMap.put("isValid", true);
		}
		}catch(Exception e) {
			log.error("Exception inside isValidSKU method ",e);
		}
		
		return validateSKUMap;
	}
	
	public RestApiSuccessResponse updateExistingCollectionNew(CollectionRequest collectionRO){
		RestApiSuccessResponse rs = null;
		
		try {
			log.info("Inside updateExistingCollection: ");
			if(collectionRO.getIsFeatured()!=null) {
			if(collectionRO.getIsFeatured()) {
				log.info("Inside  IsFeatured IF: ");
				Map<String,Object> respMap = getFeautedCollection();
				if((Boolean)respMap.get("isFeaturedExists")) {
					Long obj1 = (Long)respMap.get("collectionId");
					if(!obj1.equals(collectionRO.getCollectionId())) {
					rs = new RestApiSuccessResponse(HttpStatus.OK.value(), "One Featured Collection Already Exists", null);
					return rs;
					}
				}
			}	
			}
			
		Optional<Collection> collectionOptional = collectionRepository.findById(collectionRO.getCollectionId());
		Collection collection = null;
		if(collectionOptional.isPresent()) {
			collection = collectionOptional.get();
		}
		Map<String,Object> respMap = new HashMap<String, Object>();
		PageTypeStatus pts = pageTypeStatusRepo.findByName(collectionRO.getPageTypeName());
		Optional<ProductCountryRule> productCountryRuleOptional =  productCountryRuleRepo.findById(collectionRO.getCountryRuleId());
		
		ProductCountryRule pcr = null;

		if(productCountryRuleOptional.isPresent()) {
			pcr = productCountryRuleOptional.get();
		}
		
		List<ProductView> viewList = prodViewRepo.findByProductSkuInAndStatusId(Arrays.asList(collection.getProductSkus().split(",")), Long.valueOf(0),null);
		List<String> productUUIDList = viewList.stream().map(ProductView :: getPcUuid).collect(Collectors.toList());

		List<String> productSkuList = Arrays.asList(collectionRO.getProductSKUs().trim().split(","));
		LinkedHashSet<String> skuSet = new LinkedHashSet<>(productSkuList);
		StringBuilder skuString = new StringBuilder();
		skuSet.stream().forEach(sku -> {skuString.append(sku).append(",");});
		String collectionROSku = StringUtils.substring(skuString.toString(), 0, skuString.toString().length()-1);
		
		for(String sku:collection.getProductSkus().split(",")) {
			try {	
			Product product = productRepository.findByProductSku(sku);
				List<ProductCombinations> pcList  = productCombinationsRepo.findByProduct(product);
				for(ProductCombinations pc:pcList) {
					String collectionIds = pc.getCollectionIds();
					if(collectionIds.contains(collectionRO.getCollectionId().toString())) {
						StringBuilder newSkus = new StringBuilder();
						for(String sortedCollection:collectionIds.split(",")) {
							if(!sortedCollection.contains(collectionRO.getCollectionId().toString())) {
								newSkus.append(sortedCollection).append(",");
							}
						}
						if(!newSkus.toString().isEmpty()) {
						pc.setCollectionIds(newSkus.toString().substring(0,newSkus.toString().length()-1));
						}else {
							pc.setCollectionIds(newSkus.toString());
						}
					}
				}
				productCombinationsRepo.saveAll(pcList);
			}catch(Exception e) {
				log.error("Exception inside updateProductCombination 1",e);
			}
		}
		
		int index=0;
		for(String sku:collectionROSku.split(",")) {
			try {	
			Product product = productRepository.findByProductSku(sku);
				List<ProductCombinations> pcList  = productCombinationsRepo.findByProduct(product);
				for(ProductCombinations pc:pcList) {
					index = index+1;
					if(pc.getCollectionIds()==null) {
						pc.setCollectionIds(collectionRO.getCollectionId()+"="+index);
					}else {
						pc.setCollectionIds(pc.getCollectionIds()+","+collectionRO.getCollectionId()+"="+index);
					}
					pc.setUpdatedOn(new Date());
				}
				productCombinationsRepo.saveAll(pcList);
			}catch(Exception e) {
				log.error("Exception inside updateProductCombination 2",e);
			}
		}
		
		int isUpdated = collectionRepository.updateCollection(collectionRO.getShortDescription(), collectionRO.getLongDescription(),
				pcr,collectionRO.getWhiteListedCountries(), collectionRO.getBlackListedCountries(), pts.getId().intValue(),
				 collectionRO.getHeroBanner(), collectionRO.getRoundThumbnail(), collectionRO.getSquareThumbnail(), collectionROSku, new Date(System.currentTimeMillis()), collectionRO.getType(), collectionRO.getStatus()
				 ,collectionRO.getTitle(),collectionRO.getIsFeatured(),collectionRO.getIsBanner(),collectionRO.getAttributes(),collectionRO.getCondition(),
				 collectionRO.getCollectionId());
		
		if(collectionRO.getStatus()==0) {
			collectionRepository.updateFeaturedFalse(collectionRO.getCollectionId());
		}
		
		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
		viewList.stream().forEach(view -> {
			final ProductBulkUpdateException exception =new ProductBulkUpdateException();
			String exceptionMessage="";
			exception.setInsertedOn(new Date());
			exception.setUUUID(view.getPcUuid());
			exception.setApiName("updateCollection");
			ProductInventoryBO bo=null;
		     try {
		    	bo= uploadService.populateProductInventoryObj(view, "IN", "INR", bestSellingProductCountDetailMap);
		     }catch (Exception e) {
		    	 exception.setExceptionStage("Creating product Obj");
					exception.setRemark("Exception for creating prod obj for uuid "+view.getPcUuid());
					exception.setException(ExceptionUtils.getStackTrace(e));
					exception.setIsUpdated(false);
					exception.setFromApi(true);
					exception.setFromScheduler(false);
					exceptionMessage=e.getMessage();
					try {
						exceptionRepo.save(exception);
					} catch (Exception e1) {
						log.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
						exception.setException(exceptionMessage);
						exceptionRepo.save(exception);
					}
			}
			
			
			pushProductToKafkaScheduler.sendToKafka(bo,exception);
			exception.setExceptionStage("Updated Successfully !!!");
			exception.setRemark(" prod obj Updated Successfully for uuid "+view.getPcUuid());
			exception.setException("N/A");
			exception.setIsUpdated(true);
			exception.setFromApi(true);
			exception.setFromScheduler(false);
			exceptionMessage="N/A";
			try {
				exceptionRepo.save(exception);
			} catch (Exception e1) {
				log.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
				exception.setException(exceptionMessage);
				exceptionRepo.save(exception);
			}
		});		
		respMap.put("isUpdated", isUpdated);
		rs = new RestApiSuccessResponse(HttpStatus.OK.value(), "Collection Updated Successfully!!", respMap);
		return rs;
		
		}catch(Exception e) {
			log.error("exception inside updateExistingCollection 3",e);
		}
		
		return rs;
	}
	
	
	public RestApiSuccessResponse updateExistingCollection(CollectionRequest collectionRO){
		RestApiSuccessResponse rs = null;
		try {
		log.info("Inside updateExistingCollection: ");
		if(collectionRO.getIsFeatured()!=null) {
		if(collectionRO.getIsFeatured()) {
			log.info("Inside  IsFeatured IF: ");
			Map<String,Object> respMap = getFeautedCollection();
			if((Boolean)respMap.get("isFeaturedExists")) {
				Long obj1 = (Long)respMap.get("collectionId");
				if(!obj1.equals(collectionRO.getCollectionId())) {
				rs = new RestApiSuccessResponse(HttpStatus.OK.value(), "One Featured Collection Already Exists", null);
				return rs;
				}
			}
		}	
		}	
		
		Map<String,Object> respMap = new HashMap<String, Object>();
		List<ProductView> viewList = new ArrayList<ProductView>();
		PageTypeStatus pts = pageTypeStatusRepo.findByName(collectionRO.getPageTypeName());
		Optional<ProductCountryRule> productCountryRuleOptional =  productCountryRuleRepo.findById(collectionRO.getCountryRuleId());
		
		ProductCountryRule pcr = null;

		if(productCountryRuleOptional.isPresent()) {
			pcr = productCountryRuleOptional.get();
		}
		
		Optional<Collection> collectionOptional = collectionRepository.findById(collectionRO.getCollectionId());
		Collection collection = null;
		if(collectionOptional.isPresent()) {
			collection = collectionOptional.get();
		}
		String[] dbSavedSKUArray = collection.getProductSkus().split(",");
		List<String> dbSavedSkuList = new ArrayList<String>();
		for(String sku:dbSavedSKUArray) {
			dbSavedSkuList.add(sku.trim());
		}
		List<String> differenceForRemoving = new ArrayList<String>(dbSavedSkuList);
		String[] newSKUArray = collectionRO.getProductSKUs().split(",");
		List<String> newSkuList = new ArrayList<String>();
		for(String sku:newSKUArray) {
			newSkuList.add(sku.trim());
		}
		List<String> differenceForAdding = new ArrayList<String>(newSkuList);
		differenceForRemoving.removeAll(newSkuList);
		differenceForAdding.removeAll(dbSavedSkuList);
		log.info("SKUs that are to be removed ",differenceForRemoving);
		log.info("SKUs that are to be added ",differenceForAdding);
		
		String[] arr = new String[differenceForAdding.size()];
		String newSKUsToAdd ="";
		String oldSKUsToRemove = "";
		String totalUpdatedSKUs = "";
        for (int i = 0; i < differenceForAdding.size(); i++) {
            arr[i] = differenceForAdding.get(i);
            newSKUsToAdd = newSKUsToAdd + differenceForAdding.get(i)+",";
        }
        for (int i = 0; i < differenceForRemoving.size(); i++) {
        	oldSKUsToRemove = oldSKUsToRemove + differenceForRemoving.get(i)+",";
        }
        totalUpdatedSKUs = oldSKUsToRemove + "" +newSKUsToAdd;
        if(totalUpdatedSKUs.length()!=0) {
        totalUpdatedSKUs = totalUpdatedSKUs.substring(0, totalUpdatedSKUs.length()-1);
        }
        log.info("Total updated SKUs ",differenceForAdding);
		Map<String,Object> validateSKUMap =  isValidSKU(arr);
		log.info("Invalid SKUs Map ",validateSKUMap.toString());
		if((Boolean)validateSKUMap.get("isValid")==false) {
			 rs = new RestApiSuccessResponse(HttpStatus.OK.value(), "SKUs provided are not valid", validateSKUMap.get("inValidSKUs"));
			return rs;
		}
		if(differenceForRemoving.size()>0) {
			removeCollectionIdFromProduct(differenceForRemoving,collectionRO.getCollectionId().toString());
		}
		
		int isUpdated = collectionRepository.updateCollection(collectionRO.getShortDescription(), collectionRO.getLongDescription(),
				pcr,collectionRO.getWhiteListedCountries(), collectionRO.getBlackListedCountries(), pts.getId().intValue(),
				 collectionRO.getHeroBanner(), collectionRO.getRoundThumbnail(), collectionRO.getSquareThumbnail(), collectionRO.getProductSKUs().trim(), new Date(System.currentTimeMillis()), collectionRO.getType(), 
				 collectionRO.getStatus(),collectionRO.getTitle(),collectionRO.getIsFeatured(),collectionRO.getIsBanner(),collectionRO.getAttributes(),collectionRO.getCondition(),collectionRO.getCollectionId());
		if(collectionRO.getStatus()==0) {
			collectionRepository.updateFeaturedFalse(collectionRO.getCollectionId());
		}
		updateProductCombination(newSKUsToAdd,collectionRO.getCollectionId());
		viewList = prodViewRepo.findByProductSkuInAndStatusId(Arrays.asList(totalUpdatedSKUs), Long.valueOf(0),null);
		List<String> productUUIDList = viewList.stream().map(ProductView :: getPcUuid).collect(Collectors.toList());
		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
		viewList.stream().forEach(view -> {
			final ProductBulkUpdateException exception =new ProductBulkUpdateException();
			String exceptionMessage="";
			exception.setInsertedOn(new Date());
			exception.setUUUID(view.getPcUuid());
			exception.setApiName("updateCollection");
			ProductInventoryBO bo=null;
		     try {
		    	bo= uploadService.populateProductInventoryObj(view, "IN", "INR", bestSellingProductCountDetailMap);
		     }catch (Exception e) {
		    	 exception.setExceptionStage("Creating product Obj");
					exception.setRemark("Exception for creating prod obj for uuid "+view.getPcUuid());
					exception.setException(ExceptionUtils.getStackTrace(e));
					exception.setIsUpdated(false);
					exception.setFromApi(true);
					exception.setFromScheduler(false);
					exceptionMessage=e.getMessage();
					try {
						exceptionRepo.save(exception);
					} catch (Exception e1) {
						log.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
						exception.setException(exceptionMessage);
						exceptionRepo.save(exception);
					}
			}
			
			
			pushProductToKafkaScheduler.sendToKafka(bo,exception);
			exception.setExceptionStage("Updated Successfully !!!");
			exception.setRemark(" prod obj Updated Successfully for uuid "+view.getPcUuid());
			exception.setException("N/A");
			exception.setIsUpdated(true);
			exception.setFromApi(true);
			exception.setFromScheduler(false);
			exceptionMessage="N/A";
			try {
				exceptionRepo.save(exception);
			} catch (Exception e1) {
				log.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
				exception.setException(exceptionMessage);
				exceptionRepo.save(exception);
			}
		});		
		respMap.put("isUpdated", isUpdated);
		rs = new RestApiSuccessResponse(HttpStatus.OK.value(), "Collection Updated Successfully!!", respMap);
		flushCollectionCache();
		return rs;
		}catch(Exception e) {
			log.error("exception inside updateExistingCollection ",e);
		}
		flushCollectionCache();
		return rs;
	}
	
	public RestApiSuccessResponse createNewCollection(CollectionRequest collectionRO){
		log.info("Inside createNewCollection method with title "+collectionRO.getTitle());
		if(collectionRO.getIsFeatured()) {
			log.info("Inside IF condtion for ISFeatured Collection");
			Map<String,Object> respMap = getFeautedCollection();
			if((Boolean)respMap.get("isFeaturedExists")) {
				RestApiSuccessResponse rs = new RestApiSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "One Featured Collection Already Exists", null);
				return rs;
			}
		}
		/*Map<String,Object> validateSKUMap =  isValidSKU(collectionRO.getProductSKUs().split(","));
		if((Boolean)validateSKUMap.get("isValid")==false) {
			RestApiSuccessResponse rs = new RestApiSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SKUs provided are not valid", validateSKUMap.get("inValidSKUs"));
			return rs;
		}*/
		
		try {
		Collection cc = collectionRepository.findByTitle(collectionRO.getTitle());
		if(cc!=null) {
			RestApiSuccessResponse rs = new RestApiSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Duplicate Collection", null);
			return rs;
		}
		}catch(Exception e) {
			RestApiSuccessResponse rs = new RestApiSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "‘Duplicate Collection", null);
			return rs;
		}

		Map<String,Object> respMap = new HashMap<String, Object>();
		PageTypeStatus pts = pageTypeStatusRepo.findByName(collectionRO.getPageTypeName());
		Optional<ProductCountryRule> productCountryRule =  productCountryRuleRepo.findById(collectionRO.getCountryRuleId());
		
		List<String> productSkuList = Arrays.asList(collectionRO.getProductSKUs().trim().split(","));
		LinkedHashSet<String> skuSet = new LinkedHashSet<>(productSkuList);
		StringBuilder skuString = new StringBuilder();
		skuSet.stream().forEach(sku -> {skuString.append(sku).append(",");});
		String collectionROSku = StringUtils.substring(skuString.toString(), 0, skuString.toString().length()-1);
		
		log.info("Going to add new Collection");
		if(collectionRO.getIsBanner()==null) {
			collectionRO.setIsBanner(false);
		}
		Collection collectionNew = new Collection(collectionRO.getTitle(), collectionRO.getShortDescription(), collectionRO.getLongDescription(),
				 productCountryRule.get(),collectionRO.getWhiteListedCountries(), collectionRO.getBlackListedCountries(), pts.getId().intValue(),
				 collectionRO.getHeroBanner(), collectionRO.getRoundThumbnail(), collectionRO.getSquareThumbnail(), collectionROSku,
				 new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), collectionRO.getType(), collectionRO.getStatus(),
				 collectionRO.getIsFeatured(),collectionRO.getIsBanner(),collectionRO.getAttributes(),collectionRO.getCondition());
		collectionNew = collectionRepository.save(collectionNew);
		log.info("Collection created with collection id "+collectionNew.getId());
		String catalogCollectionUrl="";
		catalogCollectionUrl = catalogCollectionSearchUrl.replace("{collectionId}", collectionNew.getId().toString()).replace("{title}", collectionNew.getTitle());
		log.info("Banner url created"+catalogCollectionUrl);
		collectionNew.setBannerUrl(catalogCollectionUrl);
		collectionNew = collectionRepository.save(collectionNew);
		log.info("After saving collection banner url of collection id "+collectionNew.getId());
		SchedulerConfig config = schedulerConfigRepo.findBySchedulerName("CreateCollectionScheduler");
		config.setRun(true);
		config.setRecoredId(collectionNew.getId());
		schedulerConfigRepo.save(config);
		//updateProductCombination(collectionNew.getProductSkus(),collectionNew.getId());
		flushCollectionCache();
		respMap.put("isCreated", 1);
		//updateProductOnKafka(collectionNew);
		RestApiSuccessResponse rs = new RestApiSuccessResponse(HttpStatus.OK.value(), "Collection created succesfully, you will be able to see products in the new collection in a while !!!", respMap);
		return rs;
	}
	
	public void updateProductOnKafka(Collection collectionNew) {
		List<ProductView> viewList = prodViewRepo.findByProductSkuInAndStatusId(Arrays.asList(collectionNew.getProductSkus().split(",")), Long.valueOf(0),null);
		if(!viewList.isEmpty()) {
		List<String> productUUIDList = viewList.stream().map(ProductView :: getPcUuid).collect(Collectors.toList());
		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
		viewList.stream().forEach(view -> {
			final ProductBulkUpdateException exception =new ProductBulkUpdateException();
			String exceptionMessage="";
			exception.setInsertedOn(new Date());
			exception.setUUUID(view.getPcUuid());
			exception.setApiName("createNewCollection");
			ProductInventoryBO bo=null;
		     try {
		    	bo= uploadService.populateProductInventoryObj(view, "IN", "INR", bestSellingProductCountDetailMap);
		     }catch (Exception e) {
		    	 exception.setExceptionStage("Creating product Obj");
					exception.setRemark("Exception for creating prod obj for uuid "+view.getPcUuid());
					exception.setException(ExceptionUtils.getStackTrace(e));
					exception.setIsUpdated(false);
					exception.setFromApi(true);
					exception.setFromScheduler(false);
					exceptionMessage=e.getMessage();
					try {
						exceptionRepo.save(exception);
					} catch (Exception e1) {
						log.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
						exception.setException(exceptionMessage);
						exceptionRepo.save(exception);
					}
			}
			
			
			pushProductToKafkaScheduler.sendToKafka(bo,exception);
			exception.setExceptionStage("Updated Successfully !!!");
			exception.setRemark(" prod obj Updated Successfully for uuid "+view.getPcUuid());
			exception.setException("N/A");
			exception.setIsUpdated(true);
			exception.setFromApi(true);
			exception.setFromScheduler(false);
			exceptionMessage="N/A";
			try {
				exceptionRepo.save(exception);
			} catch (Exception e1) {
				log.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
				exception.setException(exceptionMessage);
				exceptionRepo.save(exception);
			}
		});
		}
	}
	
	
	public Map<String,Object> getFeautedCollection(){
		Map<String,Object> respMap = new HashMap<String, Object>();
		Collection collection = collectionRepository.findByIsFeatured(true);
		if(collection!=null) {
			respMap.put("isFeaturedExists", true);
			respMap.put("collectionId", collection.getId());
		}else {
			respMap.put("isFeaturedExists", false);
		}
		return respMap;
	}
	
	public void updateProductCombination(String productSkus,Long collectionId) {
		log.info("Inside updateProductCombination with productSkus "+productSkus.toString()+" and collectionId : "+collectionId);
		List<String> skuList = new ArrayList<>(Arrays.asList(productSkus.split(",")));
		if(!skuList.isEmpty()) {
		try {
		List<ProductCombinations> pcList  = productCombinationsRepo.findBySkuIn(skuList);
		pcList.stream().forEach(pc->{
			try {
			if(pc.getCollectionIds()==null) {
				pc.setCollectionIds(collectionId.toString());
			}else {
				if(!pc.getCollectionIds().contains(collectionId.toString())) {
					pc.setCollectionIds(pc.getCollectionIds()+","+collectionId);
				}
			}
			pc.setUpdatedOn(new Date());
			}catch(Exception e) {
				log.error("Exception inside updateProductCombination for unique identifier ",pc.getUniqueIdentifier(),e);
			}
		});
		productCombinationsRepo.saveAll(pcList);
		}catch(Exception e) {
			log.error("Exception inside updateProductCombination ",e);
		}
		}		
	}

	public CollectionBO mapCollectionDOtoBO(Collection collectionDO) {
		List<ProductInventoryBO> combinationList = new ArrayList<ProductInventoryBO>();
		CollectionBO collectionBO = new CollectionBO();
		collectionBO.setAddedOn(collectionDO.getAddedOn());
		collectionBO.setBlackListedCountries(collectionDO.getBlackListedCountries());
		collectionBO.setCountryRuleId(collectionDO.getProductCountryRule().getId());
		collectionBO.setHeroBanner(collectionDO.getHeroBanner());
		collectionBO.setLastUpdatedOn(collectionDO.getLastUpdatedOn());
		collectionBO.setLongDescription(collectionDO.getLongDescription());
		collectionBO.setPageTypeId(collectionDO.getPageTypeId());
		collectionBO.setRoundThumbnail(collectionDO.getRoundThumbnail());
		collectionBO.setShortDescription(collectionDO.getShortDescription());
		collectionBO.setSquareThumbnail(collectionDO.getSquareThumbnail());
		collectionBO.setStatus(collectionDO.getStatus());
		collectionBO.setTitle(collectionDO.getTitle());
		collectionBO.setType(collectionDO.getType());
		collectionBO.setWhiteListedCountries(collectionDO.getWhiteListedCountries());
		collectionBO.setCollectionId(collectionDO.getId());
		collectionBO.setProductSKUs(collectionDO.getProductSkus());
		collectionBO.setIsFeatured(collectionDO.getIsFeatured());
		String[] skuId = collectionDO.getProductSkus().split(",");
		List<ProductView> viewList = prodViewRepo.findBySkuInAndStatusId(Arrays.asList(skuId), Long.valueOf(0),null);
		List<String> productUUIDList = viewList.stream().map(ProductView :: getPcUuid).collect(Collectors.toList());
		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
		viewList.stream().forEach(view -> {
			combinationList.add(uploadService.populateProductInventoryObj(view, "IN", "INR", bestSellingProductCountDetailMap));
		});
		collectionBO.setProductInventoryBo(combinationList);
		collectionBO.setIsBanner(collectionDO.getIsBanner());
		collectionBO.setBannerUrl(collectionDO.getBannerUrl());
		
		return collectionBO;
	}
	
	public CollectionBO mapCollectionDOtoBODuplicate(Collection collectionDO) {	
		CollectionBO collectionBO = new CollectionBO();
		Optional<PageTypeStatus> ptsOpt = pageTypeStatusRepo.findById(Long.valueOf(collectionDO.getPageTypeId().toString()));
		if(ptsOpt.isPresent()){
			PageTypeStatus pts = ptsOpt.get();
			collectionBO.setPageTypeName(pts.getName());
		}	
		collectionBO.setAddedOn(collectionDO.getAddedOn());
		collectionBO.setBlackListedCountries(collectionDO.getBlackListedCountries());
		collectionBO.setCountryRuleId(collectionDO.getProductCountryRule().getId());
		collectionBO.setHeroBanner(collectionDO.getHeroBanner());
		collectionBO.setLastUpdatedOn(collectionDO.getLastUpdatedOn());
		collectionBO.setLongDescription(collectionDO.getLongDescription());
		collectionBO.setPageTypeId(collectionDO.getPageTypeId());
		collectionBO.setRoundThumbnail(collectionDO.getRoundThumbnail());
		collectionBO.setShortDescription(collectionDO.getShortDescription());
		collectionBO.setSquareThumbnail(collectionDO.getSquareThumbnail());
		collectionBO.setStatus(collectionDO.getStatus());
		collectionBO.setTitle(collectionDO.getTitle());
		collectionBO.setType(collectionDO.getType());
		collectionBO.setWhiteListedCountries(collectionDO.getWhiteListedCountries());
		collectionBO.setCollectionId(collectionDO.getId());
		collectionBO.setProductSKUs(collectionDO.getProductSkus());
		collectionBO.setIsFeatured(collectionDO.getIsFeatured());
		collectionBO.setIsBanner(collectionDO.getIsBanner());
		collectionBO.setBannerUrl(collectionDO.getBannerUrl());
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(collectionDO.getAttributes()!=null)
				collectionBO.setAttributes(mapper.readTree(collectionDO.getAttributes()));
		}catch(JsonMappingException e){
			log.error("Exception e",e);
		}catch(JsonProcessingException e1) {
			log.error("Exception e1",e1);
		}
		collectionBO.setCondition(collectionDO.getCondition());
		
		
		return collectionBO;
	}
	
	private Integer getPagedStartIndex(Integer pageNum, Integer pageSize) {
		Integer startIndex = 1;
		if (pageNum != null && pageNum != 0) {
			startIndex = pageNum * pageSize + 1;
		}
		return startIndex;
	}

	private Integer getPagedEndIndex(Integer pageNum, Integer pageSize, Integer pagedDataCount) {
		return getPagedStartIndex(pageNum, pageSize) + pagedDataCount -1;
	}

	private Pageable getPagable(Integer pageNum, Integer pageSize, String sortOrder,String sortBy) {
		Pageable pageable = null;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
			pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, StringUtils.isNotBlank(sortBy)?sortBy:"id"));
		} else {
			pageable = PageRequest.of(pageNum, pageSize, Sort.by(StringUtils.isNotBlank(sortBy)?sortBy:"id"));
		}

		return pageable;
	}
	
	 public CollectionBO activateDeActivateCollection(Long collectionId,Integer status) {
		 Collection collection = null;
		 Optional<Collection> collectionOpt  = collectionRepository.findById(collectionId);
		 if(collectionOpt.isPresent()) {
			 collection = collectionOpt.get();
		 }
		 collection.setStatus(status);
		 if(status==0) {
			 collection.setIsFeatured(false);
		 }
		 collection = collectionRepository.save(collection);
		 flushCollectionCache();
		 CollectionBO collectionBO = mapCollectionDOtoBODuplicate(collection);
		 return collectionBO;
	 }
	 
	 public void removeCollectionIdFromProduct(List<String> skuToBeRemoved,String collectionId) {
		 log.info("Inside removeCollectionIdFromProduct method with skuToBeRemoved "+skuToBeRemoved.toString()+" and collectionId : ",collectionId);
		 try {
			List<Product> productList = productRepository.findByProductSkuIn(skuToBeRemoved);
			 log.info("Total count of Products ",productList.size());
			for(Product product:productList) {
				List<ProductCombinations> pcList  = productCombinationsRepo.findByProduct(product);
				for(ProductCombinations pc:pcList) {
					if(pc.getCollectionIds().contains(collectionId)) {
						if(pc.getCollectionIds().contains(collectionId+",")) {
							pc.setCollectionIds(pc.getCollectionIds().replace(collectionId+",", ""));
						}else {
							pc.setCollectionIds(pc.getCollectionIds().replace(collectionId, ""));
						}
					}
				}
				productCombinationsRepo.saveAll(pcList);
			}
		}catch(Exception e) {
			log.error("Exception Inside removeCollectionIdFromProduct method ",e); 
		 }
	 }
	 
		public void flushCollectionCache() {
			log.info("Before calling flushCollectionCache");
			cache.remove(InventoryConstants.COLLECTION_CACHE_KEY);
			log.info("After calling flushCollectionCache");
		}
		
		
	public void updateAllCollections() {
		for(Collection collection : collectionRepository.findAll()) {
			String[] skuList = collection.getProductSkus().split(",");
			List<String> productSkuList = Arrays.asList(skuList);			
			try {
				int index=0;	
				List<Product> productList = productRepository.findByProductSkuIn(productSkuList);
				log.info("Count of  Products ",productList.size());
				for(Product product:productList) {
					List<ProductCombinations> pcList  = productCombinationsRepo.findByProduct(product);	
					for(ProductCombinations pc:pcList) {
						index = index+1;
						String collectionSortId = collection.getId()+"="+index;
						if(pc.getCollectionIds()!=null) {
							if(pc.getCollectionIds().contains("=")) {
								if(!pc.getCollectionIds().contains(collectionSortId)) {
									pc.setCollectionIds(pc.getCollectionIds()+","+collectionSortId);
								}
							}else {
								pc.setCollectionIds(collectionSortId);
							}
						}else {
							pc.setCollectionIds(collectionSortId);
						}
						pc.setUpdatedOn(new Date());
					}
					productCombinationsRepo.saveAll(pcList);
				}
				}catch(Exception e) {
					log.error("Exception inside updateProductCombination ",e);
				}
			
			//updateProductOnKafka(collection);	
		}
	}

	@Transactional
	public void deleteCollection(Long collectionId){
		Optional<Collection> collection = collectionRepository.findById(collectionId);
		if(collection.isPresent()){
			String productSkus = collection.get().getProductSkus();
			String[] skus = productSkus.split(",");
			List<Product> productList = productRepository.findByProductSkuIn(Arrays.asList(skus));
			log.info("Product List size : {}", productList.size());
			List<Long> productIds = productList.stream().map(Product::getId).collect(Collectors.toList());
			List<ProductCombinations> pcList = productCombinationsRepo.findByProductIdIn(productIds);
			log.info("ProductCombinations size : {}", pcList.size());
			pcList.stream().filter(pc -> pc != null).forEach(pc -> {
				String collectionIds = pc.getCollectionIds();
				if(collectionIds != null){
				List<String> collectionIdList = Arrays.asList(collectionIds.split(","));
				log.info("collectionIdList : {}", collectionIdList);
				List<String> collect = collectionIdList.stream().filter(collectionString -> !collectionString.contains(String.valueOf(collectionId)))
						.collect(Collectors.toList());
				log.info("collectionIdList : {}", collect);
				String collectionIdListString = String.join(",", collect);
				log.info("collectionIdListString : {}", collectionIdListString);
				pc.setCollectionIds(collectionIdListString);
				pc.setUpdatedOn(new Date());
			}});
			log.info("updating product combinations started");
			productCombinationsRepo.saveAll(pcList);
			log.info("updating product combinations completed");
			log.info("Started deleting collection with id : {}",collectionId);
			collectionRepository.deleteCollection(collectionId);
			log.info("Completed deleting collection with id : {}",collectionId);
		}else{
			throw new NoSuchElementException("Collection not found");
		}
	}
		
	
	public String addNewAttributeToCollectionMaster(DynamicCollectionAttributeMasterBo dynamicCollectionAttributeMasterBo) {
		String message ="";
		//dynamicCollectionAttributeMasterRepo.save(mapBoToDo(dynamicCollectionAttributeMasterBo));
		message="Added Successfully";
		return message; 		
	}
	
	public List<DynamicCollectionAttributeMasterBo> getAllAttributes(){
		List<DynamicCollectionAttributeMasterBo> masterBoList = new ArrayList<>();
		List<DynamicCollectionAttributeMaster> masterDoList = dynamicCollectionAttributeMasterRepo.findAll();
		masterDoList.stream().forEach(masterDo->{
			masterBoList.add(mapDoToBo(masterDo));
		});
		return masterBoList;
	}
	
	/*
	public DynamicCollectionAttributeMaster mapBoToDo(DynamicCollectionAttributeMasterBo dynamicCollectionAttributeMasterBo) {
		DynamicCollectionAttributeMaster masterDo = new DynamicCollectionAttributeMaster();
		try {
		masterDo.setAttribute(dynamicCollectionAttributeMasterBo.getAttribute());
		masterDo.setBetweenOperator(dynamicCollectionAttributeMasterBo.getOperations().getBetweenOperator());
		masterDo.setColumnName(dynamicCollectionAttributeMasterBo.getColumnName());
		masterDo.setEqualToOperator(dynamicCollectionAttributeMasterBo.getOperations().getEqualToOperator());
		masterDo.setGreaterThanOperator(dynamicCollectionAttributeMasterBo.getOperations().getGreaterThanOperator());
		masterDo.setInOperator(dynamicCollectionAttributeMasterBo.getOperations().getInOperator());
		masterDo.setLesserThanOperator(dynamicCollectionAttributeMasterBo.getOperations().getLesserThanOperator());
		masterDo.setNotEqualToOperator(dynamicCollectionAttributeMasterBo.getOperations().getNotEqualToOperator());
		masterDo.setNotInOperator(dynamicCollectionAttributeMasterBo.getOperations().getNotInOperator());
		}catch(Exception e){
			log.error("Exception inside mapBoToDo ",e);
		}
		return masterDo;
	}*/
	
	public DynamicCollectionAttributeMasterBo mapDoToBo(DynamicCollectionAttributeMaster dynamicCollectionAttributeMasterDo) {
		String jsonResp ="";
		DynamicCollectionAttributeMasterBo masterBo = new DynamicCollectionAttributeMasterBo();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		try {
		Map<String,String> operations = new HashMap<String, String>();
		if(dynamicCollectionAttributeMasterDo.getBetweenOperator())
			operations.put("betweenOperator", "Between"); 
		if(dynamicCollectionAttributeMasterDo.getEqualToOperator())	
			operations.put("equalToOperator", "Equal To");
		if(dynamicCollectionAttributeMasterDo.getGreaterThanOperator())
			operations.put("greaterThanOperator", "Greater Than");
		if(dynamicCollectionAttributeMasterDo.getInOperator())
			operations.put("inOperator", "In");
		if(dynamicCollectionAttributeMasterDo.getLesserThanOperator())
			operations.put("lesserThanOperator", "Less Than");
		if(dynamicCollectionAttributeMasterDo.getNotEqualToOperator())
			operations.put("notEqualToOperator", "Not Equal To");
		if(dynamicCollectionAttributeMasterDo.getNotInOperator())
			operations.put("notInOperator", "Not In");

		masterBo.setAttribute(dynamicCollectionAttributeMasterDo.getAttribute());
		masterBo.setColumnName(dynamicCollectionAttributeMasterDo.getColumnName());
		masterBo.setOperations(operations);	
		
		/*if(masterBo.getAttribute().equalsIgnoreCase("Brand")) {
			Set<String> brandNames=new HashSet<>();
			Set<BrandBO> brands = brandService.getAllBrands("IN");
			brands.stream().forEach(brand->{
				brandNames.add(brand.getName());
			});
			masterBo.setValues(brandNames);
		}*/

		if(masterBo.getAttribute().equalsIgnoreCase("Brand")) {
			Set<String> brandNames=new HashSet<>();
			List<Brands> brands = brandsRepository.findAllActiveBrands();
			brands.stream().forEach(brand->{
				brandNames.add(brand.getName());
			});
			masterBo.setValues(brandNames);
		}

		if(masterBo.getAttribute().equalsIgnoreCase("Tags")) {
			Set<String> tagNames=new HashSet<>();
			List<TagDto> tags = tagRepository.findAll();
			tags.stream().forEach(tag->{
				tagNames.add(tag.getTagName());
			});
			masterBo.setValues(tagNames);
		}

		if(masterBo.getAttribute().equalsIgnoreCase("LowestCategory")) {
			masterBo.setValues(brandService.getLowestLevelCategory("IN"));
		}
		
		}catch(Exception e) {
			log.error("Exception inside mapDoToBo ",e);
		}
		return masterBo;
	}

	public RestApiSuccessResponse addDynamicCollection(DynamicCollectionRequest dynamicCollectionRequest) {
		CollectionRequest collectionRq = new CollectionRequest();
		if(dynamicCollectionRequest.getIsBanner()==null) {
			dynamicCollectionRequest.setIsBanner(false);
		}
		collectionRq.setBannerUrl(dynamicCollectionRequest.getBannerUrl());
		collectionRq.setBlackListedCountries(dynamicCollectionRequest.getBlackListedCountries());
		collectionRq.setCountryRuleId(dynamicCollectionRequest.getCountryRuleId());
		collectionRq.setHeroBanner(dynamicCollectionRequest.getHeroBanner());
		collectionRq.setIsBanner(dynamicCollectionRequest.getIsBanner());
		collectionRq.setIsFeatured(dynamicCollectionRequest.getIsFeatured());
		collectionRq.setLongDescription(dynamicCollectionRequest.getLongDescription());
		collectionRq.setPageTypeName(dynamicCollectionRequest.getPageTypeName());
		collectionRq.setRoundThumbnail(dynamicCollectionRequest.getRoundThumbnail());
		collectionRq.setShortDescription(dynamicCollectionRequest.getShortDescription());
		collectionRq.setSquareThumbnail(dynamicCollectionRequest.getSquareThumbnail());
		collectionRq.setStatus(dynamicCollectionRequest.getStatus());
		collectionRq.setTitle(dynamicCollectionRequest.getTitle());
		collectionRq.setType(dynamicCollectionRequest.getType());
		collectionRq.setWhiteListedCountries(dynamicCollectionRequest.getWhiteListedCountries());
		collectionRq.setCondition(dynamicCollectionRequest.getCondition());
		ObjectMapper mapper = new ObjectMapper();
		String attributesJson ="";
		try {
			attributesJson = mapper.writeValueAsString(dynamicCollectionRequest.getAttributes());
		}catch(JsonProcessingException e) {
			log.error("Exception in Object mapper ",e);
		}
		collectionRq.setAttributes(attributesJson);
		String productSku = getProductSKUs(dynamicCollectionRequest);
		/*if(productSku.isEmpty()) {
			Map<String,Object> respMap = new HashMap<>();
			respMap.put("isCreated", 0);
			RestApiSuccessResponse rs = new RestApiSuccessResponse(HttpStatus.OK.value(), "Couldn't find any SKU based on the provided creteria", respMap);
			return rs;
		}*/
		collectionRq.setProductSKUs(productSku);
		if(dynamicCollectionRequest.getCollectionId()!=null) {
			collectionRq.setCollectionId(dynamicCollectionRequest.getCollectionId());
			return updateExistingCollection(collectionRq);
		}else {
			return createNewCollection(collectionRq);
		}	
		
	}
	
	/*public String getProductSKUs(DynamicCollectionRequest dynamicCollectionRequest) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductView> criteriaQuery = criteriaBuilder.createQuery(ProductView.class);
		Root<ProductView> itemRoot = criteriaQuery.from(ProductView.class);
		List<Predicate> predicates = new ArrayList<>();
		
		dynamicCollectionRequest.getAttributes().stream().forEach(dynamicCollectionCondtion->{
			boolean brand=false;
			boolean supplierId=false;
			boolean supplierEmail=false;
			boolean lowestCategory=false;
			boolean price=false;
			boolean stock=false;

			boolean brandIsEqualTo = false;
			boolean brandIn = false;
			boolean brandIsNotEqualTo = false;
			boolean brandIsNotIn = false;
			boolean supplierIdIsEqualTo = false;
			boolean supplierIdIn = false;
			boolean supplierIdIsNotEqualTo = false;
			boolean supplierIdIsNotIn = false;
			boolean supplierEmailIsEqualTo = false;
			boolean supplierEmailIn = false;
			boolean supplierEmailIsNotEqualTo = false;
			boolean supplierEmailIsNotIn = false;
			boolean lowestCategoryIsEqualTo = false;
			boolean lowestCategoryIn = false;
			boolean lowestCategoryIsNotEqualTo = false;
			boolean lowestCategoryIsNotIn = false;
			boolean priceIsEqualTo = false;
			boolean priceLessThan = false;
			boolean priceGreaterThan = false;
			boolean priceBetween = false;
			boolean stockIsEqualTo = false;
			boolean stockLessThan = false;
			boolean stockGreaterThan = false;
			boolean stockBetween = false;
			
			if(dynamicCollectionCondtion.getName().equalsIgnoreCase("Brand")) {
				brand = true;
				if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					brandIsEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					brandIn = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					brandIsNotEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					brandIsNotIn = true;
				}
			}
			if(dynamicCollectionCondtion.getName().equalsIgnoreCase("SupplierID")) {
				supplierId=true;
				if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					supplierIdIsEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					supplierIdIn = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					supplierIdIsNotEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					supplierIdIsNotIn = true;
				}
			}
			if(dynamicCollectionCondtion.getName().equalsIgnoreCase("SupplierEmail")) {
				supplierEmail=true;
				if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					supplierEmailIsEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					supplierEmailIn = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					supplierEmailIsNotEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					supplierEmailIsNotIn = true;
				}
			}
			if(dynamicCollectionCondtion.getName().equalsIgnoreCase("LowestCategory")) {
				lowestCategory=true;
				if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					lowestCategoryIsEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					lowestCategoryIn = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					lowestCategoryIsNotEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					lowestCategoryIsNotIn = true;
				}
			}
			if(dynamicCollectionCondtion.getName().equalsIgnoreCase("Price")) {
				price=true;
				if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					priceIsEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("lesserThanOperator")) {
					priceLessThan = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("greaterThanOperator")) {
					priceGreaterThan = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("betweenOperator")) {
					priceBetween = true;
				}
			}
			if(dynamicCollectionCondtion.getName().equalsIgnoreCase("Stock")) {
				stock=true;
				if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					stockIsEqualTo = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("lesserThanOperator")) {
					stockLessThan = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("greaterThanOperator")) {
					stockGreaterThan = true;
				}else if(dynamicCollectionCondtion.getOperator().equalsIgnoreCase("betweenOperator")) {
					stockBetween = true;
				}
			}
			
			if(brand) {
				if(brandIsEqualTo || brandIn) {
					Predicate predicate = itemRoot.get("brandName").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				}else if(brandIsNotEqualTo || brandIsNotIn) {
					Predicate predicate = itemRoot.get("brandName").in(dynamicCollectionCondtion.getValues()).not();
				    predicates.add(predicate);
				}
			}
			
			if(supplierId) {
				if(supplierIdIsEqualTo || supplierIdIn) {
					Predicate predicate = itemRoot.get("supplierId").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				}else if(supplierIdIsNotEqualTo || supplierIdIsNotIn) {
					Predicate predicate = itemRoot.get("supplierId").in(dynamicCollectionCondtion.getValues()).not();
					predicates.add(predicate);
				}
			}
			
			if(supplierEmail) {
				if(supplierEmailIsEqualTo || supplierEmailIn) {
					Predicate predicate = itemRoot.get("supplierEmail").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				}else if(supplierEmailIsNotEqualTo || supplierEmailIsNotIn){
					Predicate predicate = itemRoot.get("supplierEmail").in(dynamicCollectionCondtion.getValues()).not();
					predicates.add(predicate);
				}
			}
			
			if(lowestCategory) {
				if(lowestCategoryIsEqualTo || lowestCategoryIn) {
					Predicate predicate = itemRoot.get("lowestCategoryName").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				}else if(lowestCategoryIsNotEqualTo || lowestCategoryIsNotIn) {
					Predicate predicate = itemRoot.get("lowestCategoryName").in(dynamicCollectionCondtion.getValues()).not();
					predicates.add(predicate);
				}
			}
			
			if(price) {
				if(priceIsEqualTo) {
					Predicate predicate = itemRoot.get("price").in(dynamicCollectionCondtion.getValues().get(0));
					predicates.add(predicate);
				}else if(priceLessThan) {
					Predicate predicate = criteriaBuilder.lessThan(itemRoot.get("price"), dynamicCollectionCondtion.getValues().get(0));
					predicates.add(predicate);
				}else if(priceGreaterThan) {
					Predicate predicate = criteriaBuilder.greaterThan(itemRoot.get("price"), dynamicCollectionCondtion.getValues().get(0));
					predicates.add(predicate);
				}else if(priceBetween) {
					Predicate predicate = criteriaBuilder.between(itemRoot.get("price"), dynamicCollectionCondtion.getValues().get(0),
							dynamicCollectionCondtion.getValues().get(1));
					predicates.add(predicate);
				}
			}
			
			if(stock) {
				if(stockIsEqualTo) {
					Predicate predicate = itemRoot.get("availableStock").in(Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				}else if(stockLessThan) {
					Predicate predicate = criteriaBuilder.lessThan(itemRoot.get("availableStock"),Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				}else if(stockGreaterThan) {
					Predicate predicate = criteriaBuilder.greaterThan(itemRoot.get("availableStock"),Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				}else if(stockBetween) {
					Predicate predicate = criteriaBuilder.between(itemRoot.get("availableStock"),Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)),
							Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				}
			}

		});
		
		
		
		if(dynamicCollectionRequest.getCondition().equalsIgnoreCase("AND")) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}
		if(dynamicCollectionRequest.getCondition().equalsIgnoreCase("OR")) {
			criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
		}
		List<ProductView> viewList = entityManager.createQuery(criteriaQuery).getResultList();
		log.info("list fetched");
		StringBuilder builder = new StringBuilder();
		viewList.stream().forEach(view->{
			if(view.getIsActive()) {
				builder.append(view.getSku()).append(",");
			}
		});
		String productSkus ="";
		if(builder.length()>0) {
			productSkus = builder.substring(0, builder.length()-1).toString();
		}
		return productSkus;
	}*/

	public String getProductSKUs(DynamicCollectionRequest dynamicCollectionRequest) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductView> criteriaQuery = criteriaBuilder.createQuery(ProductView.class);
		Root<ProductView> itemRoot = criteriaQuery.from(ProductView.class);
		List<Predicate> predicates = new ArrayList<>();
		String productSkus = "";
		List<List<ProductView>> viewList = new ArrayList<>();
		AtomicReference<List<ProductView>> viewBestSellerList = null;

		dynamicCollectionRequest.getAttributes().stream().forEach(dynamicCollectionCondtion -> {
			boolean brand = false;
			boolean supplierId = false;
			boolean supplierEmail = false;
			boolean lowestCategory = false;
			boolean price = false;
			boolean stock = false;
			boolean tags = false;

			boolean brandIsEqualTo = false;
			boolean brandIn = false;
			boolean brandIsNotEqualTo = false;
			boolean brandIsNotIn = false;
			boolean supplierIdIsEqualTo = false;
			boolean supplierIdIn = false;
			boolean supplierIdIsNotEqualTo = false;
			boolean supplierIdIsNotIn = false;
			boolean supplierEmailIsEqualTo = false;
			boolean supplierEmailIn = false;
			boolean supplierEmailIsNotEqualTo = false;
			boolean supplierEmailIsNotIn = false;
			boolean lowestCategoryIsEqualTo = false;
			boolean lowestCategoryIn = false;
			boolean lowestCategoryIsNotEqualTo = false;
			boolean lowestCategoryIsNotIn = false;
			boolean priceIsEqualTo = false;
			boolean priceLessThan = false;
			boolean priceGreaterThan = false;
			boolean priceBetween = false;
			boolean stockIsEqualTo = false;
			boolean stockLessThan = false;
			boolean stockGreaterThan = false;
			boolean stockBetween = false;
			boolean tagsIsEqualTo = false;
			boolean tagsIn = false;
			boolean tagsIsNotEqualTo = false;
			boolean tagsIsNotIn = false;

			if (dynamicCollectionCondtion.getName().equalsIgnoreCase("Brand")) {
				brand = true;
				if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					brandIsEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					brandIn = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					brandIsNotEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					brandIsNotIn = true;
				}
			}
			if (dynamicCollectionCondtion.getName().equalsIgnoreCase("SupplierID")) {
				supplierId = true;
				if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					supplierIdIsEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					supplierIdIn = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					supplierIdIsNotEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					supplierIdIsNotIn = true;
				}
			}
			if (dynamicCollectionCondtion.getName().equalsIgnoreCase("SupplierEmail")) {
				supplierEmail = true;
				if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					supplierEmailIsEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					supplierEmailIn = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					supplierEmailIsNotEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					supplierEmailIsNotIn = true;
				}
			}
			if (dynamicCollectionCondtion.getName().equalsIgnoreCase("LowestCategory")) {
				lowestCategory = true;
				if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					lowestCategoryIsEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					lowestCategoryIn = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					lowestCategoryIsNotEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					lowestCategoryIsNotIn = true;
				}
			}
			if (dynamicCollectionCondtion.getName().equalsIgnoreCase("Price")) {
				price = true;
				if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					priceIsEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("lesserThanOperator")) {
					priceLessThan = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("greaterThanOperator")) {
					priceGreaterThan = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("betweenOperator")) {
					priceBetween = true;
				}
			}
			if (dynamicCollectionCondtion.getName().equalsIgnoreCase("Stock")) {
				stock = true;
				if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					stockIsEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("lesserThanOperator")) {
					stockLessThan = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("greaterThanOperator")) {
					stockGreaterThan = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("betweenOperator")) {
					stockBetween = true;
				}
			}
			if (dynamicCollectionCondtion.getName().equalsIgnoreCase("Tags")) {
				tags = true;
				if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("equalToOperator")) {
					tagsIsEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("inOperator")) {
					tagsIn = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notEqualToOperator")) {
					tagsIsNotEqualTo = true;
				} else if (dynamicCollectionCondtion.getOperator().equalsIgnoreCase("notInOperator")) {
					tagsIsNotIn = true;
				}
			}

			if (brand) {
				if (brandIsEqualTo || brandIn) {
					Predicate predicate = itemRoot.get("brandName").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				} else if (brandIsNotEqualTo || brandIsNotIn) {
					Predicate predicate = itemRoot.get("brandName").in(dynamicCollectionCondtion.getValues()).not();
					predicates.add(predicate);
				}
			}

			if (supplierId) {
				if (supplierIdIsEqualTo || supplierIdIn) {
					Predicate predicate = itemRoot.get("supplierId").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				} else if (supplierIdIsNotEqualTo || supplierIdIsNotIn) {
					Predicate predicate = itemRoot.get("supplierId").in(dynamicCollectionCondtion.getValues()).not();
					predicates.add(predicate);
				}
			}

			if (supplierEmail) {
				if (supplierEmailIsEqualTo || supplierEmailIn) {
					Predicate predicate = itemRoot.get("supplierEmail").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				} else if (supplierEmailIsNotEqualTo || supplierEmailIsNotIn) {
					Predicate predicate = itemRoot.get("supplierEmail").in(dynamicCollectionCondtion.getValues()).not();
					predicates.add(predicate);
				}
			}

			if (lowestCategory) {
				if (lowestCategoryIsEqualTo || lowestCategoryIn) {
					Predicate predicate = itemRoot.get("lowestCategoryName").in(dynamicCollectionCondtion.getValues());
					predicates.add(predicate);
				} else if (lowestCategoryIsNotEqualTo || lowestCategoryIsNotIn) {
					Predicate predicate = itemRoot.get("lowestCategoryName").in(dynamicCollectionCondtion.getValues()).not();
					predicates.add(predicate);
				}
			}

			if (price) {
				if (priceIsEqualTo) {
					Predicate predicate = criteriaBuilder.like(itemRoot.get("displayPrice").as(String.class), Float.valueOf(dynamicCollectionCondtion.getValues().get(0)).toString());
					predicates.add(predicate);
				} else if (priceLessThan) {
					Predicate predicate = criteriaBuilder.lessThan(itemRoot.get("displayPrice"), Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				} else if (priceGreaterThan) {
					Predicate predicate = criteriaBuilder.greaterThan(itemRoot.get("displayPrice"), Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				} else if (priceBetween) {
					Predicate predicate = criteriaBuilder.between(itemRoot.get("displayPrice"), Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)),
							Float.parseFloat(dynamicCollectionCondtion.getValues().get(1)));
					predicates.add(predicate);
				}
			}

			if (stock) {
				if (stockIsEqualTo) {
					Predicate predicate = itemRoot.get("availableStock").in(Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				} else if (stockLessThan) {
					Predicate predicate = criteriaBuilder.lessThan(itemRoot.get("availableStock"), Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				} else if (stockGreaterThan) {
					Predicate predicate = criteriaBuilder.greaterThan(itemRoot.get("availableStock"), Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)));
					predicates.add(predicate);
				} else if (stockBetween) {
					Predicate predicate = criteriaBuilder.between(itemRoot.get("availableStock"), Float.parseFloat(dynamicCollectionCondtion.getValues().get(0)),
							Float.parseFloat(dynamicCollectionCondtion.getValues().get(1)));
					predicates.add(predicate);
				}
			}

			if (tags) {
				Date sevenDaysBefore = new Date();
				Date thirtyDaysBefore = new Date();
				LocalDateTime sevenDaysBeforeLocalDate = LocalDateTime.now().minusDays(7);
				sevenDaysBefore = Date.from(sevenDaysBeforeLocalDate.atZone(ZoneId.systemDefault()).toInstant());

				LocalDateTime thirtyDaysBeforeLocalDate = LocalDateTime.now().minusDays(30);
				thirtyDaysBefore = Date.from(thirtyDaysBeforeLocalDate.atZone(ZoneId.systemDefault()).toInstant());

				List<ProductView> bestSellingProducts = new ArrayList<>();
				
				if (tagsIsEqualTo) {
					if (dynamicCollectionCondtion.getValues().get(0).equals("Next Day Dispatch")) {
						Predicate predicate = criteriaBuilder.lessThan(itemRoot.get("handlingTime"), 2);
						predicates.add(predicate);
					} else if (dynamicCollectionCondtion.getValues().get(0).equals("New Arrivals")) {
						Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(itemRoot.get("createdOn"), sevenDaysBefore);
						predicates.add(predicate);
					} else if (dynamicCollectionCondtion.getValues().get(0).equals("Premium")) {
						Predicate predicate = criteriaBuilder.equal(itemRoot.get("isPremium"), true);
						predicates.add(predicate);
					} else if (dynamicCollectionCondtion.getValues().get(0).equals("Best sellers")) {
						List<String> bsProductIds = restTemplate.getForEntity(getBestSellingProductDetailsUrl, 60000, List.class).getBody();
						for(String uuid : bsProductIds) {
							Predicate predicate = criteriaBuilder.equal(itemRoot.get("pcUuid"), uuid);
							predicates.add(predicate);
						}
					
						
					}
				} else if (tagsIsNotEqualTo) {
					if (dynamicCollectionCondtion.getValues().get(0).equals("Next Day Dispatch")) {
						Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(itemRoot.get("handlingTime"), 2);
						predicates.add(predicate);
					}
					if (dynamicCollectionCondtion.getValues().get(0).equals("New Arrivals")) {
						Predicate predicate = criteriaBuilder.lessThan(itemRoot.get("createdOn"), sevenDaysBefore);
						predicates.add(predicate);
					}
					if (dynamicCollectionCondtion.getValues().get(0).equals("Premium")) {
						Predicate predicate = criteriaBuilder.notEqual(itemRoot.get("isPremium"), true);
						predicates.add(predicate);
					}
					if (dynamicCollectionCondtion.getValues().get(0).equals("Best sellers")) {
						List<String> bsProductIds = restTemplate.getForEntity(getBestSellingProductDetailsUrl, 60000, List.class).getBody();
						for(String uuid : bsProductIds) {
							Predicate predicate = criteriaBuilder.notEqual(itemRoot.get("pcUuid"), uuid);
							predicates.add(predicate);
						}
					}
				} else if (tagsIn) {
					Predicate predicate = null;
					for(String value: dynamicCollectionCondtion.getValues()) {
						if (value.equalsIgnoreCase("Next Day Dispatch")) {
							predicate = criteriaBuilder.lessThan(itemRoot.get("handlingTime"), 2);
							predicates.add(predicate);
						} else if (value.equalsIgnoreCase("New Arrivals")) {
							predicate = criteriaBuilder.greaterThanOrEqualTo(itemRoot.get("createdOn"), sevenDaysBefore);
							predicates.add(predicate);
						} else if (value.equalsIgnoreCase("Premium")) {
							predicate = criteriaBuilder.equal(itemRoot.get("isPremium"), true);
							predicates.add(predicate);
						} else if (value.equalsIgnoreCase("Best sellers")) {
							List<String> bsProductIds = restTemplate.getForEntity(getBestSellingProductDetailsUrl, 60000, List.class).getBody();
							predicate = itemRoot.get("pcUuid").in(bsProductIds);
							predicates.add(predicate);
						}
					}
				} else if (tagsIsNotIn) {
					Predicate predicate = null;
					for (int i = 0; i < dynamicCollectionCondtion.getValues().size(); i++) {
						if (dynamicCollectionCondtion.getValues().get(i).equals("Next Day Dispatch")) {
							predicate = criteriaBuilder.greaterThanOrEqualTo(itemRoot.get("handlingTime"), 2);
							predicates.add(predicate);
						} else if (dynamicCollectionCondtion.getValues().get(i).equals("New Arrivals")) {
							predicate = criteriaBuilder.lessThan(itemRoot.get("createdOn"), sevenDaysBefore);
							predicates.add(predicate);
						} else if (dynamicCollectionCondtion.getValues().get(i).equals("Premium")) {
							predicate = criteriaBuilder.notEqual(itemRoot.get("isPremium"), true);
							predicates.add(predicate);
						} else if (dynamicCollectionCondtion.getValues().get(i).equals("Best sellers")) {
							List<String> bsProductIds = restTemplate.getForEntity(getBestSellingProductDetailsUrl, 60000, List.class).getBody();
							predicate = itemRoot.get("pcUuid").in(bsProductIds).not();
							predicates.add(predicate);
						}
					}
				}
			}
		});

		if (dynamicCollectionRequest.getCondition().equalsIgnoreCase("AND")) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}
		if (dynamicCollectionRequest.getCondition().equalsIgnoreCase("OR")) {
			criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
		}
		List<ProductView> viewNewList = entityManager.createQuery(criteriaQuery).getResultList();
		log.info("list fetched");
		StringBuilder builder = new StringBuilder();
		viewNewList.stream().forEach(view -> {
		if (view.getIsActive()) {
			builder.append(view.getSku()).append(",");
		}
		});
		if (builder.length() > 0) {
			productSkus = builder.substring(0, builder.length() - 1).toString();
		}
		return productSkus;
		//}
	}
}
