package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.ProductBanner;

public interface ProductBannerRepository extends JpaRepository<ProductBanner, Long>{
	
	List<ProductBanner> findByIsActiveTrueAndIsCategoryBannerFalseOrderBySortOrderAsc();
	List<ProductBanner> findByIsActiveTrueAndIsCategoryBannerFalseOrderByUpdatedOnDesc();
	List<ProductBanner> findByIsActiveTrueAndIsCategoryBannerTrueOrderByUpdatedOnDesc();

	List<ProductBanner> findByIsActiveTrueAndIsCategoryBannerTrueOrderBySortOrderAsc();
	List<ProductBanner> findByIsActiveTrueAndIsCategoryBannerTrueOrderBySortOrderDesc();
	List<ProductBanner> findByIsActiveTrueAndIsCategoryBannerFalseOrderBySortOrderDesc();

}
