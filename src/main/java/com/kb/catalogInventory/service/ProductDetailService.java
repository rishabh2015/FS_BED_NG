package com.kb.catalogInventory.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kb.catalogInventory.constant.InventoryConstants;
import com.kb.catalogInventory.datatable.Category;
import com.kb.catalogInventory.datatable.CategoryExcel;
import com.kb.catalogInventory.datatable.Product;
import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.ProductStatus;
import com.kb.catalogInventory.datatable.ProductVariationOptionValue;
import com.kb.catalogInventory.datatable.ProductView;
import com.kb.catalogInventory.datatable.Supplier;
import com.kb.catalogInventory.exception.InventoryException;
import com.kb.catalogInventory.listener.ProductReportkafkaListener;
import com.kb.catalogInventory.model.AdminServiceInventoryRS;
import com.kb.catalogInventory.model.CategoryRQ;
import com.kb.catalogInventory.model.CountryMarginBO;
import com.kb.catalogInventory.model.CurrencyEntity;
import com.kb.catalogInventory.model.EmptySearchFormat;
import com.kb.catalogInventory.model.FinalPriceAndSupplierDetailModel;
import com.kb.catalogInventory.model.ImagesBO;
import com.kb.catalogInventory.model.MarginBO;
import com.kb.catalogInventory.model.PremiumBrandDetails;
import com.kb.catalogInventory.model.ProductCacheBO;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.ProductPrice;
import com.kb.catalogInventory.model.ProductPriceMatrix;
import com.kb.catalogInventory.model.ProductQcBo;
import com.kb.catalogInventory.model.ProductStockBO;
import com.kb.catalogInventory.model.ProductVariationBO;
import com.kb.catalogInventory.model.RemoveProductBO;
import com.kb.catalogInventory.model.SimilarProductBO;
import com.kb.catalogInventory.model.StatusConstant;
import com.kb.catalogInventory.model.SupplierListRQ;
import com.kb.catalogInventory.model.VariationBO;
import com.kb.catalogInventory.repository.CategoryExeclRepo;
import com.kb.catalogInventory.repository.CategoryRepository;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.repository.ProductRepository;
import com.kb.catalogInventory.repository.ProductStatusRepository;
import com.kb.catalogInventory.repository.ProductVariationOptionValueRepository;
import com.kb.catalogInventory.repository.ProductViewForAdminRepo;
import com.kb.catalogInventory.repository.ProductViewRepository;
import com.kb.catalogInventory.repository.SupplierRepository;
import com.kb.catalogInventory.service.admin.BulkUpdateAdminService;
import com.kb.catalogInventory.util.Utils;
import com.kb.java.utils.KbRestTemplate;
import com.kb.java.utils.RestApiSuccessResponse;
import com.kb.kafka.producer.KafkaBroadcastingService;

@Component
public class ProductDetailService {

    @Autowired
    KbRestTemplate restTemplate;

    @Autowired
    private ProductUploadService productUploadService;

    @Autowired
    private ProductCombinationsRepository productCombinationRepo;
    
    @Autowired
    private ProductStockService productStockService;


    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductVariationOptionValueRepository productOptionValueRepo;

    @Autowired
    private SupplierRepository supplierRepo;

    @Autowired
    private ProductViewRepository productViewRepository;


    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
	private BulkUpdateAdminService bulkservice;

    @Autowired
    private ProductCacheService productCacheService;

    @Value("${pricing.service.getproduct.price.bulk.buy.url}")
    private String _GetProductPriceBulkBuyUrl;


    @Value("${pricing.all.margin.url}")
    private String _pricingAllMarginUrl;

    @Value("${pricing.service.getproduct.price.multiple.bulk.buy.url}")
    private String _GetProductMultiplePriceBulkBuyUrl;

    @Value("${pricing.service.getproduct.price.bulk.buy.url.timeout}")
    private int _GetProductPriceBulkBuyUrlTimeout;

    @Value("${pricing.service.getproduct.price.bulk.buy.for.supplier.url}")
    private String _GetProductPriceBulkBuyForSupplierUrl;

    @Value("${pricing.service.getproduct.price.bulk.buy.for.supplier.url.timeout}")
    private int _GetProductPriceBulkBuyForSupplierUrlTimeout;

    @Value("${pricing.service.multiproduct.price.url}")
    private String _GetmultipleProductPrice;

    @Value("${order.service.bestselling.getsellingcountdetail.url}")
    private String getSellingCountDetailsUrl;

    @Value("${order.service.bestselling.getsellingcountdetail.url.timeout}")
    private int getSellingCountDetailsUrlTimeout;

    @Value("${review.service.customerreview.getaverage.rating.reviewcount.url}")
    private String _GetAverageRatingUrl;
    @Value("${review.service.customerreview.getaverage.rating.reviewcount.url.timeout}")
    private int _GetAverageRatingUrlTimeout;

    @Value("${utility.service.country.getdefaultcurrency.url}")
    private String _GetDefaultCurrencyUrl;
    @Value("${utility.service.country.getdefaultcurrency.url.timeout}")
    private int _GetDefaultCurrencyUrlTimeout;

    @Value("${utility.service.country.get.country.conversiondata.url}")
    private String _GetCountryConversionDataUrl;
    @Value("${utility.service.country.get.country.conversiondata.url.timeout}")
    private int _GetCountryConversionDataUrlTimeout;

    @Value("${pricing.service.url}")
    private String productPricingBaseUrl;

    @Autowired
    private CategoryExeclRepo categoryExeclRepo;

    @Value("${utility.service.country.getCurrencyCode.url}")
    private String _GetCurrencyCodeUrl;
    @Value("${utility.service.country.getCurrencyCode.url.timeout}")
    private int _GetCurrencyCodeUrlTimeout;

    @Value("${utility.service.country.getCurrencyId.url}")
    private String _GetCurrencyIdUrl;
    @Value("${utility.service.country.getCurrencyId.url.timeout}")
    private int _GetCurrencyIdUrlTimeout;

    @Value("${environment.context.path.url}")
    private String apiBaseUrl;
    @Value("${environment.context.path.url.timeout}")
    private int apiBaseUrlTimeout;

    @Value("${categorydetail.cahe.refresh.url}")
    private String cacheRefreshUrl;

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Autowired
    private ProductViewForAdminRepo productViewForAdminRepo;


    private static final Integer precision = 2;

    @Autowired
    KafkaBroadcastingService kafkaBroadcastingService;

    @Value("kafka.topic.for.product.report")
    String kafkaTopicForReport;

    @Autowired
    ProductReportkafkaListener productReportkafkaListener;

    private final static Logger _logger = LoggerFactory.getLogger(ProductDetailService.class);

    /**
     * @param uniqueIdentifier(UUID from product_combination)
     * @return
     */
    public ProductInventoryBO productDetailByUniqueIdentifier(String uniqueIdentifier, String displayCountryCode,
                                                              String displayCurrencyCode, String serviceCalledFor, String contact, Boolean callFromOrder) {
        _logger.info("Inside productDetailByUniqueIdentifier with uniqueIdentifier {}", uniqueIdentifier);
        try {
            Map<String, Long> bestSellingProductCountDetailMap = new HashMap<>();
            Long t1 = System.currentTimeMillis();
            if (callFromOrder == null || !callFromOrder) {
                bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(Collections.singletonList(uniqueIdentifier));
            }
            Long t2 = System.currentTimeMillis();
            _logger.info("time taken in best selleing products " + (t2 - t1));
            return productUploadService.populateProductInventoryObj(uniqueIdentifier, displayCountryCode, displayCurrencyCode,
                    bestSellingProductCountDetailMap, serviceCalledFor, contact, callFromOrder);
        } catch (Exception e) {
            _logger.error("Error while creating product obj :{}", uniqueIdentifier, e);
            return null;
        }
    }

    public Map<String, Long> fetchBestSellingProductDetailCount(List<String> productIdList) {
        Map<String, Long> outputMap = new HashMap<>();
        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<BestSellingRQ> entity = new HttpEntity<>(BestSellingRQ.builder().productIds(productIdList).build(), headers);
//            ResponseEntity<String> result = restTemplate.exchange(
//                    getSellingCountDetailsUrl, HttpMethod.POST, entity, getSellingCountDetailsUrlTimeout, String.class);
//            if (result.getStatusCode() == HttpStatus.OK) {
//                org.codehaus.jettison.json.JSONObject response = new org.codehaus.jettison.json.JSONObject(Objects.requireNonNull(result.getBody()));
//                org.codehaus.jettison.json.JSONArray jsonArray = response.optJSONArray("data");
//                if (jsonArray != null && jsonArray.length() > 0) {
//                    for (int index = 0; index < jsonArray.length(); index++) {
//                        org.codehaus.jettison.json.JSONObject object = jsonArray.optJSONObject(index);
//                        outputMap.put(object.optString("productId"), object.optLong("sellingCount"));
//                    }
//                }
//            }
            productIdList.forEach(productId -> outputMap.put(productId, 0L));
        } catch (Exception e) {
            _logger.error("Exception occurred while data parsing of bestSellingProductDetails", e);
        }
        return outputMap;
    }

    /**
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return all product details
     */
    public List<ProductInventoryBO> getAllProductsDetail(String displayCountryCode, String displayCurrencyCode) {

        _logger.info("Inside getAllProductsDetail with displayCountryCode {}", displayCountryCode);
        final List<ProductInventoryBO> allProductDetails = new ArrayList<ProductInventoryBO>();
        List<String> productUUIDList = productCombinationRepo.findAllUniqueIdentifiers();
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
        productUUIDList.forEach(uuid -> {
            try {
                allProductDetails.add(productUploadService.populateProductInventoryObj(uuid, displayCountryCode,
                        displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false));
            } catch (Exception e) {
                _logger.error("Error while creating product obj :{}", uuid, e);
            }
        });
        return allProductDetails;
    }

    /**
     * @param uuid
     * @return
     */
    public ProductStockBO getAvailableStockOfProduct(String uuid) {

        _logger.info("Inside getAvailableStockOfProduct with uuid {}", uuid);
        ProductStockBO stock = new ProductStockBO();
        stock.setAvailableStock(Integer.valueOf(0));
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> productStatusList = new ArrayList<ProductStatus>();
        productStatusList.add(productStatus.get());
        ProductCombinations combination = productCombinationRepo.findByUniqueIdentifierAndProductStatusNotIn(uuid, productStatusList);
        if (null != combination) {
            if (combination.getIsActive() && combination.getStatus().getId() == StatusConstant.Active
                    && combination.getAvailableStock() != null) {
                stock.setAvailableStock((combination.getAvailableStock() >= combination.getMinOrderQuantity())
                        ? combination.getAvailableStock()
                        : 0);
            } else if (!combination.getIsActive() || !(combination.getStatus().getId() == StatusConstant.Active)) {
                stock.setAvailableStock(0);
                stock.setIsArchived(true);
            }
        } else {
            stock.setAvailableStock(0);
            stock.setIsArchived(true);
        }
        stock.setUuid(uuid);
        return stock;

    }

    /**
     * @param UUID
     * @return
     */
    public Object getBasePricePerUnit(String UUID) {

        _logger.info("Inside getBasePricePerUnit with uuid {}", UUID);
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> productStatusList = new ArrayList<ProductStatus>();
        productStatusList.add(productStatus.get());
        return Optional.of(productCombinationRepo.findByUniqueIdentifierAndProductStatusNotIn(UUID, productStatusList).getPrice()).orElse(null);
    }

    public FinalPriceAndSupplierDetailModel getBasePricePerUnitCurr(String UUID, String countryCode) throws InventoryException {

        _logger.info("Inside getBasePricePerUnitCurr with uuid {}", UUID);
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        FinalPriceAndSupplierDetailModel priceModel = new FinalPriceAndSupplierDetailModel();
        List<ProductStatus> productStatusList = new ArrayList<>();
        productStatusList.add(productStatus.get());
        ProductCombinations pc = productCombinationRepo.findByUniqueIdentifierAndProductStatusNotIn(UUID, productStatusList);
        priceModel.setBasePrice(Utils.roundSetPrecison((double) pc.getPrice(), precision));
        priceModel.setBaseCurrency("INR"); //Need to make it dynamic
        String defaultCurrencyUrl = _GetDefaultCurrencyUrl + countryCode;
        String defaultCurrency = null;
        try {

            ResponseEntity<Map> res = restTemplate.getForEntity(defaultCurrencyUrl, _GetDefaultCurrencyUrlTimeout, Map.class);
            if (res != null && res.getBody() != null) {
                LinkedHashMap<String, Object> rsMap = (LinkedHashMap) (res.getBody());
                defaultCurrency = ((String) rsMap.get("data"));
                _logger.info("exiting  defaultCurrency {}", defaultCurrency);
            }
            if (defaultCurrency == null || defaultCurrency.equalsIgnoreCase("")) {
                defaultCurrency = "INR";
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("Exception getBasePricePerUnitCurr ", e);
            throw new InventoryException(InventoryConstants.DEFAULT_CURRENCY_FETCH_ERROR);
        }
        if (priceModel.getBaseCurrency().equalsIgnoreCase(defaultCurrency)) {
            priceModel.setConvertedCurrency(defaultCurrency);
            priceModel.setConvertedPrice(Utils.roundSetPrecison(priceModel.getBasePrice(), precision));
            return priceModel;
        }
        Double factor = 1D;
        String currencyConversionUrl = _GetCountryConversionDataUrl + "INR/" + defaultCurrency;
        try {

            ResponseEntity<Map> res = restTemplate.getForEntity(currencyConversionUrl, _GetCountryConversionDataUrlTimeout, Map.class);
            if (res != null && res.getBody() != null) {
                LinkedHashMap<String, Object> rsMap = ((LinkedHashMap) (res.getBody()).get("data"));
                factor = ((Double) rsMap.get("conversionFactor"));
                _logger.debug("exiting  currencyConversionUrl {}", res.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("Exception getBasePricePerUnitCurr", e);
            throw new InventoryException(InventoryConstants.DEFAULT_CURRENCY_CONVERSIONFACTOR_FETCH_ERROR);

        }
        priceModel.setConvertedCurrency(defaultCurrency);
        priceModel.setConvertedPrice(Utils.roundSetPrecison((priceModel.getBasePrice() * factor), precision));

        return priceModel;
    }


    /**
     * @param UUID
     * @return
     */
    public Object getSupplierBasePricePerUnit(String UUID) {
        _logger.info("Inside getSupplierBasePricePerUnit with uuid {}", UUID);
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> productStatusList = new ArrayList<>();
        productStatusList.add(productStatus.get());
        ProductCombinations productCombinations = null;
        Float supplier_price = 0.0F;
        try {
            productCombinations = productCombinationRepo.findByUniqueIdentifierAndProductStatusNotIn(UUID, productStatusList);
            supplier_price = productCombinations.getSupplierPrice();
        } catch (Exception e) {
            return supplier_price;
        }

        return supplier_price;
    }

    /**
     * @param productUUID
     * @return
     */
    public List<ProductVariationBO> getProductVariationByProductId(String productUUID) {

        _logger.info("Inside getProductVariationByProductId with productUUID {}", productUUID);
        List<ProductVariationBO> productVariationList = new ArrayList<>();
        Product product = productRepo.findByUUID(productUUID);

        List<ProductVariationOptionValue> variationList = productOptionValueRepo.findByProduct(product);
        productCombinationRepo.findByProduct(product).stream().forEach(pc -> {
            ProductVariationBO productVariationBO = new ProductVariationBO();
            List<VariationBO> variations = new ArrayList<>();
            String combinationStringarr[] = StringUtils.split(pc.getCombinationString(), "-");
            for (String variationString : combinationStringarr) {
                Optional<ProductVariationOptionValue> pvov = variationList.stream()
                        .filter(var -> StringUtils.containsIgnoreCase(var.getVariationOptionName(), variationString))
                        .findFirst();
                VariationBO vbO = new VariationBO();
                vbO.setVariationValue(variationString);
                if (pvov.isPresent()) {
                    vbO.setVariationName(pvov.get().getVariationOptions().getVariation().getVariationName());
                } else {
                    vbO.setVariationName("");
                }

                variations.add(vbO);

            }
            productVariationBO.setProductVariationOptionValue(variations);
            productVariationBO.setProductCombinationValue(pc.getUniqueIdentifier());
            productVariationList.add(productVariationBO);
        });

        return productVariationList;

    }

    /**
     * @param uuids
     * @return ProductStockBO
     */

    public List<ProductStockBO> getMultipleAvailableStockOfProduct(List<String> uuids) {

        _logger.debug("Inside getMultipleAvailableStockOfProduct with uuids {}", new Gson().toJson(uuids));
        List<ProductStockBO> allAvailibility = new ArrayList<>();
        uuids.stream().forEach(uuid -> {
            allAvailibility.add(getAvailableStockOfProduct(uuid));
        });
        return allAvailibility;

    }

    /**
     * @param supplierId
     * @return Supplier DO
     */
    public Supplier getSupplier(@NotNull Long supplierId) {

        _logger.info("Inside getSupplier with supplierId {}", supplierId);

        return supplierRepo.findById(supplierId).get();
    }


    /**
     * @param supplierIdList
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> getProductsDetail(@NotNull List<Long> supplierIdList, String sortOrder,
                                                       Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) {

        _logger.debug("Inside getProductsDetail with supplierIdList {}", new Gson().toJson(supplierIdList));

        List<Map<String, Object>> resMapList = new ArrayList<>();
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "pcId"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("pcId"));
        }
        for (long supplierId : supplierIdList) {
            List<ProductInventoryBO> piBOList = new ArrayList<>();
            List<ProductView> viewList = productViewRepository.findBySupplierIdAndIsActiveSuppTrue(supplierId, pageable);
            List<String> productUUIDList = viewList.stream().map(ProductView::getPcUuid).collect(Collectors.toList());
            Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
            for (ProductView view : viewList) {
                ProductInventoryBO piBO = productUploadService.populateProductInventoryObj(view, displayCountryCode,
                        displayCurrencyCode, bestSellingProductCountDetailMap);
                piBOList.add(piBO);
                long totalCount = productViewRepository.countBySupplierId(supplierId);
                _logger.info("TotalDataCount   {} SupplierId {}", totalCount, supplierId);
                Map<String, Object> resMap = new HashMap<>();
                resMap.put("data", piBOList);
                resMap.put("TotalDataCount", totalCount);
                resMap.put("PagedDataCount", piBOList.size());
                Integer startIndex = 0;
                if (pageNum != null && pageNum != 0) {
                    startIndex = pageNum * pageSize + 1;
                }
                resMap.put("SerialNumberStartIndex", startIndex);
                resMapList.add(resMap);
            }
        }

        return resMapList;
    }

    /**
     * @param supplierIdList
     * @param sortOrder
     * @param pageNum
     * @param pageSize           //* @param displayCurrencyId
     * @param displayCountryCode
     * @return List<Map < String, Object>>
     */

    public List<Map<String, Object>> getAllActiveProductsDetail(@NotNull List<Long> supplierIdList, String sortOrder,
                                                                Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) {

        _logger.debug("Inside getAllActiveProductsDetail with supplierIdList {}", new Gson().toJson(supplierIdList));

        List<Map<String, Object>> resMapList = new ArrayList<>();
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "pcId"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("pcId"));
        }
        for (long supplierId : supplierIdList) {
            List<ProductInventoryBO> piBOList = new ArrayList<>();
            List<ProductView> viewList = productViewRepository.findBySupplierIdAndIsActiveSuppTrueAndIsActiveTrue(supplierId, pageable);
            List<String> productUUIDList = viewList.stream().map(ProductView::getPcUuid).collect(Collectors.toList());
            Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
            for (ProductView view : viewList) {
                ProductInventoryBO piBO = new ProductInventoryBO();
                piBO = productUploadService.populateProductInventoryObj(view, displayCountryCode,
                        displayCurrencyCode, bestSellingProductCountDetailMap);
                piBOList.add(piBO);
            }
            long totalCount = productViewRepository.countBySupplierId(supplierId);
            _logger.info("TotalDataCount   {} SupplierId {}", totalCount, supplierId);
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("data", piBOList);
            resMap.put("TotalDataCount", totalCount);
            resMap.put("PagedDataCount", piBOList.size());
            Integer startIndex = 0;
            if (pageNum != null && pageNum != 0) {
                startIndex = pageNum * pageSize + 1;
            }
            resMap.put("SerialNumberStartIndex", startIndex);
            resMapList.add(resMap);
        }

        return resMapList;
    }

    /**
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return List<Map < String, Object>>
     */


    /**
     * @return AdminServiceInventoryRS
     */

    public AdminServiceInventoryRS getSupplierAndProductsCount() {

        _logger.info("Inside getSupplierAndProductsCount");
        AdminServiceInventoryRS res = new AdminServiceInventoryRS();
        List<Supplier> suppliersList = supplierRepo.findAll();
        int totalProducts = 0;
        int totalSuppliers = 0;
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());
        for (Supplier sp : suppliersList) {
            Supplier supplier = supplierRepo.findByIdAndIsActive(sp.getId(), true);
            totalSuppliers = totalSuppliers + 1;
            List<ProductCombinations> listProducts = productCombinationRepo.findBySupplierAndProductStatusNotIn(supplier, psList, null);
            totalProducts = totalProducts + listProducts.size();
        }
        res.setTotalProducts(totalProducts);
        res.setTotalSuppliers(totalSuppliers);
        return res;
    }


    /**
     * @param supplierId
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getInventoryCount(long supplierId) {

        _logger.info("Inside getInventoryCount {}", supplierId);
        Map<String, Integer> resMap = new HashMap<>();

        int activeCount = productCombinationRepo.countActiveInventoryOfSupplier(supplierId);
        int inactiveCount = productCombinationRepo.countInactiveInventoryOfSupplier(supplierId);
        int inventoryCount = activeCount + inactiveCount;
        resMap.put("Inventory", inventoryCount);
        resMap.put("Active", activeCount);
        resMap.put("InActive", inactiveCount);
        return resMap;
    }

    /**
     * @param supplierId
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return List<Map < String, Object>>
     */

    public List<Map<String, Object>> getInActiveProducts(long supplierId, String sortOrder, Integer pageNum,
                                                         Integer pageSize, String displayCountryCode, String displayCurrencyCode) {

        _logger.info("Inside getInActiveProducts {}", supplierId);
        List<ProductInventoryBO> piBOList = new ArrayList<>();
        List<Map<String, Object>> pioBOListList = new ArrayList<>();
        Pageable pageable = null;
        Map<String, Object> resMap = new HashMap<>();

        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }

        Supplier supplier = supplierRepo.findByIdAndIsActive(supplierId, true);
        // List<ProductCombinations> pcList =
        // productCombinationRepo.findByProductIdInAndIsActive(productIds,false);
        Optional<ProductStatus> ps = productStatusRepository.findById(StatusConstant.QC_Pending);
        List<ProductCombinations> pcList = productCombinationRepo.findBySupplierAndProductStatus(supplier, ps.get(),
                pageable);
        List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
        pcList.stream().forEach(productCom -> {
            ProductInventoryBO piBO = new ProductInventoryBO();
            try {
                piBO = productUploadService.populateProductInventoryObj(productCom.getUniqueIdentifier(),
                        displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
            } catch (Exception e) {
                _logger.error("Error while creating product obj :{}", productCom.getUniqueIdentifier(), e);
            }
            piBOList.add(piBO);
        });
        resMap.put("data", piBOList);
        resMap.put("TotalDataCount", productCombinationRepo.countBySupplierAndStatus(supplier.getId(), StatusConstant.QC_Pending));
        resMap.put("PagedDataCount", piBOList.size());
        Integer startIndex = 0;
        if (pageNum != null && pageNum != 0) {
            startIndex = pageNum * pageSize + 1;
        }
        resMap.put("SerialNumberStartIndex", startIndex);
        pioBOListList.add(resMap);
        return pioBOListList;
    }

    /**
     * @param supplierIdList
     * @param inStock
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return List<Map < String, Object>>
     */

    public List<Map<String, Object>> getProductsOnStockFilter(@NotNull List<Long> supplierIdList, boolean inStock,
                                                              String sortOrder, Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) {

        _logger.debug("Inside getProductsOnStockFilter {}", new Gson().toJson(supplierIdList));

        List<Map<String, Object>> pioBOListList = new ArrayList<>();
        Pageable pageable = null;
        Map<String, Object> resMap = new HashMap<>();

        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<>();
        psList.add(productStatus.get());
        for (long supplierId : supplierIdList) {
            List<ProductInventoryBO> piBOList = new ArrayList<>();
            Supplier supplier = supplierRepo.findByIdAndIsActive(supplierId, true);

            if (inStock == true) {
                List<ProductCombinations> pcList = productCombinationRepo
                        .findBySupplierAndAvailableStockGreaterThanAndProductStatusNotIn(supplier, 0, psList, pageable);
                List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
                Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
                for (ProductCombinations productCom : pcList) {
                    ProductInventoryBO piBO = new ProductInventoryBO();
                    try {
                        piBO = productUploadService.populateProductInventoryObj(productCom.getUniqueIdentifier(),
                                displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
                    } catch (Exception e) {
                        _logger.error("Error while creating product obj :{}", productCom.getUniqueIdentifier(), e);
                    }
                    List apiRespnose = getProductPriceBulkBuy(productCom.getUniqueIdentifier(), displayCountryCode);
                    piBO.setPriceMatrix(apiRespnose);
                    piBOList.add(piBO);
                }
                resMap.put("TotalDataCount",
                        productCombinationRepo.countBySupplierAvailableStockGreaterThan(supplier.getId(), 0, psList.get(0).getId()));
            } else if (inStock == false) {
                List<ProductCombinations> pcList = productCombinationRepo
                        .findBySupplierAndAvailableStockLessThanEqual(supplier, 0, pageable);
                List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
                Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
                for (ProductCombinations productCom : pcList) {
                    ProductInventoryBO piBO = new ProductInventoryBO();
                    try {
                        piBO = productUploadService.populateProductInventoryObj(productCom.getUniqueIdentifier(),
                                displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
                    } catch (Exception e) {
                        _logger.error("Error while creating product obj :{}", productCom.getUniqueIdentifier(), e);
                    }
                    List apiRespnose = getProductPriceBulkBuy(productCom.getUniqueIdentifier(), displayCountryCode);
                    piBO.setPriceMatrix(apiRespnose);
                    piBOList.add(piBO);
                }
                resMap.put("TotalDataCount",
                        productCombinationRepo.countBySupplierAvailableStockLessThanEqual(supplier.getId(), 0, psList.get(0).getId()));
            }

            resMap.put("data", piBOList);
            resMap.put("PagedDataCount", piBOList.size());
            Integer startIndex = 0;
            if (pageNum != null && pageNum != 0) {
                startIndex = pageNum * pageSize + 1;
            }
            resMap.put("SerialNumberStartIndex", startIndex);
            pioBOListList.add(resMap);
        }
        return pioBOListList;

    }

    /**
     * @param supplierIdList
     * @param isActive
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return List<Map < String, Object>>
     */

    public List<Map<String, Object>> getProductsOnIsActiveFilter(@NotNull List<Long> supplierIdList, boolean isActive,
                                                                 String sortOrder, Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) {

        _logger.debug("Inside getProductsOnIsActiveFilter {}", new Gson().toJson(supplierIdList));

        List<Map<String, Object>> pioBOListList = new ArrayList<>();
        Pageable pageable = null;

        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());
        for (long supplierId : supplierIdList) {
            List<ProductInventoryBO> piBOList = new ArrayList<>();

            Supplier supplier = supplierRepo.findByIdAndIsActive(supplierId, true);
            List<ProductCombinations> pcList = productCombinationRepo.findBySupplierAndIsActiveAndProductStatusNotIn(supplier, isActive, psList,
                    pageable);
            List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
            Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
            for (ProductCombinations productCom : pcList) {
                ProductInventoryBO piBO = new ProductInventoryBO();
                try {
                    piBO = productUploadService.populateProductInventoryObj(productCom.getUniqueIdentifier(),
                            displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
                } catch (Exception e) {
                    _logger.error("Error while creating product obj :{}", productCom.getUniqueIdentifier(), e);
                }
                List apiRespnose = getProductPriceBulkBuy(productCom.getUniqueIdentifier(), displayCountryCode);
                piBO.setPriceMatrix(apiRespnose);
                piBOList.add(piBO);
            }
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("data", piBOList);
            resMap.put("TotalDataCount", productCombinationRepo.countSupplierActiveProducts(supplier.getId(), psList.get(0).getId(), isActive));
            resMap.put("PagedDataCount", piBOList.size());
            Integer startIndex = 0;
            if (pageNum != null && pageNum != 0) {
                startIndex = pageNum * pageSize + 1;
            }
            resMap.put("SerialNumberStartIndex", startIndex);
            pioBOListList.add(resMap);
        }
        return pioBOListList;

    }

    /**
     * @param uniqueIdentifier
     * @return List
     */

    public List<ProductPriceMatrix> getProductPriceBulkBuy(String uniqueIdentifier, String displayCountryCode) {

        _logger.info("Inside getProductPriceBulkBuy {}", uniqueIdentifier);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("countrycode", displayCountryCode);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(_GetProductPriceBulkBuyUrl + uniqueIdentifier);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductPriceMatrix[]> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, _GetProductPriceBulkBuyUrlTimeout, ProductPriceMatrix[].class);

        /*
         * ResponseEntity<List> re = restTemplate
         * .getForEntity(url.concat("productpricefactor/bulkbuy/" + uniqueIdentifier),
         * List.class);
         */
        List<ProductPriceMatrix> pmlist = Arrays
                .asList(res.getBody());
        return pmlist;
    }


    public Map<String, Object> getMultipleProductsBulkBuyPrice(List<String> uniqueIdentifiers,
                                                               String displayCountryCode) {

        _logger.info("Inside getProductPriceBulkBuy {}", uniqueIdentifiers);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("countrycode", displayCountryCode);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(_GetProductMultiplePriceBulkBuyUrl);
        HttpEntity<?> entity = new HttpEntity(uniqueIdentifiers, headers);
        ResponseEntity<Map> res = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
                _GetProductPriceBulkBuyUrlTimeout, Map.class);
        Map<String, Object> apiRespone = (Map<String, Object>) ((Map<String, Object>) res.getBody()).get("data");
        return apiRespone;
    }


    public ProductInventoryBO addDiscountAndTaxToProduct(ProductInventoryBO BO) {
        ResponseEntity<Map> response = restTemplate.getForEntity(productPricingBaseUrl.concat("productpricefactor/priceWithQuantity/" + BO.getUniqueIdentifierForSearch() + "/1"),
                60000, Map.class);
        Map<String, Object> priceResponse = response.getBody();
        List<Map<String, Object>> priceChangeFactors = new ArrayList<Map<String, Object>>();
        priceChangeFactors = (List<Map<String, Object>>) priceResponse.get("totalPriceChangeFactorNames");
        for (Map<String, Object> p : priceChangeFactors) {
            p.forEach((k, v) -> {
                if (k.equalsIgnoreCase("totalDiscountValue")) {
                    BO.setTotalDiscount((Utils.roundSetPrecison((Double) v, 2)));
                }
                if (k.equalsIgnoreCase("totalTaxValue")) {
                    BO.setTotalTax((Utils.roundSetPrecison((Double) v, 2)));
                }
                if (k.equalsIgnoreCase("totalDiscountpercent")) {
                    BO.setDiscountPercentPerUnit((Utils.roundSetPrecison((Double) v, 2)));
                }
                if (k.equalsIgnoreCase("totalTaxpercent")) {
                    BO.setTaxPercentage((Utils.roundSetPrecison((Double) v, 2)));
                }
            });
        }
        BO.setPriceBeforeTax(String.valueOf((Double.valueOf(BO.getDisplayPrice()) - BO.getTotalDiscount() / BO.getSetPeices())));
        Double priceAfterDiscount = Utils.roundSetPrecison((Double) priceResponse.get("quotedPrice"), 2);
        BO.setPriceAfterDiscount(priceAfterDiscount);
        BO.setDiscountedSetPricePerPeiece(Utils.roundSetPrecison((priceAfterDiscount / BO.getSetPeices()), 2));
        BO.setSetPrice(String.valueOf(Utils.roundSetPrecison((Double.valueOf(BO.getDisplayPrice()) * BO.getSetPeices() * (1 + (BO.getTaxPercentage() / 100))), 2)));
        String displayPrice = String.valueOf(Utils.roundSetPrecison((Double.valueOf(BO.getDisplayPrice()) * (1 + (BO.getTaxPercentage() / 100))), 2));
        BO.setDisplayPrice(displayPrice);
        /**
         * difference threshold can be changed according to requirement
         */
        if ((Double.valueOf(BO.getSetPrice()) - priceAfterDiscount) > 0.1) {
            BO.setSlashed(true);
        }
        //BO.setTaxPercentage(Utils.roundSetPrecison((BO.getTotalTax()/Double.valueOf(BO.getPriceBeforeTax()))*100,2));
        return BO;
    }

    public ProductInventoryBO addProductPrice(ProductInventoryBO bo, ProductView view, String serviceCalledFor) {

        if (view == null) {
            _logger.error("****************************************************");
            _logger.error(" ProductView is null that should not null ");
            _logger.error("****************************************************");
        }
        String url = "";
        if (serviceCalledFor != null && serviceCalledFor.equalsIgnoreCase("PendingAndFailedOrders")) {
            url = productPricingBaseUrl.concat("productpricefactor/").concat("pendingAndFailedOrders/");
        } else {
            url = productPricingBaseUrl.concat("productpricefactor/");
        }
        ResponseEntity<Map> response = restTemplate.getForEntity(
                url.concat("priceWithQuantity/" + view.getPcUuid() + "/1"), 60000,
                Map.class);
        Map<String, Object> priceResponse = response.getBody();
        List<Map<String, Object>> priceChangeFactors = new ArrayList<Map<String, Object>>();
        priceChangeFactors = (List<Map<String, Object>>) priceResponse.get("totalPriceChangeFactorNames");
        ProductPrice price = new ProductPrice();
        for (Map<String, Object> p : priceChangeFactors) {
            p.forEach((k, v) -> {
                if (k.equalsIgnoreCase("totalDiscountValue")) {
                    Double discountOnset = (Utils.roundSetPrecison((Double) v, 2));
                    price.setSupplierDiscountAppliedPerUnit((Utils.roundSetPrecison((Double) discountOnset / view.getSetPieces(), 2)));
                }
                if (k.equalsIgnoreCase("totalTaxValue")) {
                    price.setSupplierTaxApplied((Utils.roundSetPrecison((Double) v, 2)));
                }
                if (k.equalsIgnoreCase("totalDiscountpercent")) {
                    price.setApplicableDiscountPercent((Utils.roundSetPrecison((Double) v, 2)));
                }
                if (k.equalsIgnoreCase("totalTaxpercent")) {
                    price.setSupplierTaxPercent((Utils.roundSetPrecison((Double) v, 2)));
                }
            });
        }
        price.setIsSetPrice(view.getSetPieces() > 1);
        price.setMoq(view.getMinOrderQuantity());
        price.setSupplierSetPrice(view.getPrice().doubleValue());
        price.setSetPieces(view.getSetPieces());
        price.setProductMrp(view.getMrp().doubleValue());
        calculatePriceWithMargin(price);
        bo.setProductPrice(price);
        return bo;
    }


    public ProductPrice calculatePriceWithMargin(ProductPrice price) {
        if (price.getIsSetPrice()) {
            price.setSupplierPricePerUnit(price.getSupplierSetPrice() / price.getSetPieces());
        } else {
            price.setSupplierPricePerUnit(price.getSupplierSetPrice());
        }
        price.setPriceAfterSupplierDiscountPerUnit(
                price.getSupplierPricePerUnit() - price.getSupplierDiscountAppliedPerUnit());
        Double finalMarginedPrice = price.getPriceAfterSupplierDiscountPerUnit() * price.getProductMargin();
        Double finalTaxOnMarginedPricePerUnit = finalMarginedPrice * (price.getSupplierTaxPercent() / 100);
        Double finalPrice = finalMarginedPrice + finalTaxOnMarginedPricePerUnit;
        price.setSupplierTaxApplied(finalTaxOnMarginedPricePerUnit);
        price.setPriceAfterApplyingMarginPerUnit(finalMarginedPrice);
        price.setFinalPricePerUnit(finalPrice);
        price.setSetPriceAfterApplyingMargin((price.getPriceAfterApplyingMarginPerUnit()
                + finalTaxOnMarginedPricePerUnit) * price.getSetPieces());
        price.setMoqPrice(price.getSetPriceAfterApplyingMargin() * price.getMoq());

        _logger.info("price.getSetPriceAfterApplyingMargin()>>" + price.getSetPriceAfterApplyingMargin());
        _logger.info(" price.getSupplierDiscountAppliedPerUnit()>>" + price.getSupplierDiscountAppliedPerUnit());
        _logger.info("finalTaxOnMarginedPrice()>>" + finalTaxOnMarginedPricePerUnit);
        Double taxOnSupplierMarginedAmount = price.getSupplierPricePerUnit() * price.getProductMargin() * (price.getSupplierTaxPercent() / 100);
        price.setMarginedPriceWithoutDiscountWithTax(
                Utils.roundSetPrecison(
                        (price.getSupplierPricePerUnit() * price.getProductMargin() + taxOnSupplierMarginedAmount
                        ),
                        2));
        _logger.info("price.getSupplierDiscountAppliedPerUnit()>>" + price.getSupplierDiscountAppliedPerUnit());
        _logger.info(" price.getSetPriceAfterApplyingMargin()>>" + price.getSetPriceAfterApplyingMargin());

        price.setAppliedDiscountPercentageOnMarginPrice(Utils.roundSetPrecison(
                (((price.getMarginedPriceWithoutDiscountWithTax() - price.getFinalPricePerUnit()) * 100)
                        / price.getMarginedPriceWithoutDiscountWithTax()),
                2));
        price.setIsSlashedPrice(Utils.roundSetPrecison(price.getFinalPricePerUnit(), 2) < Utils.roundSetPrecison(price.getMarginedPriceWithoutDiscountWithTax(), 2));
        return price;
    }


    public ProductPrice calculatePriceWithMarginForCountryWise(ProductPrice defaultProductPriceproductPrice,
                                                               Double marginToApply) {

        ProductPrice price = new ProductPrice();
        try {
            price.setProductMargin(marginToApply);
            price.setSupplierDiscountAppliedPerUnit(
                    defaultProductPriceproductPrice.getSupplierDiscountAppliedPerUnit());
            price.setSupplierTaxApplied(defaultProductPriceproductPrice.getSupplierTaxApplied());
            price.setApplicableDiscountPercent(defaultProductPriceproductPrice.getSupplierDiscountPercentage());
            price.setSupplierTaxPercent(defaultProductPriceproductPrice.getSupplierTaxPercent());
            price.setIsSetPrice(defaultProductPriceproductPrice.getIsSetPrice());
            price.setSupplierSetPrice(defaultProductPriceproductPrice.getSupplierSetPrice());
            price.setSetPieces(defaultProductPriceproductPrice.getSetPieces());
            price.setProductMrp(defaultProductPriceproductPrice.getProductMrp() * price.getProductMargin());
            price.setMoq(defaultProductPriceproductPrice.getMoq());
            if (price.getIsSetPrice()) {
                price.setSupplierPricePerUnit(price.getSupplierSetPrice() / price.getSetPieces());
            } else {
                price.setSupplierPricePerUnit(price.getSupplierSetPrice());
            }
            price.setPriceAfterSupplierDiscountPerUnit(
                    price.getSupplierPricePerUnit() - price.getSupplierDiscountAppliedPerUnit());
            Double finalMarginedPrice = price.getPriceAfterSupplierDiscountPerUnit() * price.getProductMargin();
            Double finalTaxOnMarginedPricePerUnit = finalMarginedPrice * (price.getSupplierTaxPercent() / 100);
            Double finalPrice = finalMarginedPrice + finalTaxOnMarginedPricePerUnit;
            price.setSupplierTaxApplied(finalTaxOnMarginedPricePerUnit);
            price.setPriceAfterApplyingMarginPerUnit(finalMarginedPrice);
            price.setFinalPricePerUnit(finalPrice);
            price.setSetPriceAfterApplyingMargin((price.getPriceAfterApplyingMarginPerUnit()
                    + finalTaxOnMarginedPricePerUnit) * price.getSetPieces());

            price.setMoqPrice(price.getSetPriceAfterApplyingMargin() * price.getMoq());

            _logger.info("price.getSetPriceAfterApplyingMargin()>>" + price.getSetPriceAfterApplyingMargin());
            _logger.info(" price.getSupplierDiscountAppliedPerUnit()>>" + price.getSupplierDiscountAppliedPerUnit());
            _logger.info("finalTaxOnMarginedPrice()>>" + finalTaxOnMarginedPricePerUnit);
            Double taxOnSupplierMarginedAmount = price.getSupplierPricePerUnit() * price.getProductMargin() * (price.getSupplierTaxPercent() / 100);
            price.setMarginedPriceWithoutDiscountWithTax(
                    Utils.roundSetPrecison(
                            (price.getSupplierPricePerUnit() * price.getProductMargin() + taxOnSupplierMarginedAmount
                            ),
                            2));
            _logger.info("price.getSupplierDiscountAppliedPerUnit()>>" + price.getSupplierDiscountAppliedPerUnit());
            _logger.info(" price.getSetPriceAfterApplyingMargin()>>" + price.getSetPriceAfterApplyingMargin());

            price.setAppliedDiscountPercentageOnMarginPrice(Utils.roundSetPrecison(
                    (((price.getMarginedPriceWithoutDiscountWithTax() - price.getFinalPricePerUnit()) * 100)
                            / price.getMarginedPriceWithoutDiscountWithTax()),
                    2));

            price.setIsSlashedPrice(Utils.roundSetPrecison(price.getFinalPricePerUnit(), 2) < Utils.roundSetPrecison(price.getMarginedPriceWithoutDiscountWithTax(), 2));

        } catch (Exception e) {
            _logger.error("Error in calculatePriceWithMarginForCountryWise ", e);
        }
        return price;
    }

    public Map<String, Object> addDiscountAndTaxToProductInBulk(List<Map<String, Object>> RQ, String displayCountryCode) {
        _logger.debug("Inside addDiscountAndTaxToProductInBulk {}", RQ);
        Map<String, Object> RS = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("countrycode", displayCountryCode);
        HttpEntity<?> entity = new HttpEntity(RQ, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(_GetmultipleProductPrice, entity, 60000, Map.class);
        Map<String, Object> priceResponse = response.getBody();
        priceResponse.forEach((k, v) -> {
            Map<String, Object> res = (Map<String, Object>) v;
            RS.put((String) res.get("productCombination"), (Double) res.get("priceAfterDiscount"));
        });

        return RS;
    }

    public Map<String, Object> getMultipleImages(List<String> uuids) {
        _logger.debug("Inside addDiscountAndTaxToProductInBulk {}", uuids);
        Map<String, Object> RS = new HashMap<>();
        List<ImagesBO> images = new ArrayList<>();
        String GET_PRODUCT_IMAGE_URL = apiBaseUrl + "image-processing-service/static/getMultipleImagePaths";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(uuids, headers);
        ResponseEntity<Map> result = restTemplate.exchange(GET_PRODUCT_IMAGE_URL, HttpMethod.POST, entity,
                apiBaseUrlTimeout, Map.class);
        Map<String, Object> multipleImagePathsRS = (Map<String, Object>) result.getBody().get("data");
        multipleImagePathsRS.forEach((k, v) -> {
            try {
                List<Map<String, String>> dataArray = (List<Map<String, String>>) v;
                if (dataArray != null && dataArray.size() > 0) {
                    for (int index = 0; index < dataArray.size(); index++) {
                        Map<String, String> obj = dataArray.get(index);
                        String path = obj.get("completePath");
                        String type = obj.get("type");
                        ImagesBO imagesBO = new ImagesBO(path, type);
                        images.add(imagesBO);
                        if (type.equals("THUMBNAIL")) {
                            RS.put(k + "thumbnail", path);
                        }
                    }
                    RS.put(k, images);
                }

            } catch (Exception e) {
                _logger.error("Error while fetching getMultipleImagePaths  response ", e);
            }

        });

        return RS;
    }

    /**
     * @param uniqueIdentifier
     * @return List
     */

    public List getProductPriceBulkBuyForSupplier(String uniqueIdentifier) {

        _logger.info("Inside getProductPriceBulkBuyForSupplier {}", uniqueIdentifier);
        ResponseEntity<List> re = restTemplate.getForEntity(_GetProductPriceBulkBuyForSupplierUrl + uniqueIdentifier, _GetProductPriceBulkBuyForSupplierUrlTimeout, List.class);
        List apiRespone = re.getBody();
        return apiRespone;
    }

    /**
     * @param categoryRQ
     * @return Category BO
     */

    public Map<String, Object> addCategory(CategoryRQ categoryRQ) {

        _logger.info("Inside addCategory {}", new Gson().toJson(categoryRQ));
        Map<String, Object> responseMap = new HashMap<>();
        Category category = new Category();
        if (categoryRQ != null) {
            category.setCategoryName(categoryRQ.getCategoryName());
            category.setCategoryStage(categoryRQ.getCategoryStage());
            if (categoryRQ.getCategoryStage() == 1) {
                category.setCategoryIcon(categoryRQ.getCategoryIcon() + "#" + categoryRQ.getMainCategoryIcon());
                category.setParent(null);
                category.setNavigation(true);

                List<Category> categoryList = categoryRepository.findByParentId(null);
                for (Category cat : categoryList) {
                    if (cat.getCategoryName().toLowerCase().equals(categoryRQ.getCategoryName().toLowerCase())) {
                        responseMap.put("status", "failed");
                        responseMap.put("category", category);
                        return responseMap;
                    }
                }


            } else {

                category.setCategoryIcon(categoryRQ.getCategoryIcon());
                category.setParent(categoryRepository.findById(categoryRQ.getParentCategoryId()).get());
                category.setNavigation(false);
            }
            category = categoryRepository.save(category);
            restTemplate.exchange(cacheRefreshUrl, 60000);
            responseMap.put("status", "success");
            responseMap.put("category", category);
            return responseMap;
        }
        responseMap.put("status", "failed");
        responseMap.put("category", category);
        return responseMap;
    }

   /* public Category addCategory(CategoryRQ categoryRQ) {

        _logger.info("Inside addCategory {}", new Gson().toJson(categoryRQ));
        Category category = new Category();
        if (categoryRQ != null) {
            category.setCategoryName(categoryRQ.getCategoryName());
            category.setCategoryIcon(categoryRQ.getCategoryIcon());
            category.setCategoryStage(categoryRQ.getCategoryStage());
            if(categoryRQ.getCategoryStage()==1) {
            	category.setParent(null);
                category.setNavigation(true);


            	
            }else {
            	category.setParent(categoryRepository.findById(categoryRQ.getParentCategoryId()).get());
                category.setNavigation(false);
            }
            category = categoryRepository.save(category);
            restTemplate.exchange(cacheRefreshUrl, 60000);
            return category;
        }
        return category;
    }*/

    /**
     * @param supplierId
     * @param searchKeyword
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return Map<String, Object>
     * @throws UnsupportedEncodingException
     */

    public Map<String, Object> getProductOnSearch(long supplierId, String searchKeyword, String sortOrder,
                                                  Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) throws UnsupportedEncodingException {

        _logger.info("Inside getProductOnSearch {}", supplierId);

        Map<String, Object> pioBOListList = new HashMap<>();
        List<ProductInventoryBO> piBOList = new ArrayList<>();
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }
        searchKeyword = java.net.URLDecoder.decode(searchKeyword, StandardCharsets.UTF_8.name());
        List<ProductCombinations> pcList = productCombinationRepo
                .findByProductIdAndCombinationStringLikeAndProductDescriptionLikeAndSearchTermsLike(supplierId,
                        searchKeyword, searchKeyword, searchKeyword, pageable);
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());
        pcList.addAll(productCombinationRepo.findByItemNameLikeAndProductStatusNotIn(searchKeyword, psList, pageable));
        Integer totalCount = pcList.size();
        List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
        for (ProductCombinations productCom : pcList) {
            ProductInventoryBO piBO = new ProductInventoryBO();
            try {
                piBO = productUploadService.populateProductInventoryObj(productCom.getUniqueIdentifier(),
                        displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
            } catch (Exception e) {
                _logger.error("Error while creating product obj :{}", productCom.getUniqueIdentifier(), e);
            }
            List apiRespnose = getProductPriceBulkBuy(productCom.getUniqueIdentifier(), displayCountryCode);
            piBO.setPriceMatrix(apiRespnose);
            piBOList.add(piBO);
        }
        pioBOListList.put("data", piBOList);
        pioBOListList.put("pagedDataCount", piBOList.size());
        pioBOListList.put("TotalDataCount", totalCount);
        Integer startIndex = 0;
        if (pageNum != null && pageNum != 0) {
            startIndex = pageNum * pageSize + 1;
        }
        pioBOListList.put("SerialNumberStartIndex", startIndex);

        return pioBOListList;
    }

    /**
     * @param statusId
     * @param uuid
     * @return status of integer value
     */

    public int updateProductStatus(String statusId, String uuid, String qcFailedReason, Long adminOfQc) {


        _logger.info("Inside updateProductStatus {}", statusId);
        int isUpdated = 0;
        Date updatedOn = new Date(System.currentTimeMillis());
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<>();
        psList.add(productStatus.get());
        ProductCombinations pc = productCombinationRepo.findByUniqueIdentifierAndProductStatusNotIn(uuid, psList);
        Optional<ProductStatus> ps = productStatusRepository.findById(StatusConstant.Inactive);
        if (pc.getSupplier() != null && pc.getSupplier().isActive()) {

            if (statusId.equalsIgnoreCase("1")) {
                isUpdated = productCombinationRepo.updateQCStatus(StatusConstant.Active, uuid, true, updatedOn, qcFailedReason, adminOfQc);
            } else if (statusId.equalsIgnoreCase("5")) {
                isUpdated = productCombinationRepo.updateQCStatus(StatusConstant.QC_RejectedByAdmin, uuid, false, updatedOn, qcFailedReason, adminOfQc);
            } else if (statusId.equalsIgnoreCase("3")) {
                pc.setActive(false);
                pc.setStatus(ps.get());
                pc.setUpdatedOn(new Date());
                productCombinationRepo.save(pc);
                isUpdated = 1;
            }
        } else {
            productCombinationRepo.updateQCStatus(StatusConstant.Inactive, uuid, false, updatedOn, qcFailedReason, adminOfQc);
            isUpdated = 0;
        }
       bulkservice.pushProductToKafka(uuid, "IN", "INR");
		
        return isUpdated;
    }
    
    public int updateProductStock( String uuid, int changedStock) {


        _logger.info("Inside updateProductStock {}"+ uuid);
        int isUpdated = productStockService.selectAndUpdateStock(uuid, changedStock);
         bulkservice.pushProductToKafka(uuid, "IN", "INR");
		
        return isUpdated;
    }


    /**
     * @param Searchterms
     * @param uuid
     * @return status of integer value
     */

    public int updateproductbysearchterms(String Searchterms, String uuid) {
        _logger.info("Inside updateproductbysearchterms {}", uuid);
        int isupdated = 0;

        isupdated = productCombinationRepo.updateproductbysearchterms(uuid, Searchterms);
        return isupdated;
    }

    /**
     * @param searchKeyword
     * @param searchColumn
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @param displayCurrencyCode
     * @param displayCountryCode
     * @return Map<String, Object>
     * @throws UnsupportedEncodingException
     */

    public ResponseEntity<?> getProductOnSearchByColumns(String searchKeyword, String searchColumn, String sortOrder,
                                                         Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) throws UnsupportedEncodingException {

        _logger.info("Inside getProductOnSearchByColumns {}", searchKeyword);

        Map<String, Object> pioBOListList = new HashMap<>();
        List<ProductInventoryBO> piBOList = new ArrayList<>();
        List<ProductCombinations> pcList = new ArrayList<>();
        Integer totalCount = 0;
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }
        searchKeyword = java.net.URLDecoder.decode(searchKeyword, StandardCharsets.UTF_8.name());
        if (searchColumn.equalsIgnoreCase("productId")) {
            pcList = productCombinationRepo.findByUniqueIdentifier(searchKeyword, pageable);
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        } else if (searchColumn.equalsIgnoreCase("supplierName")) {
            List<Supplier> supplier = supplierRepo.findBySupplierNameContainingIgnoreCase(searchKeyword);

            pcList = productCombinationRepo.findBySupplierInAndProductStatusNotIn(supplier, psList, pageable);
            pcList.removeIf(Objects::isNull);
            for (Supplier s : supplier) {
                totalCount = totalCount + Integer.parseInt(productCombinationRepo.countBySupplierProducts(s.getId(), psList.get(0).getId()) + "");
            }


        } else if (searchColumn.equalsIgnoreCase("supplierId")) {
            try {
                Supplier supplier = supplierRepo.findById(Long.valueOf(searchKeyword)).get();
                pcList = productCombinationRepo.findBySupplierAndProductStatusNotIn(supplier, psList, pageable);
                pcList.removeIf(Objects::isNull);
                totalCount = Integer.parseInt(productCombinationRepo.countBySupplierProducts(supplier.getId(), psList.get(0).getId()) + "");
            } catch (Exception e) {
                totalCount = 0;

            }


        } else if (searchColumn.equalsIgnoreCase("productName")) {
			/*pcList = productCombinationRepo
					.findByProductIdAndCombinationStringLikeAndProductDescriptionLikeAndSearchTermsLike(searchKeyword,
							searchKeyword, searchKeyword, pageable);*/
            pcList.addAll(productCombinationRepo.findByItemNameLikeAndProductStatusNotIn(searchKeyword, psList, pageable));
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        } else if (searchColumn.equalsIgnoreCase("SKU")) {
            try {
                pcList.addAll(productCombinationRepo.findBySkuAndProductStatusNotIn(searchKeyword, psList, pageable));
                pcList.removeIf(Objects::isNull);
                totalCount = pcList.size();

            } catch (Exception e) {

            }
        }

        if (pcList.isEmpty()) {
            EmptySearchFormat emptySearchFormat = new EmptySearchFormat();
            emptySearchFormat.setData(new Object[]{});
            RestApiSuccessResponse errorResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(), "Product not found", emptySearchFormat);
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        } else {
            List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
            Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
            for (ProductCombinations productCom : pcList) {
                ProductInventoryBO piBO = new ProductInventoryBO();
                try {
                    piBO = productUploadService.populateProductInventoryObj(productCom.getUniqueIdentifier(),
                            displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
                } catch (Exception e) {
                    _logger.error("Error while creating product obj :{}", productCom.getUniqueIdentifier(), e);
                }
                List apiRespnose = getProductPriceBulkBuy(productCom.getUniqueIdentifier(), displayCountryCode);
                piBO.setPriceMatrix(apiRespnose);
                piBOList.add(piBO);
            }
            pioBOListList.put("data", piBOList);
            pioBOListList.put("pagedDataCount", piBOList.size());
            pioBOListList.put("TotalDataCount", totalCount);
            Integer startIndex = 0;
            if (pageNum != null && pageNum != 0) {
                startIndex = pageNum * pageSize + 1;
            }
            pioBOListList.put("SerialNumberStartIndex", startIndex);

            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "product found", pioBOListList);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }


    }

    @Autowired
    private ProductStockService stockService;

    /**
     * @param supplierId
     * @return String of Map
     */

    public String deactivateSupplierAndSupplierProduct(Long supplierId) {

        _logger.info("Inside deactivateSupplierAndSupplierProduct {}", supplierId);
        Optional<ProductStatus> ps = productStatusRepository.findById(StatusConstant.Inactive);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(ps.get());
        Supplier supplier = supplierRepo.findById(supplierId).get();
        List<ProductCombinations> pcList = productCombinationRepo.findBySupplierAndProductStatusNotIn(supplier, psList, null);
        pcList.stream().forEach(pc -> {
            pc.setActive(false);
            pc.setStatus(ps.get());
            pc.setUpdatedOn(new Date());
            RemoveProductBO bo = RemoveProductBO.builder().productCombinationId(pc.getUniqueIdentifier())
                    .availableStock(pc.getAvailableStock()).removeProductFromList(true).build();
            stockService.sendToKafka(bo);
            productCombinationRepo.save(pc);
        });
        supplier.setUpdatedOn(new Date());
        supplier.setActive(false);
        supplierRepo.save(supplier);
        JSONObject resp = new JSONObject();
        resp.put("data", "Supplier and SupllierProduct deactivated");
        resp.put("supplierId", supplierId);
        resp.put("totalProductDeactivated", pcList.size());
        return resp.toString();
    }

    public String updateAverageRating(String uniqueIdentifier) {


        _logger.info("Inside updateAverageRating:: {}", uniqueIdentifier);
        String msg = "";

        try {
            Double avgRating = getAverageRating(uniqueIdentifier);
            Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
            List<ProductStatus> psList = new ArrayList<>();
            psList.add(productStatus.get());
            ProductCombinations productCombinations = productCombinationRepo.findByUniqueIdentifierAndProductStatusNotIn(uniqueIdentifier, psList);
            productCombinations.setAverageRating(avgRating);
            productCombinations.setUpdatedOn(new Date());
            productCombinationRepo.save(productCombinations);

        } catch (Exception e) {
            msg = "update fail";
            e.printStackTrace();
            _logger.error("update fail::" + e);
        }
        msg = "update done";
        return msg;
    }

    public Double getAverageRating(String uniqueIdentifier) {

        _logger.info("Inside getFgetAverageRatingileUrl:: {}", uniqueIdentifier);
        Map resMap = null;
        Double avgRating = null;
        ResponseEntity<Map> re = restTemplate
                .getForEntity(_GetAverageRatingUrl + uniqueIdentifier, _GetAverageRatingUrlTimeout, Map.class);
        resMap = re.getBody();
        avgRating = (Double) resMap.get("AverageRating");
        return avgRating;
    }

    public Object getFileUrl(String categoryName) {
        _logger.info("Inside getFileUrl:: {}", categoryName);
        String fileUrl = "";
        try {
            CategoryExcel categoryExecl = categoryExeclRepo.findOneByCategoryName(categoryName);
            fileUrl = categoryExecl.getFileUrl();

        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("Inside getFileUrl", e);
        }
        return fileUrl;
    }


    private CurrencyEntity getCurrencyData(String displayCurrencyCode, long displayCurrencyId) {

        _logger.info("Inside getCurrencyData displayCurrencyCode {}", displayCurrencyCode);
        CurrencyEntity currencyEntity = new CurrencyEntity();
        String currencyCodeUrl = _GetCurrencyCodeUrl + displayCurrencyId;
        String currencyIdUrl = _GetCurrencyIdUrl + displayCurrencyCode;

        try {
            currencyEntity.setCurrencyCode(displayCurrencyCode);
            currencyEntity.setCurrencyId(displayCurrencyId);

            if (StringUtils.isBlank(displayCurrencyCode)) {
                ResponseEntity<Map> res = restTemplate.getForEntity(currencyCodeUrl, _GetCurrencyCodeUrlTimeout, Map.class);
                if (res != null && res.getBody() != null) {
                    LinkedHashMap<String, Object> rsMap = (LinkedHashMap) (res.getBody());
                    String currencyCode = ((String) rsMap.get("data"));
                    currencyEntity.setCurrencyCode(currencyCode);

                    _logger.info("exiting  getCurrencyData {}", displayCurrencyCode);
                }
            } else if (StringUtils.isNotBlank(displayCurrencyCode)) {

                ResponseEntity<Map> res = restTemplate.getForEntity(currencyIdUrl, _GetCurrencyIdUrlTimeout, Map.class);
                if (res != null && res.getBody() != null) {
                    LinkedHashMap<String, Object> rsMap = (LinkedHashMap) (res.getBody());
                    long currencyId = ((Long) rsMap.get("data"));
                    currencyEntity.setCurrencyId(currencyId);

                    _logger.info("exiting  getCurrencyData {}", displayCurrencyId);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyEntity;
    }

    public List<ProductCacheBO> getMultiProductDetails(List<String> UUIDs, String displayCountryCode, String displayCurrencyCode) {

        _logger.info("Inside getMultiProductDetails");
        List<ProductCacheBO> productList = new ArrayList<>();
        UUIDs.forEach(UUID -> {
            try {
                productList.add(productCacheService.getProductFromCache(UUID, displayCountryCode, displayCurrencyCode));
            } catch (Exception e) {
                _logger.error("ERROR while fetching multiple product detail  for uuids {} is {}", UUIDs.toString(), e);
            }
        });
        return productList;
    }

    public SimilarProductBO similarProductMapper(String uuid) throws InventoryException {

        ProductCacheBO cacheBo = productCacheService.getProductFromCache(uuid, "IN", "INR");

        return SimilarProductBO.builder().brandModel(cacheBo.getBrandName()).brandName(cacheBo.getBrandName())
                .categoryName(cacheBo.getCategoryName()).thumbnailURL(cacheBo.getThumbnailURL())
                .inStock(Integer.valueOf(cacheBo.getAvailabilityCount()) > 0)
                .isPremiumProduct(cacheBo.getIsPremiumBrand()).itemName(cacheBo.getItemName()).productClubingId(cacheBo.getProductClubingId())
                .uniqueIdentifierForSearch(cacheBo.getUniqueIdentifierForSearch()).build();
    }

    public ProductInventoryBO applyMarginCOuntryWise(ProductInventoryBO BO) {

        ResponseEntity<RestApiSuccessResponse> result = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        Map<String, ProductPrice> countryWisePrice = new HashMap<>();
        try {
            result = restTemplate.exchange(_pricingAllMarginUrl, HttpMethod.GET, entity, apiBaseUrlTimeout,
                    RestApiSuccessResponse.class);
            final ObjectMapper mapper = new ObjectMapper();
            List<CountryMarginBO> marginListCountryWise = Arrays
                    .asList(mapper.convertValue(result.getBody().getData(), CountryMarginBO[].class));

            for (int i = 0; i < marginListCountryWise.size(); i++) {
                try {
                    _logger.info("Entered to apply margin with size {}", marginListCountryWise.size());
                    List<MarginBO> margin = (List<MarginBO>) marginListCountryWise.get(i).getMarginList();
                    if (null != margin) {
                        MarginBO defaultMargin = margin.stream().filter(m -> m.getIsDefault()).findAny().get();

                        MarginBO singleProductMargin = null;
                        Optional<MarginBO> singleProductMarginOp = margin.stream()
                                .filter(m -> StringUtils.isNotBlank(m.getProductId())
                                        && m.getProductId().equalsIgnoreCase(BO.getUniqueIdentifierForSearch()))
                                .findAny();
                        if (singleProductMarginOp.isPresent()) {
                            singleProductMargin = singleProductMarginOp.get();
                        }

                        MarginBO supplierProductMargin = null;

                        //					Optional<MarginBO> supplierProductMarginOp = margin.stream().filter(m1->
                        //							ObjectUtils.isNotEmpty(m1.getSupplierId()) && BO.getSupplierId().equals(m1.getSupplierId()))
                        //							.findAny();

                        for (MarginBO m1 : margin) {
                            if (null != m1 && ObjectUtils.isNotEmpty(m1.getSupplierId()) && BO.getSupplierId().equals(m1.getSupplierId())) {
                                _logger.info("margin supplier " + m1.getSupplierId() + "  product supplier " + BO.getSupplierId());
                                supplierProductMargin = m1;
                            }

                        }

                        //					if(supplierProductMarginOp.isPresent()) {
                        //						supplierProductMargin=supplierProductMarginOp.get();
                        //					}

                        MarginBO categoryProductMargin = null;

                        //					Optional<MarginBO> categoryProductMarginOp = margin.stream()
                        //							.filter(m -> StringUtils.isNotBlank(m.getCategoryName())
                        //									&& m.getCategoryName().equalsIgnoreCase(BO.getCategoryName()))
                        //							.findAny();


                        for (MarginBO m1 : margin) {
                            if (null != m1 && StringUtils.isNotBlank(m1.getCategoryName())
                                    && m1.getCategoryName().equalsIgnoreCase(BO.getCategoryName())) {
                                _logger.info("margin getCategoryName {}  product getCategoryName {}", m1.getCategoryName(), BO.getCategoryName());
                                categoryProductMargin = m1;
                            }

                        }

                        //					if(categoryProductMarginOp.isPresent()) {
                        //						categoryProductMargin=categoryProductMarginOp.get();
                        //					}

                        ProductPrice defaultProductPrice = BO.getProductPrice();
                        if (null != singleProductMargin) {
                            addCountryPrice(defaultProductPrice, countryWisePrice, marginListCountryWise.get(i).getCountryCode(), BO,
                                    singleProductMargin);
                        } else if (null != supplierProductMargin) {
                            addCountryPrice(defaultProductPrice, countryWisePrice, marginListCountryWise.get(i).getCountryCode(), BO,
                                    supplierProductMargin);
                        } else if (null != categoryProductMargin) {
                            addCountryPrice(defaultProductPrice, countryWisePrice, marginListCountryWise.get(i).getCountryCode(), BO,
                                    categoryProductMargin);

                        } else {
                            /**
                             * if margin for product is not available for the country then margin for all be applicable for that country
                             */
                            if (!countryWisePrice
                                    .containsKey("All")) {
                                addCountryPrice(defaultProductPrice, countryWisePrice, marginListCountryWise.get(i).getCountryCode(), BO,
                                        defaultMargin);
                            } else {
                                countryWisePrice.put(marginListCountryWise.get(i).getCountryCode(), countryWisePrice.get("All"));
                            }
                        }
                    }
                } catch (Exception e) {
                    _logger.error("Exception applyMarginCOuntryWise in outer stream", e);
                }

            }

            BO.setCountryWisePrice(countryWisePrice);

        } catch (Exception e) {
            _logger.error("Exception applyMarginCOuntryWise", e);
        }

        return BO;
    }

    private void addCountryPrice(ProductPrice price, Map<String, ProductPrice> countryWisePrice,
                                 String countryCode, ProductInventoryBO BO, MarginBO m) {
        try {
            Double marginToApply = Double.valueOf(1);
            if (null != m) {
                if (!StringUtils.isBlank(countryCode) && !countryCode.equalsIgnoreCase("IN")) {
                    marginToApply = m.getInternationalMargin();
                } else {
                    marginToApply = m.getMargin();
                }
                ProductPrice countryWiseProductPrice = calculatePriceWithMarginForCountryWise(price, marginToApply);
                countryWiseProductPrice.setMarginCountry((StringUtils.isNotBlank(countryCode) ? countryCode : "All"));
                countryWisePrice.put(StringUtils.isNotBlank(countryCode) ? countryCode : "All", countryWiseProductPrice);
                BO.getPriceMatrix().stream().forEach(pm -> {
                    pm.setLowerLimitSavingAmount(String.valueOf(Double.valueOf(pm.getLowerLimitSavingAmount()) * m.getMargin()));
                    pm.setUpperLimitSavingAmount(String.valueOf(Double.valueOf(pm.getUpperLimitSavingAmount()) * m.getMargin()));
                });
            }
        } catch (Exception e) {
            _logger.error("Error in addCountryPrice for product " + BO.getUniqueIdentifierForSearch(), e);
        }
    }

    public int updateProductCombinationBasedonUpdatedKbMrgin(List<Map<String, List<String>>> kbMarginUpdatedMapList) {
        _logger.debug("Enetered updateProductCombinationBasedonUpdatedKbMrgin for scheduler operation with input {}", kbMarginUpdatedMapList);
        int isUpdated = 0;
        try {
            for (Map<String, List<String>> kbarginUpdatedMap : kbMarginUpdatedMapList) {
                if (kbarginUpdatedMap.containsKey("lowestCategoryList")) {
                    List<String> lowestCategoryList = kbarginUpdatedMap.get("lowestCategoryList");
                    if (!lowestCategoryList.isEmpty()) {
                        List<ProductView> viewList = productViewRepository.findByLowestCategoryNameIn(lowestCategoryList);
                        List<String> pcUUIDList = viewList.stream().map(v -> v.getPcUuid()).collect(Collectors.toList());
                        if (!pcUUIDList.isEmpty()) {
                            productCombinationRepo.updateUpdatedOnDateBasedPCUUId(pcUUIDList);
                        }
                    }
                } else if (kbarginUpdatedMap.containsKey("supplierIdList")) {
                    List<String> supplierIdList = kbarginUpdatedMap.get("supplierIdList");
                    if (!supplierIdList.isEmpty()) {

                        List<Long> newList = supplierIdList.stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());

                        List<Supplier> suppliersList = supplierRepo.findAllByIdList(newList);
                        if (!suppliersList.isEmpty()) {
                            productCombinationRepo.updateUpdatedOnDateBasedProducts(suppliersList);
                        }
                    }
                } else if (kbarginUpdatedMap.containsKey("pcUUIdList")) {
                    List<String> pcUUIDList = kbarginUpdatedMap.get("pcUUIdList");
                    if (!pcUUIDList.isEmpty()) {
                        productCombinationRepo.updateUpdatedOnDateBasedPCUUId(pcUUIDList);
                    }
                }
            }
            isUpdated = 1;
        } catch (Exception e) {
            _logger.error("Exception while updating updated_on column using kbMargin schduler", e);
            isUpdated = 0;
        }
        return isUpdated;
    }


    public List<ProductInventoryBO> getProductsOnSku(List<String> skuList) {
        List<ProductCombinations> pcList = productCombinationRepo.findBySkuIn(skuList);
        final List<ProductInventoryBO> allProductDetails = new ArrayList<>();
        List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
        for (ProductCombinations pc : pcList) {
            try {
                allProductDetails.add(productUploadService.populateProductInventoryObj(pc.getUniqueIdentifier(),
                        "IN", "INR", bestSellingProductCountDetailMap, null, "0", false));
            } catch (Exception e) {
                _logger.error("Error while creating product obj :{}", pc.getUniqueIdentifier(), e);
            }
        }
        return allProductDetails;
    }

    public List<ProductInventoryBO> getProductsOnSkuAdmin(List<String> skuList) {
        List<ProductCombinations> pcList = productCombinationRepo.findBySkuIn(skuList);
        final List<ProductInventoryBO> allProductDetails = new ArrayList<>();
        List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
        for (ProductCombinations pc : pcList) {
            ProductInventoryBO bo = null;
            try {
                bo = productUploadService.populateProductInventoryObj(pc.getUniqueIdentifier(),
                        "IN", "INR", bestSellingProductCountDetailMap, null, "0", false);
            } catch (Exception e) {
                _logger.error("Error while creating product obj :{}", pc.getUniqueIdentifier(), e);
            }
            if (null != bo) {
                bo.setDescription(StringUtils.replace(bo.getDescription(), "\\n", ""));
                bo.setProductDescription(StringUtils.replace(bo.getProductDescription(), "\\n", ""));
                allProductDetails.add(bo);
            }
        }
        return allProductDetails;
    }

    public PremiumBrandDetails getProductPremiumDetailsBySupplierIdAndContact(String contact,
                                                                              Long supplierId) {
        try {
            return productUploadService.getProductPremiumDetailsBySupplierIdAndContact(contact, supplierId);
        } catch (Exception e) {
            _logger.error("Exception occurred in getProductPremiumDetailsBySupplierIdAndContact", e);
            return null;
        }

    }

    public Map<Long, PremiumBrandDetails> getProductPremiumDetailsBySupplierIdsAndContact(String contact,
                                                                                          SupplierListRQ request) {
        try {
            return productUploadService.getProductPremiumDetailsBySupplierIdsAndContact(contact, request);
        } catch (Exception e) {
            _logger.error("Exception occurred in getProductPremiumDetailsBySupplierIdsAndContact", e);
            return null;
        }

    }

    public int updateProductStatusInBulk(List<ProductQcBo> qcProducts) {
        AtomicInteger isUpdated = new AtomicInteger(0);
        qcProducts.stream().forEach(qcp -> {
            isUpdated.set(updateProductStatus(qcp.getStatus(), qcp.getProductUUID(), qcp.getRejectReason(), qcp.getAdminOfQc()));
        });

        return isUpdated.get();
    }
    
    public int updateProductStockInBulk(List<ProductQcBo> qcProducts) {
        AtomicInteger isUpdated = new AtomicInteger(0);
        qcProducts.stream().forEach(qcp -> {
            isUpdated.set(updateProductStock(qcp.getProductUUID(), qcp.getIncreaseStockBy()));
        });

        return isUpdated.get();
    }


    public ResponseEntity<?> getProductOnSearchBySupplierIdAndStatus(String supplierId, Integer status, String sortOrder,
                                                                     Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) throws UnsupportedEncodingException {

        _logger.info("Inside getProductOnSearchBySupplierIdAndStatus with supplierId and productStatus {} {}", supplierId, status);

        Map<String, Object> pioBOListList = new HashMap<>();
        List<ProductInventoryBO> piBOList = new ArrayList<>();
        List<ProductCombinations> pcList = new ArrayList<>();
        Integer totalCount = 0;
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }
        if (StringUtils.isNotBlank(supplierId) && status != null) {
            if (status == 10) {
                pcList = productCombinationRepo.findBySupplierId(supplierId, pageable);
            } else {
                pcList = productCombinationRepo.findByStatusAndSupplierId(supplierId, status, pageable);
            }
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        } else if (StringUtils.isNotBlank(supplierId) && status.equals(StatusConstant.All_Product)) {
            pcList = productCombinationRepo.findBySupplierId(supplierId, pageable);
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        } else if (status != null) {
            pcList = productCombinationRepo.findByStatus(status, pageable);
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        } else if (status == 10) {
            pcList = productCombinationRepo.getAllProducts(pageable);
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        }

        if (pcList.isEmpty()) {
            EmptySearchFormat emptySearchFormat = new EmptySearchFormat();
            emptySearchFormat.setData(new Object[]{});
            RestApiSuccessResponse errorResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(), "Product not found", emptySearchFormat);
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        } else {
            List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
            Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
            for (ProductCombinations productCom : pcList) {
                ProductInventoryBO piBO = new ProductInventoryBO();
                try {
                    piBO = productUploadService.populateProductInventoryObj(productCom.getUniqueIdentifier(),
                            displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
                } catch (Exception e) {
                    _logger.error("Error while creating product obj in seach by supplierAndStatus:{}", productCom.getUniqueIdentifier(), e);
                }
                List apiRespnose = getProductPriceBulkBuy(productCom.getUniqueIdentifier(), displayCountryCode);
                piBO.setPriceMatrix(apiRespnose);
                piBOList.add(piBO);
            }
            pioBOListList.put("data", piBOList);
            pioBOListList.put("pagedDataCount", piBOList.size());
            pioBOListList.put("TotalDataCount", totalCount);
            Integer startIndex = 0;
            if (pageNum != null && pageNum != 0) {
                startIndex = pageNum * pageSize + 1;
            }
            pioBOListList.put("SerialNumberStartIndex", startIndex);

            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "product found", pioBOListList);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
    }

    public List<Map<String, Object>> getAllProducts(String sortOrder, Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) {

        _logger.info("Inside getAllProducts");


        long t1 = System.currentTimeMillis();

        List<Map<String, Object>> resMapList = new ArrayList<>();
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "pc_id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("pc_id"));
        }
        List<ProductCacheBO> piBOList = new ArrayList<>();

        List<String> uuidList = productViewRepository.findNonDeletedUniqueUuuids(pageable);

        uuidList.forEach(uuid -> {
            long t3 = System.currentTimeMillis();
            //ProductCacheBO piBO = new ProductCacheBO();
            ProductCacheBO piBO = productCacheService.getProductFromCache(uuid, displayCountryCode, displayCurrencyCode);
            if (piBO != null) {
                piBOList.add(piBO);
            }
            long t4 = System.currentTimeMillis();
            _logger.info("Total time taken for one product in get all product for uuid {} is {}", uuid, (t4 - t1));
        });
        //long totalCount=productViewRepository.count();
        long totalCount = productViewRepository.countOfNonDeletedProducts();
        _logger.error("TotalDataCount getAllProduct  {}", totalCount);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("data", piBOList);
        resMap.put("TotalDataCount", totalCount);
        resMap.put("PagedDataCount", piBOList.size());
        Integer startIndex = 0;
        if (pageNum != null && pageNum != 0) {
            startIndex = pageNum * pageSize + 1;
        }
        resMap.put("SerialNumberStartIndex", startIndex);
        resMapList.add(resMap);


        long t2 = System.currentTimeMillis();
        _logger.info("Total time taken before exiting {}", (t2 - t1));
        return resMapList;
    }

    public ResponseEntity<?> getAllProductOnSearchBySupplierAndStatus(String searchBy, String searchValue, Integer status, String sortOrder,
                                                                      Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode, String searchStock) throws UnsupportedEncodingException {

        _logger.info("Inside getProductOnSearchBySupplierIdAndStatus with searchBy and searchValue {} {}", searchBy, searchValue);

        Map<String, Object> pioBOListList = new HashMap<>();
        List<String> pcList = new ArrayList<>();
        Integer totalCount = 0;
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());

        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "pc_id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("pc_id"));
        }
        if (StringUtils.isEmpty(searchValue) || searchValue == null) {
            if (searchStock.equals("allStock")) {
                if (status == 10) {
                    pcList = productViewForAdminRepo.findAllProducts(pageable);
                    pcList.removeIf(Objects::isNull);
                    totalCount = productViewForAdminRepo.countAllProducts();
                } else {
                    pcList = productViewForAdminRepo.findByStatusId(status, pageable);
                    pcList.removeIf(Objects::isNull);
                    totalCount = productViewForAdminRepo.countByStatusId(status);
                }
            }
            if (searchStock.equals("lowStock")) {
                if (status == 10) {
                    pcList = productViewForAdminRepo.findAllProductsWithLowStock(pageable);
                    pcList.removeIf(Objects::isNull);
                    totalCount = productViewForAdminRepo.countAllProductsWithLowStock();
                } else {
                    pcList = productViewForAdminRepo.findByStatusIdWithLowStock(status, pageable);
                    pcList.removeIf(Objects::isNull);
                    totalCount = productViewForAdminRepo.countByStatusIdWithLowStock(status);
                }
            }
            if (searchStock.equals("zeroStock")) {
                if (status == 10) {
                    pcList = productViewForAdminRepo.findAllProductsWithZeroStock(pageable);
                    pcList.removeIf(Objects::isNull);
                    totalCount = productViewForAdminRepo.countAllProductsWithZeroStock();
                } else {
                    pcList = productViewForAdminRepo.findByStatusIdWithZeroStock(status, pageable);
                    pcList.removeIf(Objects::isNull);
                    totalCount = productViewForAdminRepo.countByStatusIdWithZeroStock(status);
                }
            }
        } else {
            if (StringUtils.isNotBlank(searchBy) && status != null) {
                if (searchBy.equals("supplierName")) {
                    if (searchStock.equals("allStock")) {
                        if (status == 10) {
                            pcList = productViewForAdminRepo.findBySupplierName(searchValue, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierName(searchValue);
                        } else {
                            pcList = productViewForAdminRepo.findBySupplierNameAndStatusId(searchValue, status, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierNameAndStatusId(searchValue, status);
                        }
                    }
                    ;
                    if (searchStock.equals("lowStock")) {
                        if (status == 10) {
                            pcList = productViewForAdminRepo.findBySupplierNameWithLowStock(searchValue, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierNameWithLowStock(searchValue);
                        } else {
                            pcList = productViewForAdminRepo.findBySupplierNameAndStatusIdWithLowStock(searchValue, status, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierNameAndStatusWithLowStock(searchValue, status);
                        }
                    }
                    if (searchStock.equals("zeroStock")) {
                        if (status == 10) {
                            pcList = productViewForAdminRepo.findBySupplierNameWithZeroStock(searchValue, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierNameWithZeroStock(searchValue);
                        } else {
                            pcList = productViewForAdminRepo.findBySupplierNameAndStatusIdWithZeroStock(searchValue, status, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierNameAndStatusIdWithZeroStock(searchValue, status);
                        }
                    }
                } else if (searchBy.equals("supplierId")) {
                    if (searchStock.equals("allStock")) {
                        if (status == 10) {
                            pcList = productViewForAdminRepo.findBySupplierId(searchValue, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierId(searchValue);
                        } else {
                            pcList = productViewForAdminRepo.findBySupplierIdAndStatusId(searchValue, status, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierIdAndStatusId(searchValue, status);
                        }
                    }
                    if (searchStock.equals("lowStock")) {
                        if (status == 10) {
                            pcList = productViewForAdminRepo.findBySupplierIdWithLowStock(searchValue, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierIdWithLowStock(searchValue);
                        } else {
                            pcList = productViewForAdminRepo.findBySupplierIdAndStatusIdWithLowStock(searchValue, status, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierIdAndStatusIdWithLowStock(searchValue, status);
                        }
                    }
                    if (searchStock.equals("zeroStock")) {
                        if (status == 10) {
                            pcList = productViewForAdminRepo.findBySupplierIdWithZeroStock(searchValue, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierIdWithZeroStock(searchValue);
                        } else {
                            pcList = productViewForAdminRepo.findBySupplierIdAndStatusIdWithZeroStock(searchValue, status, pageable);
                            pcList.removeIf(Objects::isNull);
                            totalCount = productViewForAdminRepo.countBySupplierIdAndStatusIdWithZeroStock(searchValue, status);
                        }
                    }

                } else {
                    pcList = searchProductOnSearchByColumns(searchValue, searchBy, sortOrder, pageNum, pageSize, displayCountryCode, displayCurrencyCode);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                }


            } else if (StringUtils.isNotBlank(searchBy)) {
                if (searchBy.equals("supplierName")) {
                    if (searchStock.equals("allStock")) {
                        pcList = productViewForAdminRepo.findBySupplierName(searchValue, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countBySupplierName(searchValue);
                    }
                    if (searchStock.equals("lowStock")) {
                        pcList = productViewForAdminRepo.findBySupplierNameWithLowStock(searchValue, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countBySupplierNameWithLowStock(searchValue);
                    }
                    if (searchStock.equals("zeroStock")) {
                        pcList = productViewForAdminRepo.findBySupplierNameWithZeroStock(searchValue, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countBySupplierNameWithZeroStock(searchValue);
                    }
                } else if (searchBy.equals("supplierId")) {
                    if (searchStock.equals("allStock")) {
                        pcList = productViewForAdminRepo.findBySupplierId(searchValue, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countBySupplierId(searchValue);
                    }
                    if (searchStock.equals("lowStock")) {
                        pcList = productViewForAdminRepo.findBySupplierIdWithLowStock(searchValue, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countBySupplierIdWithLowStock(searchValue);
                    }
                    if (searchStock.equals("zeroStock")) {
                        pcList = productViewForAdminRepo.findBySupplierIdWithZeroStock(searchValue, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countBySupplierIdWithZeroStock(searchValue);
                    }

                } else {
                    pcList = searchProductOnSearchByColumns(searchValue, searchBy, sortOrder, pageNum, pageSize, displayCountryCode, displayCurrencyCode);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                }
            } else if (status != null) {
                if (searchStock.equals("allStock")) {
                    if (status == 10) {
                        pcList = productViewForAdminRepo.findAllProducts(pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countAllProducts();
                    } else {
                        pcList = productViewForAdminRepo.findByStatusId(status, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countByStatusId(status);
                    }
                }
                if (searchStock.equals("lowStock")) {
                    if (status == 10) {
                        pcList = productViewForAdminRepo.findAllProductsWithLowStock(pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countAllProductsWithLowStock();
                    } else {
                        pcList = productViewForAdminRepo.findByStatusIdWithLowStock(status, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countByStatusIdWithLowStock(status);
                    }
                }
                if (searchStock.equals("zeroStock")) {
                    if (status == 10) {
                        pcList = productViewForAdminRepo.findAllProductsWithZeroStock(pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countAllProductsWithZeroStock();
                    } else {
                        pcList = productViewForAdminRepo.findByStatusIdWithZeroStock(status, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countByStatusIdWithZeroStock(status);
                    }
                }
            } else if (searchBy == null && status != null) {
                if (searchStock.equals("allStock")) {
                    if (status == 10) {
                        pcList = productViewForAdminRepo.findAllProducts(pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countAllProducts();
                    } else {
                        pcList = productViewForAdminRepo.findByStatusId(status, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countByStatusId(status);
                    }
                }
                if (searchStock.equals("lowStock")) {
                    if (status == 10) {
                        pcList = productViewForAdminRepo.findAllProductsWithLowStock(pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countAllProductsWithLowStock();
                    } else {
                        pcList = productViewForAdminRepo.findByStatusIdWithLowStock(status, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countByStatusIdWithLowStock(status);
                    }
                }
                if (searchStock.equals("zeroStock")) {
                    if (status == 10) {
                        pcList = productViewForAdminRepo.findAllProductsWithZeroStock(pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countAllProductsWithZeroStock();
                    } else {
                        pcList = productViewForAdminRepo.findByStatusIdWithZeroStock(status, pageable);
                        pcList.removeIf(Objects::isNull);
                        totalCount = productViewForAdminRepo.countByStatusIdWithZeroStock(status);
                    }
                }
            }
        }

        if (pcList.isEmpty()) {
            EmptySearchFormat emptySearchFormat = new EmptySearchFormat();
            emptySearchFormat.setData(new Object[]{});
            RestApiSuccessResponse errorResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(), "Product not found", emptySearchFormat);
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        } else {
            List<ProductCacheBO> piBOList = productCacheService.getProductFromCache(pcList);
            pioBOListList.put("data", piBOList);
            pioBOListList.put("pagedDataCount", piBOList.size());
            pioBOListList.put("TotalDataCount", totalCount);
            Integer startIndex = 0;
            if (pageNum != null && pageNum != 0) {
                startIndex = pageNum * pageSize + 1;
            }
            pioBOListList.put("SerialNumberStartIndex", startIndex);

            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "product found", pioBOListList);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getAllProductOnSearchBySupplierAndStatusWithOutPagination(String emailId, String processingType, String searchBy,
                                                                                       String searchValue, Integer status, String sortOrder, String displayCountryCode, String displayCurrencyCode)
            throws UnsupportedEncodingException {

        if (!StringUtils.isEmpty(emailId)) {
            _logger.info("Generating report in FG with mail id : " + emailId + " and searchBy : " + searchBy + " and searchValue : " + searchValue);
            productReportkafkaListener.fetchDataAndSendReport(searchBy, searchValue, status, sortOrder, displayCountryCode, displayCurrencyCode, emailId);
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Report will be shared on your registered email id in maximum 30 minute", null);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        _logger.info("Inside getAllProductOnSearchBySupplierAndStatusWithOutPagination with searchBy and searchValue {} {}", searchBy,
                searchValue);

        Map<String, Object> pioBOListList = new HashMap<>();
        List<ProductCacheBO> piBOList = new ArrayList<>();
        List<ProductCombinations> pcList = new ArrayList<>();
        Integer totalCount = 0;
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());


        String sortingOrderString = "";
        Sort sort = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {

            sortingOrderString = " order by pc.id desc";
            sort = Sort.by(Sort.Direction.DESC, "id");
        } else {
            sortingOrderString = " order by pc.id asc";
            sort = Sort.by("id");
        }
        if (StringUtils.isEmpty(searchValue) || searchValue == null) {
            if (status == 10) {
                pcList = productCombinationRepo.findAllWithOutPagination(PageRequest.of(0, Integer.MAX_VALUE, sort));
                pcList.removeIf(Objects::isNull);
                totalCount = pcList.size();
            } else {
                pcList = productCombinationRepo.findAllByStatusWithOutPagination(status, sortingOrderString);
                pcList.removeIf(Objects::isNull);
                totalCount = pcList.size();
            }
        } else {
            if (StringUtils.isNotBlank(searchBy) && status != null) {
                if (searchBy.equals("supplierName")) {
                    if (status == 10) {
                        pcList = productCombinationRepo.findBySupplierNameWithOutPagination(searchValue, sortingOrderString);
                        pcList.removeIf(Objects::isNull);
                        totalCount = pcList.size();
                    } else {
                        pcList = productCombinationRepo.findByStatusAndSupplierNameWithOutPagination(searchValue, status, sortingOrderString);
                        pcList.removeIf(Objects::isNull);
                        totalCount = pcList.size();
                    }
                    ;
                } else if (searchBy.equals("supplierId")) {
                    if (status == 10) {
                        pcList = productCombinationRepo.findBySupplierIdWithoutPagination(searchValue, sortingOrderString);
                        pcList.removeIf(Objects::isNull);
                        totalCount = pcList.size();
                    } else {
                        pcList = productCombinationRepo.findByStatusAndSupplierIdWithoutPagination(searchValue, status, sortingOrderString);
                        pcList.removeIf(Objects::isNull);
                        totalCount = pcList.size();
                    }

                } else {
                    pcList = searchProductOnSearchByColumnsWithoutPagination(searchValue, searchBy, sortOrder,
                            displayCountryCode, displayCurrencyCode);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                }

            } else if (StringUtils.isNotBlank(searchBy)) {
                if (searchBy.equals("supplierName")) {
                    pcList = productCombinationRepo.findBySupplierNameWithOutPagination(searchValue, sortingOrderString);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                } else if (searchBy.equals("supplierId")) {
                    pcList = productCombinationRepo.findBySupplierIdWithoutPagination(searchValue, sortingOrderString);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                } else {
                    pcList = searchProductOnSearchByColumnsWithoutPagination(searchValue, searchBy, sortOrder,
                            displayCountryCode, displayCurrencyCode);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                }
            } else if (status != null) {
                if (status == 10) {
                    pcList = productCombinationRepo.getAllProductsWithoutPagination(sortingOrderString);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                } else {
                    pcList = productCombinationRepo.findByStatusWithOutPagination(status, sortingOrderString);
                    pcList.removeIf(Objects::isNull);
                    totalCount = pcList.size();
                }
            }
        }

        if (pcList.isEmpty()) {
            EmptySearchFormat emptySearchFormat = new EmptySearchFormat();
            emptySearchFormat.setData(new Object[]{});
            RestApiSuccessResponse errorResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(),
                    "Product not found", emptySearchFormat);
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        } else {
            List<String> productUUIDList = pcList.stream().map(ProductCombinations::getUniqueIdentifier)
                    .collect(Collectors.toList());
            Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
            for (ProductCombinations productCom : pcList) {

                ProductCacheBO pcBO = productCacheService.getProductFromCache(productCom.getUniqueIdentifier(), displayCountryCode, displayCurrencyCode);

                piBOList.add(pcBO);
            }
            pioBOListList.put("data", piBOList);
            pioBOListList.put("pagedDataCount", piBOList.size());
            pioBOListList.put("TotalDataCount", totalCount);

            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "product found",
                    pioBOListList);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
    }

    public List<String> searchProductOnSearchByColumns(String searchKeyword, String searchColumn, String sortOrder,
                                                       Integer pageNum, Integer pageSize, String displayCountryCode, String displayCurrencyCode) throws UnsupportedEncodingException {

        _logger.info("Inside getProductOnSearchByColumns {}", searchKeyword);

        List<String> pcList = new ArrayList<>();
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "pc_id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("pc_id"));
        }
        searchKeyword = java.net.URLDecoder.decode(searchKeyword, StandardCharsets.UTF_8.name());
        if (searchColumn.equalsIgnoreCase("productId")) {
            pcList = productViewForAdminRepo.findByPcUuid(searchKeyword, pageable);
            pcList.removeIf(Objects::isNull);
        } else if (searchColumn.equalsIgnoreCase("productName")) {
			/*pcList = productCombinationRepo
					.findByProductIdAndCombinationStringLikeAndProductDescriptionLikeAndSearchTermsLike(searchKeyword,
							searchKeyword, searchKeyword, pageable);*/
            pcList.addAll(productViewForAdminRepo.findByItemNameLike(searchKeyword, pageable));
            pcList.removeIf(Objects::isNull);
        } else if (searchColumn.equalsIgnoreCase("SKU")) {
            try {
                pcList.addAll(productViewForAdminRepo.findBySku(searchKeyword, pageable));
                pcList.removeIf(Objects::isNull);

            } catch (Exception e) {
                _logger.error("error while fetching list for admin for sku serach with sku {}", searchKeyword, e);
            }
        }

        return pcList;
    }

    public List<ProductCombinations> searchProductOnSearchByColumnsWithoutPagination(String searchKeyword, String searchColumn,
                                                                                     String sortOrder, String displayCountryCode, String displayCurrencyCode)
            throws UnsupportedEncodingException {

        _logger.info("Inside searchProductOnSearchByColumnsWithoutPagination {}", searchKeyword);

        Map<String, Object> pioBOListList = new HashMap<>();
        List<ProductInventoryBO> piBOList = new ArrayList<>();
        List<ProductCombinations> pcList = new ArrayList<>();
        Integer totalCount = 0;
        Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
        List<ProductStatus> psList = new ArrayList<ProductStatus>();
        psList.add(productStatus.get());
        Pageable pageable = null;
        String sortingOrderString = "";
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            sortingOrderString = " order by pc.id desc";
        } else {
            sortingOrderString = " order by pc.id asc";
        }
        searchKeyword = java.net.URLDecoder.decode(searchKeyword, StandardCharsets.UTF_8.name());
        if (searchColumn.equalsIgnoreCase("productId")) {
            pcList = productCombinationRepo.findByUniqueIdentifier(searchKeyword, sortingOrderString);
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        } else if (searchColumn.equalsIgnoreCase("productName")) {

            pcList.addAll(
                    productCombinationRepo.findByItemNameLikeAndProductStatusNotInWithOutPagination(searchKeyword, psList, sortingOrderString));
            pcList.removeIf(Objects::isNull);
            totalCount = pcList.size();
        } else if (searchColumn.equalsIgnoreCase("SKU")) {
            try {
                pcList.addAll(productCombinationRepo.findBySkuAndProductStatusNotInWithOutPagination(searchKeyword, psList, sortingOrderString));
                pcList.removeIf(Objects::isNull);
                totalCount = pcList.size();

            } catch (Exception e) {

            }
        }

        return pcList;
    }


}