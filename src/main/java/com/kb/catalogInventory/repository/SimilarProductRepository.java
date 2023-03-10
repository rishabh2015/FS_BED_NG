package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.SimilarProductDo;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimilarProductRepository extends JpaRepository<SimilarProductDo,Long> {

    SimilarProductDo findByProductCombinationUniqueId(String uuid);
}
