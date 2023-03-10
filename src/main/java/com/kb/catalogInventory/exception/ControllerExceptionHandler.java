package com.kb.catalogInventory.exception;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {

	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		log.error("Exception came : ",ex);
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ConstraintViolationException.class)
	public Map<String, String> handleConstraintViolation(ConstraintViolationException ex) {
	    Map<String, String> errors = new HashMap<>();
	    log.error("Exception came : ",ex);
	    ex.getConstraintViolations().forEach(cv -> {
	    	 errors.put("errorMessage", cv.getMessageTemplate());
	       // errors.put("InvalidValue", (cv.getInvalidValue()).toString());
	    }); 
	    return errors;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ConnectException.class)
	public Map<String, String> handleConnectException(ConnectException ex) {
		log.error("Exception came : ",ex);
	    Map<String, String> errors = new HashMap<>();
	    errors.put("errorMessage", ex.getMessage());
	    return errors;
	}
	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Map<String, String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		log.error("Exception came : ",ex);
	    Map<String, String> errors = new HashMap<>();
	    errors.put("errorMessage", ex.getMessage());
	    return errors;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Map<String, String> handleException(Exception ex) {
		log.error("Exception came : ",ex);
	    Map<String, String> errors = new HashMap<>();
	    errors.put("errorMessage", ex.getMessage());
	    return errors;
	}
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InventoryException.class)
	public Map<String, String> handleException(InventoryException ex) {
		log.error("Exception came : ",ex);
	    Map<String, String> errors = new HashMap<>();
	    errors.put("errorMessage", ex.getMessage());
	    return errors;
	}
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<?> handleException(DataNotFoundException ex) {
		log.error("Exception came : ",ex);
	    return ResponseEntity.ok(ex.getRestApiErrorResponse());
	}
}
