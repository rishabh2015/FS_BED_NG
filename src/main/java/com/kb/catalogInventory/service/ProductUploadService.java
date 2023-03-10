package com.kb.catalogInventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kb.catalogInventory.constant.InventoryConstants;
import com.kb.catalogInventory.datatable.*;
import com.kb.catalogInventory.exception.InventoryException;
import com.kb.catalogInventory.model.*;
import com.kb.catalogInventory.repository.*;
import com.kb.catalogInventory.service.admin.BulkUpdateAdminService;
import com.kb.catalogInventory.util.Utils;
import com.kb.java.utils.KbRestTemplate;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductUploadService {
	private final static Logger _logger = LoggerFactory.getLogger(ProductUploadService.class);
	public static String TYPE = "text/csv";
	static String[] HEADERS = { "superCategory", "category", "subCategory", "brandName", "brandModel", "productName",
			"productString", "product_country", "variationName", "variationValue", "unitprice", "totalstock",
			"totalprice", "brandModelCategory" };

	@Autowired
	private BrandModelCategoryRepository brandModelCategoryRepository;
	@Autowired
	SupplierService supplierService;

	@Autowired
	private BrandModelsRepository brandModelsRepository;

	@Autowired
	private SupplierBrandsRespository supplierBrandsRespository;
	
	@Autowired
	private BulkUpdateAdminService bulkservice;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductCombinationsRepository productCombinationsRepository;

	@Autowired
	private SetProductInfoRespository setProductInfoRespository;
	@Autowired
	private ProductStockRepository productStockRepository;
	@Autowired
	private ProductVariationOptionImageRepository productVariationOptionImageRepository;
	@Autowired
	private ProductVariationOptionValueRepository productVariationOptionValueRepository;
	@Autowired
	private VariationOptionsRepository variationOptionsRepository;
	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private ImageGalleryRepository galleryRepo;

	@Autowired
	private VariationRepository variationRepository;

	@Autowired
	KafkaBroadcastingService kafkaBroadcastingService;

	@Autowired
	private ProductDetailService productDetailService;

	@Autowired
	private BrandsRepository brandsRepository;

	@Autowired
	private KbRestTemplate restTemplate;

	@Autowired
	private ProductCountryRuleRepository countryRuleRepo;

	@Autowired
	GroupsRespository groupsRespository;

	@Value("${pricing.service.url:null}")
	private String pricingServiceURL;

	@Value("${pricing.service.url.timeout}")
	private int pricingServiceURLTimeout;

	@Value("${environment.context.path.url}")
	private String apiBaseUrl;

	@Value("${environment.context.path.url.timeout}")
	private int apiBaseUrlTimeout;

	@Value("${image.processing.service.url}")
	private String imageProcessingServiceUrl;
	
	@Value("${image.processing.service.update.url}")
	private String imageProcessingUpdateServiceUrl;

	@Value("${image.processing.service.url.timeout}")
	private int imageProcessingServiceUrlTimeout;

	@Value("${admin.service.url}")
	private String adminServiceUrl;

	@Value("${admin.service.url.timeout}")
	private int adminServiceUrlTimeout;

	@Autowired
	BrandService brandService;

	@Autowired
	private ProductViewRepository productViewRepository;

	@Autowired
	private ProductStatusRepository productStatusRepository;

	@Autowired
	private ProductAttributeRepository productAttributeRepo;

	private static final ObjectMapper _mapper = new ObjectMapper();

	@Autowired
	private CacheAndBulkDataService cascheAndBulkDataService;

	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public void csvToDataTables(InputStream is) throws InventoryException {
		_logger.info("Inside csvToDataTables");

		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				_logger.info("entered with csv row ----- " + csvRecord.toString());
//				populateProductInventory(csvRecord.get("superCategory"), csvRecord.get("category"),
//						csvRecord.get("subCategory"), csvRecord.get("brandName"), csvRecord.get("brandModel"),
//						csvRecord.get("productName"), csvRecord.get("brandModelCategory"),
//						csvRecord.get("productString"), csvRecord.get("product_country"),
//						csvRecord.get("variationName"), csvRecord.get("variationValue"),UUID.randomUUID().toString());
			}

			// sendToKafkaTest();

		} catch (IOException e) {
			_logger.error("IOException e", e);
			throw new InventoryException(InventoryConstants.EXCEL_PARSING_FAILED + e.getMessage());
		}
		// sendToKafka();
	}

	/**
	 * For ADMIN pannel and ResellerPanel
	 * 
	 * @param productUniqueIdentifier uniqueIdentifier from product_combination
	 * @return
	 * @throws Exception 
	 */
	public ProductInventoryBO populateProductInventoryObj(String productUniqueIdentifier, String displayCountryCode,
			String displayCurrencyCode, Map<String, Long> bestSellingProductCountDetailMap,String serviceCalledFor, String contact,Boolean callFromOrder) throws Exception {

		_logger.info(" Inside populateProductInventoryObj ");
		ProductInventoryBO productInventory = new ProductInventoryBO();
		try {
			ProductView view = productViewRepository.findByPcUuid(productUniqueIdentifier);
			productDetailService.addProductPrice(productInventory, view,serviceCalledFor);
			productInventory.setWareHouseId(view.getKbwarehouseid());
			productInventory.setProductSKU(view.getProductSku());
			productInventory.setProductClubingId(view.getProductUuid());
			productInventory.setName(view.getBmcName());
			productInventory.setBrandModel(view.getBmName());
			productInventory.setUserPhoneString(getSupplierGroupusers(view.getSupplierId()));
			/* Newly added by wasif */
			productInventory.setBrandModelId(view.getBmId());
			productInventory.setBrandId(view.getBrandId());
			productInventory.setBrandIcon(view.getBrandICon());
			productInventory.setBrandIsFeatured(view.getBrandIsFeatured());
			productInventory.setBrandSortOrder(view.getBrandSortOrder());
            productInventory.setBrandSameManufacturerAs(view.getBrandSameManufacturerAs());
			productInventory.setCombStrWithoutSize(view.getCombStrWithoutSize());
			/* Newly added code ends */
			/*new code added by vivek*/
			List<ColorSizeCombination> colorSizeCombination=new ArrayList<>();
			try {
				List<SetProductInfo> setProductInfos=setProductInfoRespository.findByProductcombinationidentifier(productUniqueIdentifier);
				setProductInfos.stream().forEach(si->{
					ColorSizeCombination colorSizeCombo=new ColorSizeCombination();
					colorSizeCombo.setColor(si.getColor());
					colorSizeCombo.setSize(si.getSize());
					colorSizeCombo.setQtyPerSet(si.getQtyPerSet());
					colorSizeCombination.add(colorSizeCombo);
				});
			}catch (Exception e){
			_logger.error("error while fetch the color size combination",e);
			}

			/*try {
				productInventory.setProductAttribute(paMapperToBo(productAttributeRepo.findByProductCombinationId(productUniqueIdentifier)));
			} catch (Exception e) {
				_logger.error("Error while fetching product attribute for product {} {}", productUniqueIdentifier, e);
			}*/
			 productInventory.setColorSizeCombinationList(colorSizeCombination);

			/*new code end*/

		    productInventory.setIsSupplierMovApplicabe(view.getIsSupplierMovApplicable());
			productInventory.setBrands(view.getBrandName());
			productInventory.setSKU(view.getSku());
			productInventory.setMrp(view.getMrp());
			productInventory.setProductURL("/" + view.getSku());
			productInventory.setUniqueIdentifierForSearch(productUniqueIdentifier);
			productInventory.setSellerName(view.getSupplierName());
			productInventory.setSupplierMov(view.getSupplierMov());
			productInventory.setSupplierId(view.getSupplierId());
			productInventory.setSellerAddress(view.getSuppAddress());
			productInventory.setPickUpPincode(view.getSuppPinCode());
			productInventory.setSellerId(view.getSupplierId());
			productInventory.setHSN(view.getHsn());
			productInventory.setWeight(view.getProductWeight());
			productInventory.setLength(view.getProductLength());
			productInventory.setWidth(view.getProductWidth());
			productInventory.setHeight(view.getProductHeight());
			productInventory.setStatus(view.getStatus());
			productInventory.setPremiumProduct(view.isPremium());
			productInventory
					.setRatingAvg(view.getAverageRating() != null ? String.valueOf(view.getAverageRating()) : null);
			// Need to add column in supplier table
			productInventory.setSellerCountry("India");
			productInventory.setCombStrWithoutSize(view.getCombStrWithoutSize());
			productInventory.setCreatedOn(new SimpleDateFormat("dd-MM-yyyy").format(view.getCreatedOn()));
			List<String> options = Arrays.asList(view.getCombinationString().split("-"));
			// need to map product variation with product combination
			List<ProductVariationOptionValue> pvopvList = productVariationOptionValueRepository
					.findByProductAndVariationOptionNameIn(productRepository.findById(view.getProductId()).get(),
							options);
			List<Variation> availableVariations = variationRepository.findAll();
			Map<String, String> combinationMap = new HashMap<>();
			try {

				availableVariations.stream().forEach(var -> {
					Optional<ProductVariationOptionValue> pvopv = pvopvList.stream().filter(p -> p.getVariationOptions()
							.getVariation().getVariationName().equalsIgnoreCase(var.getVariationName())).findAny();

					if (pvopv.isPresent() && StringUtils
							.isNotBlank(pvopv.get().getVariationOptions().getVariation().getVariationName())) {
						combinationMap.put(pvopv.get().getVariationOptions().getVariation().getVariationName()
								.replaceAll("\\s", ""), pvopv.get().getVariationOptions().getVariationOptionName());
					} else {
						if (StringUtils.isNotBlank(var.getVariationName())) {
							combinationMap.put(var.getVariationName().replaceAll("\\s", ""), null);
						}
					}

				});

			} catch (Exception e) {
				_logger.error("exception came ", e);
			}
			combinationMap.put("Gender",
					view.getGender() != null && view.getGender().name() != null ? view.getGender().name() : null);
			combinationMap.put("Rating",
					view.getAverageRating() != null ? String.valueOf(view.getAverageRating()) : null);
			productInventory.setCombination(combinationMap);
			// needs to be taken from image gallery , as no mapping found to get single row
			// of product_variation_option_value from product_combination
			String imageNameString = view.getPcUuid();
			List<ImagesBO> images = new ArrayList<>();
			String thumbnailUrl = null;
			String GET_PRODUCT_IMAGE_URL = apiBaseUrl + "image-processing-service/static/getImagePaths/"
					+ imageNameString;
			try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity entity = new HttpEntity(headers);
			ResponseEntity<String> result = restTemplate.exchange(GET_PRODUCT_IMAGE_URL, HttpMethod.GET, entity,
					apiBaseUrlTimeout, String.class);

			
			if (result.getStatusCode() == HttpStatus.OK) {
				JSONObject apiResponse = new JSONObject(Objects.requireNonNull(result.getBody()));
				if (apiResponse.has("data")) {
					JSONArray dataArray = apiResponse.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						int count=0;
						for (int index = 0; index < dataArray.length(); index++) {
							JSONObject obj = dataArray.getJSONObject(index);
							String path = obj.getString("completePath");
							String type = obj.getString("type");
							ImagesBO imagesBO = new ImagesBO(path, type);
							images.add(imagesBO);
							if (type.contains("THUMBNAIL") && count==0 ) {
								thumbnailUrl = path;
							}
							if(type.contains("THUMBNAIL_A1")) {
								count = 1;
								thumbnailUrl = path;
							}
						}
					}
				}
			}
			}catch(Exception e) {
				_logger.error("Exception inside fetching images while creating product inventory obj",e);
			}
			productInventory.setImages(images);
			
			productInventory.setTotalImageCount(this.calculateImageCount(images));
			productInventory.setDisplayPrice(String.valueOf(Utils.roundSetPrecison(view.getPrice()/view.getSetPieces(), 2)));
			// need to have a seperate column for description
			productInventory.setDescription(view.getProductDescription());
			productInventory.setThumbnailURL(thumbnailUrl);
			productInventory.setAvailabilityCount(view.getAvailableStock().toString());
			if (view.getAvailableStock() >= 0 && view.getAvailableStock() >= (view.getMinOrderQuantity())) {
				productInventory.setInStock(true);
			} else {
				productInventory.setInStock(false);
			}
			productInventory.setActive(view.getIsActive());
			// need to have a mapping for this property
			productInventory.setCurrency("INR");
			// List<Category> categoryList =
			// categoryRepository.findByCategoryName(subsubCategoryStringArr[subsubCategoryStringArr.length
			// -1]);
			Category lowestcategory = categoryRepository.findById(view.getLowestCategory()).get();
			productInventory.setCategoryName(lowestcategory.getCategoryName());
			productInventory.setCategoryId(lowestcategory.getId() + "");
			productInventory.setResellerDiscount(view.getResellerDiscount());
			Map<String, Map<String, String>> subcategoriesMap = new TreeMap<>();
			Map<String, Map<String, String>> subcategoriesFinalMap = new TreeMap<>();
			String subCategeryStageKeyString = "subCategoryStage";
			Map<String, String> categoryNameIdMap = new HashMap<>();
			categoryNameIdMap.put("name", lowestcategory.getCategoryName());
			categoryNameIdMap.put("id", lowestcategory.getId() + "");
			subcategoriesMap.put(subCategeryStageKeyString + lowestcategory.getCategoryStageIntValue(),
					categoryNameIdMap);
			if (lowestcategory.getParent() != null) {
				Category parent = lowestcategory.getParent();
				while (parent != null) {
					categoryNameIdMap = new HashMap<>();
					categoryNameIdMap.put("name", parent.getCategoryName().trim());
					categoryNameIdMap.put("id", parent.getId() + "");
					subcategoriesMap.put(subCategeryStageKeyString + parent.getCategoryStageIntValue(),
							categoryNameIdMap);
					parent = parent.getParent();
				}

				/*
				 * AtomicInteger count = new AtomicInteger(counter);
				 * subcategoriesMap.forEach((k, v) -> {
				 * subcategoriesFinalMap.put(subCategeryStageKeyString +
				 * count.getAndDecrement(), v); });
				 */

			}
			String categoryString = "";
			productInventory.setCategoryList(subcategoriesMap);
			try {
				for (int i = 1; i <= subcategoriesMap.size(); i++) {
					categoryString = categoryString + "-" + subcategoriesMap.get("subCategoryStage" + i).get("name");
				}
			} catch (Exception e) {
				_logger.error("Exception while categoriesMap ",e);
			}
			categoryString = categoryString.replaceFirst("-", "");
			productInventory.setLegalDisclaimerDescription(view.getLegalDisclaimerDescription());
			productInventory.setManufacturerContactNumber(view.getManufacturerContactNumber());
			productInventory.setMaxOrderQuantity(view.getMaxOrderQuantity());
			productInventory.setMinOrderQuantity(view.getMinOrderQuantity());

			productInventory.setProductDescription(view.getProductDescription());
			productInventory.setSearchTerms(view.getSearchTerms());
			productInventory.setCategoryString(view.getCategoryString());//
			productInventory.setItemName(view.getItemName());
			productInventory.setBrandName(view.getBrandName());
			productInventory.setManufacturerPartNumber(view.getManufacturerPartNumber());
			productInventory.setGender(view.getGender());
			productInventory.setTotalStock(view.getAvailableStock());
			productInventory.setTargertGender(view.getTargertGender());

			StringBuilder sb = new StringBuilder();
			combinationMap.forEach((k, v) -> {
				sb.append(k + ":" + v + "-");
			});
			productInventory.setVariationVariationOptionString(StringUtils.removeEnd(sb.toString(), "-"));//
			productInventory.setProductTaxCode(view.getProductTaxCode());
			/**
			 * for reseller supplier amount is with kb margin
			 */
			productInventory.setSupplierAmount(view.getPrice());
			productInventory.setSaleDiscountPrice(null);
			productInventory.setSaleStartDate(null);
			productInventory.setSaleEndDate(null);
			productInventory.setHandlingTime(view.getHandlingTime());
			productInventory.setCountryOfOrigion(view.getCountryOfOrigion());
			productInventory.setBulletPoints(
					null != view.getBulletPoints() ? Arrays.asList(view.getBulletPoints().split("#@@&")) : null);
			productInventory.setTargetAudienceKeywords(view.getTargetAudienceKeywords());
			productInventory.setOccasion(view.getOccasion());
			productInventory.setOccasionLifeStyle(view.getOccasionLifeStyle());
			productInventory.setImageNameString(productUniqueIdentifier);
			productInventory.setSupplierString(view.getSupplierName());
			productInventory.setBrandModelString(view.getBmName());
			productInventory.setSupplierDetail(null);
			productInventory.setProductString(view.getProductString());
			productInventory.setProductNameString(view.getProductName());
			productInventory.setSKU(view.getSku());
			productInventory.setMrp(view.getMrp());
			productInventory.setItemName(view.getItemName());
			productInventory.setSupplierSignature(getSupplierSignature(String.valueOf(productInventory.getSellerId())));
			productInventory.setCollectionIds(view.getCollectionIds());
			productInventory.setSetPeices(view.getSetPieces());
			productInventory.setSetPrice(String.valueOf(Utils.roundSetPrecison(view.getPrice(), 2)));
			try {
				try {
					//if (view.getIsActive() && view.getStatusId() != 1 && view.getStatusId() != 3
						//	&& view.getStatusId() != 5 && view.getStatusId() != 7) {
					if(view.getIsActive() && (view.getStatusId()==4 || view.getStatusId()==0)) {
						List l = productDetailService.getProductPriceBulkBuy(productUniqueIdentifier,
								displayCountryCode);
						productInventory.setPriceMatrix(l);
					}
				}catch (Exception e) {
					_logger.error("Error while fetching bulkby while creating product for uuid "+productUniqueIdentifier,e);
				}
//				_logger.info("status of product with uuid "+productUniqueIdentifier+" is "+view.getStatusId()+" and is active "+view.getIsActive());
//				if (view.getIsActive() && view.getStatusId() != 1 && view.getStatusId() != 3 && view.getStatusId() != 5
//						&& view.getStatusId() != 7) {
//					_logger.info("going to call  addDiscountAndTaxToProduct for UUID "+productUniqueIdentifier);
//					productInventory = productDetailService.addDiscountAndTaxToProduct(productInventory);
//				}
			} catch (Exception ee) {
				_logger.error("Exception while adding priceMatrix while sending to kafka {}", ee);
			}

			/**
			 * APPLY PRODUCT COUNTRY RULE
			 */
			productInventory = applyProductCountryRule(productInventory, view);
			
			/**
			 * Add Margin CountryWise
			 */
			//if (view.getIsActive() && view.getStatusId() != 1 && view.getStatusId() != 3 && view.getStatusId() != 5
				//	&& view.getStatusId() != 7) {
			if(view.getIsActive() && (view.getStatusId()==4 || view.getStatusId()==0)) {
				productInventory=productDetailService.applyMarginCOuntryWise(productInventory);
			}

			/*
			 * Map map = (Map)l.get(0); map.get
			 */

			/**
			 * Commented as it needs to be implemented in scheduler
			 */
			Optional<Brands> brandOpt = brandsRepository.findById(productInventory.getBrandId());
			Brands brand = null;
			if(brandOpt.isPresent()) {
				brand = brandOpt.get();
			}
			productInventory.setBasedIn(brand.getBasedIn());
			int totalBrandReview =0;
			Float totalRating = 0f;
			if(brand!=null && brand.getBrandReviews()!=null && !brand.getBrandReviews().isEmpty()) {
			for(BrandReviews br:brand.getBrandReviews()) {
				totalBrandReview=totalBrandReview+1;
				totalRating = totalRating + br.getReviewRating();
			}
			productInventory.setTotalBrandReviews(totalBrandReview);
			if(totalBrandReview!=0) {
			productInventory.setAverageBrandRating(totalRating/totalBrandReview);
			}
			}
            if(callFromOrder==null || !callFromOrder) {
				setRetailMarginAndBestSellingProductCount(productInventory, bestSellingProductCountDetailMap);
			}
			if (!contact.equalsIgnoreCase("0")){
				Optional<Supplier> supplierOptional = supplierRepository.findById(productInventory.getSupplierId());
				if (supplierOptional.isPresent()){
					if (!ObjectUtils.isEmpty(supplierOptional.get().getGroupString())){
						productInventory.setIsUserLoginRequired(true);
						productInventory.setIsPremiumBrand(true);
						List<Long> groupIds = Arrays.stream(supplierOptional.get().getGroupString().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
						Map<Long, Groups> groupsHashMap = groupsRespository.findAllById(groupIds)
								.stream().collect(Collectors.toMap(Groups::getId, Function.identity()));

						AtomicReference<Boolean> isUserBelongToBrand = new AtomicReference<>(false);
							groupIds.forEach(groupId ->{
								if (groupsHashMap.containsKey(groupId)){
									Groups groups = groupsHashMap.get(groupId);
									if (groups.getPhoneString().contains(contact)){
										isUserBelongToBrand.set(true);
									}
								}

							});
						productInventory.setIsUserBelongToBrand(isUserBelongToBrand.get());

					} else {
						productInventory.setIsUserLoginRequired(false);
						productInventory.setIsPremiumBrand(false);
						productInventory.setIsUserBelongToBrand(true);
					}
				} else {
					productInventory.setIsUserLoginRequired(false);
					productInventory.setIsPremiumBrand(false);
					productInventory.setIsUserBelongToBrand(true);
				}
			} else {
				productInventory.setIsUserLoginRequired(false);
				productInventory.setIsPremiumBrand(false);
				productInventory.setIsUserBelongToBrand(true);
			}
			// sendToKafka(productInventory);
			//updateDisplayPriceInProdCombination(productInventory.getCountryWisePrice().get(displayCountryCode).getFinalPricePerUnit(), view.getPcId());
			//_logger.info("Going to updateDisplayPriceInProdCombination with countryWiseMap {} with dipslayCOuntryCode {} of pcuuid {}",productInventory.getCountryWisePrice().get(displayCountryCode),displayCountryCode,view.getPcId());
			ProductPrice countryWisePrice=null;
			if(productInventory.getCountryWisePrice().containsKey(displayCountryCode)){
				countryWisePrice=productInventory.getCountryWisePrice().get(displayCountryCode);
			}else{
				countryWisePrice=productInventory.getCountryWisePrice().get("All");
			}
			if(null!=countryWisePrice){
				updateDisplayPriceInProdCombination(countryWisePrice.getFinalPricePerUnit(), view.getPcId());
			}

		} catch (Exception e) {
			_logger.info("Exception while creting productInventory of unique key ::::::::: {}",
					productUniqueIdentifier);
			_logger.error("Exception while creting productInventory of unique key", e);

		}
		return productInventory;
	}

	public void updateDisplayPriceInProdCombination(Double displayPrice, String productCominationId) {
		_logger.info("Entered in updateDisplayPriceInProdCombination for PC Id "+ productCominationId + " displayPrice "+displayPrice );	
	;
		try {
		
		productCombinationsRepository.updateDisplayPriceInProductCombination(displayPrice.floatValue(), Long.parseLong(productCominationId));
		}catch(Exception e) {
			_logger.error("Exception in updateDisplayPriceInProdCombination for PC Id "+ productCominationId + " displayPrice "+displayPrice +" error ", e );		}
		
	}


	public void setRetailMarginAndBestSellingProductCount(ProductInventoryBO pi, Map<String, Long> bestSellingProductCountDetailMap) {
		if(pi==null) {
			return;
		}
		if(pi.getCountryWisePrice()!=null) {
		pi.getCountryWisePrice().forEach((key, value) ->
		{
			double retailMargin = Utils.calculateRetailMargin(value.getProductMrp(), value.getFinalPricePerUnit(), value.getSetPieces());
			_logger.info("productId:[{}] -- retailMargin:[{}]", pi.getUniqueIdentifierForSearch(), retailMargin);
			value.setRetailMargin(retailMargin);
		});
		}
		if(bestSellingProductCountDetailMap!=null && pi.getUniqueIdentifierForSearch()!=null && bestSellingProductCountDetailMap.containsKey(pi.getUniqueIdentifierForSearch().trim())){
			pi.setBestSellingProductCount(bestSellingProductCountDetailMap.getOrDefault(pi.getUniqueIdentifierForSearch().trim(), 0L));
		}

	}

	public  String getSupplierGroupusers(Long suppID)
	{
		StringBuilder PhoneStringRs=new StringBuilder();
		try {
		Supplier supplier=supplierRepository.findByIdAndIsActive(suppID,true);
		String groupString=supplier.getGroupString();
		 Set<String> phoneSet=new HashSet<>();
		 if (!ObjectUtils.isEmpty(groupString))
		 {
			 String strPattern = "^[0-9]{10}$";
			 List<GroupsBo> groupsBoList=supplierService.getGroupsFromDb(groupString);
			 for (GroupsBo gb:groupsBoList){
				 String[] 	phone_string=gb.getPhoneString().split(",");
				 for (String s:phone_string){
					 if (!s.equalsIgnoreCase("")&& s.matches(strPattern))
					 {
						 phoneSet.add(s);
					 }
				 }




			 }

			 int count=0;

			 for (String ph:phoneSet){
				 if (count<phoneSet.size()-1)
				 {
					 PhoneStringRs.append(ph);
					 PhoneStringRs.append(",");
				 }else {
					 PhoneStringRs.append(ph);

				 }
				++count;
			 }
		 }

		}catch(Exception e) {
			
		}


		// get group string from supplier
		// for each group find phoneString
		//parse this phone string and add each phonenumber into set
		//convert setObject inpt string
		//

		return String.valueOf(PhoneStringRs);
	}


	/**
	 * Product Detail for supplierPanel
	 * 
	 * @param productUniqueIdentifier uniqueIdentifier from product_combination
	 * @return
	 */
	public ProductInventoryBO populateProductInventoryObjForSupplier(String productUniqueIdentifier) {
		ProductInventoryBO productInventory = new ProductInventoryBO();
		try {
			ProductView view = productViewRepository.findByPcUuid(productUniqueIdentifier);
			productDetailService.addProductPrice(productInventory, view,null);
			productInventory.setWareHouseId(view.getKbwarehouseid());
			productInventory.setProductSKU(view.getProductSku());
			productInventory.setProductClubingId(view.getPcUuid());
			productInventory.setName(view.getBmcName());
			productInventory.setBrandModel(view.getBmName());
			/* Newly added by wasif */
			productInventory.setBrandModelId(view.getBmId());
			/* Newly added code ends */
			productInventory.setBrands(view.getBrandName());
			productInventory.setBrandId(view.getBrandId());
			productInventory.setBrandIcon(view.getBrandICon());
			productInventory.setSKU(view.getSku());
			productInventory.setProductURL("/" + view.getSku());
			productInventory.setUniqueIdentifierForSearch(productUniqueIdentifier);
			productInventory.setSellerName(view.getSupplierName());
			productInventory.setSupplierMov(view.getSupplierMov());
			 productInventory.setIsSupplierMovApplicabe(view.getIsSupplierMovApplicable());
				
			productInventory.setSupplierId(view.getSupplierId());
			productInventory.setSellerAddress(view.getSuppAddress());
			productInventory.setPickUpPincode(view.getSuppPinCode());
			productInventory.setSellerId(view.getSupplierId());
			productInventory.setHSN(view.getHsn());
			productInventory.setWeight(view.getProductWeight());
			productInventory.setLength(view.getProductLength());
			productInventory.setWidth(view.getProductWidth());
			productInventory.setHeight(view.getProductHeight());
			productInventory.setStatus(view.getStatus());
			// Need to add column in supplier table
			productInventory.setSellerCountry("India");
			productInventory.setCombStrWithoutSize(view.getCombStrWithoutSize());
			productInventory.setPremiumProduct(view.isPremium());
			productInventory
					.setRatingAvg(view.getAverageRating() != null ? String.valueOf(view.getAverageRating()) : null);
			productInventory.setCreatedOn(new SimpleDateFormat("dd-MM-yyyy").format(view.getCreatedOn()));
			List<String> options = Arrays.asList(view.getCombinationString().split("-"));
			// need to map product variation with product combination
			List<ProductVariationOptionValue> pvopvList = productVariationOptionValueRepository
					.findByProductAndVariationOptionNameIn(productRepository.findById(view.getProductId()).get(),
							options);
			List<Variation> availableVariations = variationRepository.findAll();
			Map<String, String> combinationMap = new HashMap<>();
			try {

				availableVariations.stream().forEach(var -> {
					Optional<ProductVariationOptionValue> pvopv = pvopvList.stream().filter(p -> p.getVariationOptions()
							.getVariation().getVariationName().equalsIgnoreCase(var.getVariationName())).findAny();

					if (pvopv.isPresent() && StringUtils
							.isNotBlank(pvopv.get().getVariationOptions().getVariation().getVariationName())) {
						combinationMap.put(pvopv.get().getVariationOptions().getVariation().getVariationName(),
								pvopv.get().getVariationOptions().getVariationOptionName());
					} else {
						if (StringUtils.isNotBlank(var.getVariationName())) {
							combinationMap.put(var.getVariationName(), null);
						}
					}

				});

			} catch (Exception e) {
				_logger.error("exception came ", e);
			}
			combinationMap.put("Gender", view.getGender() != null ? view.getGender().name() : null);
			combinationMap.put("Rating",
					view.getAverageRating() != null ? String.valueOf(view.getAverageRating()) : null);
			productInventory.setCombination(combinationMap);
			// needs to be taken from image gallery , as no mapping found to get single row
			// of product_variation_option_value from product_combination
			String imageNameString = view.getPcUuid();
			List<ImagesBO> images = new ArrayList<>();
			String thumbnailUrl = null;
			String GET_PRODUCT_IMAGE_URL = apiBaseUrl + "image-processing-service/static/getImagePaths/"
					+ imageNameString;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity entity = new HttpEntity(headers);

			ResponseEntity<String> result = restTemplate.exchange(GET_PRODUCT_IMAGE_URL, HttpMethod.GET, entity,
					apiBaseUrlTimeout, String.class);

			if (result.getStatusCode() == HttpStatus.OK) {
				JSONObject apiResponse = new JSONObject(Objects.requireNonNull(result.getBody()));
				if (apiResponse.has("data")) {
					JSONArray dataArray = apiResponse.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						int count =0;
						for (int index = 0; index < dataArray.length(); index++) {
							JSONObject obj = dataArray.getJSONObject(index);
							String path = obj.getString("completePath");
							String type = obj.getString("type");
							ImagesBO imagesBO = new ImagesBO(path, type);
							images.add(imagesBO);
							if (type.contains("THUMBNAIL") && count==0) {
								thumbnailUrl = path;
							}
							if(type.contains("THUMBNAIL_A1")) {
								count = 1;
								thumbnailUrl = path;
							}
						}
					}
				}
			}

			productInventory.setImages(images);
			productInventory.setTotalImageCount(this.calculateImageCount(images));
			productInventory.setDisplayPrice(String.valueOf(Utils.roundSetPrecison(view.getSupplierPrice()/view.getSetPieces(), 2)));
			// need to have a seperate column for description
			productInventory.setDescription(view.getCombinationString());
			productInventory.setThumbnailURL(thumbnailUrl);
			productInventory.setAvailabilityCount(view.getAvailableStock().toString());
			if (view.getAvailableStock() > 0 && view.getAvailableStock() >= view.getMinOrderQuantity()) {
				productInventory.setInStock(true);
			} else {
				productInventory.setInStock(false);
			}
			productInventory.setActive(view.getIsActive());
			// need to have a mapping for this property
			productInventory.setCurrency("INR");
			// List<Category> categoryList =
			// categoryRepository.findByCategoryName(subsubCategoryStringArr[subsubCategoryStringArr.length
			// -1]);
			Category lowestcategory = categoryRepository.findById(view.getLowestCategory()).get();
			productInventory.setCategoryName(lowestcategory.getCategoryName());
			productInventory.setCategoryId(lowestcategory.getId() + "");
			Map<String, Map<String, String>> subcategoriesMap = new TreeMap<>();
			Map<String, Map<String, String>> subcategoriesFinalMap = new TreeMap<>();
			String subCategeryStageKeyString = "subCategoryStage";
			Map<String, String> categoryNameIdMap = new HashMap<>();
			categoryNameIdMap.put("name", lowestcategory.getCategoryName());
			categoryNameIdMap.put("id", lowestcategory.getId() + "");
			subcategoriesMap.put(subCategeryStageKeyString + lowestcategory.getCategoryStageIntValue(),
					categoryNameIdMap);
			if (lowestcategory.getParent() != null) {
				Category parent = lowestcategory.getParent();
				while (parent != null) {
					categoryNameIdMap = new HashMap<>();
					categoryNameIdMap.put("name", parent.getCategoryName().trim());
					categoryNameIdMap.put("id", parent.getId() + "");
					subcategoriesMap.put(subCategeryStageKeyString + parent.getCategoryStageIntValue(),
							categoryNameIdMap);
					parent = parent.getParent();
				}

				/*
				 * AtomicInteger count = new AtomicInteger(counter);
				 * subcategoriesMap.forEach((k, v) -> {
				 * subcategoriesFinalMap.put(subCategeryStageKeyString +
				 * count.getAndDecrement(), v); });
				 */

			}
			String categoryString = "";
			productInventory.setCategoryList(subcategoriesMap);
			try {
				for (int i = 1; i <= subcategoriesMap.size(); i++) {
					categoryString = categoryString + "-" + subcategoriesMap.get("subCategoryStage" + i).get("name");
				}
			} catch (Exception e) {

			}
			categoryString = categoryString.replaceFirst("-", "");
			productInventory.setLegalDisclaimerDescription(view.getLegalDisclaimerDescription());
			productInventory.setManufacturerContactNumber(view.getManufacturerContactNumber());
			productInventory.setMaxOrderQuantity(view.getMaxOrderQuantity());
			productInventory.setMinOrderQuantity(view.getMinOrderQuantity());
			productInventory.setMrp(view.getMrp());
			productInventory.setProductDescription(view.getProductDescription());
			productInventory.setSearchTerms(view.getSearchTerms());
			productInventory.setCategoryString(view.getCategoryString());//
			productInventory.setItemName(view.getItemName());
			productInventory.setBrandName(view.getBrandName());
			productInventory.setManufacturerPartNumber(view.getManufacturerPartNumber());
			productInventory.setGender(view.getGender());
			productInventory.setTotalStock(view.getAvailableStock());
			productInventory.setTargertGender(view.getTargertGender());

			StringBuilder sb = new StringBuilder();
			combinationMap.forEach((k, v) -> {
				sb.append(k + ":" + v + "-");
			});
			productInventory.setVariationVariationOptionString(StringUtils.removeEnd(sb.toString(), "-"));//
			productInventory.setProductTaxCode(view.getProductTaxCode());
			/**
			 * for supplier amount is without kb margin
			 */
			productInventory.setSupplierAmount(view.getSupplierPrice());
			productInventory.setSaleDiscountPrice(null);
			productInventory.setSaleStartDate(null);
			productInventory.setSaleEndDate(null);
			productInventory.setHandlingTime(view.getHandlingTime());
			productInventory.setCountryOfOrigion(view.getCountryOfOrigion());
			productInventory.setBulletPoints(
					null != view.getBulletPoints() ? Arrays.asList(view.getBulletPoints().split("#@@&")) : null);
			productInventory.setTargetAudienceKeywords(view.getTargetAudienceKeywords());
			productInventory.setOccasion(view.getOccasion());
			productInventory.setOccasionLifeStyle(view.getOccasionLifeStyle());
			productInventory.setImageNameString(productUniqueIdentifier);
			productInventory.setSupplierString(view.getSupplierName());
			productInventory.setBrandModelString(view.getBmName());
			productInventory.setSupplierDetail(null);
			productInventory.setProductString(view.getProductString());
			productInventory.setProductNameString(view.getProductName());
			productInventory.setSKU(view.getSku());
			productInventory.setItemName(view.getItemName());
			productInventory.setDisplayCurrencyId(view.getSupplierCurrencyId());
			productInventory.setCollectionIds(view.getCollectionIds());
			productInventory.setSetPeices(view.getSetPieces());
			productInventory.setSetPrice(String.valueOf(Utils.roundSetPrecison(view.getSupplierPrice(), 2)));
			try {
				List l = productDetailService.getProductPriceBulkBuyForSupplier(productUniqueIdentifier);

				productInventory.setPriceMatrix(l);
			} catch (Exception ee) {
				_logger.info("Exception while getProductPriceBulkBuyForSupplier of unique key ::::::::: {} ",
						productUniqueIdentifier);
			}
			/*
			 * Map map = (Map)l.get(0); map.get
			 */

			/**
			 * APPLY PRODUCT COUNTRY RULE
			 */
			productInventory = applyProductCountryRule(productInventory, view);
			
			/**
			 * Add Margin CountryWise
			 */
			
			//productInventory=productDetailService.applyMarginCOuntryWise(productInventory);

			/**
			 * Commented as it needs to be implemented in scheduler
			 */
//	sendToKafka(productInventory);
		} catch (Exception e) {

			_logger.info("Exception while creting productInventory of unique key ::::::::: {} ",
					productUniqueIdentifier);
			_logger.error("", e);

		}
		return productInventory;
	}

	public Integer calculateImageCount(List<ImagesBO> images) {
		boolean shouldRemoveCompressedImage = false;
		int totalImageCount = 0;
 
		for (ImagesBO imagesBO : images) {
			if(imagesBO.getType().contains("A2")) {
				shouldRemoveCompressedImage = true;
				break;
			}
		}
		
		if(shouldRemoveCompressedImage) {
			for (ImagesBO imagesBO : images) {
				if(!(imagesBO.getType().contains("A1")|| imagesBO.getType().contains("A3"))) {
					totalImageCount++;
				}
			}
			return totalImageCount;
		} else {
			return images.size();
		}
	}

	@Value("${default.warehouse.url}")
	private String defaultWarehouseUrl;

	@Value("${default.warehouse.url.timeout}")
	private int defaultWarehouseUrlTimeout;

	/**
	 * 
	 * 
	 * @param categoryString
	 * @param brandModelString
	 * @param variationVariationOptionString
	 * @param supplierString
	 * @param supplierDetail
	 * @param totalStock
	 * @param imageNameString
	 * @param productString
	 * @param productNameString
	 * @param SKU
	 * @param supplierAmount
	 * @param supplierCurrencyId
	 */
	public List<ProductInventoryBO> populateProductCategoryWrapper(String categoryString, String brandModelString,
			String variationVariationOptionString, String supplierString, String supplierDetail, String totalStock,
			String imageNameString, String productString, String productNameString, String SKU, String supplierAmount,
			String legalDisclaimerDescription, String manufacturerContactNumber, int minOrderQuantity,
			int maxOrderQuantity, Double mrp, String productDescription, String searchTerms, String hsn, Float weight,
			Float length, Float width, Float height, String oldStock, List<Map<String, String>> priceMatrixMapList,
			String brandName, String startDate, String endDate, String manufacturerPartNumber, Gender gender,
			Gender targetGender, String productTaxCode, String handlingTime, String countryOrigion, String bulletPoints,
			String targetAudienceKeywords, String occasion, String occasionLifeStyle, String itemName,
			String productCombinationUUID, Boolean isPremium, String productSku, long supplierCurrencyId,
			boolean isSingleListing, long supplierId, Long productContryRuleId,Integer setPeices,List<ColorSizeCombination> colorSizeCombList,String attribute) throws Exception {

		String[] variationOptionString = StringUtils.split(variationVariationOptionString, "-");
		List<ProductInventoryBO> response = new ArrayList<>();
		Map<String, List<String>> variationVarOptMap = new HashMap<>();
		for (String str : variationOptionString) {
			variationVarOptMap.put(str.split(":")[0], Arrays.asList(str.split(":")[1].split(",")));
		}
		List<String> combinations = getCombinationList(variationVarOptMap);

		for (String varVariationOptionString : combinations) {
			String trimedVarVariationOptionString = StringUtils.removeFirst(varVariationOptionString, "-");
			ProductInventoryBO bo = new ProductInventoryBO();
			bo = populateProductCategory(categoryString, brandModelString, trimedVarVariationOptionString,
					supplierString, supplierDetail, totalStock, imageNameString, productString, productNameString, SKU,
					supplierAmount, legalDisclaimerDescription, manufacturerContactNumber, minOrderQuantity,
					maxOrderQuantity, mrp, productDescription, searchTerms, hsn, weight, length, width, height,
					oldStock, priceMatrixMapList, brandName, startDate, endDate, manufacturerPartNumber, gender,
					targetGender, productTaxCode, handlingTime, countryOrigion, bulletPoints, targetAudienceKeywords,
					occasion, occasionLifeStyle, itemName, productCombinationUUID, isPremium, productSku,
					supplierCurrencyId, isSingleListing, supplierId, productContryRuleId,setPeices,colorSizeCombList,attribute);
			response.add(bo);
		}
		bulkservice.pushProductToKafka(productCombinationUUID, "IN", "INR");
		
		return response;
	}

	/**
	 * 
	 * 
	 * @param categoryString
	 * @param brandModelString
	 * @param variationVariationOptionString
	 * @param supplierString
	 * @param supplierDetail
	 * @param totalStock
	 * @param imageNameString
	 * @param productString
	 * @param productNameString
	 * @param SKU
	 * @param supplierAmount
	 * @param supplierCurrencyId
	 */
	@Transactional
	public ProductInventoryBO populateProductCategory(String categoryString, String brandModelString,
			String variationVariationOptionString, String supplierString, String supplierDetail, String totalStock,
			String imageNameString, String productString, String productNameString, String SKU, String supplierAmount,
			String legalDisclaimerDescription, String manufacturerContactNumber, int minOrderQuantity,
			int maxOrderQuantity, Double mrp, String productDescription, String searchTerms, String hsn, Float weight,
			Float length, Float width, Float height, String oldStock, List<Map<String, String>> priceMatrixMapList,
			String brandName, String startDate, String endDate, String manufacturerPartNumber, Gender gender,
			Gender targetGender, String productTaxCode, String handlingTime, String countryOrigion, String bulletPoints,
			String targetAudienceKeywords, String occasion, String occasionLifeStyle, String itemName,
			String productCombinationUUID, Boolean isPremium, String productSku, long supplierCurrencyId,
			boolean isSingleListing, long supplierId, Long productCountryRuleId,Integer setPeices,List<ColorSizeCombination> colorSizeCombinationList,String attributes) throws Exception {
		Long defaultWareHouseId = null;
		ProductCombinations productCombinations = null;
		if (StringUtils.isBlank(productCombinationUUID)) {
			try {
				ResponseEntity<Map> response = restTemplate.getForEntity(defaultWarehouseUrl, apiBaseUrlTimeout,
						Map.class);
				defaultWareHouseId = Long.valueOf((Integer) response.getBody().get("id"));
			} catch (Exception e) {
				_logger.info("could not found default warehouse id:: {}", e.getMessage());
			}

			String[] subsubCategoryStringArr = StringUtils.split(categoryString, "-");
			boolean isNewPc = false;
			// BrandModels brandModels = brandModelsRepository.findByName(brandModelString);
			if (brandName == null || brandName.equalsIgnoreCase("")) {
				brandName = "Generic";
			}
			if (brandModelString == null || brandModelString.equalsIgnoreCase("")) {
				brandModelString = "Generic";
			}
			if (productString == null || productString.equalsIgnoreCase("")) {
				productString = "Generic";
			}
			if (productNameString == null || productNameString.equalsIgnoreCase("")) {
				productNameString = "Generic";
			}
			// Brands brands = brandsRepository.findByName(brandName);
			Brands brands = null;

			_logger.info("brandname"+brandName);
			if (StringUtils.isNotBlank(brandName)) {
				brands = brandsRepository.findByName(brandName);
			}
			if (null == brands) {
				brands = brandsRepository.findByName("Generic");
			}



			// BrandModels brandModels =
			// brandModelsRepository.findByNameAndBrandId(brandModelString,brands.getId());//brandId
			// only not brandModelString
			BrandModels brandModels = brandModelsRepository.findByBrands(brands);
			if (null == brandModels) {
				brandModels = new BrandModels();
				brandModels.setBrands(brands);
				brandModels.setName(brandModelString);
				brandModels=brandModelsRepository.save(brandModels);

			}

			_logger.info("brandmodel Id>>"+brandModels.getId());

			Optional<Category> categoryOptional = categoryRepository
					.findById(Long.parseLong(subsubCategoryStringArr[subsubCategoryStringArr.length - 1]));
			Category category = new Category();
			if (categoryOptional.isPresent()) {
				category = categoryOptional.get();
			}
			BrandModelCategory brandModelCategory = brandModelCategoryRepository.findByCategoryAndBrandModels(category,
					brandModels);
			if (brandModelCategory == null) {
				// List<Category> category = categoryRepository
				// .findByCategoryName(subsubCategoryStringArr[subsubCategoryStringArr.length -
				// 1]);

				brandModelCategory = new BrandModelCategory(brandName,
						subsubCategoryStringArr[subsubCategoryStringArr.length - 2], category, brandModels);
				brandModelCategoryRepository.save(brandModelCategory);
			}
			_logger.info("brandModelCategory Id>>"+brandModelCategory.getId());

			// Supplier supplier=supplierRepository.findBySupplierName(supplierString);
			Optional<Supplier> supplierOptional = supplierRepository.findById(supplierId);
			Supplier supplier = new Supplier();
			if (supplierOptional.isPresent()) {
				// supplier=new Supplier(supplierString, "", "");
				supplier = supplierOptional.get();
			} else {
				ProductInventoryBO pibo1 = new ProductInventoryBO();
				_logger.info("supplierId not found"+supplierId);
				pibo1.setErrMessage("Supplier does not exists");
				return pibo1;
			}
			// supplierRepository.save(supplier);
			/*
			 * Product product = productRepository.
			 * findByProductStringAndProductNameAndBrandModelCategoryAndSupplierAndProductSku(
			 * productString, productNameString, brandModelCategory, supplier,productSku);
			 */
			Product product = productRepository.findByProductSkuAndSupplierAndBrandModelCategory(productSku, supplier,brandModelCategory);
			ImageGallery imgGallery = null;
			if (null == product) {
				product = new Product(productNameString, productString, imageNameString, Integer.parseInt("10"),
						brandModelCategory, supplier, new Date(System.currentTimeMillis()), legalDisclaimerDescription,
						manufacturerContactNumber, UUID.randomUUID().toString(), productSku);
			}

			if (!subsubCategoryStringArr[subsubCategoryStringArr.length - 2]
					.equalsIgnoreCase(product.getBrandModelCategory().getCategory().getCategoryName())) {
				ProductInventoryBO pibo1 = new ProductInventoryBO();
				pibo1.setErrMessage("Product SKU already associated with another category");
				return pibo1;
			}
			product = productRepository.save(product);

			_logger.info("product created with Id"+product.getId());
			String productCombinationString = "";
			String productCombinationStringWithoutSizeVar = "";

			String[] variationOptionString = StringUtils.split(variationVariationOptionString, "-");
			for (String str : variationOptionString) {
				Variation variation = variationRepository.findByVariationName(str.split(":")[0]);
				VariationOptions option = variationOptionsRepository
						.findByVariationOptionNameAndVariation(str.split(":")[1], variation);
				if (StringUtils.isNotBlank(productCombinationString)) {
					productCombinationString = productCombinationString + "-" + str.split(":")[1];
					if (!variation.getVariationName().equalsIgnoreCase("Size")) {
						productCombinationStringWithoutSizeVar = productCombinationStringWithoutSizeVar + "-"
								+ str.split(":")[1];
					}
				} else {
					productCombinationString = str.split(":")[1];
					if (!variation.getVariationName().equalsIgnoreCase("Size")) {
						productCombinationStringWithoutSizeVar = str.split(":")[1];
					}
				}
				ProductVariationOptionValue pvov = productVariationOptionValueRepository
						.findByProductAndVariationOptions(product, option);
				if (null == pvov) {
					pvov = new ProductVariationOptionValue(str.split(":")[1], product, option);
				}
				pvov = productVariationOptionValueRepository.save(pvov);

				if (StringUtils.isBlank(imageNameString)) {
					imageNameString = brandModelString + "_" + productString + productCombinationString;
				}

				imgGallery = new ImageGallery(imageNameString + "_small_Image.jpg",
						imageNameString + "_medium_Image.jpg", imageNameString + "_large_Image.jpg",
						imageNameString + "_Image_4.jpg", imageNameString + "_Image_5.jpg",
						imageNameString + "_Image_6.jpg", imageNameString + "_Image_7.jpg",
						imageNameString + "_Image_8.jpg", imageNameString + "_Image_9.jpg");
				galleryRepo.save(imgGallery);

				ProductVariationOptionImage productVariationOptionImage = productVariationOptionImageRepository
						.findByProductAndProductVariationOptionValue(product, pvov);
				if (null == productVariationOptionImage) {
					productVariationOptionImage = new ProductVariationOptionImage(product, imgGallery, pvov, true);
				}

				productVariationOptionImage = productVariationOptionImageRepository.save(productVariationOptionImage);
			}
			Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
			List<ProductStatus> psList = new ArrayList<ProductStatus>();
			psList.add(productStatus.get());
			productCombinations = productCombinationsRepository
					.findByProductAndCombinationStringAndSupplierPriceAndProductStatusNotIn(product,
							productCombinationString, Float.parseFloat(supplierAmount), psList);
			String productUniqueIdentifier = UUID.randomUUID().toString();
			if (null == productCombinations) {
				isNewPc = true;
				if (isSingleListing) {
					SKU = SKU + "-" + productCombinationString;
				}



				Optional<ProductStatus> ps = productStatusRepository.findById(StatusConstant.QC_Pending);


				productCombinations = new ProductCombinations(product, productCombinationString, SKU,
						Float.parseFloat(supplierAmount), Float.parseFloat(supplierAmount), productUniqueIdentifier,
						Integer.parseInt(totalStock), new Date(System.currentTimeMillis()),
						new Date(System.currentTimeMillis()), hsn, weight, length, width, height, false,
						minOrderQuantity, maxOrderQuantity, mrp, productDescription, searchTerms, ps.get(),
						manufacturerPartNumber, gender, targetGender, productTaxCode, handlingTime, countryOrigion,
						bulletPoints, targetAudienceKeywords, occasion, occasionLifeStyle, itemName, categoryString,setPeices);
				productCombinations.setSupplier(supplier);
				productCombinations.setKbWareHouseId(defaultWareHouseId);
				productCombinations.setSupplierCurrencyId(supplierCurrencyId);
				productCombinations.setIsPremium(isPremium);
				productCombinations.setCombStrWithoutSize(attributes);
				productCombinations.setProductCountryRule(
						productCountryRuleId != null ? countryRuleRepo.findById(productCountryRuleId).get()
								: countryRuleRepo.findByIsDefaultTrue());
			} else {
				// productCombinations.setAvailableStock(productCombinations.getAvailableStock()+Integer.parseInt(totalStock));
				productCombinations.setUpdatedOn(new Date(System.currentTimeMillis()));
			}
			if (isNewPc) {
				productCombinations = productCombinationsRepository.save(productCombinations);
			}
			if (colorSizeCombinationList!=null&&colorSizeCombinationList.size()>0){

				colorSizeCombinationList.stream().forEach(combo->{
					try {
						SetProductInfo info=setProductInfoRespository.findByColorAndSizeAndProductcombinationidentifier(combo.getColor(),combo.getSize(),productUniqueIdentifier);
						if (info!=null){
							info.setQtyPerSet(combo.getQtyPerSet());
							setProductInfoRespository.save(info);
						}else {
							SetProductInfo info1=new SetProductInfo();
							info1.setColor(combo.getColor());
							info1.setSize(combo.getSize());
							info1.setQtyPerSet(combo.getQtyPerSet());
							info1.setProductcombinationidentifier(productUniqueIdentifier);
							setProductInfoRespository.save(info1);
						}



					}catch (Exception e){
						_logger.error("Error while saving set info in bulk listing ",e);
					}
				});

			}

			/*if(attributes!=null){
				attributes.setProductCombinationId(productUniqueIdentifier);
				ProductAttributeDo dto=     paMapperToDto(attributes);
				productAttributeRepo.save(dto);
			}*/


			List<ProductStock> psl = productStockRepository.findByProductCombinationsAndUnitPriceAndIsActive(
					productCombinations, Float.parseFloat(supplierAmount), true);
			ProductStock ps = new ProductStock();
			ProductStock ps1 = null;
			if (psl.isEmpty()) {
				ps1 = new ProductStock(productCombinations, Integer.parseInt(totalStock),
						Float.parseFloat(supplierAmount),
						Float.parseFloat(supplierAmount) * Integer.parseInt(totalStock));

			} else {
				ps.setUpdatedOn(new Date());
				String stockChangeString = "Stock Changed From " + ps.getTotalStock() + "to " + totalStock
						+ " By Supplier" + productCombinations.getSupplier().getSupplierName() + "Available stock was "
						+ productCombinations.getAvailableStock();
				ps.setStockChangeComment(stockChangeString);
				ps.setOldStock(ps.getTotalStock());
				ps.setTotalStock(Integer.parseInt(totalStock));
				productCombinations.setAvailableStock(Integer.parseInt(totalStock));
				productCombinations.setUpdatedOn(new Date());
			}
			productCombinations = productCombinationsRepository.save(productCombinations);
			if(ps!=null) {
			productStockRepository.save(ps);
			}
			if(ps1!=null) {
			productStockRepository.save(ps1);
			}

			List<Map<String, String>> priceMatrixResponse = new ArrayList<>();

			if (priceMatrixMapList != null && !priceMatrixMapList.toString().equalsIgnoreCase("")) {
				/*
				 * for(Map<String,String> priceMatrixMap:priceMatrixMapList) {
				 * priceMatrixMap.put(productCombinationString, productUniqueIdentifier) }
				 */
				for (Map<String, String> priceMatrixMap : priceMatrixMapList) {
					Map<String, Object> addPriceStepUpStepdownMap = new HashMap<>();
					Float discountPercentage = ((Float.parseFloat(supplierAmount) - Float.parseFloat(
							priceMatrixMap.get("BasePriceperunit") != null ? priceMatrixMap.get("BasePriceperunit")
									: "0"))
							/ Float.parseFloat(supplierAmount)) * 100;
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					final String url1 = pricingServiceURL.concat("productpricefactor/addPriceStepUpStedDown");
					if (((String) priceMatrixMap.get("istax")).equalsIgnoreCase("true")) {
						addPriceStepUpStepdownMap.put("lowerLimit", priceMatrixMap.get("lowerLimit"));
						addPriceStepUpStepdownMap.put("productCombinationuniqueId",
								productCombinations.getUniqueIdentifier());
						addPriceStepUpStepdownMap.put("upperLimit", priceMatrixMap.get("upperLimit"));
						addPriceStepUpStepdownMap.put("discountPercentage", "");
						addPriceStepUpStepdownMap.put("startDate", formatter.format(new Date()));
						addPriceStepUpStepdownMap.put("endDate", formatter.format(new Date()));
						addPriceStepUpStepdownMap.put("isTax", true);
						addPriceStepUpStepdownMap.put("taxName", "TAX");
						addPriceStepUpStepdownMap.put("taxPercent", (priceMatrixMap.get("taxpercentage")) + "");

					} else {
						addPriceStepUpStepdownMap.put("lowerLimit", priceMatrixMap.get("lowerLimit"));
						addPriceStepUpStepdownMap.put("productCombinationuniqueId",
								productCombinations.getUniqueIdentifier());
						addPriceStepUpStepdownMap.put("upperLimit", priceMatrixMap.get("upperLimit"));
						addPriceStepUpStepdownMap.put("discountPercentage", "-" + discountPercentage);
						addPriceStepUpStepdownMap.put("startDate", formatter.format(new Date()));
						addPriceStepUpStepdownMap.put("endDate", formatter.format(new Date()));
						addPriceStepUpStepdownMap.put("isTax", false);
						addPriceStepUpStepdownMap.put("taxName", "");
						addPriceStepUpStepdownMap.put("taxPercent", "");
					}
					Gson gson = new Gson();
					String jsonString = gson.toJson(addPriceStepUpStepdownMap);
					_logger.debug("product price factor request " + jsonString);
					JSONObject obj = new JSONObject(jsonString);
					HttpHeaders headers1 = new HttpHeaders();
					headers1.setContentType(MediaType.APPLICATION_JSON);
					HttpEntity<String> request1 = new HttpEntity<String>(obj.toString(), headers1);
					ResponseEntity<Map> response1 = restTemplate.exchange(url1, HttpMethod.POST, request1,
							pricingServiceURLTimeout, Map.class);
					if (response1.getBody().get("Status").equals("Success")) {
						priceMatrixResponse.add(priceMatrixMap);
					}

				}
			}

			/**
			 * REMOVED ADDING KB MARGIN IN PRICE ATTRIBUTE OF PC KB-MARGIN will get added in
			 * pricing service
			 */

			/*	*//**
					 * adding kbmargin to pc price
					 *//*
						 * Float marginValue=Float.valueOf(0); try { final String marginUrl =
						 * pricingServiceURL.concat("productpricefactor/kbmargin/"+product.getUUID()+"/"
						 * +supplier.getId()); HttpEntity<String> request1 = new HttpEntity<String>(new
						 * HttpHeaders()); ResponseEntity<Map> response1 =
						 * restTemplate.exchange(marginUrl, HttpMethod.GET,
						 * request1,pricingServiceURLTimeout,Map.class); marginValue=
						 * ((Double)response1.getBody().get("kbMargin")).floatValue(); }catch (Exception
						 * e) {
						 * _logger.info("Exception in adding KB margin for product combination UUID  {}"
						 * ,product.getUUID()); _logger.
						 * error(" Reason for Exception in adding KB margin for product combination UUID "
						 * ,e); throw new
						 * InventoryException(InventoryConstants.PRICING_CALCULATOR_ERROR); }
						 * 
						 * Float price= productCombinations.getPrice(); Float priceWithKbMargin=price
						 * +((Double)(price*marginValue*0.01)).floatValue();
						 * productCombinations.setPrice(priceWithKbMargin);
						 * productCombinationsRepository.save(productCombinations);
						 */

		} else {
			productCombinations = updateProductCombination(productCombinationUUID, priceMatrixMapList, oldStock,
					totalStock, supplierAmount, productDescription, itemName);
		}

		if (isSingleListing) {
			ProductInventoryBO piBO = populateProductInventoryObjForSupplier(productCombinations.getUniqueIdentifier());
			// piBO.setCategoryString(categoryString);
			// piBO.setSaleDiscountPrice();
			piBO.setSaleStartDate(startDate);
			// piBO.setPriceMatrix(priceMatrixResponse);
			piBO.setSaleEndDate(endDate);
			return piBO;
		} else {
			ProductInventoryBO piBO = new ProductInventoryBO();
			piBO.setProductClubingId(productCombinations.getUniqueIdentifier());
			return piBO;
		}
		// return piBO;
	}

	@Transactional
	private ProductCombinations updateProductCombination(String UUID, List<Map<String, String>> priceMatrixMapList,
			String oldStock, String totalStock, String supplierAmount, String productDescription, String itemName)
			throws Exception {
		Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
		List<ProductStatus> productStatusList = new ArrayList<>();
		productStatusList.add(productStatus.get());
		ProductCombinations productCombination = productCombinationsRepository
				.findByUniqueIdentifierAndProductStatusNotIn(UUID, productStatusList);
		List<Map<String, String>> priceMatrixResponse = new ArrayList<>();
		Float newPriceWithMargin = productCombination.getPrice();
		Map<String, String> basePricePriceMatrix = priceMatrixMapList.stream()
				.min(Comparator.comparing(map -> map.get("lowerLimit"))).orElse(null);
		// (map->map.get("lowerLimit").equalsIgnoreCase("1")).findFirst().orElse(null);
		if (null != basePricePriceMatrix) {
			String supplierAmountfromMinPM = basePricePriceMatrix.get("basePriceperunit");
			if (!supplierAmountfromMinPM.equalsIgnoreCase(supplierAmount)) {
				supplierAmount = supplierAmountfromMinPM;
				/**
				 * adding kbmargin to pc price
				 */
				Float marginValue = Float.valueOf(0);
				try {
//					final String marginUrl = pricingServiceURL
//							.concat("productpricefactor/kbmargin/" + productCombination.getUniqueIdentifier() + "/"
//									+ productCombination.getSupplier().getId());
//					HttpEntity<String> request1 = new HttpEntity<String>(new HttpHeaders());
//					ResponseEntity<Map> response1 = restTemplate.exchange(marginUrl, HttpMethod.GET, request1,
//							pricingServiceURLTimeout, Map.class);
//					marginValue = ((Double) response1.getBody().get("kbMargin")).floatValue();
					Float changedSupplieramount = Float.valueOf(supplierAmount);
					newPriceWithMargin = ((Double) (changedSupplieramount * marginValue * 0.01)).floatValue()
							+ changedSupplieramount;
				} catch (Exception e) {
					_logger.info(
							"Exception in adding KB margin for product combination UUID while updating the product  {}",
							productCombination.getUniqueIdentifier());
					_logger.error(
							" Reason for Exception in adding KB margin for product combination UUID while updating the product ",
							e);
					throw new InventoryException(InventoryConstants.PRICING_CALCULATOR_ERROR);
				}

			}
		}

		if (priceMatrixMapList != null && !priceMatrixMapList.toString().equalsIgnoreCase("")) {
			/*
			 * for(Map<String,String> priceMatrixMap:priceMatrixMapList) {
			 * priceMatrixMap.put(productCombinationString, productUniqueIdentifier) }
			 */
			for (Map<String, String> priceMatrixMap : priceMatrixMapList) {
				Map<String, Object> addPriceStepUpStepdownMap = new HashMap<>();
				Float discountPercentage = ((Float.parseFloat(supplierAmount) - Float.parseFloat(
						priceMatrixMap.get("basePriceperunit") != null ? priceMatrixMap.get("basePriceperunit") : "0"))
						/ Float.parseFloat(supplierAmount)) * 100;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				final String url1 = pricingServiceURL.concat("productpricefactor/addPriceStepUpStedDown");
				if (((String) priceMatrixMap.get("istax")).equalsIgnoreCase("true")) {
					addPriceStepUpStepdownMap.put("lowerLimit", priceMatrixMap.get("lowerLimit"));
					addPriceStepUpStepdownMap.put("productCombinationuniqueId",
							productCombination.getUniqueIdentifier());
					addPriceStepUpStepdownMap.put("upperLimit", priceMatrixMap.get("upperLimit"));
					addPriceStepUpStepdownMap.put("discountPercentage", "");
					addPriceStepUpStepdownMap.put("startDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("endDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("isTax", true);
					addPriceStepUpStepdownMap.put("taxName", "TAX");
					addPriceStepUpStepdownMap.put("taxPercent", (priceMatrixMap.get("taxpercentage")) + "");

				} else {
					addPriceStepUpStepdownMap.put("lowerLimit", priceMatrixMap.get("lowerLimit"));
					addPriceStepUpStepdownMap.put("productCombinationuniqueId",
							productCombination.getUniqueIdentifier());
					addPriceStepUpStepdownMap.put("upperLimit", priceMatrixMap.get("upperLimit"));
					if (discountPercentage > 0) {
						addPriceStepUpStepdownMap.put("discountPercentage", "-" + discountPercentage);
					} else {
						addPriceStepUpStepdownMap.put("discountPercentage", +Math.abs(discountPercentage));
					}
					addPriceStepUpStepdownMap.put("startDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("endDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("isTax", false);
					addPriceStepUpStepdownMap.put("taxName", "");
					addPriceStepUpStepdownMap.put("taxPercent", "");
				}
				Gson gson = new Gson();
				String jsonString = gson.toJson(addPriceStepUpStepdownMap);
				JSONObject obj = new JSONObject(jsonString);
				HttpHeaders headers1 = new HttpHeaders();
				headers1.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> request1 = new HttpEntity<String>(obj.toString(), headers1);
				ResponseEntity<Map> response1 = restTemplate.exchange(url1, HttpMethod.POST, request1,
						pricingServiceURLTimeout, Map.class);
				if (response1.getBody().get("Status").equals("Success")) {
					priceMatrixResponse.add(priceMatrixMap);
				}

			}
		}
		/******** CODE CHANGES FOR INSERTING NEW ROW IN PRODUCT STOCK TABLE AFTER EVERY UPDATE IN PRODUCT STOCK ********/
		List<ProductStock> psList = productStockRepository.findByProductCombinationsAndUnitPriceAndIsActive(productCombination,
				Float.parseFloat(supplierAmount), true);
		ProductStock ps = new ProductStock();
		ProductStock ps1 = null;
		  if (psList.isEmpty()) {
			  ps1 = new ProductStock(productCombination, Integer.parseInt(totalStock), Float.parseFloat(supplierAmount), Float.parseFloat(supplierAmount) * Integer.parseInt(totalStock));		  
		  } else {
			  ps.setInsertedOn(new Date());
			  ps.setIsActive(true);
			  ps.setOldStock(productCombination.getAvailableStock());
			  ps.setProductCombinations(productCombination);
			  ps.setStockChangeComment("Stock Changed From " + Integer.parseInt(totalStock) + "to " + totalStock + " By Supplier"
					+ productCombination.getSupplier().getSupplierName() + "Available stock was "
					+ productCombination.getAvailableStock());
			  ps.setTotalStock(Integer.parseInt(totalStock));
			  ps.setTotalPrice(newPriceWithMargin*ps.getTotalStock());
			  ps.setUnitPrice(newPriceWithMargin);
			  ps.setUpdatedOn(new Date());
		  }
		if(ps!=null) {  
		productStockRepository.save(ps);
		}
		if(ps1!=null) {
		productStockRepository.save(ps1);
		}
		

		 /* else { String stockChangeString = "Stock Changed From " +
		 * ps.getTotalStock() + "to " + totalStock + " By Supplier" +
		 * productCombination.getSupplier().getSupplierName() + "Available stock was " +
		 * productCombination.getAvailableStock();
		 * productStockRepository.updateAvailableProductStock(new Date(), ps.getId(),
		 * Integer.parseInt(totalStock), ps.getTotalStock(), stockChangeString); }
		 */
		Long countryRuleId = Long.valueOf(1);
		if (productCombination.getProductCountryRule() != null) {
			countryRuleId = productCombination.getProductCountryRule().getId();
		} else {
			countryRuleId = countryRuleRepo.findByIsDefaultTrue().getId();
		}
		productCombinationsRepository.updateAvailableStock(Integer.parseInt(totalStock),
				productCombination.getUniqueIdentifier(), productDescription, itemName,
				Float.parseFloat(supplierAmount), newPriceWithMargin, countryRuleId);

		return productCombination;

	}

	private List<String> getCombinationList(Map<String, List<String>> variationOptions) {
		Set<String> combinationSet = new HashSet<>();
		List<List<String>> lists = new ArrayList<>();
		for (String key : variationOptions.keySet()) {
			List<String> values = variationOptions.get(key);
			List<String> optionList = new ArrayList<>();
			for (String value : values) {
				optionList.add(key + ":" + value);
			}
			lists.add(optionList);
		}
		generateCombinations(lists, combinationSet, 0, "");

		return combinationSet.stream().collect(Collectors.toList());

	}

	private void generateCombinations(List<List<String>> lists, Set<String> result, int depth, String current) {
		if (depth == lists.size()) {
			result.add(current);
			return;
		}

		for (int i = 0; i < lists.get(depth).size(); i++) {
			generateCombinations(lists, result, depth + 1, current + "-" + lists.get(depth).get(i));
		}
	}

	public void sendToKafka(ProductInventoryBO pi) {
		try {
			String productInventryString = _mapper.writeValueAsString(pi);
			KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-topic",
					pi.getUniqueIdentifierForSearch(), productInventryString);
			kafkaBroadcastingService.broadcast(kbr);
		} catch (JsonProcessingException e) {
			_logger.error("product inventory not send to kafka ............", e);
		} catch (Exception e) {
			_logger.error("Exception info", e);
		}

	}

	public void sendToKafkaTest() {
		KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("product_inventory",
				"T-shirt-color/red-size/xl-sleeves/half", "T-shirt-color/red-size/xl-sleeves/half-product-inventory");
		kafkaBroadcastingService.broadcast(kbr);
	}

	/**
	 * For ADMIN pannel and ResellerPanel
	 * 
	 * @paramproductUniqueIdentifier uniqueIdentifier from product_combination
	 * @return
	 */
	public ProductInventoryBO populateProductInventoryObj(ProductView view, String displayCountryCode,
														  String displayCurrencyCode, Map<String, Long> bestSellingProductCountDetailMap) {
		ProductInventoryBO productInventory = new ProductInventoryBO();
		_logger.info("Entered populateProductInventoryObj for UUID "+view.getPcUuid());
		try {
			_logger.debug("Entered populateProductInventoryObj with OBJ -- "+ new Gson().toJson(view));
			productDetailService.addProductPrice(productInventory, view,null);
			productInventory.setWareHouseId(view.getKbwarehouseid());
			productInventory.setProductSKU(view.getProductSku());
			productInventory.setProductClubingId(view.getProductUuid());
			productInventory.setName(view.getBmcName());
			productInventory.setBrandModel(view.getBmName());
			/* Newly added by wasif */
			productInventory.setBrandModelId(view.getBmId());
			/* Newly added code ends */
			productInventory.setBrands(view.getBrandName());
			productInventory.setBrandId(view.getBrandId());
			productInventory.setBrandIcon(view.getBrandICon());
			productInventory.setSKU(view.getSku());
			productInventory.setProductURL("/" + view.getSku());
			productInventory.setUniqueIdentifierForSearch(view.getPcUuid());
			productInventory.setSellerName(view.getSupplierName());
			productInventory.setSupplierMov(view.getSupplierMov());
			 productInventory.setIsSupplierMovApplicabe(view.getIsSupplierMovApplicable());
				
			productInventory.setSupplierId(view.getSupplierId());
			productInventory.setSellerAddress(view.getSuppAddress());
			productInventory.setPickUpPincode(view.getSuppPinCode());
			productInventory.setSellerId(view.getSupplierId());
			productInventory.setHSN(view.getHsn());
			productInventory.setWeight(view.getProductWeight());
			productInventory.setLength(view.getProductLength());
			productInventory.setWidth(view.getProductWidth());
			productInventory.setHeight(view.getProductHeight());
			productInventory.setStatus(view.getStatus());

			// Need to add column in supplier table
			productInventory.setSellerCountry("India");
			productInventory.setCombStrWithoutSize(view.getCombStrWithoutSize());
			productInventory.setPremiumProduct(view.isPremium());
			productInventory
					.setRatingAvg(view.getAverageRating() != null ? String.valueOf(view.getAverageRating()) : null);
			productInventory.setCreatedOn(new SimpleDateFormat("yyyy-MM-dd").format(view.getCreatedOn()));
			List<String> options = Arrays.asList(view.getCombinationString().split("-"));
			// need to map product variation with product combination
			List<ProductVariationOptionValue> pvopvList = productVariationOptionValueRepository
					.findByProductAndVariationOptionNameIn(productRepository.findById(view.getProductId()).get(),
							options);
			List<Variation> availableVariations = variationRepository.findAll();
			Map<String, String> combinationMap = new HashMap<>();
			try {

				availableVariations.stream().forEach(var -> {
					Optional<ProductVariationOptionValue> pvopv = pvopvList.stream().filter(p -> p.getVariationOptions()
							.getVariation().getVariationName().equalsIgnoreCase(var.getVariationName())).findAny();

					if (pvopv.isPresent() && StringUtils
							.isNotBlank(pvopv.get().getVariationOptions().getVariation().getVariationName())) {
						combinationMap.put(pvopv.get().getVariationOptions().getVariation().getVariationName(),
								pvopv.get().getVariationOptions().getVariationOptionName());
					} else {
						if (StringUtils.isNotBlank(var.getVariationName())) {
							combinationMap.put(var.getVariationName(), null);
						}
					}

				});

			} catch (Exception e) {
				_logger.error("exception came ", e);
			}
			combinationMap.put("Gender", view.getGender() != null ? view.getGender().name() : null);
			combinationMap.put("Rating",
					view.getAverageRating() != null ? String.valueOf(view.getAverageRating()) : null);
			productInventory.setCombination(combinationMap);
			// needs to be taken from image gallery , as no mapping found to get single row
			// of product_variation_option_value from product_combination
			String imageNameString = view.getPcUuid();
			List<ImagesBO> images = new ArrayList<>();
			String thumbnailUrl = null;
			String GET_PRODUCT_IMAGE_URL = apiBaseUrl + "image-processing-service/static/getImagePaths/"
					+ imageNameString;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity entity = new HttpEntity(headers);
			ResponseEntity<String> result = restTemplate.exchange(GET_PRODUCT_IMAGE_URL, HttpMethod.GET, entity,
					apiBaseUrlTimeout, String.class);

			if (result.getStatusCode() == HttpStatus.OK) {
				JSONObject apiResponse = new JSONObject(Objects.requireNonNull(result.getBody()));
				if (apiResponse.has("data")) {
					int count=0;
					JSONArray dataArray = apiResponse.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int index = 0; index < dataArray.length(); index++) {
							JSONObject obj = dataArray.getJSONObject(index);
							String path = obj.getString("completePath");
							String type = obj.getString("type");
							ImagesBO imagesBO = new ImagesBO(path, type);
							images.add(imagesBO);
							if (type.contains("THUMBNAIL") && count==0) {
								thumbnailUrl = path;
							}
							if(type.contains("THUMBNAIL_A1")) {
								count =1;
								thumbnailUrl = path;
							}
						}
					}
				}
			}

			productInventory.setImages(images);
			productInventory.setTotalImageCount(this.calculateImageCount(images));
			productInventory.setDisplayPrice(String.valueOf(Utils.roundSetPrecison(view.getPrice()/view.getSetPieces(), 2)));
			// need to have a seperate column for description
			productInventory.setDescription(view.getCombinationString());
			productInventory.setThumbnailURL(thumbnailUrl);
			productInventory.setAvailabilityCount(view.getAvailableStock().toString());
			if (view.getAvailableStock() > 0 && view.getAvailableStock() >= view.getMinOrderQuantity()) {
				productInventory.setInStock(true);
			} else {
				productInventory.setInStock(false);
			}
			productInventory.setActive(view.getIsActive());
			// need to have a mapping for this property
			productInventory.setCurrency("INR");
			// List<Category> categoryList =
			// categoryRepository.findByCategoryName(subsubCategoryStringArr[subsubCategoryStringArr.length
			// -1]);
			Category lowestcategory = categoryRepository.findById(view.getLowestCategory()).get();
			productInventory.setCategoryName(lowestcategory.getCategoryName());
			productInventory.setCategoryId(lowestcategory.getId() + "");
			Map<String, Map<String, String>> subcategoriesMap = new TreeMap<>();
			Map<String, Map<String, String>> subcategoriesFinalMap = new TreeMap<>();
			String subCategeryStageKeyString = "subCategoryStage";
			Map<String, String> categoryNameIdMap = new HashMap<>();
			categoryNameIdMap.put("name", lowestcategory.getCategoryName());
			categoryNameIdMap.put("id", lowestcategory.getId() + "");
			subcategoriesMap.put(subCategeryStageKeyString + lowestcategory.getCategoryStageIntValue(),
					categoryNameIdMap);
			if (lowestcategory.getParent() != null) {
				Category parent = lowestcategory.getParent();
				while (parent != null) {
					categoryNameIdMap = new HashMap<>();
					categoryNameIdMap.put("name", parent.getCategoryName().trim());
					categoryNameIdMap.put("id", parent.getId() + "");
					subcategoriesMap.put(subCategeryStageKeyString + parent.getCategoryStageIntValue(),
							categoryNameIdMap);
					parent = parent.getParent();
				}

				/*
				 * AtomicInteger count = new AtomicInteger(counter);
				 * subcategoriesMap.forEach((k, v) -> {
				 * subcategoriesFinalMap.put(subCategeryStageKeyString +
				 * count.getAndDecrement(), v); });
				 */

			}
			String categoryString = "";
			productInventory.setCategoryList(subcategoriesMap);
			try {
				for (int i = 1; i <= subcategoriesMap.size(); i++) {
					categoryString = categoryString + "-" + subcategoriesMap.get("subCategoryStage" + i).get("name");
				}
			} catch (Exception e) {

			}
			categoryString = categoryString.replaceFirst("-", "");
			productInventory.setLegalDisclaimerDescription(view.getLegalDisclaimerDescription());
			productInventory.setManufacturerContactNumber(view.getManufacturerContactNumber());
			productInventory.setMaxOrderQuantity(view.getMaxOrderQuantity());
			productInventory.setMinOrderQuantity(view.getMinOrderQuantity());
			productInventory.setMrp(view.getMrp());
			productInventory.setProductDescription(view.getProductDescription());
			productInventory.setSearchTerms(view.getSearchTerms());
			productInventory.setCategoryString(view.getCategoryString());//
			productInventory.setItemName(view.getItemName());
			productInventory.setBrandName(view.getBrandName());
			productInventory.setManufacturerPartNumber(view.getManufacturerPartNumber());
			productInventory.setGender(view.getGender());
			productInventory.setTotalStock(view.getAvailableStock());
			productInventory.setTargertGender(view.getTargertGender());

			StringBuilder sb = new StringBuilder();
			combinationMap.forEach((k, v) -> {
				sb.append(k + ":" + v + "-");
			});
			productInventory.setVariationVariationOptionString(StringUtils.removeEnd(sb.toString(), "-"));//
			productInventory.setProductTaxCode(view.getProductTaxCode());
			/**
			 * for reseller supplier amount is with kb margin
			 */
			productInventory.setSupplierAmount(view.getPrice());
			productInventory.setSaleDiscountPrice(null);
			productInventory.setSaleStartDate(null);
			productInventory.setSaleEndDate(null);
			productInventory.setHandlingTime(view.getHandlingTime());
			productInventory.setCountryOfOrigion(view.getCountryOfOrigion());
			productInventory.setBulletPoints(
					null != view.getBulletPoints() ? Arrays.asList(view.getBulletPoints().split("#@@&")) : null);
			productInventory.setTargetAudienceKeywords(view.getTargetAudienceKeywords());
			productInventory.setOccasion(view.getOccasion());
			productInventory.setOccasionLifeStyle(view.getOccasionLifeStyle());
			productInventory.setImageNameString(view.getPcUuid());
			productInventory.setSupplierString(view.getSupplierName());
			productInventory.setBrandModelString(view.getBmName());
			productInventory.setSupplierDetail(null);
			productInventory.setProductString(view.getProductString());
			productInventory.setProductNameString(view.getProductName());
			productInventory.setSKU(view.getSku());
			productInventory.setItemName(view.getItemName());
			productInventory.setResellerDiscount(view.getResellerDiscount());
			productInventory.setCollectionIds(view.getCollectionIds());
			try {
				List l = productDetailService.getProductPriceBulkBuy(view.getPcUuid(), displayCountryCode);

				productInventory.setPriceMatrix(l);
			} catch (Exception ee) {
				_logger.info("Exception while getProductPriceBulkBuy of unique key ::::::::: {}", view.getPcUuid());
			}
			/*
			 * Map map = (Map)l.get(0); map.get
			 */
			/**
			 * APPLY PRODUCT COUNTRY RULE
			 */
			productInventory = applyProductCountryRule(productInventory, view);
			
			/**
			 * Add Margin CountryWise
			 */
			
			productInventory=productDetailService.applyMarginCOuntryWise(productInventory);

			/**
			 * Commented as it needs to be implemented in scheduler
			 */
			Optional<Brands> brandOpt = brandsRepository.findById(productInventory.getBrandId());
			Brands brand = null;
			if(brandOpt.isPresent()) {
				brand = brandOpt.get();
			}
			productInventory.setBasedIn(brand.getBasedIn());
			int totalBrandReview =0;
			Float totalRating = 0f;
			if(brand!=null && brand.getBrandReviews()!=null && !brand.getBrandReviews().isEmpty()) {
			for(BrandReviews br:brand.getBrandReviews()) {
				totalBrandReview=totalBrandReview+1;
				totalRating = totalRating + br.getReviewRating();
			}
			productInventory.setTotalBrandReviews(totalBrandReview);
			if(totalBrandReview!=0) {
			productInventory.setAverageBrandRating(totalRating/totalBrandReview);
			}
			}

			setRetailMarginAndBestSellingProductCount(productInventory, bestSellingProductCountDetailMap);

			// sendToKafka(productInventory);
		} catch (Exception e) {
			_logger.debug("Inventory Object created till Exception "+ new Gson().toJson(productInventory));
			_logger.info("Exception while creting productInventory of unique key ::::::::: {}", view.getPcUuid());
			_logger.error("", e);

		}
		return productInventory;
	}

	public Map<String, List<Map<String, String>>> constructRelativePathRequest(String uuid, String images) {
		_logger.info("Entering constructRelativePathRequest method");
		images = images.trim();
		if (images.endsWith(",")) {
			images = images.substring(0, images.length() - 1);
		}
		List<String> imageUrlList = new ArrayList<String>();
		String[] imagesArray = images.split(",");
		for (String image : imagesArray) {
			imageUrlList.add(image.trim());
		}

		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("relativePath", imageUrlList.get(0));
		map.put("type", "THUMBNAIL");
		map.put("uuid", uuid);
		mapList.add(map);
		for (int i = 0; i < imageUrlList.size(); i++) {
			if (i > 0) {
				if (!imageUrlList.get(i).equalsIgnoreCase(" ")) {
					Map<String, String> map1 = new HashMap<String, String>();
					map1.put("relativePath", imageUrlList.get(i));
					map1.put("type", "OTHER");
					map1.put("uuid", uuid);
					mapList.add(map1);
				}
			}
		}

		Map<String, List<Map<String, String>>> finalMap = new HashMap<String, List<Map<String, String>>>();
		finalMap.put("relativePathObj", mapList);
		_logger.info("Leaving constructRelativePathRequest method with final Map " + finalMap.toString());
		return finalMap;

	}

	public String saveProductImageinBulk(List<Map<String, List<Map<String, String>>>> relativePathRequestList,String calledFor) {
		_logger.info("Enetered saveProductImageinBulk method with relativePathRequestList ");
		long t1 = System.currentTimeMillis();
		String url1 = "";//imageProcessingServiceUrl.concat("bulkImages");
		if(calledFor.equalsIgnoreCase("Listing")) {
			url1 = imageProcessingServiceUrl.concat("bulkImages");
		}
		if(calledFor.equalsIgnoreCase("Update")) {
			url1 = imageProcessingUpdateServiceUrl;
		}
		Gson gson = new Gson();
		String jsonString = gson.toJson(relativePathRequestList);
		// JSONObject obj = new JSONObject(jsonString);
		JSONArray objArray = new JSONArray(jsonString);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(objArray.toString(), headers);
		_logger.info("Image processing service called with url  " + url1 + " and request body " + objArray.toString());
		ResponseEntity<Map> response = restTemplate.exchange(url1, HttpMethod.POST, request,
				imageProcessingServiceUrlTimeout, Map.class);

		Map<String, String> resbody = (Map<String, String>) response.getBody();
		_logger.info("Image processing service response " + resbody);
		String respMessage = resbody.get("message");
		long t2 = System.currentTimeMillis();
		_logger.info("Time taken by saveProductImageinBulk " + (t2 - t1));
		_logger.info("Leaving saveProductImageinBulk method with relativePathRequestList "
				+ relativePathRequestList.toString());
		return respMessage;

	}

	public Map<String, Object> validateProductSKUForBulkListing(String supplierName, String categoryName,
			List<String> productSKUList) {
		boolean isValid = true;
		Map<String, Object> map = new HashMap<>();
		Supplier supplier = supplierRepository.findById(Long.valueOf(supplierName)).get();
		Set<String> productSKUs = new HashSet<>();
		List<String> inValidSKUs = new ArrayList<>();
		for (String sku : productSKUList) {
			productSKUs.add(sku);
		}
		for (String productSku : productSKUs) {
			List<ProductCombinations> productComb = productCombinationsRepository.findBySkuAndSupplier(productSku, supplier);
			if (!productComb.isEmpty()) {
				//if (!categoryName.equalsIgnoreCase(product.getBrandModelCategory().getCategory().getCategoryName())) {
					isValid = false;
					inValidSKUs.add(productSku);
					map.put("isValid", "false");
					map.put("productSKUs", inValidSKUs);
				//}
			}
		}
		if (isValid) {
			map.put("isValid", "true");
		}
		return map;
	}

	public Map<String,Object> validateSkuAndSupplier(String sku,Long supplierId,int rowId){
		boolean isValid = true;
		Map<String, Object> map = new HashMap<>();
		Optional<Supplier> supplierOpt = supplierRepository.findById(supplierId);
		if(supplierOpt.isPresent()) {
			List<ProductCombinations> pcList = productCombinationsRepository.findBySupplierAndSku(supplierOpt.get(), sku);			
			if(pcList.isEmpty()) {
				isValid = false;
				map.put("rowId", rowId);
				map.put("message", "The SKU does not belong to the given supplier");
			}
		}else {
			isValid = false;
			map.put("rowId", rowId);
			map.put("message", "supplier does not exists");
		}
		map.put("isValid",isValid);
		return map;
	}
	
	
	public List<String> populateProductInventoryBulk(List<ProductInventoryRQ> inventories) {
		_logger.info("-----------------------Entered populateProductInventoryBulk : {}------------");
		List<String> responseList = new ArrayList<String>();
		ProductCombinations productCombinations = null;
	/*	StringBuilder brandName = new StringBuilder();*/
		/*StringBuilder brandModelString = new StringBuilder();*/
		/*StringBuilder productString = new StringBuilder();
		StringBuilder productNameString = new StringBuilder();*/
		ResponseEntity<Map> response = null;
		_logger.info("Going to get the warehouse details");
		try {
			response = restTemplate.getForEntity(defaultWarehouseUrl, apiBaseUrlTimeout, Map.class);
		} catch (Exception e) {
			_logger.info("could not found default warehouse id:: {}", e.getMessage());
		}
		_logger.info("After fetching the warehouse details");
		final Long defaultWareHouseId = Long.valueOf((Integer) response.getBody().get("id"));

		String[] subsubCategoryStringArr = StringUtils.split(inventories.get(0).getCategoryString(), "-");
		boolean isNewPc = false;
		// BrandModels brandModels = brandModelsRepository.findByName(brandModelString);

		// Supplier supplier=supplierRepository.findBySupplierName(supplierString);
		_logger.info("Going to get the supplier details for supplier Id " + inventories.get(0).getSupplierId());
		Optional<Supplier> supplierOptional = supplierRepository.findById(inventories.get(0).getSupplierId());
		final Supplier supplier = new Supplier();
		if (supplierOptional.isPresent()) {
			Supplier supplier1 = supplierOptional.get();
			supplierMapper(supplier1, supplier);
		} else {
			ProductInventoryBO pibo1 = new ProductInventoryBO();
			responseList.add("Supplier does not exists");
			return responseList;
		}
		Optional<ProductStatus> ps = productStatusRepository.findById(StatusConstant.QC_Pending);
		Optional<ProductStatus> notProductStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
		List<Map<String, List<Map<String, String>>>> relativePathRequestList = new ArrayList<>();
		List<List<Map<String, Object>>> priceMatrixMapList = new ArrayList<>();

		int cores = Runtime.getRuntime().availableProcessors();
		_logger.info("Available core on node in service populateProductInventoryBulk--- " + cores);

		/**
		 * Added 10 threadPool for execution
		 */
		
		

		_logger.info("Inventory size "+inventories.size());
		long t3 = System.currentTimeMillis();
		inventories.stream().forEach(pirq -> {
			_logger.info("product=="+new Gson().toJson(pirq));

			if (pirq.getBrandName() == null || pirq.getBrandName().equalsIgnoreCase("")) {
				pirq.setBrandName("Generic");
			}
			if (pirq.getBrandModelString() == null
					|| pirq.getBrandModelString().equalsIgnoreCase("")) {
				pirq.setBrandModelString("Generic");
			}
			if (pirq.getProductString() == null
					|| pirq.getProductString().equalsIgnoreCase("")) {
				pirq.setProductString("Generic");
			}
			if (pirq.getProductNameString() == null
					|| pirq.getProductNameString().equalsIgnoreCase("")) {
				pirq.setProductNameString("Generic");
			}

			//Brands brands = brandsRepository.findByNameAndSupplier(pirq.getBrandName(),pirq.getSupplierId());

			List<Brands> allBrands = brandsRepository.findAll();
			for(Brands singleBrand : allBrands){
				if(singleBrand.getName().toLowerCase().equals(pirq.getBrandName().toLowerCase())){
					pirq.setBrandName(singleBrand.getName());
				}
			}
			_logger.info("Brand name entered : {} ", pirq.getBrandName());
			Brands brands = brandsRepository.findByName(pirq.getBrandName());

			if (null == brands) {
				brands = brandsRepository.findByName("Generic");
			}

			SupplierBrands supplierBrands =null;
			 if (brands!=null) {
				 supplierBrands= supplierBrandsRespository.findBySupplierIdAndBrandId(String.valueOf(pirq.getSupplierId()), String.valueOf(brands.getId()));
			 }
			if (null == supplierBrands) {
				brands = brandsRepository.findByName("Generic");
			}
			// Brands brands = brandsRepository.findByName(brandName);
			_logger.info("Going to get the brand models by " + pirq.getBrandModelString() + " and brand id " + brands.getId());
			// BrandModels brandModels =
			// brandModelsRepository.findByNameAndBrandId(brandModelString,brands.getId());
			BrandModels brandModels = brandModelsRepository.findByBrands(brands);
			_logger.info("After fetching the brand models  " + brandModels);
			if (null == brandModels) {
				brandModels = new BrandModels();
				brandModels.setBrands(brands);
				brandModels.setName(pirq.getBrandModelString().toString());
				brandModelsRepository.save(brandModels);

			}
			Optional<Category> categoryOptional = categoryRepository
					.findById(Long.parseLong(subsubCategoryStringArr[subsubCategoryStringArr.length - 1]));
			Category category = new Category();
			if (categoryOptional.isPresent()) {
				category = categoryOptional.get();
			}
			BrandModelCategory brandModelCategory = brandModelCategoryRepository.findByCategoryAndBrandModels(category,
					brandModels);
			_logger.info("After fetching the brandModelCategory  " + brandModelCategory);
			if (brandModelCategory == null) {
				_logger.info("The brandModelCategory is null going to create category & brandModelCategory");
				// List<Category> category = categoryRepository
				// .findByCategoryName(subsubCategoryStringArr[subsubCategoryStringArr.length -
				// 1]);

				brandModelCategory = new BrandModelCategory(pirq.getBrandName().toString(),
						subsubCategoryStringArr[subsubCategoryStringArr.length - 2], category, brandModels);
				brandModelCategoryRepository.saveAndFlush(brandModelCategory);
				_logger.info("After creating category & brandModelCategory");
			}

			BrandModelCategory brandModelCategoryForStream=brandModelCategory;


			try {
				long t1 = System.currentTimeMillis();
				if(StringUtils.isNotBlank(pirq.getProductSKU())) {					
					ProductInventoryBO piBO = uploadProductInBulk(pirq, defaultWareHouseId, pirq.getBrandName().toString(),
							pirq.getBrandModelString().toString(), pirq.getProductString().toString(), pirq.getProductNameString().toString(), supplier,
							subsubCategoryStringArr, ps.get(), notProductStatus.get(),brandModelCategoryForStream, pirq.getImage1());
				_logger.info("Step one product uuid "+piBO.getProductClubingId());
				Map<String, List<Map<String, String>>> relativePathRequest = constructRelativePathRequest(
						piBO.getProductClubingId(), pirq.getImage1());
				if(StringUtils.isNotBlank(piBO.getProductClubingId())) {
					relativePathRequestList.add(relativePathRequest);
					priceMatrixMapList.add(piBO.getPriceMatrixMapList());
				}
				long t2 = System.currentTimeMillis();
				_logger.info("priceMatrixMapList " + priceMatrixMapList.toString());

				_logger.info("time taken in product sku " + pirq.getSKU() + " product--------- " + (t2 - t1));
				}
			} catch (Exception e) {
				_logger.error("Exception Came while Parallel listing of bulk upload for sku --- > " + pirq.getSKU(), e);
				if (!(priceMatrixMapList.size() > 0)) {
					_logger.error("priceMatrixMapList size is zero", e);
				}
			}
		});
		long t4 = System.currentTimeMillis();
		_logger.info("time taken in whole inventory -------- " + (t4 - t3));
		
		try {
			String responseMessage = saveProductImageinBulk(relativePathRequestList,"Listing");
			responseList.add(responseMessage);
		} catch (Exception e) {
			responseList.add(e.getMessage());
			_logger.error("Exception in uploadProduct ", e);

		}
		long t5 = System.currentTimeMillis();
		_logger.info("time taken in saveProductImageinBulk -------- " + (t5 - t4));
		String response1 = callBulkPriceStepupStepDown(priceMatrixMapList);
		
		_logger.info("-----------------------Entered populateProductInventoryBulk : {}------------");
		return responseList;
	}

	public String callBulkPriceStepupStepDown(List<List<Map<String, Object>>> priceMatrixMapList){
		ResponseEntity<List> response1 = null;
		if (priceMatrixMapList.size() > 0) {
			Gson gson = new Gson();
			final String url1 = pricingServiceURL.concat("productpricefactor/addPriceStepUpStedDown/bulk");
			String jsonString = gson.toJson(priceMatrixMapList);
			_logger.info("calling addPriceStepUpStedDown Bulk service with request " + jsonString + " and url " + url1);
			JSONArray jsonArray = new JSONArray(jsonString);
			HttpHeaders headers1 = new HttpHeaders();
			headers1.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request1 = new HttpEntity<String>(jsonArray.toString(), headers1);
			 response1 = restTemplate.exchange(url1, HttpMethod.POST, request1,
					pricingServiceURLTimeout, List.class);
			_logger.info("addPriceStepUpStedDown Bulk service response " + response1.getBody());
			
		}
		return response1.getBody().get(0).toString();
	}
	
	public ProductInventoryBO uploadProductInBulk(ProductInventoryRQ pirq, Long defaultWareHouseId, String brandName,
			String brandModelString, String productString, String productNameString, Supplier supplier,
			String[] subsubCategoryStringArr, ProductStatus prodctStatus, ProductStatus notProdctStatus,BrandModelCategory brandModelCategory, String imageName) {
		ProductInventoryBO piBO = new ProductInventoryBO();
		try {
			_logger.info("Entering the uploadProductInBulk method");
			ProductCombinations productCombinations = null;
			boolean isNewPc = false;
			_logger.info("Going to get the brand by name " + brandName);

			_logger.info(
					"Before find  product by supplier Id " + supplier.getId() + " and sku " + pirq.getProductSKU());
			Product product = productRepository.findByProductSkuAndSupplierAndBrandModelCategory(pirq.getProductSKU(), supplier,brandModelCategory);
			_logger.info("after fetching  product,the product is " + product);
			ImageGallery imgGallery = null;
			if (null == product) {
				_logger.info("Going to create a new product");
				// Image name added.
				product = new Product(productNameString, productString, imageName, Integer.parseInt("10"),
						brandModelCategory, supplier, new Date(System.currentTimeMillis()),
						pirq.getLegalDisclaimerDescription(), pirq.getManufacturerContactNumber(),
						UUID.randomUUID().toString(), pirq.getProductSKU());
			}
			_logger.info("Category String "+subsubCategoryStringArr.toString());
			_logger.info("Lowest level category Name"+subsubCategoryStringArr[subsubCategoryStringArr.length - 2]);
			_logger.info("Product Category Name"+product.getBrandModelCategory().getCategory().getCategoryName());
			if (!subsubCategoryStringArr[subsubCategoryStringArr.length - 2]
					.equalsIgnoreCase(product.getBrandModelCategory().getCategory().getCategoryName())) {
				ProductInventoryBO pibo1 = new ProductInventoryBO();
				_logger.info("Product SKU already associated with another category");
				pibo1.setErrMessage("Product SKU already associated with another category");
				return pibo1;
			}
			_logger.info("Product SKU"+pirq.getProductSKU());
			product = productRepository.save(product);
			String productCombinationString = "";
			String productCombinationStringWithoutSizeVar = "";

			String[] variationOptionString = StringUtils.split(pirq.getVariationVariationOptionString(), "-");
			_logger.info("variation Option String " + variationOptionString);
			for (String str : variationOptionString) {
				Variation variation = variationRepository.findByVariationName(str.split(":")[0]);
				_logger.info("variation by variation name " + str.split(":")[0] + " is " + variation);
				_logger.info("Going to get variation options " + str.split(":")[1]);
				VariationOptions option = variationOptionsRepository
						.findByVariationOptionNameAndVariation(str.split(":")[1], variation);
				_logger.info("Fetched variation option is " + option);
				if (StringUtils.isNotBlank(productCombinationString)) {
					productCombinationString = productCombinationString + "-" + str.split(":")[1];
					if (!variation.getVariationName().equalsIgnoreCase("Size")) {
						productCombinationStringWithoutSizeVar = productCombinationStringWithoutSizeVar + "-"
								+ str.split(":")[1];
					}
				} else {
					productCombinationString = str.split(":")[1];
					if (!variation.getVariationName().equalsIgnoreCase("Size")) {
						productCombinationStringWithoutSizeVar = str.split(":")[1];
					}
				}
				_logger.info("Fetched variation option is " + option);
				ProductVariationOptionValue pvov = productVariationOptionValueRepository
						.findByProductAndVariationOptions(product, option);
				if (null == pvov) {
					pvov = new ProductVariationOptionValue(str.split(":")[1], product, option);
				}
				pvov = productVariationOptionValueRepository.save(pvov);
				String imageNameString = "";
				if (StringUtils.isBlank(pirq.getImageNameString())) {
					imageNameString = brandModelString + "_" + productString + productCombinationString;
				}

				imgGallery = new ImageGallery(imageNameString + "_small_Image.jpg",
						imageNameString + "_medium_Image.jpg", imageNameString + "_large_Image.jpg",
						imageNameString + "_Image_4.jpg", imageNameString + "_Image_5.jpg",
						imageNameString + "_Image_6.jpg", imageNameString + "_Image_7.jpg",
						imageNameString + "_Image_8.jpg", imageNameString + "_Image_9.jpg");
				galleryRepo.save(imgGallery);
				_logger.info("Going to get ProductVariationOptionImage");
				ProductVariationOptionImage productVariationOptionImage = productVariationOptionImageRepository
						.findByProductAndProductVariationOptionValue(product, pvov);
				if (null == productVariationOptionImage) {
					productVariationOptionImage = new ProductVariationOptionImage(product, imgGallery, pvov, true);
				}

				productVariationOptionImage = productVariationOptionImageRepository.save(productVariationOptionImage);
			}
			_logger.info("Product combination SKU "+pirq.getSKU());
			List<ProductStatus> notPcList = new ArrayList<ProductStatus>();
			_logger.info("productCombinationString "+productCombinationString);

			productCombinations = productCombinationsRepository
					.findByProductAndCombinationStringAndSupplierPriceAndProductStatusNotIn(product,
							productCombinationString, Float.parseFloat(pirq.getSupplierAmount()), notPcList);
			String productUniqueIdentifier = UUID.randomUUID().toString();
			if (null == productCombinations) {
				_logger.info("Inside condition for creating new product combiantion");
				_logger.info("Product combination SKU Inside null condition"+pirq.getSKU());
				isNewPc = true;

				productCombinations = new ProductCombinations(product, productCombinationString, pirq.getSKU(),
						Float.parseFloat(pirq.getSupplierAmount()), Float.parseFloat(pirq.getSupplierAmount()),
						productUniqueIdentifier, Integer.parseInt(pirq.getTotalStock()),
						new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), pirq.getHsnCode(),
						pirq.getWeight(), pirq.getLength(), pirq.getWidth(), pirq.getHeight(), false,
						pirq.getMinOrderQuantity(), pirq.getMaxOrderQuantity(), pirq.getMrp(),
						pirq.getProductDescription(), pirq.getSearchTerms(), prodctStatus,
						pirq.getManufacturerPartNumber(), pirq.getGender(), pirq.getTargertGender(),
						pirq.getProductTaxCode(), pirq.getHandlingTime(), pirq.getCountryOfOrigion(),
						pirq.getBulletPoints(), pirq.getTargetAudienceKeywords(), pirq.getOccasion(),
						pirq.getOccasionLifeStyle(), pirq.getItemName(), pirq.getCategoryString(),pirq.getSetPeices());
				productCombinations.setSupplier(supplier);
				productCombinations.setKbWareHouseId(defaultWareHouseId);
				productCombinations.setSupplierCurrencyId(pirq.getSupplierCurrencyId());
				productCombinations.setIsPremium(pirq.getIsPremium());
				productCombinations.setCombStrWithoutSize(pirq.getProductAttribute());
				productCombinations.setProductCountryRule(pirq.getProductContryRuleId() != null
						? countryRuleRepo.findById(pirq.getProductContryRuleId()).get()
						: countryRuleRepo.findByIsDefaultTrue());
			} else {
				// productCombinations.setAvailableStock(productCombinations.getAvailableStock()+Integer.parseInt(totalStock));

				if (pirq.getColorSizeCombinationList()!=null&&pirq.getColorSizeCombinationList().size()>0){

					pirq.getColorSizeCombinationList().stream().forEach(combo->{
						try {
							SetProductInfo info=setProductInfoRespository.findByColorAndSizeAndProductcombinationidentifier(combo.getColor(),combo.getSize(),productUniqueIdentifier);
                             if (info!=null){
								 info.setQtyPerSet(combo.getQtyPerSet());
								 setProductInfoRespository.save(info);
							 }else {
								 SetProductInfo info1=new SetProductInfo();
								 info1.setColor(combo.getColor());
								 info1.setSize(combo.getSize());
								 info1.setQtyPerSet(combo.getQtyPerSet());
								 info1.setProductcombinationidentifier(productUniqueIdentifier);
								 setProductInfoRespository.save(info1);
							 }



						}catch (Exception e){
						_logger.error("Error while saving set info in bulk listing ",e);
						}
								});

				}


				productCombinations.setUpdatedOn(new Date(System.currentTimeMillis()));

				_logger.info("Inside condtion to update old productCombinations");
			}


			if (isNewPc) {
				productCombinations = productCombinationsRepository.save(productCombinations);
				if (pirq.getColorSizeCombinationList()!=null&&pirq.getColorSizeCombinationList().size()>0){

					pirq.getColorSizeCombinationList().stream().forEach(combo->{

						SetProductInfo info=new SetProductInfo();
						info.setColor(combo.getColor());
						info.setSize(combo.getSize());
						info.setQtyPerSet(combo.getQtyPerSet());
						info.setProductcombinationidentifier(productUniqueIdentifier);
						setProductInfoRespository.save(info);
					});

				}
				/*if(pirq.getProductAttribute()!=null){
					pirq.getProductAttribute().setProductCombinationId(productUniqueIdentifier);
              ProductAttributeDo dto=     paMapperToDto(pirq.getProductAttribute());
			  productAttributeRepo.save(dto);
				}*/

			}
			List<ProductStock> psList = productStockRepository.findByProductCombinationsAndUnitPriceAndIsActive(
					productCombinations, Float.parseFloat(pirq.getSupplierAmount()), true);
			ProductStock ps = new ProductStock();
			ProductStock ps1 = null;
			if (psList.isEmpty()) {
				_logger.info("Inside if null == ps condtion");
				ps1 = new ProductStock(productCombinations, Integer.parseInt(pirq.getTotalStock()),
						Float.parseFloat(pirq.getSupplierAmount()),
						Float.parseFloat(pirq.getSupplierAmount()) * Integer.parseInt(pirq.getTotalStock()));

			} else {
				_logger.info("Inside else null == ps condition");
				ps.setUpdatedOn(new Date());
				String stockChangeString = "Stock Changed From " + ps.getTotalStock() + "to " + pirq.getTotalStock()
						+ " By Supplier" + productCombinations.getSupplier().getSupplierName() + "Available stock was "
						+ productCombinations.getAvailableStock();
				ps.setStockChangeComment(stockChangeString);
				ps.setOldStock(ps.getTotalStock());
				ps.setTotalStock(Integer.parseInt(pirq.getTotalStock()));
				productStockRepository.save(ps);
				productCombinations.setAvailableStock(Integer.parseInt(pirq.getTotalStock()));
				productCombinations.setUpdatedOn(new Date());
			}
			productCombinations = productCombinationsRepository.save(productCombinations);
			if(ps!=null) {
			productStockRepository.save(ps);
			}
			if(ps1!=null) {
			productStockRepository.save(ps1);
			}

			_logger.info("After saving the product combinations");
			_logger.info("Going to create price step up step down Map with price matrix " + pirq.getPriceMatrix());
			List<Map<String, Object>> priceMatrixMapList = constructPriceMatrixMapList(pirq.getPriceMatrix(),pirq.getSupplierAmount(), productCombinations.getUniqueIdentifier());
			_logger.info("addPriceStepUpStepdownMap List" + priceMatrixMapList.toString());
			/**
			 * adding kbmargin to pc price
			 */
			Float marginValue = Float.valueOf(0);
//		try {
//			final String marginUrl = pricingServiceURL
//					.concat("productpricefactor/kbmargin/" + product.getUUID() + "/" + supplier.getId());
//			HttpEntity<String> request1 = new HttpEntity<String>(new HttpHeaders());
//			_logger.info("Kb margin request url" + marginUrl);
//			ResponseEntity<Map> response1 = restTemplate.exchange(marginUrl, HttpMethod.GET, request1,
//					pricingServiceURLTimeout, Map.class);
//			marginValue = ((Double) response1.getBody().get("kbMargin")).floatValue();
//			_logger.info("Kb margin respone " + response1.getBody());
//		} catch (Exception e) {
//			_logger.info("Exception in adding KB margin for product combination UUID  {}", product.getUUID());
//			_logger.error(" Reason for Exception in adding KB margin for product combination UUID ", e);
//			throw new InventoryException(InventoryConstants.PRICING_CALCULATOR_ERROR);
//		}

			Float price = productCombinations.getPrice();
			Float priceWithKbMargin = price + ((Double) (price * marginValue * 0.01)).floatValue();
			productCombinations.setPrice(priceWithKbMargin);
			productCombinationsRepository.save(productCombinations);

			piBO.setProductClubingId(productCombinations.getUniqueIdentifier());
			piBO.setPriceMatrixMapList(priceMatrixMapList);
			_logger.info("Leaving the uploadProductInBulk method with ProductInventoryBO " + piBO);
		} catch (Exception e) {
			_logger.error("Exception while Bulk upload for product SKU -- " + pirq.getSKU(), e);
		}
		return piBO;

		// return priceMatrixMapList;

	}

	public List<Map<String, Object>> constructPriceMatrixMapList(List<Map<String,String>> priceMatrix,String supplierAmount,String uniqueIdentifier){
		List<Map<String, String>> priceMatrixResponse = new ArrayList<>();
		List<Map<String, Object>> priceMatrixMapList = new ArrayList<Map<String, Object>>();
		Float basePriceperunit = 0f;
		for(Map<String,String> map:priceMatrix) {
			if (map.containsKey("BasePriceperunit")) {
				basePriceperunit = Float.parseFloat(map.get("BasePriceperunit"));
			}
		}
		
		
		if (priceMatrix != null && !priceMatrix.toString().equalsIgnoreCase("")) {
			/*
			 * for(Map<String,String> priceMatrixMap:priceMatrixMapList) {
			 * priceMatrixMap.put(productCombinationString, productUniqueIdentifier) }
			 */
			for (Map<String, String> priceMatrixMap : priceMatrix) {
				Map<String, Object> addPriceStepUpStepdownMap = new HashMap<>();
				Float discountPercentage = 0f;
				if (priceMatrixMap.containsKey("discountPercentage")) {
					if((Float.parseFloat(supplierAmount)) > 0){
						discountPercentage = (Float.parseFloat(priceMatrixMap.get("discountPercentage")) / Float.parseFloat(supplierAmount)) * 100;
					}
				}else
					{
						 discountPercentage = ((Float.parseFloat(supplierAmount) - basePriceperunit)
								/ Float.parseFloat(supplierAmount)) * 100;
					}

				_logger.info("discount percentage : " , discountPercentage);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if (((String) priceMatrixMap.get("istax")).equalsIgnoreCase("true")) {
					addPriceStepUpStepdownMap.put("lowerLimit", priceMatrixMap.get("lowerLimit"));
					addPriceStepUpStepdownMap.put("productCombinationuniqueId",
							uniqueIdentifier);
					addPriceStepUpStepdownMap.put("upperLimit", priceMatrixMap.get("upperLimit"));
					addPriceStepUpStepdownMap.put("discountPercentage", "");
					addPriceStepUpStepdownMap.put("startDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("endDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("isTax", true);
					addPriceStepUpStepdownMap.put("taxName", "TAX");
					addPriceStepUpStepdownMap.put("taxPercent", (priceMatrixMap.get("taxpercentage")) + "");

				} else {
					addPriceStepUpStepdownMap.put("lowerLimit", priceMatrixMap.get("lowerLimit"));
					addPriceStepUpStepdownMap.put("productCombinationuniqueId",
							uniqueIdentifier);
					addPriceStepUpStepdownMap.put("upperLimit", priceMatrixMap.get("upperLimit"));
					if(discountPercentage ==0) {
					addPriceStepUpStepdownMap.put("discountPercentage",  + discountPercentage);
					}else {
						addPriceStepUpStepdownMap.put("discountPercentage", "-" + discountPercentage);
					}
					addPriceStepUpStepdownMap.put("startDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("endDate", formatter.format(new Date()));
					addPriceStepUpStepdownMap.put("isTax", false);
					addPriceStepUpStepdownMap.put("taxName", "");
					addPriceStepUpStepdownMap.put("taxPercent", "");
				}
				priceMatrixMapList.add(addPriceStepUpStepdownMap);

			}
		}
		return priceMatrixMapList;
	}
	
	
	private Supplier supplierMapper(Supplier s, Supplier s1) {

		s1.setId(s.getId());
		s1.setSupplierName(s.getSupplierName());
		s1.setSupplierAddress(s.getSupplierAddress());
		s1.setGstOrUdyogNumber(s.getGstOrUdyogNumber());
		s1.setAddressType(s.getAddressType());
		s1.setEmail(s.getEmail());
		s1.setPhone(s.getPhone());
		s1.setAddress2(s.getAddress2());
		s1.setCity(s.getCity());
		s1.setState(s.getState());
		s1.setCountry(s.getCountry());
		s1.setPin_code(s.getPin_code());
		s1.setGender(s.getGender());
		s1.setCreatedOn(s.getCreatedOn());
		s1.setActive(s.isActive());
		s1.setUpdatedOn(s.getUpdatedOn());
		s1.setDirectShipment(s.isDirectShipment());
		s1.setFirstName(s.getFirstName());
		s1.setLastName(s.getLastName());
		s1.setAvgHandlingTimePerProd(s.getAvgHandlingTimePerProd());
		s1.setShippingPickUpLocId(s.getShippingPickUpLocId());

		return s1;
	}



	public String getSupplierSignature(String supplierId) {
		return cascheAndBulkDataService.getSupplierCertiFicate(supplierId);
	}

	public ProductInventoryBO applyProductCountryRule(ProductInventoryBO bo, ProductView view) {
		_logger.info("Inside  applyProductCountryRule  ");
		try {
		String countryRuleName = view.getPcrZone();
		String whiteListedCounreiesForProduct = "";
		if (view.getPcrDa()) {
			whiteListedCounreiesForProduct = "All";
		} else if (view.getPcrInter()) {
			whiteListedCounreiesForProduct = "International";
		} else {
			whiteListedCounreiesForProduct = view.getPcrWlc();
			if (StringUtils.isNotBlank(view.getProductWlc())
					&& !StringUtils.contains(whiteListedCounreiesForProduct, view.getProductWlc())) {
				whiteListedCounreiesForProduct = whiteListedCounreiesForProduct + "," + view.getProductWlc();
			}
			if (StringUtils.isNotBlank(view.getProductBlc())
					&& StringUtils.contains(whiteListedCounreiesForProduct, view.getProductBlc())) {
				List<String> countryCodes = Arrays.asList(whiteListedCounreiesForProduct.split(","));
				whiteListedCounreiesForProduct = countryCodes.stream().filter(i -> !view.getProductBlc().contains(i))
						.collect(Collectors.joining(","));
			}
		}
		bo.setCountryRuleName(countryRuleName);
		bo.setWhiteListedCountries(whiteListedCounreiesForProduct);
		bo.setCountryRuleId(view.getPcrId());
		}catch(Exception e) {
			_logger.error("Excepition while applyProductCountryRule  ",e);
		}
		return bo;
	}

	public Boolean isProductApplicableForProdcutContryRule(ProductView view, String displayCountryCode) {
		boolean isValid = false;
		if (view.getPcrDa()) {
			return true;
		} else if (view.getPcrInter() && displayCountryCode.equalsIgnoreCase("IN")) {
			return false;
		} else if (view.getPcrInter() && !displayCountryCode.equalsIgnoreCase("IN")) {
			return true;
		} else {

			if (StringUtils.isNotBlank(view.getPcrWlc())
					&& StringUtils.contains(view.getPcrWlc(), displayCountryCode)) {
				isValid = true;
			}

			if (StringUtils.isNotBlank(view.getProductWlc())
					&& StringUtils.contains(view.getProductWlc(), displayCountryCode)) {
				isValid = true;
			}

			if (StringUtils.isNotBlank(view.getProductBlc())
					&& StringUtils.contains(view.getProductBlc(), displayCountryCode)) {
				isValid = false;
			}

		}
		return isValid;
	}

	public List<String> bulkUpdateProductDetail(List<ProductDetailUpdateBO> productDetailUpdateBOList) {
		List<String> responseList = new ArrayList<String>();
		List<List<Map<String, Object>>> listPriceMatrixMapList = new  ArrayList<>();
		List<Map<String, List<Map<String, String>>>> relativePathRequestList = new ArrayList<>();
		productDetailUpdateBOList.stream().forEach(productDetailUpdateBO ->{
			try {
			List<ProductCombinations> pcList = productCombinationsRepository.findBySupplierAndSku(supplierRepository.findByIdAndIsActive(productDetailUpdateBO.getSupplierId(),true), productDetailUpdateBO.getSku());
			for(ProductCombinations pc:pcList) {
				if(productDetailUpdateBO.getItemName()!=null && !productDetailUpdateBO.getItemName().isEmpty()) {
					pc.setItemName(productDetailUpdateBO.getItemName());
				}
				if(productDetailUpdateBO.getMrp()!=null){
				pc.setMrp(productDetailUpdateBO.getMrp());
				}

				if(productDetailUpdateBO.getProductDescription()!=null && !productDetailUpdateBO.getProductDescription().isEmpty()) {
					pc.setProductDescription(productDetailUpdateBO.getProductDescription());
				}
				if(productDetailUpdateBO.getMoq()!=null && productDetailUpdateBO.getMoq()>0) {
					pc.setMinOrderQuantity(productDetailUpdateBO.getMoq());
				}
				if(productDetailUpdateBO.getWeight()!=null && productDetailUpdateBO.getWeight()>0.0) {
					pc.setWeight(productDetailUpdateBO.getWeight());
				}
				if(productDetailUpdateBO.getSetPeices()!=null && productDetailUpdateBO.getSetPeices()>0) {
					pc.setSetPeices(productDetailUpdateBO.getSetPeices());
				}
				if(productDetailUpdateBO.getSupplierPrice()!=null && !productDetailUpdateBO.getSupplierPrice().isEmpty()) {
					pc.setSupplierPrice(Float.parseFloat(productDetailUpdateBO.getSupplierPrice()));
					pc.setPrice(Float.parseFloat(productDetailUpdateBO.getSupplierPrice()));
				}
				if(productDetailUpdateBO.getImages()!=null && !productDetailUpdateBO.getImages().isEmpty()) {
					relativePathRequestList.add(constructRelativePathRequest(pc.getUniqueIdentifier(), productDetailUpdateBO.getImages()));
				}
				if(productDetailUpdateBO.getTaxPercentage()!=null || productDetailUpdateBO.getSupplierDiscount()!=null) {
					if(productDetailUpdateBO.getSupplierPrice()!=null) {
					listPriceMatrixMapList.add(constructPriceMatrixMapList(constructPriceMatrix(productDetailUpdateBO.getSupplierPrice().toString(), productDetailUpdateBO.getTaxPercentage(), productDetailUpdateBO.getSupplierDiscount()), productDetailUpdateBO.getSupplierPrice(), pc.getUniqueIdentifier()));
					}else {
						listPriceMatrixMapList.add(constructPriceMatrixMapList(constructPriceMatrix(pc.getSupplierPrice().toString(), productDetailUpdateBO.getTaxPercentage(), productDetailUpdateBO.getSupplierDiscount()), pc.getSupplierPrice().toString(), pc.getUniqueIdentifier()));
					}
				}
				if(productDetailUpdateBO.getHsn()!=null && !productDetailUpdateBO.getHsn().isEmpty() && checkIsHsnValid(productDetailUpdateBO.getHsn())) {
					if(productDetailUpdateBO.getHsn().contains(".0")) {
						productDetailUpdateBO.setHsn(productDetailUpdateBO.getHsn().substring(0, productDetailUpdateBO.getHsn().length()-2));
					}
					pc.setHSN(productDetailUpdateBO.getHsn());
				}
				if(productDetailUpdateBO.getStock()!=null) {
					ProductStock ps = new ProductStock();
					ps.setInsertedOn(new Date());
					ps.setIsActive(true);
					ps.setOldStock(pc.getAvailableStock());
					ps.setTotalStock(productDetailUpdateBO.getStock());
					ps.setStockChangeComment("Stock changed from Bulk Product Update By Admin, Old Stock was "+ps.getOldStock()+", new Stock is "+ps.getTotalStock());
					ps.setTotalPrice(pc.getPrice()*ps.getTotalStock());
					ps.setUnitPrice(pc.getPrice());
					ps.setProductCombinations(pc);
					ps.setUpdatedOn(new Date());
					
					productStockRepository.save(ps);
					pc.setAvailableStock(productDetailUpdateBO.getStock());
				}
				if(pcList.size()==1) {
					if(productDetailUpdateBO.getActivate()!=null){
						if(productDetailUpdateBO.getActivate()) {
							pc.setIsActive(true);
							pc.setStatus(productStatusRepository.findById(StatusConstant.Active).get());
						}else {
							pc.setIsActive(false);
							pc.setStatus(productStatusRepository.findById(StatusConstant.QC_RejectedByAdmin).get());
						}
					}
				}
				pc.setUpdatedOn(new Date());
				productCombinationsRepository.save(pc);
			}		
			
			
			}catch(Exception e) {
				_logger.error("Excepition while bulk Updating ProductDetail  ",e);
			}
		});
		String responseMessage = "Success";
		try {
		if(!listPriceMatrixMapList.isEmpty()) {
		callBulkPriceStepupStepDown(listPriceMatrixMapList);
		}
		
		}catch(Exception e) {
			responseMessage = "Failure in updating tax";
		}
		try {
		if(!relativePathRequestList.isEmpty())
		{
			saveProductImageinBulk(relativePathRequestList,"Update");
		}
		}catch(Exception e) {
			responseMessage = "Failure in updating images paths";
		}
		responseList.add(responseMessage);
		
		return responseList;
	}
	
	public boolean checkIsHsnValid(String hsn) {
		if(hsn.contains(".0")) {
			hsn = hsn.substring(0, hsn.length()-2);
		}
		try {
			Integer.parseInt(hsn);
			return true;
		}catch(NumberFormatException nfe) {
			_logger.error("Invalid HSN "+hsn);
			return false;
		}
	}	
	
	public List<Map<String, String>> constructPriceMatrix(String supplierAmount, Float taxPercentage, Float supplierDiscount) {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("BasePriceperunit", supplierAmount);
        map.put("BasePriceperunitDiscounted", "");
        map.put("DiscountOnQuantity", "");
        map.put("DiscountValueperUnit", "");
        map.put("DisplayPrice", "");
        map.put("TotalPriceAfterDiscount", "");
        map.put("endDate", "");
        map.put("essTimeOdDelivery", "");
        map.put("id", "");
        map.put("lowerLimit", "1");
        map.put("startDate", "");
        map.put("upperLimit", "5000");
        map.put("productClubingId", "");
        map.put("istax", "false");
        map.put("taxpercentage", "null");
        if(supplierDiscount!=null) {
		map.put("discountPercentage",  Float.toString(supplierDiscount));
        }

        Map<String, String> map1 = new HashMap<String, String>();

        //map1.put("BasePriceperunit", "null");
        map1.put("BasePriceperunitDiscounted", "");
        map1.put("DiscountOnQuantity", "");
        map1.put("DiscountValueperUnit", "");
        map1.put("DisplayPrice", "");
        map1.put("TotalPriceAfterDiscount", "");
        map1.put("endDate", "");
        map1.put("essTimeOdDelivery", "");
        map1.put("id", "");
        map1.put("lowerLimit", "1");
        map1.put("startDate", "");
        map1.put("upperLimit", "1");
        map1.put("productClubingId", "");
        map1.put("istax", "true");
        if(taxPercentage!=null) {
        map1.put("taxpercentage", Float.toString(taxPercentage));
        }else {
        	map1.put("taxpercentage", "");
        }
		//map.put("discountPercentage","");

        mapList.add(map);
        mapList.add(map1);

        return mapList;
    }

	public PremiumBrandDetails getProductPremiumDetailsBySupplierIdAndContact(String contact, Long supplierId) {
		Boolean isUserLoginRequired;
		Boolean isPremiumBrand;
		Boolean isUserBelongToBrand;
			Optional<Supplier> supplierOptional = supplierRepository.findById(supplierId);
			if (supplierOptional.isPresent()) {
				if (!ObjectUtils.isEmpty(supplierOptional.get().getGroupString())) {
					isUserLoginRequired = true;
					isPremiumBrand = true;
					List<Long> groupIds = Arrays.stream(supplierOptional.get().getGroupString().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
					Map<Long, Groups> groupsHashMap = groupsRespository.findAllById(groupIds)
							.stream().collect(Collectors.toMap(Groups::getId, Function.identity()));

					AtomicReference<Boolean> isUserBelongToBrandAtomic = new AtomicReference<>(false);
					if (!contact.equalsIgnoreCase("0")) {
						groupIds.forEach(groupId -> {
							if (groupsHashMap.containsKey(groupId)) {
								Groups groups = groupsHashMap.get(groupId);
								if (groups.getPhoneString().contains(contact)) {
									isUserBelongToBrandAtomic.set(true);
								}
							}

						});
					}
					isUserBelongToBrand = isUserBelongToBrandAtomic.get();

				} else {
					isUserLoginRequired = false;
					isPremiumBrand = false;
					isUserBelongToBrand = true;
				}
			} else {
				isUserLoginRequired = false;
				isPremiumBrand = false;
				isUserBelongToBrand = true;
			}
		return PremiumBrandDetails
				.builder()
				.isUserLoginRequired(isUserLoginRequired)
				.isPremiumBrand(isPremiumBrand)
				.isUserBelongToBrand(isUserBelongToBrand)
				.build();

	}

	public Map<Long, PremiumBrandDetails> getProductPremiumDetailsBySupplierIdsAndContact(String contact, SupplierListRQ request) {
		Map<Long, PremiumBrandDetails> output = new HashMap<>();
		Map<Long, Groups> groupsHashMap = new HashMap<>();
		if (CollectionUtils.isEmpty(request.getSupplierIdList())) {
			return new HashMap<>();
		}
		List<Supplier> supplierList = supplierRepository.findAllByIdList(request.getSupplierIdList());
		if (!CollectionUtils.isEmpty(supplierList)) {
			Map<Long, Supplier> supplierHashMap = supplierList.stream().collect(Collectors.toMap(Supplier::getId, Function.identity()));
			List<Long> groupIds = new ArrayList<>();
			for (Supplier supplier : supplierList) {
				if (!ObjectUtils.isEmpty(supplier) && !ObjectUtils.isEmpty(supplier.getGroupString())) {
					groupIds.addAll(Arrays.stream(supplier.getGroupString().split(",")).map(s ->
							Long.parseLong(s.trim())).collect(Collectors.toList()));
				}
			}
			if (!org.springframework.util.ObjectUtils.isEmpty(groupIds) && !contact.equalsIgnoreCase("0")) {
				List<Groups> groups = groupsRespository.findAllById(groupIds);
				groupsHashMap = groups.stream().collect(Collectors.toMap(Groups::getId, Function.identity()));
			}

			Map<Long, Groups> finalGroupsHashMap = groupsHashMap;
			request.getSupplierIdList().forEach(supplierId -> {
				if (supplierHashMap.containsKey(supplierId)) {
					Supplier supplier = supplierHashMap.get(supplierId);
					if (!org.springframework.util.ObjectUtils.isEmpty(supplier.getGroupString())) {
						Boolean isUserLoginRequired = true;
						Boolean isPremiumBrand = true;
						AtomicReference<Boolean> isUserBelongToBrand = new AtomicReference<>(false);
						if (!contact.equalsIgnoreCase("0")) {
							List<Long> groupIdList = Arrays.stream(supplier.getGroupString().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
							groupIdList.forEach(groupId -> {
								if (finalGroupsHashMap.containsKey(groupId)) {
									Groups groups = finalGroupsHashMap.get(groupId);
									if (groups.getPhoneString().contains(contact)) {
										isUserBelongToBrand.set(true);
									}
								}

							});
						}
						output.put(supplierId, setPremiumDetails(isUserLoginRequired, isPremiumBrand, isUserBelongToBrand.get()));
					} else {
						output.put(supplierId, setPremiumDetails(false, false, true));
					}
				} else {
					output.put(supplierId, setPremiumDetails(false, false, true));
				}

			});
		} else {
			for (Long supplierId : request.getSupplierIdList()) {
				output.put(supplierId, setPremiumDetails(false, false, true));
			}
		}
		return output;
	}

	private PremiumBrandDetails setPremiumDetails(Boolean isUserLoginRequired, Boolean isPremiumBrand, Boolean isUserBelongToBrand){
		return PremiumBrandDetails
				.builder()
				.isUserLoginRequired(isUserLoginRequired)
				.isPremiumBrand(isPremiumBrand)
				.isUserBelongToBrand(isUserBelongToBrand)
				.build();
	}

	private ProductAttributeDo paMapperToDto(ProductAttributeBo bo){
		ProductAttributeDo dto = new ProductAttributeDo();
		dto.setClosure(bo.getClosure());
		dto.setDesign(bo.getDesign());
		dto.setChestSize(bo.getChestsize());
		dto.setFabric(bo.getFabric());
		dto.setHemline(bo.getHemline());
		dto.setFitOrShape(bo.getFitorshape());
		dto.setLenghtSize(bo.getLengthsize());
		dto.setLength(bo.getLength());
		dto.setNeck(bo.getNeck());
		dto.setNoOfPocket(bo.getNoofpocket());
		dto.setPattern(bo.getPattern());
		dto.setPrintOrPatternType(bo.getPrintorpatterntype());
		dto.setSleeveLength(bo.getSleevelength());
		dto.setSleeveStyling(bo.getSleevestyling());
		dto.setWavePattern(bo.getWavepattern());
		dto.setProductCombinationId(bo.getProductCombinationId());
		return dto;
	}

	public ProductAttributeBo paMapperToBo(ProductAttributeDo dto){
		ProductAttributeBo bo = new ProductAttributeBo();
		if(dto!=null) {
		bo.setClosure(dto.getClosure());
		bo.setDesign(dto.getDesign());
		bo.setChestsize(dto.getChestSize());
		bo.setFabric(dto.getFabric());
		bo.setHemline(dto.getHemline());
		bo.setFitorshape(dto.getFitOrShape());
		bo.setLengthsize(dto.getLenghtSize());
		bo.setLength(dto.getLength());
		bo.setNeck(dto.getNeck());
		bo.setNoofpocket(dto.getNoOfPocket());
		bo.setPattern(dto.getPattern());
		bo.setPrintorpatterntype(dto.getPrintOrPatternType());
		bo.setSleevelength(dto.getSleeveLength());
		bo.setSleevestyling(dto.getSleeveStyling());
		bo.setWavepattern(dto.getWavePattern());
		bo.setProductCombinationId(dto.getProductCombinationId());
		}
		return bo;
	}

	public ProductInventoryRQ mapProductInventoryObj(ProductInventoryRQ1 productListingBO) {
		ProductInventoryRQ newListingObj = new ProductInventoryRQ();
		newListingObj.setBrandModelString(productListingBO.getBrandModelString());
    	newListingObj.setBrandName(productListingBO.getBrandName());
    	newListingObj.setBulletPoints(productListingBO.getBulletPoints());
    	newListingObj.setCategoryString(productListingBO.getCategoryString());
    	newListingObj.setColorSizeCombinationList(productListingBO.getColorSizeCombinationList());
    	newListingObj.setCountryOfOrigion(productListingBO.getCountryOfOrigion());
    	newListingObj.setGender(productListingBO.getGender());
    	newListingObj.setHandlingTime(productListingBO.getHandlingTime());
    	newListingObj.setHeight(productListingBO.getHeight());
    	newListingObj.setHsnCode(productListingBO.getHsnCode());
    	newListingObj.setImage1(productListingBO.getImage1());
    	newListingObj.setImage2(productListingBO.getImage2());
    	newListingObj.setImage3(productListingBO.getImage3());
    	newListingObj.setImage4(productListingBO.getImage4());
    	newListingObj.setImage5(productListingBO.getImage5());
    	newListingObj.setImage6(productListingBO.getImage6());
    	newListingObj.setImage7(productListingBO.getImage7());
    	newListingObj.setImage8(productListingBO.getImage8());
    	newListingObj.setImage9(productListingBO.getImage9());
    	newListingObj.setImageNameString(productListingBO.getImageNameString());
    	newListingObj.setIsPremium(productListingBO.getIsPremium());
    	newListingObj.setItemName(productListingBO.getItemName());
    	newListingObj.setLegalDisclaimerDescription(productListingBO.getLegalDisclaimerDescription());
    	newListingObj.setLength(productListingBO.getLength());
    	newListingObj.setManufacturerContactNumber(productListingBO.getManufacturerContactNumber());
    	newListingObj.setManufacturerPartNumber(productListingBO.getManufacturerPartNumber());
    	newListingObj.setMaxOrderQuantity(productListingBO.getMaxOrderQuantity());
    	newListingObj.setMinOrderQuantity(productListingBO.getMinOrderQuantity());
    	newListingObj.setMrp(productListingBO.getMrp());
    	newListingObj.setOccasion(productListingBO.getOccasion());
    	newListingObj.setOccasionLifeStyle(productListingBO.getOccasionLifeStyle());
    	newListingObj.setOldStock(productListingBO.getOldStock());
    	newListingObj.setPriceMatrix(productListingBO.getPriceMatrix());
    	newListingObj.setProductAttribute(String.valueOf(productListingBO.getProductAttribute()));
    	newListingObj.setProductCombinationUUID(productListingBO.getProductCombinationUUID());
    	newListingObj.setProductContryRuleId(productListingBO.getProductContryRuleId());
    	newListingObj.setProductDescription(productListingBO.getProductDescription());
    	newListingObj.setProductId(productListingBO.getProductId());
    	newListingObj.setProductNameString(productListingBO.getProductNameString());
    	newListingObj.setProductSKU(productListingBO.getProductSKU());
    	newListingObj.setProductString(productListingBO.getProductString());
    	newListingObj.setProductTaxCode(productListingBO.getProductTaxCode());
    	newListingObj.setSaleDiscountPrice(productListingBO.getSaleDiscountPrice());
    	newListingObj.setSaleEndDate(productListingBO.getSaleEndDate());
    	newListingObj.setSaleStartDate(productListingBO.getSaleStartDate());
    	newListingObj.setSearchTerms(productListingBO.getSearchTerms());
    	newListingObj.setSetPeices(productListingBO.getSetPeices());
    	newListingObj.setSKU(productListingBO.getSKU());
    	newListingObj.setSupplierAmount(productListingBO.getSupplierAmount());
    	newListingObj.setSupplierDetail(productListingBO.getSupplierDetail());
    	newListingObj.setSupplierId(productListingBO.getSupplierId());
    	newListingObj.setSupplierString(productListingBO.getSupplierString());
    	newListingObj.setTargertGender(productListingBO.getTargertGender());
    	newListingObj.setTargetAudienceKeywords(productListingBO.getTargetAudienceKeywords());
    	newListingObj.setTotalStock(productListingBO.getTotalStock());
    	newListingObj.setVariationVariationOptionString(productListingBO.getVariationVariationOptionString());
    	newListingObj.setWeight(productListingBO.getWeight());
    	newListingObj.setWidth(productListingBO.getWidth());
		return newListingObj;
	}
	
	
	public boolean inactivateProduct(String uuid) {
		_logger.info("Inside inactivateProduct method with unique identifier {}",uuid);
		boolean isUpdated = false;
		try {
		_logger.info("Going to fetch Product Status");
		ProductStatus ps = productStatusRepository.findByStatusName("Deleted_Product");
		_logger.info("After fetching Product Status");
		ProductCombinations pc = productCombinationsRepository.findByUniqueIdentifier(uuid);
		if(pc!=null && ps!=null) {
			_logger.info("Inside condition to update product status");
			pc.setIsActive(false);
			pc.setStatus(ps);
			pc.setUpdatedOn(new Date());
			productCombinationsRepository.save(pc);
			_logger.info("After updating product status");
			isUpdated = true;
		}
		}catch(Exception e) {
			_logger.error("Exception while inactivate product for UUID "+uuid+" ",e);
		}
		return isUpdated;
	}
	
}



