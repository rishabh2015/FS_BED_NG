package com.kb.catalogInventory.datatable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="locked_items")
public class LockedItems {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "cart_id")
	private String cartId;
	
	@Column(name = "order_attempt_id")
	private String orderAttemptId;
	
	@Column(name = "lockeditems_to_qtymap" ,columnDefinition = "json")
	private String lockedItemsToQtyMap; 
	
	@Column(name = "total_price")
	private double totalPrice;
	
	@Column(name = "locked_time")
	private Date lockedtime;
	
	@Column(name = "status")
	private String status;//LOCKED,UNLOCKED,PROCESSED 
		
	@Column(name = "booking_created")
	private boolean bookingCreated;
	
	@Column(name = "booking_id")
	private String bookingId;
	
	@Column(name = "last_update_time")
	private Date lastUpdateTime;
	
	@Column(name = "is_active")
	private boolean isActive;
}
