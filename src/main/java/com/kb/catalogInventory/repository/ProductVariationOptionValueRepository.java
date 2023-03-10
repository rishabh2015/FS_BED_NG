package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.Product;
import com.kb.catalogInventory.datatable.ProductVariationOptionValue;
import com.kb.catalogInventory.datatable.VariationOptions;
@Repository
public interface ProductVariationOptionValueRepository extends JpaRepository<ProductVariationOptionValue, Long>{ 
	
	ProductVariationOptionValue findByProductAndVariationOptions(Product product, VariationOptions option);
	
	List<ProductVariationOptionValue> findByProduct(Product product);
	
	List<ProductVariationOptionValue> findByProductAndVariationOptionNameIn(Product product,List<String> optionNames);

}
