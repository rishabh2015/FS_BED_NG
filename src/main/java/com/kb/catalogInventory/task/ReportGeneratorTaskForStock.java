package com.kb.catalogInventory.task;

import com.google.gson.Gson;
import com.kb.catalogInventory.model.ProductCacheBO;
import com.kb.catalogInventory.service.admin.SupplierProductDetailService;
import com.kb.catalogInventory.util.EmailService;
import com.kb.java.utils.KbRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGeneratorTaskForStock extends KbRunnable {

    SupplierProductDetailService productDetailService;

    EmailService emailService;
    @NotNull List<Long> supplierIdList;
    boolean isActive;
    String mailId;

    String sortOrder;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    int recordsPerPage;


    public ReportGeneratorTaskForStock(SupplierProductDetailService productDetailService, EmailService emailService, @NotNull List<Long> supplierIdList, boolean isActive, String sortOrder, String mailId, int recordsPerPage) {
        this.productDetailService = productDetailService;
        this.emailService = emailService;
        this.supplierIdList = supplierIdList;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
        this.mailId = mailId;
        this.recordsPerPage = recordsPerPage;
    }

    @Override
    public void myRun() {
        try {

            long t1 = System.currentTimeMillis();
            int pageSize = recordsPerPage;
            int pageNum = 0;
            Map<Long, List<ProductCacheBO>> supplierWiseResponseMap = new HashMap<>();
            int totalCount = 0;
            for (Long supplierId : supplierIdList) {
                List<ProductCacheBO> productCacheBOListPerSupplier = new ArrayList<>();
                logger.info("fetching data for report in while loop for supplierIdList : " + new Gson().toJson(supplierIdList) + " and isActive : " + isActive + " sortOrder : " + sortOrder + "and mailId : " + mailId);
                while (true) {
                    try {
                        logger.info("fetching data for report in while loop for supplierIdList : " + new Gson().toJson(supplierIdList) + " and isActive : " + isActive + " sortOrder : " + sortOrder + "and mailId : " + mailId + " and pagenumber : " + pageNum);
                        List<Long> singleSupplierAsList = new ArrayList<>();
                        singleSupplierAsList.add(supplierId);
                        List<Map<String, Object>> list = productDetailService.getProductsOnStockFilter(singleSupplierAsList, isActive, sortOrder, pageNum, pageSize);
                        if (list == null || list.isEmpty() || list.get(0) == null || !(list.get(0) instanceof Map) ||
                                !(list.get(0)).containsKey("data") || ((Map) list.get(0)).get("data") == null ||
                                ((List) ((list.get(0)).get("data"))).isEmpty()) {
                            logger.info("Breaking loop for report in while loop for supplierIdList : " + new Gson().toJson(supplierIdList) + " and isActive : " + isActive + " sortOrder : " + sortOrder + "and mailId : " + mailId + " and pagenumber : " + pageNum);
                            break;
                        }
                        totalCount += list.size();
                        productCacheBOListPerSupplier.addAll((List) ((list.get(0)).get("data")));
                        logger.info("Data fetched for loop for report in while loop for supplierIdList : " + new Gson().toJson(supplierIdList) + " and isActive : " + isActive + " sortOrder : " + sortOrder + "and mailId : " + mailId + " and pagenumber : " + pageNum);
                    } catch (Exception e) {
                        logger.error("Exception came for report in while loop for supplierIdList : " + new Gson().toJson(supplierIdList) + " and isActive : " + isActive + " sortOrder : " + sortOrder + "and mailId : " + mailId + " and pagenumber : " + pageNum, e);
                    }
                    pageNum++;
                }
                supplierWiseResponseMap.put(supplierId, productCacheBOListPerSupplier);
                logger.info("Outside for loop for report in while loop for supplierid : " + supplierId + " and isActive : " + isActive + " sortOrder : " + sortOrder + "and mailId : " + mailId + " and pagenumber : " + pageNum);
            }
            long t2 = System.currentTimeMillis();
            logger.info("Time taken in generating report for emailId is : " + (t2 - t1));
            emailService.sendEmailForActiveProduct(mailId, supplierWiseResponseMap, totalCount);
        } catch (Exception e) {
            logger.error("Excpetion came while generating report for email id : " + mailId, e);
        }

    }
}
