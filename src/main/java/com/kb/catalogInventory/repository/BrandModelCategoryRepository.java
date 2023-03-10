package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.BrandModelCategory;
import com.kb.catalogInventory.datatable.BrandModels;
import com.kb.catalogInventory.datatable.Category;
@Repository
public interface BrandModelCategoryRepository extends JpaRepository<BrandModelCategory, Long>{

	BrandModelCategory findByCategoryNameAndBrandModels(String categoryName,BrandModels brandmodel);
	List<BrandModelCategory>  findByBrandModelsIn(List<BrandModels> brandModels);
	
	List<BrandModelCategory> findByBrandModels (BrandModels brandModels);
	
	 
	BrandModelCategory findByCategoryAndBrandModels(Category category,BrandModels brandmodel);
	
	
}
