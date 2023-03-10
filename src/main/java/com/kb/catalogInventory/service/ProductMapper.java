package com.kb.catalogInventory.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.datatable.Category;
import com.kb.catalogInventory.datatable.ProductVariationOptionValue;
import com.kb.catalogInventory.datatable.ProductView;
import com.kb.catalogInventory.datatable.Variation;
import com.kb.catalogInventory.model.ImagesBO;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.ProductPriceMatrix;
import com.kb.catalogInventory.repository.CategoryRepository;
import com.kb.catalogInventory.repository.ProductRepository;
import com.kb.catalogInventory.repository.ProductVariationOptionValueRepository;
import com.kb.catalogInventory.repository.ProductViewRepository;
import com.kb.catalogInventory.repository.VariationRepository;
import com.kb.catalogInventory.util.Utils;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ProductMapper {

	@Autowired
	private KafkaBroadcastingService kafkaBroadcastingService;

	@Autowired
	private ProductViewRepository productViewRepository;

	@Autowired
	private ProductVariationOptionValueRepository productVariationOptionValueRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private VariationRepository variationRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductUploadService productUploadService;

	@Autowired
	private ProductDetailService productDetailService;

	public void createProductWrapper(List<String> pcUUIDS, String displayCountryCode, String displayCurrencyCode) {
		if (pcUUIDS != null && pcUUIDS.size() > 0) {
			int cores = Runtime.getRuntime().availableProcessors();
			log.info("Available core on node --- " + cores);

			Map<String, Object> productToImagemap = productDetailService.getMultipleImages(pcUUIDS);
			List<Map<String, Object>> discountAndTaxRQ = new ArrayList<>();
			pcUUIDS.stream().forEach(uuid -> {
				try {
					Map<String, Object> productToQuantity = new HashMap<>();
					productToQuantity.put("productCombinationId", uuid);
					productToQuantity.put("quantity", 1);
					discountAndTaxRQ.add(productToQuantity);
				} catch (Exception e) {
					log.error("Error in parallel stream of createProductWrapper while list to map conversion ", e);
				}

			});

			Map<String, Object> discountAndTaxRS = productDetailService
					.addDiscountAndTaxToProductInBulk(discountAndTaxRQ, displayCountryCode);

			Map<String, Object> bulkBuyRS = productDetailService.getMultipleProductsBulkBuyPrice(pcUUIDS,
					displayCountryCode);

			List<ProductView> views = productViewRepository.findByPcUuidIn(pcUUIDS);
			

			ForkJoinPool pool = new ForkJoinPool(Math.min(cores, pcUUIDS.size()));
			try {
				pool.submit(() -> views.stream().forEach(view -> {
					try {
						String uniqueIdentifier=view.getPcUuid();
						ProductInventoryBO pi = create(view, displayCountryCode, displayCurrencyCode);
						ObjectMapper mapper=new ObjectMapper();
						List<ProductPriceMatrix> pmlist=		Arrays
						.asList(mapper.convertValue(bulkBuyRS.get(uniqueIdentifier), ProductPriceMatrix[].class));
						pi.setPriceMatrix(pmlist);
						pi.setThumbnailURL((String) productToImagemap.get(uniqueIdentifier+"thumbnail"));
						pi.setImages((List<ImagesBO>) productToImagemap.get(uniqueIdentifier));
						pi.setPriceAfterDiscount((Double) discountAndTaxRS.get(uniqueIdentifier));
						sendToKafka(pi);
					} catch (Exception e) {
						log.error("Error in parallel stream in lockItem purging -- ", e);
					}

				})).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * For ADMIN pannel and ResellerPanel
	 * 
	 * @param productUniqueIdentifier uniqueIdentifier from product_combination
	 * @return
	 */
	public ProductInventoryBO create(ProductView view, String displayCountryCode, String displayCurrencyCode) {

		log.info(" Inside populateProductInventoryObj ");
		ProductInventoryBO productInventory = new ProductInventoryBO();
		String productUniqueIdentifier = view.getPcUuid();
		try {
			productInventory.setWareHouseId(view.getKbwarehouseid());
			productInventory.setProductSKU(view.getProductSku());
			productInventory.setProductClubingId(view.getProductUuid());
			productInventory.setName(view.getBmcName());
			productInventory.setBrandModel(view.getBmName());
			/* Newly added by wasif */
			productInventory.setBrandModelId(view.getBmId());
			productInventory.setBrandId(view.getBrandId());
			productInventory.setBrandIcon(view.getBrandICon());
			/* Newly added code ends */
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

			productInventory.setDisplayPrice(String.valueOf(Utils.roundSetPrecison(view.getPrice()/view.getSetPieces(), 2)));
			// need to have a seperate column for description
			productInventory.setDescription(view.getProductDescription());

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
			productInventory.setSupplierSignature(
					productUploadService.getSupplierSignature(String.valueOf(productInventory.getSellerId())));
			productInventory.setCollectionIds(view.getCollectionIds());
			productInventory.setSetPeices(view.getSetPieces());
			productInventory.setSetPrice(String.valueOf(Utils.roundSetPrecison(view.getPrice(), 2)));

			/**
			 * APPLY PRODUCT COUNTRY RULE
			 */
			productInventory = productUploadService.applyProductCountryRule(productInventory, view);
		} catch (Exception e) {
			log.info("Exception while creting productInventory of unique key ::::::::: {}", view.getPcUuid());
			log.error("Exception while creting productInventory of unique key", e);
		}
		return productInventory;
	}

	public void sendToKafka(ProductInventoryBO pi) {
		ObjectMapper _mapper = new ObjectMapper();
		try {
			if (StringUtils.isNotBlank(pi.getUniqueIdentifierForSearch())) {
				log.info("pushing  pc  to kafka with id ............{}", pi.getUniqueIdentifierForSearch());
				String productInventryString = _mapper.writeValueAsString(pi);
				KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-topic",
						pi.getUniqueIdentifierForSearch(), productInventryString);
				kafkaBroadcastingService.broadcast(kbr);
			}
		} catch (JsonProcessingException e) {
			log.error("JsonProcessingException product inventory not send to kafka ............", e);
		} catch (Exception e) {
			log.error("Exception product inventory not send to kafka ............", e);
		}

	}
}
