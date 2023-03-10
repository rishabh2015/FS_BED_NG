package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.ProductStatus;

public interface ProductStatusRepository extends JpaRepository<ProductStatus,Long> {
	
	ProductStatus findByStatusName(String statusName);
	
}
