package com.kb.catalogInventory.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.service.WhatsAppIntegrationService;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@RequestMapping("/whatsapp")
@Log4j2
public class TwilioWhatsappAPIController {
	
	@Autowired
	private WhatsAppIntegrationService whatsAppIntegrationService;
	
	@PostMapping(value="/fetchProducts",consumes = {"application/x-www-form-urlencoded"})
	public ResponseEntity<Object> fetchProductsForWhatsApp(@RequestParam Map<String, String> paramMap) {
		log.info("fetchProductsForWhatsApp called from twilio incoming message");
		return ResponseEntity.ok(whatsAppIntegrationService.fetchProductsForWhatsApp(paramMap));
	}
	
	
	@PostMapping(value="/startConvo",consumes = {"application/x-www-form-urlencoded"})
	public ResponseEntity<Object> startConversationForWhatsApp(@RequestParam Map<String, String> paramMap) {
		log.info("fetchProductsForWhatsApp called from twilio incoming message");
		return ResponseEntity.ok(whatsAppIntegrationService.sendStartingMessage(paramMap));
	}
}
