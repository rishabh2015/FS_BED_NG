package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.ProductCache;
@Repository
public interface ProductCacheRepository extends JpaRepository<ProductCache, Long>{

}
