package com.kb.catalogInventory.controller;

import com.kb.catalogInventory.service.SimilarProductsService;
import com.kb.java.utils.RestApiSuccessResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/similarProduct")
@Log4j2
public class SimilarProductController {

    @Autowired
    private SimilarProductsService similarProductsService;

    @PostMapping("/add")
    public ResponseEntity<?> addSimilarProduct(@RequestBody Map<String, List<String>> request) {
        log.info("Entered addSimilarProduct with request {} ",request);
        try {
            similarProductsService.add(request);
        }catch (Exception e){
            log.error("Error while adding similar product ",e);
            return new ResponseEntity<>(new RestApiSuccessResponse(HttpStatus.OK.ordinal(), "Failed", e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new RestApiSuccessResponse(HttpStatus.OK.ordinal(), "SUCCESS", "added successfully"), HttpStatus.OK);
    }

    @GetMapping("/fetch/{uuid}")
    public ResponseEntity<?> fetchSimilarProduct(@PathVariable("uuid") String uuid) {
        log.info("Entered fetchSimilarProduct with uuid {} ",uuid);
        Object obj=null;
        try {
          obj=   similarProductsService.fetch(uuid);
        }catch (Exception e){
            log.error("Error while fetching similar product for uuid  {} {}",uuid,e);
            return new ResponseEntity<>(new RestApiSuccessResponse(HttpStatus.OK.ordinal(), "Failed", e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new RestApiSuccessResponse(HttpStatus.OK.ordinal(), "SUCCESS", obj), HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<?> sendSimilarProduct(@RequestBody Map<String, List<String>> request) {
        log.info("Entered sendToKafka with request {} ",request);
        try {
            similarProductsService.sendToKafka(request);
        }catch (Exception e){
            log.error("Error while adding similar product ",e);
            return new ResponseEntity<>(new RestApiSuccessResponse(HttpStatus.OK.ordinal(), "Failed", e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new RestApiSuccessResponse(HttpStatus.OK.ordinal(), "SUCCESS", "added successfully"), HttpStatus.OK);
    }
}
