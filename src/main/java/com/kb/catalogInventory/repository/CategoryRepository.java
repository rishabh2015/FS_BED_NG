package com.kb.catalogInventory.repository;

import java.util.List;

import com.kb.catalogInventory.model.CategoryBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	List<Category> findByCategoryName(String name);
	
	List<Category> findByParentId(Long parentId);


	
}
