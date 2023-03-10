package com.kb.catalogInventory.service;

import com.kb.catalogInventory.datatable.Brands;
import com.kb.catalogInventory.datatable.Groups;
import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.Supplier;
import com.kb.catalogInventory.mapper.BrandMapper;
import com.kb.catalogInventory.mapper.GroupMapper;
import com.kb.catalogInventory.mapper.SupplierMapper;
import com.kb.catalogInventory.model.*;
import com.kb.catalogInventory.repository.*;
import com.kb.java.utils.KbRestTemplate;
import com.kb.java.utils.RestApiErrorResponse;
import com.kb.java.utils.RestApiSuccessResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.ListUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class SupplierService {
	@Autowired
	BrandMapper brandMapper;
	@Autowired
	GroupService groupService;
	@Autowired
	GroupsRespository groupsRespository;
	@Autowired
	GroupMapper groupMapper;

	@Autowired
	BrandsRepository brandsRepository;
	@Autowired
	private SupplierRepository supplierRepo;

	@Autowired
	private ProductCombinationsRepository productCombRepo;

	@Autowired
	SupplierMapper supplierMapper;

	@Autowired
	KbRestTemplate restTemplate;

	@Autowired
	ProductCombinationsRepository productCombinationsRepo;

	@Value("${login.service.user.profile.by.contacts.url}")
	private String getUserProfileByContactsUrl;

	@Value("${cart.service.product.removal.wishlist.cart.url}")
	private String getProuctRemovalFromWishlistAndCartUrl;


	@Value("${gst.number.validation.url}")
	private String gstNumberValidationUrl;

	@Value("${gst.number.validation.url.timeout}")
	private int gstNumberValidationUrlTimeout;

	@Value("${gst.number.validation.api.key}")
	private String gstNumberValidationApiKey;


	@Autowired
	private ProductViewRepository productViewRepo;

	public Supplier createSupplier(CreateSupplierRQ request) throws Exception {
		Supplier supplier = null;
		supplier=supplierRepo.findByEmail(request.getEmail());
		if(null==supplier) {
			supplier= new Supplier();
			supplier.setSupplierName(request.getSupplierName());// chnaging for name
			supplier.setEmail(request.getEmail());
			supplier.setCreatedOn(new Date());
			supplier.setUpdatedOn(new Date());
			supplier.setActive(true);
			supplier.setFirstName(request.getFirstName());
			supplier.setLastName(request.getLastName());
			supplier.setDirectShipment(true);
			supplier.setMovPrice(Double.valueOf(5000));
			supplier.setSupplierMovApplicable(false);
			supplier.setSupplierAdditionDiscountApplicable(false);
			
			supplier = supplierRepo.save(supplier);
			
		}else{
			throw new Exception("Supplier Already exists with emailId "+request.getEmail());
		}
		return supplier;
	}

	public Supplier updateSupplier(long supplierId, UpdateSupplierRQ request) throws Exception{
		Supplier supplier = supplierRepo.findById(supplierId).get();
		if(ObjectUtils.isEmpty(supplier))
			throw new Exception("Invalid supplier id");
		supplier.setId(supplierId);
		supplier.setSupplierName(request.getSupplierName());
		supplier.setSupplierAddress(request.getSupplierAddress());
		supplier.setGstOrUdyogNumber(request.getGstOrUdyogNumber());
		supplier.setAddressType(request.getAddressType());
		supplier.setEmail(request.getEmail());
		supplier.setPhone(request.getPhone());
		supplier.setAddress2(request.getAddress2());
		supplier.setCity(request.getCity());
		supplier.setState(request.getState());
		supplier.setCountry(request.getCountry());
		supplier.setPin_code(request.getPinCode());
		supplier.setUpdatedOn(new Date());
		supplier.setGender(request.getGender());
		supplier.setFirstName(request.getFirstName());
		supplier.setLastName(request.getLastName());
		supplier.setLegalname(request.getLegalnm());
		supplier.setShippingPickUpLocId(request.getShipmentPickUpLocationId());
		supplier.setKbPickup(request.getIsKbPickup()!=null?request.getIsKbPickup():false);
		if (!ObjectUtils.isEmpty(request.getIsCustomSupplierInvoiceNum())){
			supplier.setIsCustomSupplierInvoiceNum(request.getIsCustomSupplierInvoiceNum());
		}
		StringBuilder groupsString=new StringBuilder();
		StringBuilder phoneString=new StringBuilder();

		 int count=0;
		 if(!CollectionUtils.isEmpty(request.getGroupList())) {
			 for (GroupsBo groupsBo:request.getGroupList()){
	                if (count<request.getGroupList().size()-1) {
						groupsString.append(groupsBo.getId());
						groupsString.append(",");
						phoneString.append(groupsBo.getPhoneString());
						phoneString.append(",");
					}else {
						groupsString.append(groupsBo.getId());
						phoneString.append(groupsBo.getPhoneString());
					}
					++count;

			 }
		 }
		 supplier.setGroupString(String.valueOf(groupsString));
		 supplier.setMovPrice(Double.valueOf(5000));
			supplier.setSupplierMovApplicable(false);
			supplier.setSupplierAdditionDiscountApplicable(false);

		supplier = supplierRepo.save(supplier);
		try {
			if (!ObjectUtils.isEmpty(phoneString.toString())){
				removeProductsFromCartAndWishlist(phoneString.toString(), Collections.singletonList(supplier));
			}
		} catch (Exception e){
			log.info("Exception Occurred while removing products from cart and wishlist");
		}

		if(!CollectionUtils.isEmpty(request.getGroupList())) {
			productCombinationsRepo.updateUpdatedOnDateBasedProducts(Collections.singletonList(supplier));
		}
		return supplier;
	}

	public String updateSupplierNameByFirstNameLastName() throws Exception{


		List<Supplier> supplierList=supplierRepo.findAll();
		   supplierList.stream().forEach(sp->{
			 sp.setSupplierName(sp.getFirstName()+" "+sp.getLastName());
			 supplierRepo.save(sp);
		   });


		return "updated successfully";
	}

	public List<Supplier> fetchSuppliersByGroupString(String groupString) {
		List<Supplier> supplierList = supplierRepo.findByGroupStringLike(groupString);
		if (!CollectionUtils.isEmpty(supplierList)) {
			return supplierList;
		} else {
			return new ArrayList<>();
		}
	}

	@Async
	public void removeProductsFromCartAndWishlist(String phoneString, List<Supplier> supplierList) throws JSONException {
		log.info("Inside removeProductsFromCartAndWishlist with phoneString:{}", phoneString);
		List<String> phoneNumberList = new ArrayList<>();
		List<Integer> userProfileIdList = new ArrayList<>();
		//TODO: Since currently supported for india appending +91 if changed then have to populate accordingly
		Stream.of(phoneString.split(",", -1))
				.collect(Collectors.toList()).forEach(phone -> phoneNumberList.add("+91" + phone));
		if (!CollectionUtils.isEmpty(phoneNumberList)) {
			String userDetails = restTemplate.postForEntity(getUserProfileByContactsUrl,
					UserProfileByPhoneNoRQ.builder().phoneNoList(phoneNumberList).build(), 60_000, String.class).getBody();
			assert userDetails != null;
			JSONObject userDetailsObject = new JSONObject(userDetails);
			JSONArray dataArr = userDetailsObject.getJSONArray("data");
			if (dataArr != null && dataArr.length() > 0) {
				for (int index = 0; index < dataArr.length(); index++) {
					JSONObject userObj = dataArr.getJSONObject(index);
					userProfileIdList.add(userObj.getInt("userProfileId"));
				}
			}
		}
		if (!CollectionUtils.isEmpty(phoneNumberList)) {
			List<String> productIdList = new ArrayList<>();
			List<ProductCombinations> productCombinationsList = productCombRepo.findBySupplierIn(supplierList);
			if (!CollectionUtils.isEmpty(productCombinationsList)) {
				productIdList = productCombinationsList.stream()
						.map(ProductCombinations::getUniqueIdentifier).collect(Collectors.toList());
			}
			if (!CollectionUtils.isEmpty(userProfileIdList) && !CollectionUtils.isEmpty(productIdList)) {
				log.info("Calling cart-service to remove the products from cart and wishlist with userIds:{} " +
						"and productIds:{}", userProfileIdList, productIdList);
				restTemplate.postForEntity(getProuctRemovalFromWishlistAndCartUrl,
						CartWishlistProductRemovalRQ.builder()
								.notInUserProfileIds(userProfileIdList)
								.productIdList(productIdList)
								.build(), 60_000, String.class);
			} else {
				log.info("userIds and productIds are empty cannot remove products from cart and wishlist");
			}
		}
	}

	public Object getAllSupplier(FetchAllSupplierRQ request) throws Exception {
		List<Supplier> supplierList = supplierRepo.findAllByIdList(request.getSupplierProfileIdList());
		List<SupplierBo> supplierBoList= supplierList.stream().map(supplier->{

			SupplierBo supplierBo =supplierMapper.convertSupplierToBo(supplier);
			if (supplier.getGroupString()!=null &&!org.apache.commons.lang3.ObjectUtils.isEmpty(supplier.getGroupString())){
				supplierBo.setGroups(getGroupsFromDb(supplier.getGroupString()));
			}
			List<Brands> brands = brandsRepository.findBrandBySupplier(supplier);
			List<BrandBO> brandBOList=new ArrayList<>();
			if (brands != null&& brands.size()>0) {
				brands.stream().forEach(bd->{
					brandBOList.add(brandMapper.convertEntityToBo(bd));
				});
				supplierBo.setBrandBOList(brandBOList);

			}
			return supplierBo;
		}).collect(Collectors.toList());
		Map<String, List<SupplierBo>> node = new HashMap<>();
		node.put("supplierList", supplierBoList);
		return new RestApiResponse(System.currentTimeMillis(), "200", "Success", node);
	}
	public Object getSupplier(String Id) throws Exception {

		Supplier supplier = null;
		try {
			supplier=supplierRepo.findById(Long.valueOf(Id)).get();
		}catch (Exception e){
			return new ResponseEntity<>("Supplier not found",HttpStatus.OK);
		}

		SupplierBo supplierBo =supplierMapper.convertSupplierToBo(supplier);
		List<Brands> brands = brandsRepository.findBrandBySupplier(supplier);
		List<BrandBO> brandBOList=new ArrayList<>();
		if (brands != null&& brands.size()>0) {
			brands.stream().forEach(bd->{
				brandBOList.add(brandMapper.convertEntityToBo(bd));
			});
			supplierBo.setBrandBOList(brandBOList);

		}

		Map<String,SupplierBo> node = new HashMap<>();
		node.put("supplier", supplierBo);
		return new RestApiResponse(System.currentTimeMillis(), "200", "Success", node);
	}
	public Object getSupplierByEmail(String emailId) throws Exception {

		Supplier supplier = null;
		try {
			supplier=supplierRepo.findByEmail(emailId);
		}catch (Exception e){
			return new ResponseEntity<>("Supplier not found",HttpStatus.OK);
		}

		SupplierBo supplierBo =supplierMapper.convertSupplierToBo(supplier);
		List<Brands> brands = brandsRepository.findBrandBySupplier(supplier);
		List<BrandBO> brandBOList=new ArrayList<>();
		if (brands != null&& brands.size()>0) {
			brands.stream().forEach(bd->{
				brandBOList.add(brandMapper.convertEntityToBo(bd));
			});
			supplierBo.setBrandBOList(brandBOList);

		}

		Map<String,SupplierBo> node = new HashMap<>();
		node.put("supplier", supplierBo);
		return new RestApiResponse(System.currentTimeMillis(), "200", "Success", node);
	}
	public Object SaveLegalName(String Id,String Name) throws Exception {

		Supplier supplier = null;
		try {
			supplier=supplierRepo.findById(Long.valueOf(Id)).get();
			supplier.setLegalname(Name);
			supplierRepo.save(supplier);
		}catch (Exception e){
			return new ResponseEntity<>("Supplier not found",HttpStatus.OK);
		}

		SupplierBo supplierBo =supplierMapper.convertSupplierToBo(supplier);
		List<Brands> brands = brandsRepository.findBrandBySupplier(supplier);
		List<BrandBO> brandBOList=new ArrayList<>();
		if (brands != null&& brands.size()>0) {
			brands.stream().forEach(bd->{
				brandBOList.add(brandMapper.convertEntityToBo(bd));
			});
			supplierBo.setBrandBOList(brandBOList);

		}

		Map<String,SupplierBo> node = new HashMap<>();
		node.put("supplier", supplierBo);
		return new RestApiResponse(System.currentTimeMillis(), "200", "Success", node);
	}

	public ResponseEntity<?> getSupplierBySearch(String SearchColumn, String searchkeyword){
		List<Supplier> supplierList=null;
		List<Supplier> supplierFilteredList=null;
		    if (!ObjectUtils.isEmpty(SearchColumn))
			{
			 if (SearchColumn.equalsIgnoreCase("firstname")){
				 supplierList=supplierRepo.searchByFirstName(searchkeyword);

			 }else if (SearchColumn.equalsIgnoreCase("lastname")) {
				 supplierList=supplierRepo.searchByLastName(searchkeyword);

			 }
			else if (SearchColumn.equalsIgnoreCase("email")) {
				 supplierList=supplierRepo.searchByEmail(searchkeyword);
			 }
			else if (SearchColumn.equalsIgnoreCase("phone")) {

				 supplierList=supplierRepo.searchByPhone(searchkeyword);
			}

			List<SupplierBo> supplierBoList= supplierList.stream().map(supplier->{
					SupplierBo supplierBo =supplierMapper.convertSupplierToBo(supplier);
				List<Brands> brands = brandsRepository.findBrandBySupplier(supplier);
				List<BrandBO> brandBOList=new ArrayList<>();
				if (brands != null&& brands.size()>0) {
					brands.stream().forEach(bd->{
						brandBOList.add(brandMapper.convertEntityToBo(bd));
					});
					supplierBo.setBrandBOList(brandBOList);

				}
					return supplierBo;
				}).collect(Collectors.toList());
				Map<String, List<SupplierBo>> node = new HashMap<>();
				node.put("supplierList", supplierBoList);
				 RestApiSuccessResponse s=new RestApiSuccessResponse(HttpStatus.OK.value(), "supplierlist",node);
				return new ResponseEntity<>(s,HttpStatus.OK);

			}else {

				RestApiErrorResponse errorResponse=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), " Search Column Can't be blank","");
				return new ResponseEntity<>(errorResponse,HttpStatus.NOT_ACCEPTABLE);

			}







	}




	public Map<String, FinalPriceAndSupplierDetailModel> getsupplierDetailFromProduct(List<String> prodCombIds) {
		Map<String, FinalPriceAndSupplierDetailModel> res = new HashMap<>();
		prodCombIds.stream().forEach(UUID -> {
			try {
			long pc1 = System.currentTimeMillis();
			List<Object[]> resultSet = productCombRepo.findSupplierByUniqueIdentifier(UUID);
			long pc2 = System.currentTimeMillis();
			log.info("time taken in finding pc ---->" + (pc2 - pc1));
			long sup1 = System.currentTimeMillis();
		/*	log.info("getsupplierDetailFromProduct index 1"+resultSet.get(0)[1]);
			log.info("getsupplierDetailFromProduct index 2"+resultSet.get(0)[2]);
			log.info("getsupplierDetailFromProduct index 3"+resultSet.get(0)[3]);
			log.info("getsupplierDetailFromProduct index 4"+resultSet.get(0)[4]);
			log.info("getsupplierDetailFromProduct index 5"+resultSet.get(0)[5]);
			log.info("getsupplierDetailFromProduct index 6"+resultSet.get(0)[6]);
			log.info("getsupplierDetailFromProduct index 0"+resultSet.get(0)[0]);
			log.info("getsupplierDetailFromProduct index 0"+resultSet.get(0).toString());*/
			
			Supplier supplier = supplierRepo.findByIdAndIsActive((Long) resultSet.get(0)[0], true);
			Long brands=	productViewRepo.getBrandOfProduct(UUID);
			long sup2 = System.currentTimeMillis();
			FinalPriceAndSupplierDetailModel finalPriceAndSupplierDetailModel = new FinalPriceAndSupplierDetailModel(
					(double)((Float) resultSet.get(0)[1]), supplier, (Float) resultSet.get(0)[2], (Float) resultSet.get(0)[3],(Float) resultSet.get(0)[4],
					(Float) resultSet.get(0)[5],(Float) resultSet.get(0)[6],supplier.getKbPickup());
			log.info("time taken in finding supplier ---->" + (sup2 - sup1));
				finalPriceAndSupplierDetailModel.setProductBrandId(brands);
			res.put(UUID, finalPriceAndSupplierDetailModel);
			}catch (Exception e) {
				log.error("Exception while fetching supplierDetail from product --- ",e);
			}
		});

		return res;
	}
	
	public Map<String, FinalPriceAndSupplierDetailModel> getAllSupplierDetailFromProduct(List<String> prodCombIds) {
		Map<String, FinalPriceAndSupplierDetailModel> res = new HashMap<>();
		prodCombIds.stream().forEach(UUID -> {
			try {
			long pc1 = System.currentTimeMillis();
			List<Object[]> resultSet = productCombRepo.findSupplierByUniqueIdentifier(UUID);
			long pc2 = System.currentTimeMillis();
			log.info("time taken in finding pc ---->" + (pc2 - pc1));
			long sup1 = System.currentTimeMillis();		
			Supplier supplier = supplierRepo.findById((Long) resultSet.get(0)[0]).get();
			Long brands=productViewRepo.getBrandOfProduct(UUID);
			long sup2 = System.currentTimeMillis();
			FinalPriceAndSupplierDetailModel finalPriceAndSupplierDetailModel = new FinalPriceAndSupplierDetailModel(
					(double)((Float) resultSet.get(0)[1]), supplier, (Float) resultSet.get(0)[2], (Float) resultSet.get(0)[3],(Float) resultSet.get(0)[4],
					(Float) resultSet.get(0)[5],(Float) resultSet.get(0)[6],supplier.getKbPickup());
			log.info("time taken in finding supplier ---->" + (sup2 - sup1));
				finalPriceAndSupplierDetailModel.setProductBrandId(brands);
			res.put(UUID, finalPriceAndSupplierDetailModel);
			}catch (Exception e) {
				log.error("Exception while fetching supplierDetail from product --- ",e);
			}
		});

		return res;
	}
	
	
	
	public Object getSupplierList(List<String> supplierIdList) throws Exception {

		Map<String, SupplierBo> supplierDetailList = new HashMap<>();
		if (supplierIdList != null && supplierIdList.size() > 0) {
			supplierIdList.stream().forEach(id -> {
				Supplier supplier = supplierRepo.findById(Long.valueOf(id)).get();
				if (supplier != null) {
					SupplierBo supplierBo = supplierMapper.convertSupplierToBo(supplier);
					List<Brands> brands = brandsRepository.findBrandBySupplier(supplier);
					List<BrandBO> brandBOList=new ArrayList<>();
					if (brands != null&& brands.size()>0) {
					  brands.stream().forEach(bd->{
						  brandBOList.add(brandMapper.convertEntityToBo(bd));
					  });
					  supplierBo.setBrandBOList(brandBOList);

					}
					supplierDetailList.put(id, supplierBo);
				} else {
					supplierDetailList.put(id, null);
				}
			});
			return new RestApiResponse(System.currentTimeMillis(), "200", "Success", supplierDetailList);
		} else {
			return new ResponseEntity<>("Supplier Id List is empty", HttpStatus.OK);
		}

	}


	public List<GroupsBo> getGroupsFromDb(String groupString){
	  String[] 	groupid=groupString.split(",");
		List<GroupsBo> groups=new ArrayList<>();
	  for (String s:groupid)
	  {
		  try{
			  Groups groupsById=groupsRespository.getById(Long.valueOf(s));
			  if(groupsById!=null) {
				  groups.add(groupMapper.map(groupsById));
			  }
		  }catch (Exception e)
		  {

		  }


	  }

	  return groups;


	}


	public Object getAllSupplier() throws Exception {
		List<Object []> suppMap=	supplierRepo.getAllSupplierEmailToIdMap();
		List<Map<String ,Object>> suppDropDownList=new ArrayList<>();
		suppMap.stream().forEach(am->{
			Map<String ,Object> suppDropDown= new HashMap<>();
			suppDropDown.put("key",am[1]);
			suppDropDown.put("value",am[0]);
			suppDropDownList.add(suppDropDown);
		});
		return new RestApiResponse(System.currentTimeMillis(), "200", "Success", suppDropDownList);
	}

	public Object getBrandsOfSupplier(Long supplierId) throws Exception {
		List<Object []>	brands =brandsRepository.findBrandIdsOfSupplier(supplierId);
		List<Map<String ,Object>> brandDdList=new ArrayList<>();
		brands.stream().forEach(am->{
			Map<String ,Object> brandDd=new HashMap<>();
			brandDd.put("key",am[1]);
			brandDd.put("value",am[0]);
			brandDdList.add(brandDd);
		});
		return new RestApiResponse(System.currentTimeMillis(), "200", "Success", brandDdList);
	}

	public Object getEmailOfSupplier(Long supplierId) throws Exception {
		List<String> emailIdOfSupplier = supplierRepo.getEmailIdOfSupplier(supplierId);
		return new RestApiResponse(System.currentTimeMillis(), "200", "Success", emailIdOfSupplier);
	}


	public  void UpdateSupplierLegalName(){

		List<Supplier> allSupplier=supplierRepo.findAll();

		allSupplier.stream().forEach(supplier -> {
			if (supplier.getGstOrUdyogNumber()!=null){
				try {
					Map<String,Object> res=validateGstNumber(supplier.getGstOrUdyogNumber());
					supplier.setLegalname((String) ((Map<String ,Object>) res.get("data")).get("lgnm"));
					supplierRepo.save(supplier);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		});


	}




	public Map<String, Object> validateGstNumber(String gstNumber) throws Exception {
		log.info("Entered validateGstNumber with gstNumber {}",gstNumber);
		if (ObjectUtils.isEmpty(gstNumber))
			throw new Exception("gstNumber cannot be empty");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String fetchSupplierProfileUrl = gstNumberValidationUrl+gstNumberValidationApiKey+"/"+gstNumber;
		log.info("fetchSupplierProfileUrl {}",fetchSupplierProfileUrl);
		HttpEntity entity = new HttpEntity(headers);
		ResponseEntity<Map> result = restTemplate.exchange(
				fetchSupplierProfileUrl, HttpMethod.GET, entity,gstNumberValidationUrlTimeout, Map.class);
		if(result.getStatusCode()== HttpStatus.OK){
			Map<String, Object> rs=(Map<String,Object>)result.getBody();
			boolean flag =(boolean) rs.get("flag");
			String message=(String)rs.get("message");
			log.info("flag for first try ::::: "+ flag);
			if(!flag && message.equalsIgnoreCase("Retry API")) {
				ResponseEntity<Map> resultRetry = restTemplate.exchange(
						fetchSupplierProfileUrl, HttpMethod.GET, entity,gstNumberValidationUrlTimeout, Map.class);
				if(result.getStatusCode()== HttpStatus.OK){
					rs = (Map<String,Object>)resultRetry.getBody();;
					flag = (boolean)rs.get("flag");
				}
			}
			log.info("flag for second  try ::::: "+ flag);
			return rs;
		}
		Map<String,Object> res = new HashMap<String, Object>();
		res.put("flag", false);
		return res;

	}


}
