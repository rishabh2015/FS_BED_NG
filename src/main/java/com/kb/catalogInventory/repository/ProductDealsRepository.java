package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.ProductDeals;

public interface ProductDealsRepository extends JpaRepository<ProductDeals, Long>{
	
	List<ProductDeals> findByDealsIdIn(List<Long> dealsId);

}
