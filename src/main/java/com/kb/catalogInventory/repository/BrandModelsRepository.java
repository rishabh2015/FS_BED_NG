package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.BrandModels;
import com.kb.catalogInventory.datatable.Brands;
@Repository
public interface BrandModelsRepository extends JpaRepository<BrandModels, Long>{

	BrandModels findByName(String brandModelNameStr);
	
	@Query("select bm from BrandModels bm where bm.name=:brandModelNameStr and bm.brands.id=:brandModelId")
	BrandModels findByNameAndBrandId(@Param("brandModelNameStr") String brandModelNameStr,@Param("brandModelId") long brandModelId);
	
	List<BrandModels> findByBrandsIn(List<Brands> brands);
	
//	List<BrandModels> findByBrands(long brands);
	
	BrandModels findByBrands(Brands brands);
}
