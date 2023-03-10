package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.ProductAttributeDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeDo, Long> {


    ProductAttributeDo findByProductCombinationId(String pcUUID);
}
