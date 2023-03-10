package com.kb.catalogInventory.util;

import com.kb.catalogInventory.model.ProductCacheBO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${sent_email_id}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String email, List<ProductCacheBO> productCacheBOList, int count) {
        logger.info("Sending report email to user : " + email);

        try {

            Workbook xlsFile = new HSSFWorkbook(); // create a workbook
            CreationHelper helper = xlsFile.getCreationHelper();
            Sheet sheet1 = xlsFile.createSheet("Sheet 1"); // add a sheet to your workbook

            String[] headers = new String[]{"Product Name", "Seller Name", "Items in Set",
                    "HSN", "SKU", "TAX %", "Category", "Brand", "Visible To", "Added Date",
                    "MRP", "Supplier Price", ",Discounted Price", "Stock", "Status", "Image URL"};

            Row headerRow = sheet1.createRow(0);
            for (int rn = 0; rn < headers.length; rn++) {
                headerRow.createCell(rn).setCellValue(headers[rn]);
            }


            int rowNumber = 1;
            int sheetNumber = 2;
            logger.info("Generating xls report email to user : " + email);
            for (ProductCacheBO productCacheBO : productCacheBOList) {
                Row row = null;
                if (rowNumber > 65000) {
                    Sheet newSheet = xlsFile.createSheet("Sheet " + sheetNumber++); // add a sheet to your workbook
                    Row r = newSheet.createRow(0);
                    for (int rn = 0; rn < headers.length; rn++) {
                        r.createCell(rn).setCellValue(headers[rn]);
                    }
                    rowNumber = 1;
                    row = newSheet.createRow(rowNumber);
                } else {
                    row = sheet1.createRow(rowNumber);
                }

                String itemName = StringUtil.isBlank(productCacheBO.getItemName()) ? "N/A" : productCacheBO.getItemName();
                String sellerName = StringUtil.isBlank(productCacheBO.getSellerName()) ? "N/A" : productCacheBO.getSellerName();
                String setPrices = "N/A";
                if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getSetPieces() != null) {
                    setPrices = productCacheBO.getProductPrice().getSetPieces().toString();
                }

                String hsn = StringUtil.isBlank(productCacheBO.getHSN()) ? "N/A" : productCacheBO.getHSN();
                String sku = StringUtil.isBlank(productCacheBO.getSKU()) ? "N/A" : productCacheBO.getSKU();
                String supplierTaxPercent = "N/A";
                if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getSupplierTaxPercent() != null) {
                    supplierTaxPercent = productCacheBO.getProductPrice().getSupplierTaxPercent().toString();
                }
                String categoryName = StringUtil.isBlank(productCacheBO.getCategoryName()) ? "N/A" : productCacheBO.getCategoryName();
                String brandName = StringUtil.isBlank(productCacheBO.getBrandName()) ? "N/A" : productCacheBO.getBrandName();
                String countryRuleName = StringUtil.isBlank(productCacheBO.getCountryRuleName()) ? "N/A" : productCacheBO.getCountryRuleName();
                String createdOn = StringUtil.isBlank(productCacheBO.getCreatedOn()) ? "N/A" : productCacheBO.getCreatedOn();
                String imageUrl = StringUtil.isBlank(productCacheBO.getThumbnailURL()) ? "N/A" : productCacheBO.getThumbnailURL();

                String productMrp = "N/A";
                if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getProductMrp() != null) {
                    productMrp = productCacheBO.getProductPrice().getProductMrp().toString();
                }

                String supplierSetPrice = "N/A";
                if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getSupplierSetPrice() != null) {
                    supplierSetPrice = productCacheBO.getProductPrice().getSupplierSetPrice().toString();
                }

                String discountedPrice = "N/A";
                if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getPriceAfterSupplierDiscountPerUnit() != null && productCacheBO.getProductPrice().getSetPieces() != null) {
                    try {
                        discountedPrice = new Double(productCacheBO.getProductPrice().getPriceAfterSupplierDiscountPerUnit() * productCacheBO.getProductPrice().getSetPieces()).toString();
                    } catch (Exception e) {
                        logger.error("Exception came while getting discounted price", e);
                    }
                }

                String availabilityCount = StringUtil.isBlank(productCacheBO.getAvailabilityCount()) ? "N/A" : productCacheBO.getAvailabilityCount();
                String status = StringUtil.isBlank(productCacheBO.getStatus()) ? "N/A" : productCacheBO.getStatus();

                int colNum = 0;

                row.createCell(colNum).setCellValue(helper.createRichTextString(itemName));
                colNum++;
                row.createCell(colNum).setCellValue(helper.createRichTextString(sellerName));
                colNum++;
                row.createCell(colNum).setCellValue(setPrices);
                colNum++;
                row.createCell(colNum).setCellValue(helper.createRichTextString(hsn));
                colNum++;
                row.createCell(colNum).setCellValue(helper.createRichTextString(sku));
                colNum++;
                row.createCell(colNum).setCellValue(supplierTaxPercent);
                colNum++;
                row.createCell(colNum).setCellValue(helper.createRichTextString(categoryName));
                colNum++;
                row.createCell(colNum).setCellValue(helper.createRichTextString(brandName));
                colNum++;
                row.createCell(colNum).setCellValue(helper.createRichTextString(countryRuleName));
                colNum++;
                row.createCell(colNum).setCellValue(createdOn);
                colNum++;
                row.createCell(colNum).setCellValue(productMrp);
                colNum++;
                row.createCell(colNum).setCellValue(supplierSetPrice);
                colNum++;
                row.createCell(colNum).setCellValue(discountedPrice);
                colNum++;
                row.createCell(colNum).setCellValue(availabilityCount);
                colNum++;
                row.createCell(colNum).setCellValue(helper.createRichTextString(status));
                colNum++;
                row.createCell(colNum).setCellValue(imageUrl);
                rowNumber++;
            }
            logger.info("xls report generated for email to user : " + email);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                xlsFile.write(bos);
            } finally {
                bos.close();
            }
            byte[] bytes = bos.toByteArray();
            logger.info("byte array generated for email to user with size : " + bytes.length + " is : " + email);
            compose(new String[]{email}, fromEmail, null, null, "Product Catalog Report", "", bytes, "SupplierReport.xls");
        } catch (Exception e) {
            logger.error("Exception came while sending report on email : " + email, e);
        }

    }

    public void sendEmailForActiveProduct(String email, Map<Long, List<ProductCacheBO>> supplierWiseMap, int count) {
        logger.info("Sending report email to user : " + email);

        try {

            Workbook xlsFile = new HSSFWorkbook(); // create a workbook
            CreationHelper helper = xlsFile.getCreationHelper();
            Sheet sheet1 = xlsFile.createSheet("Sheet 1"); // add a sheet to your workbook

            String[] headers = new String[]{"Product Name", "Seller Name", "Items in Set",
                    "HSN", "SKU", "TAX %", "Category", "Brand", "Visible To", "Added Date",
                    "MRP", "Supplier Price", ",Discounted Price", "Stock", "Status", "Image URL"};

            Row headerRow = sheet1.createRow(0);
            for (int rn = 0; rn < headers.length; rn++) {
                headerRow.createCell(rn).setCellValue(headers[rn]);
            }


            int rowNumber = 1;
            int sheetNumber = 2;
            logger.info("Generating xls report email to user : " + email);
            for (Long supplierId : supplierWiseMap.keySet()) {
                List<ProductCacheBO> productCacheBOList = supplierWiseMap.get(supplierId);
                for (ProductCacheBO productCacheBO : productCacheBOList) {
                    Row row = null;
                    if (rowNumber > 65000) {
                        Sheet newSheet = xlsFile.createSheet("Sheet " + sheetNumber++); // add a sheet to your workbook
                        Row r = newSheet.createRow(0);
                        for (int rn = 0; rn < headers.length; rn++) {
                            r.createCell(rn).setCellValue(headers[rn]);
                        }
                        rowNumber = 1;
                        row = newSheet.createRow(rowNumber);
                    } else {
                        row = sheet1.createRow(rowNumber);
                    }

                    String itemName = StringUtil.isBlank(productCacheBO.getItemName()) ? "N/A" : productCacheBO.getItemName();
                    String sellerName = StringUtil.isBlank(productCacheBO.getSellerName()) ? "N/A" : productCacheBO.getSellerName();
                    String setPrices = "N/A";
                    if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getSetPieces() != null) {
                        setPrices = productCacheBO.getProductPrice().getSetPieces().toString();
                    }
                    String hsn = StringUtil.isBlank(productCacheBO.getHSN()) ? "N/A" : productCacheBO.getHSN();
                    String sku = StringUtil.isBlank(productCacheBO.getSKU()) ? "N/A" : productCacheBO.getSKU();
                    String supplierTaxPercent = "N/A";
                    if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getSupplierTaxPercent() != null) {
                        supplierTaxPercent = productCacheBO.getProductPrice().getSupplierTaxPercent().toString();
                    }
                    String categoryName = StringUtil.isBlank(productCacheBO.getCategoryName()) ? "N/A" : productCacheBO.getCategoryName();
                    String brandName = StringUtil.isBlank(productCacheBO.getBrandName()) ? "N/A" : productCacheBO.getBrandName();
                    String countryRuleName = StringUtil.isBlank(productCacheBO.getCountryRuleName()) ? "N/A" : productCacheBO.getCountryRuleName();
                    String createdOn = StringUtil.isBlank(productCacheBO.getCreatedOn()) ? "N/A" : productCacheBO.getCreatedOn();
                    String imageUrl = StringUtil.isBlank(productCacheBO.getThumbnailURL()) ? "N/A" : productCacheBO.getThumbnailURL();
                    String productMrp = "N/A";
                    if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getProductMrp() != null) {
                        productMrp = productCacheBO.getProductPrice().getProductMrp().toString();
                    }

                    String supplierSetPrice = "N/A";
                    if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getSupplierSetPrice() != null) {
                        supplierSetPrice = productCacheBO.getProductPrice().getSupplierSetPrice().toString();
                    }

                    String discountedPrice = "N/A";
                    if (productCacheBO.getProductPrice() != null && productCacheBO.getProductPrice().getPriceAfterSupplierDiscountPerUnit() != null && productCacheBO.getProductPrice().getSetPieces() != null) {
                        try {
                            discountedPrice = new Double(productCacheBO.getProductPrice().getPriceAfterSupplierDiscountPerUnit() * productCacheBO.getProductPrice().getSetPieces()).toString();
                        } catch (Exception e) {
                            logger.error("Exception came while getting discounted price", e);
                        }
                    }

                    String availabilityCount = StringUtil.isBlank(productCacheBO.getAvailabilityCount()) ? "N/A" : productCacheBO.getAvailabilityCount();
                    String status = StringUtil.isBlank(productCacheBO.getStatus()) ? "N/A" : productCacheBO.getStatus();

                    int colNum = 0;

                    row.createCell(colNum).setCellValue(helper.createRichTextString(itemName));
                    colNum++;
                    row.createCell(colNum).setCellValue(helper.createRichTextString(sellerName));
                    colNum++;
                    row.createCell(colNum).setCellValue(setPrices);
                    colNum++;
                    row.createCell(colNum).setCellValue(helper.createRichTextString(hsn));
                    colNum++;
                    row.createCell(colNum).setCellValue(helper.createRichTextString(sku));
                    colNum++;
                    row.createCell(colNum).setCellValue(supplierTaxPercent);
                    colNum++;
                    row.createCell(colNum).setCellValue(helper.createRichTextString(categoryName));
                    colNum++;
                    row.createCell(colNum).setCellValue(helper.createRichTextString(brandName));
                    colNum++;
                    row.createCell(colNum).setCellValue(helper.createRichTextString(countryRuleName));
                    colNum++;
                    row.createCell(colNum).setCellValue(createdOn);
                    colNum++;
                    row.createCell(colNum).setCellValue(productMrp);
                    colNum++;
                    row.createCell(colNum).setCellValue(supplierSetPrice);
                    colNum++;
                    row.createCell(colNum).setCellValue(discountedPrice);
                    colNum++;
                    row.createCell(colNum).setCellValue(availabilityCount);
                    colNum++;
                    row.createCell(colNum).setCellValue(helper.createRichTextString(status));
                    colNum++;
                    row.createCell(colNum).setCellValue(imageUrl);
                    rowNumber++;
                }
            }
            logger.info("xls report generated for email to user : " + email);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                xlsFile.write(bos);
            } finally {
                bos.close();
            }
            byte[] bytes = bos.toByteArray();
            logger.info("byte array generated for email to user with size : " + bytes.length + " is : " + email);
            compose(new String[]{email}, fromEmail, null, null, "Product Catalog Report", "", bytes, "SupplierReport.xls");
        } catch (Exception e) {
            logger.error("Exception came while sending report on email : " + email, e);
        }

    }

    @Async
    public void compose(String[] toEmail, String fromEmailAdd,
                        List<String> ccList, List<String> bccList,
                        String subject, String emailBody, byte[] dataBytes, String attachmentFileWithExtension) {
        try {
            logger.info("Going to send Mail with report sent to email id : " + toEmail);
            String senderEmail = "";
            if (!ObjectUtils.isEmpty(fromEmailAdd)) {
                senderEmail = fromEmailAdd;
            } else {
                senderEmail = fromEmail;
            }

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helperMsg = new MimeMessageHelper(message, true);

            helperMsg.setTo(toEmail);
            helperMsg.setFrom(senderEmail);
            logger.info("fromEmail:" + senderEmail);
            helperMsg.setSubject(subject);
            helperMsg.setText(emailBody, true);
            if (dataBytes != null && attachmentFileWithExtension != null && dataBytes.length > 0) {
                ByteArrayDataSource attachment = new ByteArrayDataSource(dataBytes, "application/vnd.ms-excel");
                helperMsg.addAttachment(attachmentFileWithExtension, attachment);
            }
            logger.info("Mail with report sent to email id : " + toEmail);
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("Exception occur while sending mail with attached file to email id : " + toEmail, e);
        }
    }


}
