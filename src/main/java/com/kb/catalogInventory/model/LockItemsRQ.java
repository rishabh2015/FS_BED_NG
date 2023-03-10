package com.kb.catalogInventory.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class LockItemsRQ {

	private long userId;
	
	private String cartId;
	
	private String orderAttemptId; 
	
	private HashMap<String,Integer> lockedItemsToQtyMap;

	private double totalPrice;
	
	private Date lockedtime;	
	
	private String status;//LOCKED,UNLOCKED,PROCESSED 		
	
	private boolean bookingCreated;	
	
	private String bookingId;	
	
	private Date lastUpdateTime;
	
	private List<String> messsage;
}
