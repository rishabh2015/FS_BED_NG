package com.kb.catalogInventory.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.LockedItems;
@Transactional
@Repository
public interface LockItemRepository extends JpaRepository<LockedItems, Long> {

		
	@Query(value="select * from locked_items where is_active =1 and  order_attempt_id=:order_attempt_id limit 1 ",nativeQuery = true)
	LockedItems checkIsActive(@Param("order_attempt_id")String order_attempt_id);
	
	//@Lock(LockModeType.PESSIMISTIC_WRITE)
	@org.springframework.transaction.annotation.Transactional
	@Query(value="select * from locked_items where  order_attempt_id=:order_Attempt_Id limit 1 for update ",nativeQuery = true)
	LockedItems refrainConcurrentModification(@Param("order_Attempt_Id")String order_Attempt_Id);
	
	@Modifying
	@Query("update LockedItems li set li.isActive=false, li.bookingCreated=true,li.bookingId=:booking_id,li.status='PROCESSED',li.lastUpdateTime=:last_Update_Time where li.id=:id")
	int updateBookingInfo(@Param("booking_id")String booking_id,@Param("last_Update_Time")Date last_Update_Time,@Param("id")Long id);
	
	@Query(value="select * from locked_items where  order_attempt_id=:order_Attempt_Id limit 1 ",nativeQuery = true)
	LockedItems findbyorderAttemptId(@Param("order_Attempt_Id")String order_Attempt_Id);
	
	List<LockedItems> findByisActive(boolean isActive);
	
	List<LockedItems> findByCartIdAndBookingCreatedFalse(String cartId);
	
	List<LockedItems> findByBookingId(String bookingId);

	List<LockedItems> findByOrderAttemptId(String orderAttemptId);
	
}
