package com.kb.catalogInventory.repository;

import java.util.List;

import com.kb.catalogInventory.datatable.Collection;
import com.kb.catalogInventory.datatable.Groups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.Brands;
import com.kb.catalogInventory.datatable.Supplier;
@Repository
public interface BrandsRepository extends JpaRepository<Brands, Long>{

	Brands findByName(String brandNameStr);
	@Query(value = "select * from brands b where EXISTS (select 1 from supplier_brands sb where sb.brand_id =b.id and sb.supplier_id =:supplier and sb.is_active =1)",nativeQuery = true)
	List<Brands> findBrandBySupplierId(@Param("supplier") long supplier);
	
	
	@Query("select b from Brands b where b.supplier=:supplier")
	List<Brands> findBrandBySupplier(Supplier supplier);
	@Query("select b from Brands b where b.name=:brandName AND b.supplier.id=:supplier")
	Brands findByNameAndSupplier(String brandName, Long supplier);
	
	@Query("select b from Brands b where b.name  like (%:searchstr%) or b.id=:searchstr")
	List<Brands> findByNameLike(String searchstr);
	
	@Query("select b from Brands b where b.isActive=1 and b.supplier.id!=null")
	List<Brands> findAllActiveBrands();
	
	@Query("select b from Brands b where b.bestSellingSku!=null and best_selling_sku!='' order by addedOn desc ")
	List<Brands> findAllLatestUpdatedBrands();

	@Query(value = "select id,name from brands b where supplier_id =:supplierId",nativeQuery = true)
	List<Object []> findBrandIdsOfSupplier(@Param("supplierId")Long supplierId);

	Page<Brands> findAllByIsActiveTrue(Pageable pageable);

	Brands findBySortOrder(Long sortOrder);

	/*@Query(value = "select * from brands where name like (%:brandName%)", nativeQuery = true)
	List<Brands> getBrandsByBrandName(@Param("brandName") String brandName);*/

	@Query(value = "select * from brands where name like (%:brandName%)", nativeQuery = true)
	Page<Brands> getBrandsByBrandName(@Param("brandName") String brandName, Pageable pageable);

}
