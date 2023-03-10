package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.CategoryExcel;

public interface CategoryExeclRepo extends JpaRepository<CategoryExcel, Long> {
	
	CategoryExcel findOneByCategoryName(String categoryName);

}
