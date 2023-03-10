package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.SupplierBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierBrandsRespository extends JpaRepository<SupplierBrands,Long> {

    SupplierBrands findBySupplierIdAndBrandId(String supplierId,String brandId);
    List<SupplierBrands> findByBrandIdAndIsActiveTrue(String brandId);
    Page<SupplierBrands> findByBrandIdAndIsActiveTrue(String brandId, Pageable pageable);

}
