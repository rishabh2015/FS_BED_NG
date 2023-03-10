package com.kb.catalogInventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kb.catalogInventory.model.CollectionRequest;
import com.kb.catalogInventory.model.DynamicCollectionAttributeMasterBo;
import com.kb.catalogInventory.model.DynamicCollectionRequest;
import com.kb.catalogInventory.service.CollectionService;
import com.kb.java.utils.RestApiSuccessResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/collection")
@Log4j2
public class CollectionController {
	
	@Autowired
	private CollectionService collectionService;

	@ApiOperation("It gets all Collections for the Home Page")
	@GetMapping(value="/getAllCollectionsForHomePage")
	public ResponseEntity<?> getAllCollectionForHomePage(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder) {
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Collections Fetched Successfully!!",
				collectionService.getAllCollections(sortOrder, pageNum, pageSize));
		return new ResponseEntity(sr, HttpStatus.OK);
	}
	
	@ApiOperation("It gets all Collections for pageTypeName")
	@GetMapping(value="/getSingleCollectionProductsInfoForPageType/{pageTypeName}")
	public ResponseEntity<?> getSingleCollectionProductsInfoForPageType(@PathVariable("pageTypeName") String pageTypeName,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder){
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(), "Single Collection Products Fetched Successfully!!", collectionService.getSingleCollectionProductsInfo(pageTypeName,sortOrder, pageNum, pageSize));
    	return new ResponseEntity(sr,HttpStatus.OK) ;
	}
	
	@ApiOperation("It creates a new collection")
	@PostMapping(value="/createNewCollection")
	public ResponseEntity<?> createNewCollection(@RequestBody CollectionRequest collectionRO){
		log.info("Inside createNewCollection");
		RestApiSuccessResponse sr = null;
		if(collectionRO.getCollectionId()!=null) {
			log.info("Going to update collection with collection Id: ",collectionRO.getCollectionId());
			sr = collectionService.updateExistingCollection(collectionRO);	
		}else {
			log.info("Going to create a New collection");
		sr = collectionService.createNewCollection(collectionRO);
		}
		return new ResponseEntity(sr,HttpStatus.OK) ;
	}

	@DeleteMapping("/deleteCollection/{collectionId}")
	public ResponseEntity<?> deleteCollection(@PathVariable String collectionId){
		Map<String,String> response = new HashMap<>();
		try {
			collectionService.deleteCollection(Long.valueOf(collectionId));
			response.put("status","Success");
		} catch (Exception e) {
			log.info("error",e);
			response.put("status","Failed");
		}
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation("It updates all the collections Ids with sort order in product combination")
	@GetMapping(value="/updateAllCollections")
	public ResponseEntity<?> updateAllCollections(){
		log.info("Inside updateAllCollections for updating the sort order");
		collectionService.updateAllCollections();
		return null;
	}
	
	@ApiOperation("It gets all products for a collection")
	@GetMapping(value="/getCollectionAllProducts/{collectionId}")
	public ResponseEntity<?> getCollectionsAllProducts(@PathVariable("collectionId") Long collectionId,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortOrder", required = false) String sortOrder){
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(), "Collections Fetched Successfully!!", collectionService.getCollectionsAllProducts(collectionId,sortOrder, pageNum, pageSize));
    	return new ResponseEntity(sr,HttpStatus.OK) ;
	}
	
	@ApiOperation("It gets all Collections for the Home Page")
	@GetMapping(value="/getAllCollections")
	public ResponseEntity<?> getAllCollections(@RequestHeader("countrycode") String countryCode){
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(), "Collections Fetched Successfully!!", collectionService.getAllCollectionsNew(countryCode));
    	return new ResponseEntity(sr,HttpStatus.OK) ;
	}
	
	@ApiOperation("It gets all featured Collections Ids")
	@GetMapping(value="/getFeaturedCollectionId")
	public ResponseEntity<?> getAllFeaturedCollectionsIds(){
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(), "Collections Fetched Successfully!!", collectionService.getFeautedCollection());
    	return new ResponseEntity(sr,HttpStatus.OK) ;
	}
	
	@ApiOperation("It Activates/De-Activates a Collection")
	@PostMapping(value="/changeCollectionStatus")
	public ResponseEntity<?> changeCollectionStatus(@RequestBody CollectionRequest collectionRO){
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(), "Collections Status Successfully!!", collectionService.activateDeActivateCollection(collectionRO.getCollectionId(),collectionRO.getStatus()));
    	return new ResponseEntity(sr,HttpStatus.OK) ;
	}

	@ApiOperation("It add new Attributes for Dynamic Collection Creation")
	@PostMapping("/addCollectionAttributes")
	public ResponseEntity<?> addCollectionAttributes(@RequestBody DynamicCollectionAttributeMasterBo dynamicCollectionAttributeMasterBo){
		log.info("Inside addCollectionAttributes");
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(), "Attribute added Successfully!!", collectionService.addNewAttributeToCollectionMaster(dynamicCollectionAttributeMasterBo));
		if(sr.getStatus()==500) {
			return new ResponseEntity(sr,HttpStatus.INTERNAL_SERVER_ERROR) ;
		}else {
		return new ResponseEntity(sr,HttpStatus.OK) ;
		}
	}

	@ApiOperation("It gets all the attributes along with all eligible operators")
	@GetMapping("/getAllDynamicCollectionAttributes")
	public ResponseEntity<?> getAllDynamicCollectionAttributes(){
		log.info("Inside getAllDynamicCollectionAttributes");
		RestApiSuccessResponse sr = new RestApiSuccessResponse(HttpStatus.OK.value(), "Attribute fetched Successfully!!", collectionService.getAllAttributes());
		return new ResponseEntity(sr,HttpStatus.OK) ;
	}

	@ApiOperation("It create a new dynamic collection")
	@PostMapping("/createNewDynamicCollection")
	public ResponseEntity<?> addDynamicCollection(@RequestBody DynamicCollectionRequest dynamicCollectionRequest){
		log.info("Inside addDynamicCollection");
		RestApiSuccessResponse sr = collectionService.addDynamicCollection(dynamicCollectionRequest);
		if(sr.getStatus()==500) {
			return new ResponseEntity(sr,HttpStatus.INTERNAL_SERVER_ERROR) ;
		}else {
		return new ResponseEntity(sr,HttpStatus.OK) ;
		}
	}
	
	@GetMapping("/flushCollectionCache")
	public ResponseEntity<?> flushCollectionCache(){
		log.info("Inside addDynamicCollection");
		collectionService.flushCollectionCache();
		return new ResponseEntity("",HttpStatus.OK) ;
		
	}
	
}
