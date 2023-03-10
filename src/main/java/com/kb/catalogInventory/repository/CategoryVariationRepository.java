package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.CategoryVariation;
import com.kb.catalogInventory.datatable.Variation;

@Repository
public interface CategoryVariationRepository extends JpaRepository<CategoryVariation, Long> {

	List<CategoryVariation> findByCategoryName(String categoryName);
	
	CategoryVariation findByVariation(Variation variation);
	
	CategoryVariation findByVariationIdAndCategoryName(long variationId,String categoryName);

}
