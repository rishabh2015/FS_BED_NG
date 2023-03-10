package com.kb.catalogInventory.controller;


import com.kb.catalogInventory.service.QuotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller
@RequestMapping("/quotes")
public class QuotesController {

    @Autowired
    private QuotesService quotesService;

    @GetMapping("/getquote")
    public Object getSingleQuote(){
        return new ResponseEntity<>(quotesService.getRandomQuote(), HttpStatus.OK);
    }
}
