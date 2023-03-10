package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.Product;
import com.kb.catalogInventory.datatable.ProductVariationOptionImage;
import com.kb.catalogInventory.datatable.ProductVariationOptionValue;
@Repository
public interface ProductVariationOptionImageRepository extends JpaRepository<ProductVariationOptionImage, Long> {

	ProductVariationOptionImage findByProductAndProductVariationOptionValue(Product product,
			ProductVariationOptionValue productVariationOptionValue);
}
