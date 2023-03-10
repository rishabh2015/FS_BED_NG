package com.kb.catalogInventory.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.Variation;
@Repository
public interface VariationRepository extends JpaRepository<Variation, Long>{
	
	Variation findByVariationName(String variationOptionStr);

}
