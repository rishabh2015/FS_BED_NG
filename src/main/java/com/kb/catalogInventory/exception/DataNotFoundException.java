package com.kb.catalogInventory.exception;

import com.kb.java.utils.RestApiErrorResponse;

import lombok.Getter;

@Getter

public class DataNotFoundException extends RuntimeException{
	
   private RestApiErrorResponse restApiErrorResponse;
   
   public DataNotFoundException(final int status, final String message, final String error) {
	   super(message);
	   restApiErrorResponse=new RestApiErrorResponse(status, message, error);
   }
   
}
