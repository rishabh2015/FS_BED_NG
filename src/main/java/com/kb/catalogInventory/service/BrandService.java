package com.kb.catalogInventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.constant.AppConstant;
import com.kb.catalogInventory.constant.InventoryConstants;
import com.kb.catalogInventory.datatable.*;
import com.kb.catalogInventory.exception.DataNotFoundException;
import com.kb.catalogInventory.mapper.BrandMapper;
import com.kb.catalogInventory.mapper.BrandReviewsMapper;
import com.kb.catalogInventory.mapper.CategoryRS;
import com.kb.catalogInventory.model.*;
import com.kb.catalogInventory.repository.*;
import com.kb.catalogInventory.util.LocalCache;
import com.kb.java.utils.KbRestTemplate;
import com.kb.java.utils.RestApiErrorResponse;
import com.kb.java.utils.RestApiSuccessResponse;

import ch.qos.logback.classic.Logger;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class BrandService {

    @Autowired
    private BrandsRepository brandsRepository;
    @Autowired
    private SupplierBrandsRespository supplierBrandsRespository;

    @Autowired
    BrandReviewsMapper brandReviewsMapper;
    @Autowired
    SupplierService supplierService;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    ProductCombinationsRepository productCombinationsRepository;

    @Autowired
    BrandModelCategoryRepository brandModelCategoryRepository;

    @Autowired
    BrandModelsRepository brandModelsRepository;

    @Autowired
    BrandReviewsRepository brandReviewsRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    private ProductUploadService uploadService;

    @Autowired
    private ProductViewRepository prodViewRepo;

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Autowired
    GroupsRespository groupsRespository;

    @Autowired
    private LocalCache cache;

    @Value("${order.service.bestselling.getsellingcountdetail.url}")
    private String getSellingCountDetailsUrl;

    @Value("${order.service.bestselling.getsellingcountdetail.url.timeout}")
    private int getSellingCountDetailsUrlTimeout;

    @Autowired
    KbRestTemplate restTemplate;


    private Brands validateBrandId(long brandId) {
        if (brandId == 0) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), AppConstant.INVALID_BRAND_ID, AppConstant.INVALID_BRAND_ID);
        }
        Optional<Brands> brandsOpt = brandsRepository.findById(brandId);
        if (!brandsOpt.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), AppConstant.INVALID_BRAND_ID, AppConstant.INVALID_BRAND_ID);
        }
        return brandsOpt.get();
    }

    @Transactional
    public List<BrandBO> getBrandByName(String brandName, String contact) {
        List<Brands> brands = brandsRepository.findByNameLike(brandName);
        Map<Long, Supplier> supplierHashMap = new HashMap<>();
        Map<Long, Groups> groupsHashMap = new HashMap<>();
        List<Long> supplierIdList = new ArrayList<>();
        List<SupplierBrands> supplierBrandsList=new ArrayList<>();
        for (Brands  brand:brands){
              try {
                supplierBrandsList=supplierBrandsRespository.findByBrandIdAndIsActiveTrue(String.valueOf(brand.getId()));

                for (SupplierBrands supplierBrands:supplierBrandsList){
                    supplierIdList.add(Long.valueOf(supplierBrands.getSupplierId()));
                }
              }catch (Exception e){

              }


        }


       // brands.forEach(brand -> supplierIdList.add(brand.getSupplier().getId()));
        List<Long> groupIdList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(supplierIdList)) {
            List<Supplier> supplierList = supplierRepository.findAllByIdList(supplierIdList);
            supplierHashMap = supplierList.stream().collect(Collectors.toMap(Supplier::getId, Function.identity()));
            supplierList.forEach(supplier -> {
                if (!ObjectUtils.isEmpty(supplier.getGroupString()) && !contact.equalsIgnoreCase("0")) {
                    groupIdList.addAll(Arrays.stream(supplier.getGroupString().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                }
            });
            if (!ObjectUtils.isEmpty(groupIdList) && !contact.equalsIgnoreCase("0")) {
                List<Groups> groups = groupsRespository.findAllById(groupIdList);
                groupsHashMap = groups.stream().collect(Collectors.toMap(Groups::getId, Function.identity()));
            }
        }

        Map<Long, Supplier> finalSupplierHashMap = supplierHashMap;
        Map<Long, Groups> finalGroupsHashMap = groupsHashMap;
        return brands.stream().map(brand -> brandMapper.convertEntityToBo(brand, finalSupplierHashMap, finalGroupsHashMap, contact)).collect(Collectors.toList());
    }

    public Map<String, Long> fetchBestSellingProductDetailCount(List<String> productIdList) {
        Map<String, Long> outputMap = new HashMap<>();
        try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<BestSellingRQ> entity = new HttpEntity<>(BestSellingRQ.builder().productIds(productIdList).build(), headers);
//			ResponseEntity<String> result = restTemplate.exchange(
//					getSellingCountDetailsUrl, HttpMethod.POST, entity, getSellingCountDetailsUrlTimeout, String.class);
//			if (result.getStatusCode() == HttpStatus.OK) {
//				JSONObject response = new JSONObject(Objects.requireNonNull(result.getBody()));
//				JSONArray jsonArray = response.optJSONArray("data");
//				if (jsonArray != null && jsonArray.length() > 0) {
//					for (int index = 0; index < jsonArray.length(); index++) {
//						JSONObject object = jsonArray.optJSONObject(index);
//						outputMap.put(object.optString("productId"), object.optLong("sellingCount"));
//					}
//				}
//			}
            productIdList.forEach(productId -> outputMap.put(productId, 0L));
        } catch (Exception e) {
            log.error("Exception occurred while data parsing of bestSellingProductDetails", e);
        }
        return outputMap;
    }


    @Transactional
    public Set<BrandBO> getAllBrands(String countrycode) {
        // List<Brands> brands= brandsRepository.findAll();
        boolean isInternationalLoggin = false;
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add(countrycode);
        if (countrycode != null && !countrycode.equalsIgnoreCase("IN")) {
            isInternationalLoggin = true;
        }
        boolean isInternational = isInternationalLoggin;
        List<Brands> brands = brandsRepository.findAllActiveBrands();
        Set<BrandBO> brandsBO = new TreeSet<BrandBO>();
        brands.stream().forEach(brand -> {
            long totalCountdisplayAll = prodViewRepo
                    .countByBrandIdAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThanEqual(brand.getId(),
                            Long.valueOf(0), 1);
            long totalCountdisplayInter = isInternational
                    ? prodViewRepo.countByBrandIdAndStatusIdAndPcrInterTrueAndAvailableStockGreaterThanEqual(
                    brand.getId(), Long.valueOf(0), 1)
                    : 0;
            long totalCountdisplayWlc = prodViewRepo
                    .countByBrandIdAndStatusIdAndPcrWlcInAndAvailableStockGreaterThanEqual(brand.getId(),
                            Long.valueOf(0), countryCodes, 1);
            long totalCount = totalCountdisplayAll + totalCountdisplayInter + totalCountdisplayWlc;
            if (totalCount > 0) {
                brandsBO.add(brandMapper.convertEntityToBo(brand));
            }
        });
        return brandsBO;
    }

    public Object getAllBrandsForAdmin(String sortOrder, Integer pageNum, Integer pageSize) {
        // List<Brands> brands= brandsRepository.findAll();

        Page<Brands> brandsList = brandsRepository.findAllByIsActiveTrue(getPagable(pageNum, pageSize, sortOrder));
        List<BrandBO> res = new ArrayList<>();
        brandsList.getContent().stream().forEach(brands -> {
            res.add(brandMapper.convertEntityToBo(brands));
        });

        Page<BrandBO> page = new PageImpl<>(res, brandsList.getPageable(), brandsList.getTotalElements());


        return page;
    }

    private Pageable getPagable(Integer pageNum, Integer pageSize, String sortOrder) {
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sortOrder"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("sortOrder"));
        }

        return pageable;
    }

    private Pageable getPagableForBrands(Integer pageNum, Integer pageSize, String sortOrder) {
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort_order"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("sort_order"));
        }

        return pageable;
    }

    @Transactional
    public List<BrandBO> getBrandBySupplier(Long supplierId) {
        List<Brands> brandList = null;
        List<BrandBO> brands = new ArrayList<>();
        try {
            brandList = brandsRepository.findBrandBySupplierId(supplierId);
            brandList.stream().forEach(bl -> {
                brands.add(brandMapper.convertEntityToBo(bl));
            });
        } catch (Exception e) {

        }
        return brands;
    }

    private boolean isBrandExistWithSameName(BrandBO brandBo) {
        try {
            Brands brandsOpt = brandsRepository.findByName(brandBo.getName());
            if (brandsOpt != null && brandsOpt.getId() != brandBo.getId()) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    /*boolean isSupplierAssociatedWithAnotherBrand(BrandBO brandBo) {
        if(brandBo.getSupplierId()!=null) {
            Optional<Supplier> supplierOpt=supplierRepository.findById(brandBo.getSupplierId());
            if(!supplierOpt.isPresent()) {
                throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Invalid Supplier Id!", "Invalid Supplier Id!");
            }
            try {
                Brands brands=brandsRepository.findBrandBySupplier(supplierOpt.get());
                if(brands!=null && brands.getId()!=brandBo.getId()) {

                    throw new DataNotFoundException(HttpStatus.OK.value(), supplierOpt.get().getFirstName() +" associated with "+brands.getName(), supplierOpt.get().getFirstName() +" associated with "+brands.getName());
                }
            }catch(Exception e) {
                return true;
            }
        }
        return false;
    }*/
    @Transactional
    public BrandBO save(BrandBO brandBo) {

        if (isBrandExistWithSameNameAndSupplier(brandBo)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Brand with same name already exists with the supplier!", "Brand with same name already exists  with the supplier!");
        }

		/*if(isBrandExistWithSameName(brandBo)) {
			throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Brand with same name already exists!", "Brand with same name already exists!");
		}*/
        // closed for multiBrands for one supplier
		/*if(isSupplierAssociatedWithAnotherBrand(brandBo)) {
			throw new DataNotFoundException(HttpStatus.OK.value(), "Selected supplier is associated with Another brand!","Selected supplier is associated with Another brand!");
		}*/
        Brands brands = brandMapper.convertBoToEntity(brandBo);
        brands.setUpdatedOn(new Date(System.currentTimeMillis()));
        brands.setIsActive(true);
        if (brands.getAddedOn() == null) {
            brands.setAddedOn(new Date());
        }

        Brands savedbrands = brandsRepository.save(brands);
        brands.setSortOrder(savedbrands.getId());
        savedbrands = brandsRepository.save(brands);
        BrandModels brandModels = brandModelsRepository.findByBrands(savedbrands);
        if (brandModels == null) {
            brandModels = new BrandModels();
            brandModels.setName(savedbrands.getName());
            brandModels.setBrands(savedbrands);
            brandModels = brandModelsRepository.save(brandModels);
        } else if (!brandModels.getName().equalsIgnoreCase(brandBo.getName())) {
            brandModels.setName(savedbrands.getName());
            brandModels = brandModelsRepository.save(brandModels);
        }


        return brandMapper.convertEntityToBo(savedbrands);
    }

    public void CreateBrandModelCategory(String supplierId, String brandId) {

        log.info("create BrandModelCategory with supplier" + supplierId);
        try {
            if (supplierId != null) {
                Brands brands = brandsRepository.findById(Long.valueOf(brandId)).get();
                Supplier supplier = supplierRepository.findById(Long.valueOf(supplierId)).get();
                BrandModels brandModels = brandModelsRepository.findByBrands(brands);
                List<Product> productList = productRepository.findBySupplier(supplier);
                final BrandModels bModel = brandModels;
                Map<Long, BrandModelCategory> brandModelCategoryMap = new HashMap<>();
                productList.forEach(prod -> {
                    BrandModelCategory category = prod.getBrandModelCategory();
                    if (category != null && !brandModelCategoryMap.containsKey(category.getId())) {
                        category.setBrandModels(bModel);
                        brandModelCategoryMap.put(category.getId(), category);
                    }
                });
                brandModelCategoryRepository.saveAll(brandModelCategoryMap.values());
            }
        } catch (Exception e) {
            log.error("error while creating a brands category Model", e);
        }


    }


    private boolean isBrandExistWithSameNameAndSupplier(BrandBO brandBo) {
        try {

            //Brands brandsOpt=brandsRepository.findByName(brandBo.getName());
            Brands brandsOpt = brandsRepository.findByName(brandBo.getName());
            if (brandsOpt != null && brandsOpt.getId() != brandBo.getId()) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }


    public void updateProductCombinations(Long supplierId) {
        try {
            Optional<Supplier> suppOpt = supplierRepository.findById(supplierId);
            Optional<ProductStatus> ps = productStatusRepository.findById(StatusConstant.Active);
            Supplier supp = null;
            if (suppOpt.isPresent()) {
                supp = suppOpt.get();
            }
            List<ProductCombinations> pcList = productCombinationsRepository.findBySupplierAndProductStatus(supp, ps.get());
            for (ProductCombinations pc : pcList) {
                pc.setUpdatedOn(new Date(System.currentTimeMillis()));
            }
            productCombinationsRepository.saveAll(pcList);
        } catch (Exception e) {
            log.error("Exception in updating the supplier products in Brand updation ", e);
        }
    }

    @Transactional
    public BrandBO changeActiveStatus(long id, boolean markedAsActive) {
        Brands brands = validateBrandId(id);
        brands.setUpdatedOn(new Date());
        brands.setIsActive(markedAsActive);
        brands = brandsRepository.save(brands);
        return brandMapper.convertEntityToBo(brands);
    }

    public List<String> isSKUValid(Long supplierId, String skus) {
        String[] skuList = skus.split(",");
        List<String> invalidSkus = new ArrayList<String>();
        try {
            List<ProductCombinations> pcList = productCombinationsRepository.findBySkuIn(Arrays.asList(skuList));
            for (ProductCombinations pc : pcList) {
                if (pc.getSupplier().getId() != supplierId) {
                    invalidSkus.add(pc.getSku());
                }
            }
        }catch (Exception e){
          log.error("error while valid sku",e);
        }

        return invalidSkus;
    }

    public List<ProductInventoryBO> getAllPopularSku(long brandId, String displayCountryCode) {
        List<ProductInventoryBO> combinationList = new ArrayList<>();
        Brands brand = validateBrandId(brandId);
        if (brand.getBestSellingSku() == null) {
            return new ArrayList<>();
        }
        String[] skuId = brand.getBestSellingSku().split(",");
        List<ProductView> viewList = prodViewRepo.findBySkuInAndStatusIdAndAvailableStockGreaterThanEqual(
                Arrays.asList(skuId), Long.valueOf(0), 1, null);
        List<String> productUUIDList = viewList.stream().map(ProductView::getPcUuid).collect(Collectors.toList());
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
        viewList.stream().forEach(view -> {
            if (productUploadService.isProductApplicableForProdcutContryRule(view, displayCountryCode)) {
                combinationList.add(uploadService.populateProductInventoryObj(view, displayCountryCode,
                        "INR", bestSellingProductCountDetailMap));
            }
        });
        return combinationList;
    }

    public List<ProductInventoryBO> getAllBestSellingSku() {
        List<ProductInventoryBO> combinationList = new ArrayList<>();
        List<Brands> brandsList = brandsRepository.findAllLatestUpdatedBrands();
        for (Brands brands : brandsList) {
            String[] skuId = brands.getBestSellingSku().split(",");
            List<ProductView> viewList = prodViewRepo.findBySkuInAndStatusIdAndAvailableStockGreaterThanEqual(
                    Arrays.asList(skuId), Long.valueOf(0), 1, null);
            List<String> productUUIDList = viewList.stream().map(ProductView::getPcUuid).collect(Collectors.toList());
            Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
            viewList.stream().forEach(view -> {
                combinationList.add(uploadService.populateProductInventoryObj(view, "IN", "INR", bestSellingProductCountDetailMap));
            });
        }

        return combinationList;
    }

    public List<BrandCategoryAndProductBO> getAllSupplierBrand(long supplierId) {
        List<Brands> brand = brandsRepository.findBrandBySupplierId(supplierId);
        List<BrandCategoryAndProductBO> responseList = new ArrayList<>();
        List<BrandCategoryAndProductBO> getbrandModelCategoryByBrandResponse = new ArrayList<>();

        brand.stream().forEach(bd -> {
            getbrandModelCategoryByBrandResponse.addAll(getbrandModelCategoryByBrand(bd));
        });
        responseList.addAll(getbrandModelCategoryByBrandResponse);
        return responseList;
    }

    public Map<String, Object> getProductCombinationByBrand(long brandId, String sortOrder,
                                                            Integer pageNum, Integer pageSize) {
        validateBrandId(brandId);
        Map<String, Object> resMap = new HashMap<>();
        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "pcId"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("pcId"));
        }
        List<ProductInventoryBO> combinationList = new ArrayList<>();
        List<ProductView> viewList = prodViewRepo.findByBrandIdAndStatusIdAndAvailableStockGreaterThanEqual(brandId,
                Long.valueOf(0), 1, pageable);
        long totalCount = prodViewRepo.countByBrandIdAndStatusIdAndAvailableStockGreaterThanEqual(brandId,
                Long.valueOf(0), 1);
        resMap.put("TotalCount", totalCount);
        resMap.put("PageSize", pageSize);
        resMap.put("PageNum", pageNum);
        List<String> productUUIDList = viewList.stream().map(ProductView::getPcUuid).collect(Collectors.toList());
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
        viewList.stream().forEach(view -> {
            combinationList.add(uploadService.populateProductInventoryObj(view, "IN", "INR", bestSellingProductCountDetailMap));
        });
        resMap.put("PagedData", combinationList);

        return resMap;
    }

    public List<BrandCategoryAndProductBO> getbrandModelCategoryByBrand(long brandId) {
        Brands brand = validateBrandId(brandId);
        return getbrandModelCategoryByBrand(brand);
    }

    public List<BrandCategoryAndProductBO> getbrandModelCategoryByBrand(Brands brand) {

        BrandModels bransModel = brandModelsRepository.findByBrands(brand);
        Set<BrandCategoryAndProductBO> brandModelCategorySet = new HashSet<BrandCategoryAndProductBO>();
        long countOfProduct = 0;
        if (bransModel != null) {
            for (BrandModelCategory category : bransModel.getBrandModelCategories()) {
                BrandCategoryAndProductBO brandModelCategoryBO = new BrandCategoryAndProductBO();
                countOfProduct = prodViewRepo.countByBrandModelCategoryId(category.getId());
                //brand field
				/*brandMapper.convertEntityToBo(brandModelCategoryBO,brand);
				//BrandModel
				brandModelCategoryBO.setBrandModelId(bransModel.getId());
				brandModelCategoryBO.setBrandModelName(bransModel.getName());
				brandModelCategoryBO.setId(brand.getId());
				//BrandModelCategory
				brandModelCategoryBO.setBrandModelCategoryId(category.getId());
				brandModelCategoryBO.setCategoryName(category.getCategoryName());*/
                brandModelCategoryBO.setCategoryName(category.getCategoryName());
                brandModelCategoryBO.setProductCount(countOfProduct);
                brandModelCategorySet.add(brandModelCategoryBO);
            }
        }
        return brandModelCategorySet.stream().collect(Collectors.toList());
    }

    @Transactional
    public String addReview(long brandId, BrandReviewsBO brandReviewsBO) {
        Brands brands = validateBrandId(brandId);
        BrandReviews brandReviews = brandReviewsMapper.convertBoToEntity(brandReviewsBO);
        brandReviews.setReviewDate(new Date());
        brandReviews.setBrands(brands);
        brandReviewsRepository.save(brandReviews);
        return "Review Added Successfully";
    }

    public List<BrandReviewsBO> getBrandReview(long brandId) {
        List<BrandReviews> brandReviews = brandReviewsRepository.findByBrands(brandId);
        return brandReviews.stream().map(brandReview -> brandReviewsMapper.convertEntityToBo(brandReview)).collect(Collectors.toList());
    }

    @Autowired
    CategoryRepository categoryRepository;
	
	/*public Set<CategoryBO> getAllCategory(String countryCode) {
		List<String> countryCodes = new ArrayList<>();
		countryCodes.add(countryCode);
		boolean isInternationalLogin = (countryCode != null && countryCode.equalsIgnoreCase("IN")) ? false : true;
		Set<CategoryBO> list = new TreeSet<CategoryBO>();
		List<Category> supperLevelCategory = categoryRepository.findByParentId(null);
		for (Category category : supperLevelCategory) {
			CategoryBO categoryBO = CategoryBO.builder().id(category.getId()).categoryName(category.getCategoryName())
					.categoryIcon(category.getCategoryIcon()).childrenCategories(new TreeSet<>()).build();
			addLastLevelOfCategory(categoryBO, category, isInternationalLogin, countryCodes);
			categoryBO.setChildrenCategories(categoryBO.getChildrenCategories());
			if (categoryBO.getChildrenCategories()!=null && categoryBO.getChildrenCategories().size() > 0) {
				list.add(categoryBO);
			}
		}
		return list;
	}*/

    public String getAllCategory(String countryCode, boolean isRefreshCache) {
        String jsonString = "";
        ObjectMapper mapper = new ObjectMapper();
        log.info("Inside method getAllCategory");
        Set<CategoryBO> list = new TreeSet<>();
        //log.info("Category cache value -"+cache.get(InventoryConstants.CATEGORY_CACHE_KEY));
        jsonString = cache.get(InventoryConstants.CATEGORY_CACHE_KEY);
        if (StringUtils.isNotBlank(jsonString) && !isRefreshCache) {
            log.info("Inside IF Condition Getting Data from Cache for getAllCategory");
            return jsonString;
        } else {
            log.info("Inside ELSE Condition did not get Data from Cache for getAllCategory");
            List<String> countryCodes = new ArrayList<>();
            countryCodes.add(countryCode);
            boolean isInternationalLogin = (countryCode != null && countryCode.equalsIgnoreCase("IN")) ? false : true;

            List<Category> supperLevelCategory = categoryRepository.findByParentId(null);
            for (Category category : supperLevelCategory) {
                //String[] parentImages = category.getCategoryIcon().split("#");
                List<String> imageList = new ArrayList<>();
                imageList = Stream.of(category.getCategoryIcon().split("#")).collect(Collectors.toList());
                String categoryIcon = "null";
                if(imageList.equals(null)) {
                    categoryIcon = category.getCategoryIcon();
                    imageList.add("No multiple image icons");
                }

//                CategoryBO categoryBO = CategoryBO.builder().id(category.getId()).categoryName(category.getCategoryName())
//                        .categoryIcon(category.getCategoryIcon()).childrenCategories(new TreeSet<>()).build();

                CategoryBO categoryBO = new CategoryBO();
                categoryBO.setId(category.getId());
                categoryBO.setCategoryName(category.getCategoryName());
                categoryBO.setCategoryIcon(categoryIcon);
                categoryBO.setChildrenCategories(new TreeSet<>());
                categoryBO.setParentImageList(imageList);

                addLastLevelOfCategory(categoryBO, category, isInternationalLogin, countryCodes);
                categoryBO.setChildrenCategories(categoryBO.getChildrenCategories());
                if (categoryBO.getChildrenCategories() != null && categoryBO.getChildrenCategories().size() > 0) {
                    //categoryBO.setParentImageList(imageList);
                    list.add(categoryBO);
                }
            }
            try {
                jsonString = mapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                log.error("exception while converting to json string " + e);
                e.printStackTrace();
            }
            log.info("jsonString - " + jsonString);
            cache.put(InventoryConstants.CATEGORY_CACHE_KEY, jsonString);
            return jsonString;
        }
    }


    public List<CategoryBO> getTopCategory() {
        List<CategoryBO> list = new ArrayList<>();
        List<Category> supperLevelCategory = categoryRepository.findByParentId(null);
        for (Category category : supperLevelCategory) {
            CategoryBO categoryBO = CategoryBO.builder()
                    .id(category.getId())
                    .categoryName(category.getCategoryName())
                    .categoryIcon(category.getCategoryIcon())
                    .childrenCategories(new HashSet<>())
                    .build();

            list.add(categoryBO);
        }
        return list.stream().sorted(Comparator.comparing(CategoryBO::getCategoryName)).collect(Collectors.toList());
    }

    public List<CategoryBO> getAllCategory(Long categoryId, String countryCode) {
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add(countryCode);
        boolean isInternationalLogin = (countryCode != null && countryCode.equalsIgnoreCase("IN")) ? false : true;
        List<CategoryBO> list = new ArrayList<>();
        List<Category> supperLevelCategory = categoryRepository.findByParentId(categoryId);
        for (Category category : supperLevelCategory) {
            CategoryBO categoryBO = CategoryBO.builder()
                    .id(category.getId())
                    .categoryName(category.getCategoryName())
                    .categoryIcon(category.getCategoryIcon())
                    .childrenCategories(new HashSet<>())
                    .categoryStage(category.getCategoryStage())
                    .build();

            addLastLevelOfCategory(categoryBO, category, isInternationalLogin, countryCodes);
            categoryBO.setChildrenCategories(categoryBO.getChildrenCategories().stream().sorted(Comparator.comparing(Category::getCategoryName)).collect(Collectors.toSet()));
            list.add(categoryBO);
        }
        return list;
    }

    @Autowired
    private ProductUploadService productUploadService;

    public Set<String> getLowestLevelCategory(String countryCode) {
        Set<String> lowestCategory = new HashSet<String>();
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add(countryCode);
        List<Category> supperLevelCategory = categoryRepository.findByParentId(null);
        for (Category category : supperLevelCategory) {
            lowestCategory.addAll(getLowestCategoryRecursively(category, lowestCategory));
        }

        return lowestCategory;
    }


    public Set<String> getLowestCategoryRecursively(Category category, Set<String> lowestCategory) {
        if (CollectionUtils.isEmpty(category.getChildrenCategories())) {
            try {
                lowestCategory.add(category.getCategoryName());
            } catch (Exception e) {
                log.error("ERROR while adding child category for checked condition ", e);
            }
        } else {
            for (Category cat : category.getChildrenCategories()) {
                if (CollectionUtils.isEmpty(cat.getChildrenCategories())) {
                    try {
                        lowestCategory.add(cat.getCategoryName());
                    } catch (Exception e) {
                        log.error("ERROR while adding child category for checked condition ", e);
                    }
                } else {
                    getLowestCategoryRecursively(cat, lowestCategory);
                }
            }
        }
        return lowestCategory;
    }


    public List<ProductInventoryBO> getAllBrandProduct(Long brandId, String displayCountryCode, String displayCurrencyCode) {
        Brands brands = validateBrandId(brandId);
        Supplier supplier = brands.getSupplier();
        if (supplier == null) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "No Supplier is associated with Brand", "No Supplier is associated with Brand");
        }

        List<ProductCombinations> productCombinationsList = productCombinationsRepository.findBySupplier(supplier);

        List<String> productUUIDList = productCombinationsList.stream().map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
        Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);

        return productCombinationsList.stream().map(product ->
                {
                    try {
                        return productUploadService.populateProductInventoryObj(product.getUniqueIdentifier(),
                                displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);
                    } catch (Exception e) {
                        log.error("Error while creating product obj :{}", product.getUniqueIdentifier(), e);
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }

    public void addLastLevelOfCategory(CategoryBO categoryBO, Category category, Boolean isInternational,
                                       List<String> countryCodes) {

        if (CollectionUtils.isEmpty(category.getChildrenCategories())) {
            try {
                long totalCountdisplayAll = prodViewRepo
                        .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(category.getId(),
                                Long.valueOf(0), 0)
                        .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                long totalCountdisplayInter = isInternational
                        ? prodViewRepo
                        .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(category.getId(),
                                Long.valueOf(0), 0)
                        .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count()
                        : 0;
                long totalCountdisplayWlc = prodViewRepo
                        .findByLowestCategoryAndStatusIdAndPcrWlcInAndAvailableStockGreaterThan(category.getId(),
                                Long.valueOf(0), countryCodes, 0)
                        .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                Long totalProductCountOfCategory = totalCountdisplayAll + totalCountdisplayInter + totalCountdisplayWlc;

                log.info("total product count totalCountdisplayAll " + totalCountdisplayAll + " totalCountdisplayInter "
                        + totalCountdisplayInter + " totalCountdisplayWlc " + totalCountdisplayWlc + " for category id "
                        + category.getId());
                if (totalProductCountOfCategory > 0) {
                    categoryBO.getChildrenCategories().add(category);
                }
            } catch (Exception e) {
                log.error("ERROR while adding child category for checked condition ", e);
            }

        } else {
            for (Category cat : category.getChildrenCategories()) {
                if (CollectionUtils.isEmpty(cat.getChildrenCategories())) {
                    try {
                        long totalCountdisplayAll = prodViewRepo
                                .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(cat.getId(),
                                        Long.valueOf(0), 0)
                                .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                        long totalCountdisplayInter = isInternational
                                ? prodViewRepo
                                .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(
                                        cat.getId(), Long.valueOf(0), 0)
                                .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count()
                                : 0;
                        long totalCountdisplayWlc = prodViewRepo
                                .findByLowestCategoryAndStatusIdAndPcrWlcInAndAvailableStockGreaterThan(cat.getId(),
                                        Long.valueOf(0), countryCodes, 0)
                                .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                        Long totalProductCountOfCategory = totalCountdisplayAll + totalCountdisplayInter
                                + totalCountdisplayWlc;
                        log.info("total product count totalCountdisplayAll " + totalCountdisplayAll
                                + " totalCountdisplayInter " + totalCountdisplayInter + " totalCountdisplayWlc "
                                + totalCountdisplayWlc + " for category id " + cat.getId());
                        if (totalProductCountOfCategory > 0) {
                            categoryBO.getChildrenCategories().add(cat);
                        }
                    } catch (Exception e) {
                        log.error("ERROR while adding child category for checked condition for ELSE ", e);
                    }
                } else {
                    addLastLevelOfCategory(categoryBO, cat, isInternational, countryCodes);
                }
            }
        }
    }

    public void addLastLevelOfCategoryNewImplementation(CategoryRS categoryRS, Category category, Boolean isInternational,
                                       List<String> countryCodes) {

        if (CollectionUtils.isEmpty(category.getChildrenCategories())) {
            try {
                long totalCountdisplayAll = prodViewRepo
                        .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(category.getId(),
                                Long.valueOf(0), 0)
                        .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                long totalCountdisplayInter = isInternational
                        ? prodViewRepo
                        .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(category.getId(),
                                Long.valueOf(0), 0)
                        .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count()
                        : 0;
                long totalCountdisplayWlc = prodViewRepo
                        .findByLowestCategoryAndStatusIdAndPcrWlcInAndAvailableStockGreaterThan(category.getId(),
                                Long.valueOf(0), countryCodes, 0)
                        .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                Long totalProductCountOfCategory = totalCountdisplayAll + totalCountdisplayInter + totalCountdisplayWlc;

                log.info("total product count totalCountdisplayAll " + totalCountdisplayAll + " totalCountdisplayInter "
                        + totalCountdisplayInter + " totalCountdisplayWlc " + totalCountdisplayWlc + " for category id "
                        + category.getId());
                if (totalProductCountOfCategory > 0) {
                    categoryRS.getChildrenCategories().add(category);
                }
            } catch (Exception e) {
                log.error("ERROR while adding child category for checked condition ", e);
            }

        } else {
            for (Category cat : category.getChildrenCategories()) {
                if (CollectionUtils.isEmpty(cat.getChildrenCategories())) {
                    try {
                        long totalCountdisplayAll = prodViewRepo
                                .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(cat.getId(),
                                        Long.valueOf(0), 0)
                                .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                        long totalCountdisplayInter = isInternational
                                ? prodViewRepo
                                .findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(
                                        cat.getId(), Long.valueOf(0), 0)
                                .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count()
                                : 0;
                        long totalCountdisplayWlc = prodViewRepo
                                .findByLowestCategoryAndStatusIdAndPcrWlcInAndAvailableStockGreaterThan(cat.getId(),
                                        Long.valueOf(0), countryCodes, 0)
                                .stream().filter(pv -> pv.getAvailableStock() >= pv.getMinOrderQuantity()).count();
                        Long totalProductCountOfCategory = totalCountdisplayAll + totalCountdisplayInter
                                + totalCountdisplayWlc;
                        log.info("total product count totalCountdisplayAll " + totalCountdisplayAll
                                + " totalCountdisplayInter " + totalCountdisplayInter + " totalCountdisplayWlc "
                                + totalCountdisplayWlc + " for category id " + cat.getId());
                        if (totalProductCountOfCategory > 0) {
                            categoryRS.getChildrenCategories().add(cat);
                        }
                    } catch (Exception e) {
                        log.error("ERROR while adding child category for checked condition for ELSE ", e);
                    }
                } else {
                    addLastLevelOfCategoryNewImplementation(categoryRS, cat, isInternational, countryCodes);
                }
            }
        }
    }

    @Transactional
    public BrandBO patch(BrandBO brandBo) {
        Brands brands = brandMapper.convertBoToEntity(brandBo);
        brands.setUpdatedOn( new Date(System.currentTimeMillis()));
        brands = brandsRepository.save(brands);
        // need to verify this
        // updateProductCombinations(brandBo.getSupplierId());
        return brandMapper.convertEntityToBo(brands);
    }

    public void flushCategoryCache() {
    	 log.info("Going to Flush Category Cache ");
        cache.remove(InventoryConstants.CATEGORY_CACHE_KEY);
        log.info("Flushed Category Cache Successfully!!!!!");
    }

    public ResponseEntity<?> addBrandToSupplier(String supplierEmail, String brandId) {
        String responseMsg = "";
        log.info("inside the add brand to supplier of brandService");
        if (brandId.equalsIgnoreCase("") || ObjectUtils.isEmpty(brandId)) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "brandId can't be empty", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        if (supplierEmail.equalsIgnoreCase("") || ObjectUtils.isEmpty(supplierEmail)) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "supplierId can't be empty", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        Supplier supplier = supplierRepository.findByEmail(supplierEmail);
        if (supplier == null) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "Supplier not found with this id " + supplierEmail, "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }

        SupplierBrands supplierBrands = supplierBrandsRespository.findBySupplierIdAndBrandId(String.valueOf(supplier.getId()), brandId);

        if (supplierBrands != null) {
            if (supplierBrands.getIsActive()) {
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "supplier is already added to this brand", supplierBrands);
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                supplierBrands.setIsActive(true);
                supplierBrands.setUpdatedOn(new Date());
                supplierBrandsRespository.save(supplierBrands);
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "supplier is  added to this brand successfully", "");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            }
        } else {

            try {
                SupplierBrands addSupplierBrands = new SupplierBrands();
                addSupplierBrands.setSupplierId(String.valueOf(supplier.getId()));
                addSupplierBrands.setBrandId(brandId);
                addSupplierBrands.setCreatedOn(new Date());
                addSupplierBrands.setIsActive(true);
                addSupplierBrands = supplierBrandsRespository.save(addSupplierBrands);
                CreateBrandModelCategory(addSupplierBrands.getSupplierId(), addSupplierBrands.getBrandId());

                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "supplier is  added to this brand successfully", addSupplierBrands);
                return new ResponseEntity<>(successResponse, HttpStatus.OK);

            } catch (Exception e) {
                log.error("error occur while adding brand into supplier", e);
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "something went wrong try again", "");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            }
        }


    }

    public ResponseEntity<?> removeBrandToSupplier(String supplierId, String brandId) {
        String responseMsg = "";
        log.info("inside the add brand to supplier of brandService");
        if (brandId.equalsIgnoreCase("") || ObjectUtils.isEmpty(brandId)) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "brandId can't be empty", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        if (supplierId.equalsIgnoreCase("") || ObjectUtils.isEmpty(supplierId)) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "supplierId can't be empty", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        SupplierBrands supplierBrands = supplierBrandsRespository.findBySupplierIdAndBrandId(supplierId, brandId);

        if (supplierBrands != null) {
            supplierBrands.setIsActive(false);
            supplierBrands.setUpdatedOn(new Date());
            supplierBrandsRespository.save(supplierBrands);
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "supplier is  removed from this brand successfully", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } else {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "supplier is  not available for this brand", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }


    }

    public Object getSupplierByBrand(String brandId,String sortOrder, Integer pageNum, Integer pageSize) throws Exception {
        log.info("inside the add brand to supplier of brandService");

        Pageable pageable = null;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createdOn"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize);
        }

        if (brandId.equalsIgnoreCase("") || ObjectUtils.isEmpty(brandId)) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_IMPLEMENTED.value(), "brandId can't be empty", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }

        Page<SupplierBrands> supplierBrandsList = supplierBrandsRespository.findByBrandIdAndIsActiveTrue(brandId,pageable);
        List<Long> supplierIds = new ArrayList<>();
        supplierBrandsList.getContent().stream().forEach(sb -> {
            supplierIds.add(Long.valueOf(sb.getSupplierId()));
        });
        log.info("going for get the supp");
        //List<Supplier>
        FetchAllSupplierRQ fetchAllSupplierRQ = new FetchAllSupplierRQ();
        fetchAllSupplierRQ.setSupplierProfileIdList(supplierIds);

        RestApiResponse res= (RestApiResponse) supplierService.getAllSupplier(fetchAllSupplierRQ);
        Map<String, List<SupplierBo>> node = (Map<String, List<SupplierBo>>) res.getData();
           List<SupplierBo> boList=(List<SupplierBo>)node.get("supplierList");
        Page<SupplierBo> page = new PageImpl<>(boList, supplierBrandsList.getPageable(), supplierBrandsList.getTotalPages());
           RestApiSuccessResponse successResponse=new RestApiSuccessResponse(HttpStatus.OK.value(), "supplierList",page);
        return successResponse;


    }

    public Object getBrandInfoById(String brandId) {

        log.info("inside the getBrandInfo by brandId::" + brandId);

        if (brandId.equalsIgnoreCase("") || ObjectUtils.isEmpty(brandId)) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "brandId can't be empty", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }

        Optional<Brands> brands = brandsRepository.findById(Long.valueOf(brandId));
        if (!brands.isPresent()) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(), "No brand is available with this brandId", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        log.info("going to convert brand to BrandBo");
        try {
            BrandBO brandBO = brandMapper.convertEntityToBo(brands.get());

            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "data fetched successfully", brandBO);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {

            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(), "SomeThing is wrong with the brand! try again", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        }

    }

    public Object updateSortOrderAndFeatured(String brandId, String sortOrder, Boolean isFeatured) {
        log.info("inside the updateSortOrderAndFeatured by brandId::" + brandId);
        Boolean isFeaturedUpdate = false;
        Boolean isSortOrderUpdate = false;
        String responseMsg = "";

        if (brandId.equalsIgnoreCase("") || ObjectUtils.isEmpty(brandId)) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "brandId can't be empty", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        try {

            Optional<Brands> brands = brandsRepository.findById(Long.valueOf(brandId));
            if (!brands.isPresent()) {
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(), "No brand is available with this brandId", "");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            }

            Brands sortOrdered=brandsRepository.findBySortOrder(Long.valueOf(sortOrder));
            if (sortOrdered==null&&sortOrder != null && !sortOrder.equalsIgnoreCase("") && !Objects.equals(brands.get().getSortOrder(), Long.valueOf(sortOrder))) {
                brands.get().setSortOrder(Long.valueOf(sortOrder));
                isSortOrderUpdate = true;
            }else {
                isSortOrderUpdate = false;
            }
            if (isFeatured != null){
               if (brands.get().getIsFeatured()!=null)
               {
                 if ((brands.get().getIsFeatured().booleanValue() != isFeatured)){
                     brands.get().setIsFeatured(isFeatured);
                     isFeaturedUpdate = true;
                 }

               }
               else {
                   brands.get().setIsFeatured(isFeatured);
                   isFeaturedUpdate = true;
               }
            }


            if (isFeaturedUpdate && isSortOrderUpdate) {
                brandsRepository.save(brands.get());
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "updated successlly", "");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else if (isFeaturedUpdate) {
                brandsRepository.save(brands.get());
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "is Feature updated successlly", "");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else if (isSortOrderUpdate) {
                brandsRepository.save(brands.get());
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "sort order updated successlly", "");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                if (!isFeaturedUpdate)
                {
                    RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "SortOrder should be unique", "");
                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
                }
                RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Nothing to be updated", "");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            }

        } catch (Exception e) {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.NOT_FOUND.value(), "something went wrong while updating the sort order", "");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }


    }

    public List<Map<String, Object>> getAllCategoriesAndSubCategories(String countryCode) {
    	List<Map<String, Object>> mapList = new ArrayList<>();
        List<Category> parentLevelCategories = categoryRepository.findByParentId(null);
        parentLevelCategories.stream().forEach(plc->{
            Map<String, Object> map = new HashMap<>();
        	map.put("id",plc.getId());
        	map.put("categoryName",plc.getCategoryName());
        	map.put("categoryIcon",plc.getCategoryIcon());
        	map.put("childrenCategories",getAllCategoryForNewCategories(plc, countryCode));
        	map.put("categoryStage","stage1Category");
        	mapList.add(map);
        });

        return mapList;
    }

    public CategoryBO getAllCategoryForNewCategories(Category category, String countryCode) {
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add(countryCode);
        boolean isInternationalLogin = (countryCode != null && countryCode.equalsIgnoreCase("IN")) ? false : true;
      //  CategoryRS  new ArrayList<>();
      
//        CategoryBO categoryRS = CategoryRS.builder()
//                    .id(category.getId())
//                    .categoryName(category.getCategoryName())
//                    .categoryIcon(category.getCategoryIcon())
//                    .childrenCategories(new HashSet<>())
//                    .categoryStage(category.getCategoryStage())
//                    .build();
//            
        return       CategoryBO.builder().id(category.getId())
			.categoryName(category.getCategoryName()).parent(category.getParent()).childrenCategories(category.getChildrenCategories()).categoryStage(category.getCategoryStage())
			.build();

//            addLastLevelOfCategoryNewImplementation(categoryRS, category, isInternationalLogin, countryCodes);
//            categoryRS.setChildrenCategories(categoryRS.getChildrenCategories().stream().sorted(Comparator.comparing(Category::getCategoryName)).collect(Collectors.toSet()));
           // list.add(categoryRS);
        
         
    }

    public Object getBrandByBrandName(String brandName, Integer pageNum, Integer pageSize, String sortOrder){
        try{
            Page<Brands> brandList = brandsRepository.getBrandsByBrandName(brandName.toLowerCase(), getPagableForBrands(pageNum, pageSize, sortOrder));
            if(ObjectUtils.isEmpty(brandList)){
                log.info("getBrandByBrandName method returned no data with brand name :{}", brandName);
                return new RestApiSuccessResponse(HttpStatus.OK.value(), "No brand found", null);
            }
            else{
                log.info("getBrandByBrandName method returns with {} no of data with brand name {}", brandList.get().count(), brandName);
                Page<BrandBO> brandBOList = null;
                brandBOList = brandList.map(new Function<Brands, BrandBO>() {
                    @Override
                    public BrandBO apply(Brands brands) {
                        BrandBO bo = new BrandBO();
                        try {
                            bo = brandMapper.convertEntityToBo(brands);
                        } catch (Exception e) {
                            log.error("Error while mapping brand list to brand list BO in getBrandByBrandName() ", e);
                        }
                        return bo;
                    }
                });
                return new RestApiSuccessResponse(HttpStatus.OK.value(), "Brand Fetched Successfully!!", brandBOList);
            }
        }catch(Exception e){
            log.error("something went wrong while fetching brand by brand name : {}", e);
            return new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "something went wrong while fetching brand by brand name", null);
        }
    }
}