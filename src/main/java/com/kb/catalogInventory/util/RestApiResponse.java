package com.kb.catalogInventory.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;

@Component
public class RestApiResponse {

	public String status;
	public String message;
	public Object data;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", locale = "hi_IN", timezone = "IST")
	private LocalDateTime 	timeStamp;
	
	
	private RestApiResponse(){
		timeStamp = LocalDateTime.now();
	}
	public RestApiResponse(String status) {
		this();
		this.status = status;
	}
	public RestApiResponse(String status, String message) {
		this();
		this.status = status;
		this.message = message;
	}
	
	
	
	
	
	public RestApiResponse(String status, String message, Object data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
		this.timeStamp = LocalDateTime.now();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	

}
