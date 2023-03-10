package com.kb.catalogInventory.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.kb.catalogInventory.datatable.ProductView;
import com.kb.catalogInventory.repository.ProductViewRepository;
import com.kb.java.utils.KbRestTemplate;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.log4j.Log4j2;
@Component
@Log4j2
public class WhatsAppIntegrationService {

    @Autowired
    private KbRestTemplate restTemplate;
    
    @Autowired
    private ProductViewRepository productViewRepository;

	public static final String SID1 = "AC89893834b323a35086e3a52e9becbb97";	
	public static final String TOKEN1 ="5bab4cceae9a31d567f529f521c2dd7c";
	
	String addToCartUrl = "https://dev.konnectbox.in/cart-service/cart/addToCart/";
	String getUserProfile = "https://dev.konnectbox.in/login-service/userProfile";
	String verifyPhone = "https://dev.konnectbox.in/login-service/verifyphone/";
	String verifyOTP = "https://dev.konnectbox.in/login-service/verifyOtp";
	
	public String fetchProductsForWhatsApp(Map<String, String> paramMap) {
		log.info("fetchProductsForWhatsApp called from twilio incoming message with request : "+paramMap.toString());
		log.info("fetchProductsForWhatsApp called from twilio incoming message with request : "+paramMap);
		String pdfUrl = "https://images.konnectbox.in/konnectbox/1644992623997/FILE/ed43d0ff-dca8-4a82-b551-e9ae0c5a6607/REKHA.pdf";
		List<URI> uriList = new ArrayList<>();
			uriList.add(URI.create(pdfUrl));
			String imageUrl = "https://images.konnectbox.in/konnectbox/1645096438386/FILE/69d982ab-5789-400b-b67d-a160e5f028d5/sarees.jpg";
			List<URI> uriList1 = new ArrayList<>();
				uriList1.add(URI.create(imageUrl));
			Twilio.init(SID1, TOKEN1);	
			Message message = Message.fetcher(paramMap.get("MessgaeSid")).fetch();
			log.info(message.getBody());
			
			ResourceSet<Message> messages = Message.reader().read();
			for(Message msg : messages){
				if(msg.getFrom().toString().equalsIgnoreCase(paramMap.get("From"))) {
					log.info(msg.getBody());
				}
			}
			
		if(!(paramMap.get("Body").contains("FETCH PRODUCT:")|| paramMap.get("Body").contains("ADD TO CART")|| paramMap.get("Body").contains("OTP"))) {
			try {
				log.info("fetchProductsForWhatsApp called from twilio incoming message with request : ",paramMap);
				String name ="wasif";
			      Message.creator(new PhoneNumber(paramMap.get("From")),
			            new PhoneNumber(paramMap.get("To")), 
			            "Delivery Successful!\r\n"
			            + "Enjoy your new products "+name+", . Thanks for shopping with Konnectbox.For today’s deal: ").create();
			    Message.creator(new PhoneNumber(paramMap.get("From")),
			            new PhoneNumber(paramMap.get("To")), 
			            uriList).create();
			
			}catch(Exception e) {
				log.info("Exception inside fetchProductsForWhatsApp  : ",e);
			}
		}
		if(paramMap.get("Body").contains("FETCH PRODUCT:")) {
			String sizes = "M,L,XL,XXL";
			int totalSizesAvailable = 4;
			ProductView pv = getProductDetail(paramMap.get("Body").split(":")[1]);
			 Message.creator(new PhoneNumber(paramMap.get("From")),
		            new PhoneNumber(paramMap.get("To")), 
		            uriList1).create();
			 Message.creator(new PhoneNumber(paramMap.get("From")),
		            new PhoneNumber(paramMap.get("To")), 
		            pv.getProductDescription()+"\r\n"
		            +pv.getPrice()+" per pc\r\n"
		            +"MOQ : 1 SET("+pv.getSetPieces()+") Set of "+totalSizesAvailable+" sizes \r\n"+sizes).create();
		}
		if(paramMap.get("Body").contains("ADD TO CART")) {
			String URL = verifyPhone+paramMap.get("From").split(":")[1];
			ResponseEntity<Map> result = restTemplate.getForEntity(URL,60000, Map.class);
			Map<String,Object> dataMap = (Map<String, Object>) result.getBody().get("data");
			Message.creator(new PhoneNumber(paramMap.get("From")),
		            new PhoneNumber(paramMap.get("To")), 
		            dataMap.get("msg").toString()).create();
		}
		if(paramMap.get("Body").contains("OTP:")) {
			//String verifyPhoneUrl = verifyPhone+paramMap.get("From").split(":")[1];
			//ResponseEntity<Map> result = restTemplate.getForEntity(verifyPhoneUrl,60000, Map.class);
			//Map<String,Object> dataMap = (Map<String, Object>) result.getBody().get("data");
					
			/*VerifyOTPRequest vor = new VerifyOTPRequest();
			vor.setContactNo(paramMap.get("From").split(":")[1]);
			vor.setCountryCode("IN");
			vor.setOtp(paramMap.get("Body").split(":")[1]);
			vor.setVerificationToken("372eeeb3-6246-480d-86cf-7e9620b0e012");
			vor.setUserName("");
			//ResponseEntity<Map> result1 = restTemplate.postForEntity(verifyOTP, new Gson().toJson(vor), 60000, Map.class);
			
			Gson gson = new Gson();
	        String jsonString = gson.toJson(vor);
	        JSONObject obj = new JSONObject(jsonString);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<String> request = new HttpEntity<String>(obj.toString(), headers);
	        ResponseEntity<Map> result1 = restTemplate.exchange(verifyOTP, HttpMethod.POST, request, 60000, Map.class);
			
			Map<String,Object> dataMap1 = (Map<String, Object>) result1.getBody().get("data");
			String bearerToken = dataMap1.get("token").toString();
			
			HttpHeaders headers1 = new HttpHeaders();
			headers1.setBearerAuth("Bearer "+bearerToken);
			HttpEntity<List<Long>> entity = new HttpEntity<>(headers1);
			//  ResponseEntity bookedProduct=  restTemplate.exchange(getUserProfile, HttpMethod.GET, entity,60000,  Map.class);
			 // Map<String,Object> usperProfileResp = (Map<String,Object>)bookedProduct.getBody();
			 // Map<String,Object> usperProfileRespData =  (Map<String, Object>) usperProfileResp.get("data");
			  
			String cartId = "55f77a5a-57d5-41cd-b904-e93890208688";// usperProfileRespData.get("cartId").toString();
			String CartUrl = addToCartUrl+cartId+"/cd26d029-287e-4ed4-8d9a-1bd428bb2f8b"+"/2";
			
			ResponseEntity<Map> result2 = restTemplate.getForEntity(CartUrl,60000, Map.class);
			Map<String,Object> dataMap2 = (Map<String, Object>) result2.getBody().get("data");
			
			Message.creator(new PhoneNumber(paramMap.get("From")),
		            new PhoneNumber(paramMap.get("To")), 
		            dataMap2.get("status").toString()).create();	*/
			
		}
		return "";
	}
	
	public ProductView getProductDetail(String uuid) {
		return productViewRepository.findByPcUuid(uuid);
	}
	
	/*public void fetchEarlierRecords() {
		CallFetcher call = Call.fetcher(SID1);
		call.
	}*/
	
	public String sendStartingMessage(Map<String,String> paramMap) {
		Twilio.init(SID1, TOKEN1);
		String name ="T-Shrits";
		Message msg =	Message.creator(new PhoneNumber(paramMap.get("From")),
            new PhoneNumber(paramMap.get("To")), 
            "Delivery Successful!\r\n"
            + "Enjoy your new products "+name+", . Thanks for shopping with Konnectbox.For today’s deal: ").create();
	return msg.getSid();
	}
	
}
