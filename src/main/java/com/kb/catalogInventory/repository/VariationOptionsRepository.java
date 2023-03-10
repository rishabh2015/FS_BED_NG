package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.Variation;
import com.kb.catalogInventory.datatable.VariationOptions;
@Repository
public interface VariationOptionsRepository extends JpaRepository<VariationOptions, Long>{

	VariationOptions findByVariationOptionName(String variationOptionStr);
	
	@Query("select variationOptionName from VariationOptions vo where vo.variation.id =:variationId")
	List<String> findByVariationId(@Param("variationId") long variationId);
	
	VariationOptions  findByVariationOptionNameAndVariation(String variationOptionName , Variation variation);
	
	VariationOptions findByVariationOptionNameAndVariationId(String variationOptionStr,long variationId);

}
