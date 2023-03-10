package com.kb.catalogInventory.service.admin;


import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.ProductStatus;
import com.kb.catalogInventory.datatable.Supplier;
import com.kb.catalogInventory.model.ProductCacheBO;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.StatusConstant;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.repository.ProductStatusRepository;
import com.kb.catalogInventory.repository.ProductViewForAdminRepo;
import com.kb.catalogInventory.repository.SupplierRepository;
import com.kb.catalogInventory.service.ProductCacheService;
import com.kb.catalogInventory.service.ProductDetailService;
import com.kb.catalogInventory.service.ProductUploadService;
import com.kb.catalogInventory.task.ReportGeneratorTaskForActiveProduct;
import com.kb.java.utils.RestApiErrorResponse;
import com.kb.java.utils.RestApiSuccessResponse;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class SupplierProductDetailService {
    private final static Logger _logger = LoggerFactory.getLogger(SupplierProductDetailService.class);

    @Autowired
    private ProductUploadService productUploadService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductCombinationsRepository productCombinationRepo;

    @Autowired
    private SupplierRepository supplierRepo;

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Autowired
    private ProductCacheService productCacheService;

    @Autowired
    private ProductViewForAdminRepo productViewForAdminRepo;

    public ProductInventoryBO productDetailByUniqueIdentifier(String uniqueIdentifier) {
        // TODO Auto-generated method stub
        return productUploadService.populateProductInventoryObjForSupplier(uniqueIdentifier);
    }

    /**
     * @param supplierIdList
     * @param inStock
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @return List<Map < String, Object>>
     */

    public List<Map<String, Object>> getProductsOnStockFilter(@NotNull List<Long> supplierIdList, boolean inStock,
                                                              String sortOrder, Integer pageNum, Integer pageSize) {

        _logger.info("Inside getProductsOnStockFilter ");

        List<Map<String, Object>> pioBOListList = new ArrayList<>();
        Pageable pageable = null;
        Map<String, Object> resMap = new HashMap<>();

        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }
        for (long supplierId : supplierIdList) {
            List<ProductCacheBO> piBOList = new ArrayList<>();

            if (inStock == true) {
                List<String> uuids = productCombinationRepo.findBySupplierIdInStockNative(supplierId, pageable);
                piBOList.addAll(productCacheService.getProductFromCache(uuids));
                resMap.put("TotalDataCount",
                        productCombinationRepo.countBySupplierAvailableStockGreaterThan(supplierId, 0, Long.valueOf(7)));
            } else if (inStock == false) {
                List<String> uuids = productCombinationRepo.findBySupplierIdOutOfStockNative(supplierId, pageable);
                piBOList.addAll(productCacheService.getProductFromCache(uuids));
                resMap.put("TotalDataCount",
                        productCombinationRepo.countBySupplierAvailableStockLessThanEqual(supplierId, 0, Long.valueOf(7)));
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
     * @return List<Map < String, Object>>
     */


    public List<Map<String, Object>> getProductsOnIsActiveFilter(@NotNull List<Long> supplierIdList, boolean isActive,
                                                                 String sortOrder, Integer pageNum, Integer pageSize) {
        int isActiveInt = 0;
        if (isActive) {
            isActiveInt = 1;
        }

        _logger.info("Inside getProductsOnIsActiveFilter ");

        List<Map<String, Object>> pioBOListList = new ArrayList<>();
        Pageable pageable = null;

        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        }
        for (long supplierId : supplierIdList) {
            List<ProductCacheBO> piBOList = null;
            List<String> uuids = productCombinationRepo.findBySupplierIdAndIsActiveAndProductStatusNotInNative(supplierId, isActiveInt, pageable);
            piBOList = productCacheService.getProductFromCache(uuids);
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("data", piBOList);
            resMap.put("TotalDataCount", productCombinationRepo.countSupplierActiveProducts(supplierId, Long.valueOf(7), isActive));
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
     * @param supplierId
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @return List<Map < String, Object>>
     */

    public List<Map<String, Object>> getInActiveProducts(long supplierId, String sortOrder, Integer pageNum,
                                                         Integer pageSize) {

        _logger.info("Inside getInActiveProducts ");
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
        pcList.stream().forEach(productCom -> {
            ProductInventoryBO piBO = new ProductInventoryBO();
            piBO = productUploadService.populateProductInventoryObjForSupplier(productCom.getUniqueIdentifier());
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
     * @param supplierId
     * @param searchKeyword
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @return Map<String, Object>
     * @throws UnsupportedEncodingException
     */

    public ResponseEntity<?> getProductOnSearch(long supplierId, String searchKeyword, String searchColumn, String sortOrder,
                                                Integer pageNum, Integer pageSize) throws UnsupportedEncodingException {


        _logger.info("Inside getProductOnSearch with search key {}", searchKeyword);
        Integer totalCount = null;
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

        if (searchColumn.equalsIgnoreCase("productName")) {
            try {
                pcList = productCombinationRepo
                        .findByProductIdAndCombinationStringLikeAndProductDescriptionLikeAndSearchTermsLike(searchKeyword,
                                searchKeyword, searchKeyword, null);
                pcList.addAll(productCombinationRepo.findByItemNameLikeAndProductStatusNotIn(searchKeyword, psList, pageable));
                totalCount = pcList.size();
            } catch (Exception e) {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Could not find the product with " + searchColumn + "with  " + searchKeyword, "");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }


        } else if (searchColumn.equalsIgnoreCase("productID")) {

            try {
                pcList = productCombinationRepo
                        .findByProductIdAndCombinationStringLikeAndProductDescriptionLikeAndSearchTermsLike(searchKeyword,
                                searchKeyword, searchKeyword, null);
                pcList.addAll(productCombinationRepo.findByUniqueIdentifierLikeAndProductStatusNotIn(searchKeyword, psList));
                totalCount = pcList.size();
            } catch (Exception e) {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Could not find the product with " + searchColumn + "with  " + searchKeyword, "");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
        } else if (searchColumn.equalsIgnoreCase("SKU")) {

            try {
                pcList.addAll(productCombinationRepo.findBySkuAndProductStatusNotIn(searchKeyword, psList, pageable));
                totalCount = pcList.size();
            } catch (Exception e) {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Could not find the product  " + searchColumn + " with  " + searchKeyword, "");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }

        } else {
            try {
                pcList.addAll(productCombinationRepo.findByItemNameLikeAndProductStatusNotIn(searchKeyword, psList, pageable));
                totalCount = pcList.size();
            } catch (Exception e) {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Could not find the product  " + searchColumn + " with  " + searchKeyword, "");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }

        }

        for (ProductCombinations productCom : pcList) {

            try {
                ProductInventoryBO piBO = new ProductInventoryBO();
                piBO = productUploadService.populateProductInventoryObjForSupplier(productCom.getUniqueIdentifier());
                List apiRespnose = productDetailService.getProductPriceBulkBuyForSupplier(productCom.getUniqueIdentifier());
                piBO.setPriceMatrix(apiRespnose);
                piBOList.add(piBO);

            } catch (Exception e) {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Could not find the product  " + searchColumn + " with  " + searchKeyword, "");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
        }
        pioBOListList.put("data", piBOList);
        pioBOListList.put("pagedDataCount", piBOList.size());
        pioBOListList.put("TotalDataCount", totalCount);
        Integer startIndex = 0;
        if (pageNum != null && pageNum != 0) {
            startIndex = pageNum * pageSize + 1;
        }
        pioBOListList.put("SerialNumberStartIndex", startIndex);
        RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "", pioBOListList);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    /**
     * @param supplierIdList
     * @param sortOrder
     * @param pageNum
     * @param pageSize
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> getProductsDetail(@NotNull List<Long> supplierIdList, String sortOrder,
                                                       Integer pageNum, Integer pageSize) {

        _logger.info("Inside getProductsDetail");
        List<Map<String, Object>> resMapList = new ArrayList<>();
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "pc_id"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("pc_id"));
        }
        for (long supplierId : supplierIdList) {
            List<ProductCacheBO> piBOList = new ArrayList<>();

            List<String> uuids = productViewForAdminRepo.findBySupplierId(String.valueOf(supplierId), pageable);

            for (String uuid : uuids) {
                ProductCacheBO piBO = new ProductCacheBO();
                piBO = productCacheService.getProductFromCache(uuid, "IN", "INR");
                piBOList.add(piBO);
            }
            long totalCount = productCombinationRepo.countBySupplierProducts(supplierId, Long.valueOf(7));
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

}
