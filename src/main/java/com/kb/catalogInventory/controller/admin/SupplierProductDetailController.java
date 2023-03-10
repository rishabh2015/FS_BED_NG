package com.kb.catalogInventory.controller.admin;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gson.Gson;
import com.kb.catalogInventory.task.ReportGeneratorTaskForActiveProduct;
import com.kb.catalogInventory.task.ReportGeneratorTaskForStock;
import com.kb.catalogInventory.util.EmailService;
import com.kb.java.utils.RestApiSuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.service.admin.SupplierProductDetailService;

@RestController
@CrossOrigin
@RequestMapping("/suplier/product")
public class SupplierProductDetailController {

    private final static Logger _logger = LoggerFactory.getLogger(SupplierProductDetailController.class);

    @Autowired
    private SupplierProductDetailService productDetailService;

    @Autowired
    private EmailService emailService;

    @Value("${report.recordsPerPage}")
    private int recordsPerPage;

    @GetMapping("/productdetail/{uniqueidentifier}")
    public ResponseEntity<?> getProductByUniqueIdentifier(
            @PathVariable("uniqueidentifier") @NotBlank @Size(min = 32, message = "Not a Valid uniqueidentifier") String uniqueIdentifier) {
        _logger.info("Entered getProductByUniqueIdentifier with productUniqueIdentifier : {}", uniqueIdentifier);
        ProductInventoryBO piBo = productDetailService.productDetailByUniqueIdentifier(uniqueIdentifier);
        _logger.debug("Leaving getProductByUniqueIdentifier with properties : {}", piBo);
        return ResponseEntity.ok(piBo);
    }


    @GetMapping("/filterProductOnStock/{supplierId}/{inStock}")
    public ResponseEntity<?> applyfilteronStockQuantity(@PathVariable("supplierId") long supplierId, @PathVariable("inStock") boolean inStock,
                                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        @RequestParam(value = "emailId", required = false) String emailId,
                                                        @RequestParam(value = "sortOrder", required = false) String sortOrder) {

        _logger.info("Entered applyfilteronStockQuantity with supplierId : {}", supplierId);


        List<Long> supplierIdList = new ArrayList<>();
        supplierIdList.add(supplierId);
        if (emailId == null || emailId.isEmpty()) {
            List<Map<String, Object>> list = productDetailService.getProductsOnStockFilter(supplierIdList, inStock, sortOrder, pageNum, pageSize);
            Map map = new HashMap<>();
            map.put("ProductInventoryList", list);
            _logger.debug("Returned Map {}", new Gson().toJson(map));
            return ResponseEntity.ok(map);
        } else {
            _logger.info("Generating report in FG with mail id : " + emailId + " and supplierIdList : " + supplierIdList + " and inStock : " + inStock + " and sortOrder : " + sortOrder);
            new Thread(new ReportGeneratorTaskForStock(productDetailService, emailService, supplierIdList, inStock, sortOrder, emailId, recordsPerPage)).start();
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Report will be shared on your registered email id in maximum 30 minute", null);
            return ResponseEntity.ok(successResponse);
        }
    }


    @GetMapping("/filterProductOnIsActive/{supplierId}/{inStock}")
    public ResponseEntity<?> applyfilteronIsActive(@PathVariable("supplierId") long supplierId, @PathVariable("inStock") boolean inStock,
                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "emailId", required = false) String emailId,
                                                   @RequestParam(value = "sortOrder", required = false) String sortOrder) {


        _logger.info("Entered applyfilteronIsActive with supplierId : {}", supplierId);
        List<Long> supplierIdList = new ArrayList<>();
        supplierIdList.add(supplierId);
        if (emailId == null || emailId.isEmpty()) {
            List<Map<String, Object>> list = productDetailService.getProductsOnIsActiveFilter(supplierIdList, inStock, sortOrder, pageNum, pageSize);
            Map map = new HashMap<>();
            map.put("ProductInventoryList", list);
            _logger.debug("Returned Map  applyfilteronIsActive {}", new Gson().toJson(map));
            return ResponseEntity.ok(map);
        } else {
            _logger.info("Generating report in FG with mail id : " + emailId + " and supplierIdList : " + supplierIdList + " and inStock : " + inStock + " and sortOrder : " + sortOrder);
            new Thread(new ReportGeneratorTaskForActiveProduct(productDetailService, emailService, supplierIdList, inStock, sortOrder, emailId, recordsPerPage)).start();
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Report will be shared on your registered email id in maximum 30 minute", null);
            return ResponseEntity.ok(successResponse);
        }

    }

    @GetMapping("/getSubmittedProducts/{supplierId}")
    public ResponseEntity<?> getSubmittedProducts(@PathVariable("supplierId") long supplierId,
                                                  @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @RequestParam(value = "sortOrder", required = false) String sortOrder) {

        _logger.info("Entered getSubmittedProducts with supplierId : {}", supplierId);
        List<Map<String, Object>> piBOList = productDetailService.getInActiveProducts(supplierId, sortOrder, pageNum, pageSize);
        Map map = new HashMap<>();
        map.put("ProductInventoryList", piBOList);
        _logger.debug("Returned Map  getSubmittedProducts {}", new Gson().toJson(map));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/searchProduct/{supplierId}/{searchColumn}")
    public ResponseEntity<?> searchProduct(@PathVariable("supplierId") long supplierId,
                                           @RequestParam("searchKeyword") String searchKeyword, @PathVariable(value = "searchColumn", required = false) String searchColumn,
                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                           @RequestParam(value = "sortOrder", required = false) String sortOrder) throws UnsupportedEncodingException {
        _logger.info("Entered searchProduct with searchKeyword : {}", searchKeyword);
        return (productDetailService.getProductOnSearch(supplierId, searchKeyword, searchColumn, sortOrder, pageNum, pageSize));
    }

    @GetMapping("/productDetail/{supplierId}")
    public ResponseEntity<?> getProductDetail(@PathVariable("supplierId") @NotNull Long supplierId,
                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        _logger.info("Entered getProductDetail with supplierId : {}", supplierId);
        List<Long> supplierIdList = new ArrayList<>();
        supplierIdList.add(supplierId);
        List<Map<String, Object>> list = productDetailService.getProductsDetail(supplierIdList, sortOrder, pageNum, pageSize);
        Map map = new HashMap<>();
        map.put("ProductInventoryList", list);
        return ResponseEntity.ok(map);

    }
}
