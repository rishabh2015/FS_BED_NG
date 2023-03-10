package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.BrandReviews;
@Repository
public interface BrandReviewsRepository extends JpaRepository<BrandReviews, Long>{

	@Query(value = "select * from brand_reviews br where br.brand_id =:brandId ",nativeQuery = true)
	List<BrandReviews> findByBrands(@Param ("brandId")long brandId);


}
