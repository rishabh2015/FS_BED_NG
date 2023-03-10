package com.kb.catalogInventory.service;

import com.kb.catalogInventory.datatable.*;
import com.kb.catalogInventory.model.ColorSizeCombination;
import com.kb.catalogInventory.model.ImagesBO;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.repository.*;
import com.kb.catalogInventory.util.Utils;
import com.kb.java.utils.KbRestTemplate;
import com.kb.kafka.producer.KafkaBroadcastingService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Log4j2
public class SchedulerService {

    @Autowired
    private ProductDetailService productDetailService;
@Autowired
    private ProductUploadService productUploadService;
    @Autowired
    private BrandModelCategoryRepository brandModelCategoryRepository;
    @Autowired
    SupplierService supplierService;

    @Autowired
    private BrandModelsRepository brandModelsRepository;

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

    public ProductInventoryBO createProduct(String productUniqueIdentifier, String displayCountryCode,
                                                          String displayCurrencyCode, Map<String, Long> bestSellingProductCountDetailMap, String serviceCalledFor, String contact, Boolean callFromOrder,Map<String,Object> priceMap,String supplierCertiFicate) throws Exception {

        log.info(" Inside populateProductInventoryObj ");
        ProductInventoryBO productInventory = new ProductInventoryBO();
        try {
            ProductView view = productViewRepository.findByPcUuid(productUniqueIdentifier);
            productDetailService.addProductPrice(productInventory, view,serviceCalledFor);
            productInventory.setWareHouseId(view.getKbwarehouseid());
            productInventory.setProductSKU(view.getProductSku());
            productInventory.setProductClubingId(view.getProductUuid());
            productInventory.setName(view.getBmcName());
            productInventory.setBrandModel(view.getBmName());
            productInventory.setUserPhoneString(productUploadService.getSupplierGroupusers(view.getSupplierId()));
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
                log.error("error while fetch the color size combination",e);
            }

           /* try {
                productInventory.setProductAttribute(productUploadService.paMapperToBo(productAttributeRepo.findByProductCombinationId(productUniqueIdentifier)));
            } catch (Exception e) {
                log.error("Error while fetching product attribute for product {} {}", productUniqueIdentifier, e);
            }*/
            productInventory.setColorSizeCombinationList(colorSizeCombination);

            /*new code end*/


            productInventory.setBrands(view.getBrandName());
            productInventory.setSKU(view.getSku());
            productInventory.setProductURL("/" + view.getSku());
            productInventory.setUniqueIdentifierForSearch(productUniqueIdentifier);
            productInventory.setSellerName(view.getSupplierName());
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
                log.error("exception came ", e);
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
                log.error("Exception inside fetching images while creating product inventory obj",e);
            }
            productInventory.setImages(images);

            productInventory.setTotalImageCount(productUploadService.calculateImageCount(images));
            productInventory.setDisplayPrice(String.valueOf(Utils.roundSetPrecison(view.getPrice()/view.getSetPieces(), 2)));
            // need to have a seperate column for description
            productInventory.setDescription(view.getProductDescription());
            productInventory.setThumbnailURL(thumbnailUrl);
            productInventory.setAvailabilityCount(view.getAvailableStock().toString());
            if (view.getAvailableStock() > 0 && view.getAvailableStock() >= (view.getMinOrderQuantity())) {
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
                log.error("Exception while categoriesMap ",e);
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
            productInventory.setImageNameString(productUniqueIdentifier);
            productInventory.setSupplierString(view.getSupplierName());
            productInventory.setBrandModelString(view.getBmName());
            productInventory.setSupplierDetail(null);
            productInventory.setProductString(view.getProductString());
            productInventory.setProductNameString(view.getProductName());
            productInventory.setSKU(view.getSku());
            productInventory.setItemName(view.getItemName());
            productInventory.setSupplierSignature(productUploadService.getSupplierSignature(String.valueOf(productInventory.getSellerId())));
            productInventory.setCollectionIds(view.getCollectionIds());
            productInventory.setSetPeices(view.getSetPieces());
            productInventory.setSetPrice(String.valueOf(Utils.roundSetPrecison(view.getPrice(), 2)));
            try {
                try {
                    if (view.getIsActive() && view.getStatusId() != 1 && view.getStatusId() != 3
                            && view.getStatusId() != 5 && view.getStatusId() != 7) {
                        List l = productDetailService.getProductPriceBulkBuy(productUniqueIdentifier,
                                displayCountryCode);
                        productInventory.setPriceMatrix(l);
                    }
                }catch (Exception e) {
                    log.error("Error while fetching bulkby while creating product for uuid "+productUniqueIdentifier,e);
                }
//				log.info("status of product with uuid "+productUniqueIdentifier+" is "+view.getStatusId()+" and is active "+view.getIsActive());
//				if (view.getIsActive() && view.getStatusId() != 1 && view.getStatusId() != 3 && view.getStatusId() != 5
//						&& view.getStatusId() != 7) {
//					log.info("going to call  addDiscountAndTaxToProduct for UUID "+productUniqueIdentifier);
//					productInventory = productDetailService.addDiscountAndTaxToProduct(productInventory);
//				}
            } catch (Exception ee) {
                log.error("Exception while adding priceMatrix while sending to kafka {}", ee);
            }

            /**
             * APPLY PRODUCT COUNTRY RULE
             */
            productInventory = productUploadService.applyProductCountryRule(productInventory, view);

            /**
             * Add Margin CountryWise
             */
            if (view.getIsActive() && view.getStatusId() != 1 && view.getStatusId() != 3 && view.getStatusId() != 5
                    && view.getStatusId() != 7) {
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
               productUploadService.setRetailMarginAndBestSellingProductCount(productInventory, bestSellingProductCountDetailMap);
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
        } catch (Exception e) {
            log.info("Exception while creting productInventory of unique key ::::::::: {}",
                    productUniqueIdentifier);
            log.error("Exception while creting productInventory of unique key", e);
            throw new Exception(e);
        }
        return productInventory;
    }
}
