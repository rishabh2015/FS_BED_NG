package com.kb.catalogInventory.service;

import com.google.gson.Gson;
import com.kb.catalogInventory.datatable.ProductCache;
import com.kb.catalogInventory.datatable.ProductView;
import com.kb.catalogInventory.model.ProductCacheBO;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.repository.ProductViewRepository;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class ProductCacheService {
    private static final String adminProductKey = "-Admin";

    static Logger _logger = LoggerFactory.getLogger(ProductCacheService.class);

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ProductViewRepository productViewRepository;

    @Autowired
    private ProductUploadService productUploadService;

    public List<ProductCacheBO> getProductFromCache(List<String> pcList){
        List<ProductCacheBO> piBOList = new ArrayList<>();
        long t1  = System.currentTimeMillis();
        Map<String, ProductCacheBO> map = getProductOnlyFromCache(pcList);
        List<String> pendingKeys = new ArrayList<>();
        pendingKeys.addAll(pcList);
        for (String key : map.keySet()) {
            pendingKeys.remove(key);
            piBOList.add(map.get(key));
        }
        _logger.info("Time taken in fetching data from cache for product details is : "+(System.currentTimeMillis()-t1));
        t1  = System.currentTimeMillis();
        for (String uuid : pendingKeys) {

            ProductCacheBO piBO = null;
            try {
                long startTimePerProduct = System.currentTimeMillis();
                piBO = getProductFromDb(uuid, "IN", "INR");
                _logger.info("Time taken in fetching single record from db for product details is : "+(System.currentTimeMillis()-startTimePerProduct));
            } catch (Exception e) {
                _logger.error("Error while creating product obj in seach by supplierAndStatus:{}", uuid, e);
            }
            piBOList.add(piBO);
        }
        _logger.info("Time taken in fetching data from db for product details is : "+(System.currentTimeMillis()-t1));
        return piBOList;
    }

    public ProductCacheBO getProductFromCache(String productUniqueId, String displayCountryCode, String displayCurrencyCode) {
        String keyForCache = productUniqueId + adminProductKey;
        Gson mapper = new Gson();
        String productString = cacheUtils.get(productUniqueId + "_long");
        if (StringUtils.isNotBlank(productString)) {
            return mapper.fromJson(productString, ProductCacheBO.class);
        } else {
            String productStringAdmin = cacheUtils.get(keyForCache);
            if (StringUtils.isNotBlank(productStringAdmin)) {
                return mapper.fromJson(productStringAdmin, ProductCacheBO.class);
            } else {
                ProductView view = productViewRepository.findByPcUuid(productUniqueId);
                ProductInventoryBO product = productUploadService.populateProductInventoryObj(view, displayCountryCode,
                        displayCurrencyCode, null);
                if (view.getStatusId() == 0) {
                    cacheUtils.put(view.getPcUuid() + "_long", mapper.toJson(product).toString());
                    return mapper.fromJson(cacheUtils.get(view.getPcUuid() + "_long"), ProductCacheBO.class);
                } else {
                    cacheUtils.put(view.getPcUuid() + adminProductKey, mapper.toJson(product).toString());
                    return mapper.fromJson(cacheUtils.get(view.getPcUuid() + adminProductKey), ProductCacheBO.class);
                }
            }
        }

    }

    public ProductCacheBO getProductFromDb(String productUniqueId, String displayCountryCode, String displayCurrencyCode) {
        Gson mapper = new Gson();
        ProductView view = productViewRepository.findByPcUuid(productUniqueId);
        ProductInventoryBO product = productUploadService.populateProductInventoryObj(view, displayCountryCode,
                displayCurrencyCode, null);
        if (view.getStatusId() == 0) {
            cacheUtils.put(view.getPcUuid() + "_long", mapper.toJson(product).toString());
            return mapper.fromJson(cacheUtils.get(view.getPcUuid() + "_long"), ProductCacheBO.class);
        } else {
            cacheUtils.put(view.getPcUuid() + adminProductKey, mapper.toJson(product).toString());
            return mapper.fromJson(cacheUtils.get(view.getPcUuid() + adminProductKey), ProductCacheBO.class);
        }
    }


    public Map<String, ProductCacheBO> getProductOnlyFromCache(List<String> productUniqueId) {
        Map<String, ProductCacheBO> map = new HashMap<>();
        List<String> batch = new ArrayList<>();
        int counter = 0;
        for (String key : productUniqueId) {
            batch.add(key);
            counter++;
            if (counter % 200 == 0) {
                map.putAll(getProductOnlyFromCacheInBatch(batch));
                batch = new ArrayList<>();
            }
        }
        map.putAll(getProductOnlyFromCacheInBatch(batch));
        return map;
    }

    private Map<String, ProductCacheBO> getProductOnlyFromCacheInBatch(List<String> productUniqueId) {
        String[] keysToBeLookedUp1 = new String[productUniqueId.size()];

        Map<String, ProductCacheBO> finalMap = new HashMap<>();
        int i = 0;
        for (String key : productUniqueId) {
            keysToBeLookedUp1[i] = key  + "_long";
            i++;
        }

        Map<String, String> output = cacheUtils.multiGet(keysToBeLookedUp1);
        if (output != null) {
            for (String key : output.keySet()) {
                Gson mapper = new Gson();
                if (StringUtils.isNotBlank(output.get(key))) {
                    finalMap.put(key.substring(0, key.length() - 5), mapper.fromJson(output.get(key), ProductCacheBO.class));
                }
            }
        }

        List<String> keysToBeLookedUp2List = new ArrayList<>();

        i = 0;
        for (String key : productUniqueId) {
            if (!finalMap.containsKey(key)) {
                keysToBeLookedUp2List.add( key + adminProductKey);
                i++;
            }
        }
        String[] keysToBeLookedUp2 = new String[keysToBeLookedUp2List.size()];

        output = cacheUtils.multiGet(keysToBeLookedUp2List.toArray(keysToBeLookedUp2));
        if (output != null) {
            for (String key : output.keySet()) {
                Gson mapper = new Gson();
                if (StringUtils.isNotBlank(output.get(key))) {
                    finalMap.put(key.substring(0, key.length() - 6), mapper.fromJson(output.get(key), ProductCacheBO.class));
                }
            }
        }
        return finalMap;
    }
}
