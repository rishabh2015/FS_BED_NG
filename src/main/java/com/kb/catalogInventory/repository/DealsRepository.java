package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.Deals;

public interface DealsRepository extends JpaRepository<Deals, Long> {
	
	List<Deals> findByIsActive(boolean isActive);
	
	Long countByIsActiveTrue();
	
	

}
