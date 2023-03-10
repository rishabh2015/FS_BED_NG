package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.ProductCountryRule;

@Repository
public interface ProductCountryRuleRepository extends JpaRepository<ProductCountryRule, Long>{
	
	ProductCountryRule findByZone(String zone);
	
	ProductCountryRule findByIsDefaultTrue();
	
	List<ProductCountryRule> findByIsActiveTrue();

}
