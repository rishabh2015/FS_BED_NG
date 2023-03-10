package com.kb.catalogInventory.task;

import com.kb.catalogInventory.model.EmptySearchFormat;
import com.kb.catalogInventory.model.ProductCacheBO;
import com.kb.catalogInventory.service.ProductDetailService;
import com.kb.catalogInventory.util.EmailService;
import com.kb.java.utils.KbRunnable;
import com.kb.java.utils.RestApiSuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportGenerationTask extends KbRunnable {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    int recordsPerPage;
    String searchValue;
    String searchBy;
    String displayCurrencyCode;

    String mailId;
    String displayCountryCode;
    int status;
    String sortOrder;
    ProductDetailService productDetailService;
    EmailService emailService;


    public ReportGenerationTask(String searchBy, String searchValue, int status, String sortOrder, String displayCountryCode, String displayCurrencyCode, String mailId, ProductDetailService productDetailService, EmailService emailService, int recordsPerPage) {
        this.searchBy = searchBy;
        this.searchValue = searchValue;
        this.status = status;
        this.sortOrder = sortOrder;
        this.displayCountryCode = displayCountryCode;
        this.displayCurrencyCode = displayCurrencyCode;
        this.mailId = mailId;
        this.productDetailService = productDetailService;
        this.emailService = emailService;
        this.recordsPerPage = recordsPerPage;
    }

    @Override
    public void myRun() {
        try {

            long t1 = System.currentTimeMillis();
            int pageSize = recordsPerPage;
            int pageNum = 0;
            List<ProductCacheBO> completeResponse = new ArrayList();
            int totalCount = 0;
            logger.info("fetching data for report in while loop for search value : " + searchValue + " and search by : " + searchBy + " and mailId : " + mailId);
            while (true) {
                try {
                    logger.info("Inside while loop for search value : " + searchValue + " and search by : " + searchBy + " and mailId : " + mailId + " and pageNum : " + pageNum);
                    ResponseEntity res = productDetailService.getAllProductOnSearchBySupplierAndStatus(searchBy, searchValue, status, sortOrder, pageNum, pageSize, displayCountryCode, displayCurrencyCode, "allStock");
                    if (res == null || res.getBody() == null ||
                            (((RestApiSuccessResponse) res.getBody()).getData() == null) ||
                            ((RestApiSuccessResponse) res.getBody()).getData() instanceof EmptySearchFormat ||
                            !(((RestApiSuccessResponse) res.getBody()).getData() instanceof Map) ||
                            ((Map) ((RestApiSuccessResponse) res.getBody()).getData()).isEmpty() ||
                            !(((Map) ((RestApiSuccessResponse) res.getBody()).getData()).containsKey("data")) ||
                            ((((Map) ((RestApiSuccessResponse) res.getBody()).getData()).get("data"))) == null ||
                            ((List) (((Map) ((RestApiSuccessResponse) res.getBody()).getData()).get("data"))).isEmpty()) {
                        logger.info("Breaking while loop for search value : " + searchValue + " and search by : " + searchBy + " and mailId : " + mailId);
                        break;
                    }
                    RestApiSuccessResponse restApiSuccessResponse = (RestApiSuccessResponse) res.getBody();
                    Map<String, Object> body = (Map<String, Object>) restApiSuccessResponse.getData();
                    List<ProductCacheBO> productCacheBOS = (List<ProductCacheBO>) body.get("data");
                    int countPerPage = (int) body.get("pagedDataCount");
                    int countTotal = (Integer) body.get("TotalDataCount");
                    totalCount += countPerPage;
                    completeResponse.addAll(productCacheBOS);
                    logger.info("product fetched for search value : " + searchValue + " and search by : " + searchBy + " and mailId : " + mailId + " and pageNum : " + pageNum + " and record per page : " + pageSize + " and totalRecords : " + countTotal);
                } catch (Exception e) {
                    logger.error("Exception came while getting data for searchBy : " + searchBy + " and searchValue : " + searchValue, e);
                }
                pageNum++;
            }
            logger.info("Outside while loop for search value : " + searchValue + " and search by : " + searchBy + " and mailId : " + mailId);
            long t2 = System.currentTimeMillis();
            logger.info("Time taken in generating report for emailId is : " + (t2 - t1));
            emailService.sendEmail(mailId, completeResponse, totalCount);
        } catch (Exception e) {
            logger.error("Excpetion came while generating report for email id : " + mailId, e);
        }

    }
}
