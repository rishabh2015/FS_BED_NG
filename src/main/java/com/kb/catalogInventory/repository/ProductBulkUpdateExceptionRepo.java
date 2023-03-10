package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.ProductBulkUpdateException;

@Repository
public interface ProductBulkUpdateExceptionRepo extends JpaRepository<ProductBulkUpdateException, Long>{
	

}
