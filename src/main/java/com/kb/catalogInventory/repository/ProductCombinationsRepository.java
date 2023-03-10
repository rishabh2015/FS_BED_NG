package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.Product;
import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.ProductStatus;
import com.kb.catalogInventory.datatable.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
@Repository
@Transactional
public interface ProductCombinationsRepository extends JpaRepository<ProductCombinations, Long>{
	
	List<ProductCombinations> findBySkuAndProductStatusNotIn(String sku,List<ProductStatus> productStatusList,Pageable pageable);
	
	
	@Query(value = "SELECT pc.* FROM kb_catalog_inventory.product_combinations pc where  pc.sku =  :sku  AND pc.status NOT IN (:productStatusList)  :sortOrderString", nativeQuery = true)
	
	List<ProductCombinations> findBySkuAndProductStatusNotInWithOutPagination(String sku,List<ProductStatus> productStatusList,String sortOrderString);
	
	
	@Query("Select pc from ProductCombinations pc where pc.productStatus !=7 and pc.product.brandModelCategory.brandModels.brands.id=:brandId")
	List<ProductCombinations> findByBrandAndAtiveProduct(long  brandId);
	
	@Query("select combinationString from ProductCombinations pc where pc.product.id =:productId and pc.productStatus.id !=:statusId" )
	List<String> findCombinationsByProductId(@Param("productId")long productId,@Param("statusId")Long statusId);
	
	ProductCombinations findByProductAndCombinationStringAndSupplierPriceAndProductStatusNotIn(Product product,String combinationString,Float supplierPrice,List<ProductStatus> productStatusList);
	
	ProductCombinations findByUniqueIdentifierAndProductStatusNotIn(String uniqueIdentifire,List<ProductStatus> productStatus);
	
	@Query("select uniqueIdentifier from ProductCombinations")
	List<String> findAllUniqueIdentifiers();
	
	@Query("select uniqueIdentifier from ProductCombinations pc where pc.isActive=true and pc.productStatus.id=0")
	List<String> findAllActiveUniqueIdentifiers();

	List<ProductCombinations> findByProduct(Product product);

	@Query("Select pc from ProductCombinations pc where pc.uniqueIdentifier =:UUID")
	ProductCombinations findByStockUniqueIdentifier(@Param("UUID")String UUID);
@Modifying
	@Query(value = "update product_combinations pc  set pc.available_stock =:newStock," +
			"pc.updated_on = now() where pc.unique_identifier =:UUID ",nativeQuery = true)
	int updateStockOfProduct(@Param("UUID")String UUID,@Param("newStock") Integer newStock);

	@Query("Select pc.availableStock from ProductCombinations pc where pc.uniqueIdentifier =:UUID")
	Integer findStockOfProduct(@Param("UUID")String UUID);


	@Query("Select pc from ProductCombinations pc where pc.uniqueIdentifier =:UUID" )
	ProductCombinations findByStockByUniqueIdentifier(@Param("UUID")String UUID);

	List<ProductCombinations> findByProductIdIn(List<Long> productId);
	
	@Query("Select count(pc) from ProductCombinations pc where pc.supplier.id=:supplierId And pc.productStatus.id!=:statusId")
	long countBySupplierProducts(@Param("supplierId")Long supplierId,@Param("statusId") Long productStatusId);
	
	List<ProductCombinations> findBySupplierAndIsActiveAndProductStatusNotIn(Supplier supplier,boolean isActive,List<ProductStatus> productStatusList,Pageable pageable);
	
	@Query("Select pc from ProductCombinations pc where pc.supplier.id in (:supplierId) And (pc.combinationString like %:combinationString% or pc.productDescription like %:productDescription%"
			+ " or pc.searchTerms like %:searchTerms%)")
	List<ProductCombinations> findByProductIdAndCombinationStringLikeAndProductDescriptionLikeAndSearchTermsLike(@Param("supplierId") Long supplierId,
			@Param("combinationString") String combinationString,
			@Param("productDescription") String productDescription, @Param("searchTerms") String searchTerms, Pageable pageable);
	
	@Query("Select count(pc) from ProductCombinations pc where pc.supplier.id=:supplierId And pc.isActive=:isActive And pc.productStatus.id!=:statusId")
	long countSupplierActiveProducts(@Param("supplierId")Long supplierId,@Param("statusId") Long productStatusId,@Param("isActive") Boolean isActive);
	
	List<ProductCombinations> findBySupplierAndProductStatus(Supplier supplier,ProductStatus status,Pageable pageable);
	
	List<ProductCombinations> findBySupplierAndProductStatus(Supplier supplier,ProductStatus status);
	
	@Query("Select count(pc) from ProductCombinations pc where pc.supplier.id=:supplierId And pc.productStatus.id=:statusId")
	long countBySupplierAndStatus(@Param("supplierId")Long supplierId,@Param("statusId")Long statusId);
	
	List<ProductCombinations> findBySupplierAndProductStatusNotIn(Supplier supplier,List<ProductStatus> statusList,Pageable pageable);
	List<ProductCombinations> findBySupplierInAndProductStatusNotIn(List<Supplier> supplier,List<ProductStatus> statusList,Pageable pageable);

	List<ProductCombinations> findBySupplierAndAvailableStockGreaterThanAndProductStatusNotIn(Supplier supplier,Integer availableStock,List<ProductStatus> statusList,Pageable pageable);
	List<ProductCombinations> findBySupplier(Supplier supplier);
	List<ProductCombinations> findBySupplierIn(List<Supplier> supplier);
	
	List<ProductCombinations> findBySupplierAndAvailableStockLessThanEqual(Supplier supplier,Integer availableStock,Pageable pageable);
	
	@Query("Select count(pc) from ProductCombinations pc where pc.supplier.id=:supplierId And pc.availableStock <=:availableStock And pc.productStatus.id!=:statusId")
	long countBySupplierAvailableStockLessThanEqual(@Param("supplierId")Long supplierId,@Param("availableStock")Integer availableStock,@Param("statusId")Long statusId);
	
	@Query("Select count(pc) from ProductCombinations pc where pc.supplier.id=:supplierId And pc.availableStock >:availableStock And pc.productStatus.id!=:statusId")
	long countBySupplierAvailableStockGreaterThan(@Param("supplierId")Long supplierId,@Param("availableStock")Integer availableStock,@Param("statusId")Long statusId);
	
	@Modifying
	@Query("update ProductCombinations pc set pc.isActive=:isActive,pc.productStatus.id=:status,pc.updatedOn=:updateOn,pc.qcRejectedReason=:qcRejectedReason,pc.adminOfQc=:adminOfQc where uniqueIdentifier=:uuid")
		int updateQCStatus(@Param("status") Long status,@Param("uuid") String uniqueIdentifier,@Param("isActive") boolean isActive,@Param("updateOn")Date updateOn,@Param("qcRejectedReason") String qcRejectedReason,@Param("adminOfQc") Long adminOfQc);

	@Transactional
	@Modifying
	@Query("update ProductCombinations  pc set pc.searchTerms =:searchdata  where uniqueIdentifier=:uuid")
	int updateproductbysearchterms(@Param("uuid") String uniqueIdentifier,@Param("searchdata") String searchdata);


	List<ProductCombinations> findByUniqueIdentifier(String uniqueIdentifire,Pageable page);
	
	@Query(value = "SELECT pc.* FROM kb_catalog_inventory.product_combinations pc where  pc.uniqueIdentifier=:uuid  :sortOrderString", nativeQuery = true)
	List<ProductCombinations> findByUniqueIdentifier(@Param("uuid") String uniqueIdentifier,@Param("sortOrderString") String sortOrderString);
	
	
	@Query("Select pc from ProductCombinations pc where  (pc.combinationString like %:combinationString% or pc.productDescription like %:productDescription%"
			+ " or pc.searchTerms like %:searchTerms%) and pc.productStatus not in (7)")
	List<ProductCombinations> findByProductIdAndCombinationStringLikeAndProductDescriptionLikeAndSearchTermsLike(
			@Param("combinationString") String combinationString,
			@Param("productDescription") String productDescription, @Param("searchTerms") String searchTerms,
			Pageable pageable);

	@Query("Select pc from ProductCombinations pc where pc.itemName like %:searchkeyWord% AND pc.productStatus NOT IN (:productStatusList)")

	List<ProductCombinations> findByItemNameLikeAndProductStatusNotIn(@Param("searchkeyWord") String searchkeyWord,@Param("productStatusList") List<ProductStatus> productStatusList,Pageable pageable);

	@Query(value = "SELECT pc.* FROM kb_catalog_inventory.product_combinations pc where  pc.item_name like  %:searchkeyWord%  AND pc.status NOT IN (:productStatusList)  :sortOrderString", nativeQuery = true)
	
	List<ProductCombinations> findByItemNameLikeAndProductStatusNotInWithOutPagination(@Param("searchkeyWord") String searchkeyWord,@Param("productStatusList") List<ProductStatus> productStatusList,@Param("sortOrderString") String sortOrderString);

	List<ProductCombinations> findByItemNameLikeAndProductStatusNotIn(@Param("searchkeyWord") String searchkeyWord,@Param("productStatusList") List<ProductStatus> productStatusList);

	
	List<ProductCombinations> findByUniqueIdentifierLikeAndProductStatusNotIn(String searchkeyWord,List<ProductStatus> productStatusList);

	List<ProductCombinations> findByProductStatus(ProductStatus status);
	
	@Query("Select pc.supplier.id , pc.price ,pc.supplierPrice , pc.weight , pc.height, pc.width, pc.length from ProductCombinations pc where pc.uniqueIdentifier =:UUID")
	List<Object[]> findSupplierByUniqueIdentifier(@Param("UUID")String UUID);
	
	
	
	@Transactional
	@Modifying
	@Query("update ProductCombinations pc set pc.availableStock=:newStock, pc.productDescription=:productDescription, pc.itemName=:itemName,"
			+ "pc.supplierPrice=:supplierBaseAmount,pc.price=:basePrice, pc.updatedOn = now(),pc.productCountryRule.id=:countryRuleId where uniqueIdentifier=:uuid")
	int updateAvailableStock(@Param("newStock") Integer newStock,@Param("uuid") String uniqueIdentifier,@Param("productDescription") String productDescription,
			@Param("itemName") String itemName,@Param("supplierBaseAmount") Float supplierBaseAmount,@Param("basePrice") Float basePrice,@Param("countryRuleId") Long countryRuleId);

	
	@Transactional
	@Modifying
	@Query("update ProductCombinations pc set  pc.updatedOn = now(),pc.productCountryRule.id=:crId where uniqueIdentifier=:pcUUID")
	int updateProductCountryRule(@Param("pcUUID")String pcUUID, @Param("crId")Long crId);
	
	List<ProductCombinations> findBySkuIn(List<String> skuList);
	
	@Modifying
	@Query("update ProductCombinations pc set pc.updatedOn = now() where pc.categoryString like %:lowestCategoryName%")
	int updateUpdatedOnDateBasedOnLowestCategory(@Param("lowestCategoryName")String lowestCategoryName);
	
	@Modifying
	@Query("update ProductCombinations pc set pc.updatedOn = now() where pc.supplier in (:suppliersList)")
	int updateUpdatedOnDateBasedProducts(@Param("suppliersList")List<Supplier> suppliersList);
	
	@Modifying
	@Query("update ProductCombinations pc set pc.updatedOn = now() where pc.uniqueIdentifier in (:pcUUID)")
	int updateUpdatedOnDateBasedPCUUId(@Param("pcUUID")List<String> pcUUID);
	
	@Query(value = "select count(id) from product_combinations pc where   (is_active =1 or is_active =0 ) and status not in (0,7) and supplier_id =:supplierId",nativeQuery = true)
	Integer countInactiveInventoryOfSupplier(@Param("supplierId") Long supplierId);

	@Query(value = "select count(id) from product_combinations pc where   is_active =1  and status=0 and supplier_id =:supplierId",nativeQuery = true)
	Integer countActiveInventoryOfSupplier(@Param("supplierId") Long supplierId);

	@Query(value = "select t.supplier_id from (select count(id),supplier_id from kb_catalog_inventory.product_combinations where status = 0 group by supplier_id) t" , nativeQuery=true)
	List<Long> listOfActiveProductSuppliers();
	
	@Query(value = "select unique_identifier from kb_catalog_inventory.product_combinations where status = 0 and supplier_id= ?1" , nativeQuery=true)
	List<String> findUniqueIdentifiersBySupplier(Long supplierId);

	List<ProductCombinations> findBySupplierAndSku(Supplier supp,String sku);


	@Query(value = "select unique_identifier, supplier_id from kb_catalog_inventory.product_combinations pc where unique_identifier in (:uuids)",nativeQuery = true)
	List<Object[]> findAllSupplierByUniqueIdentifiers(@Param("uuids")List<String> uuids);

	@Query(value = "select * from product_combinations where status=:status and supplier_id=:supplierId", nativeQuery = true)
	List<ProductCombinations> findByStatusAndSupplierId(@Param("supplierId") String supplierId, @Param("status") Integer status, Pageable page);

	@Query(value = "select * from product_combinations where status=:status and supplier_id=:supplierId  :sortOrderString", nativeQuery = true)
	List<ProductCombinations> findByStatusAndSupplierIdWithoutPagination(@Param("supplierId") String supplierId, @Param("status") Integer status,@Param("sortOrderString") String sortOrderString);

	@Query(value = "select * from product_combinations where  supplier_id=:supplierId", nativeQuery = true)
	List<ProductCombinations> findBySupplierId(@Param("supplierId") String supplierId ,Pageable page);

	@Query(value = "select * from product_combinations where  supplier_id=:supplierId  :sortOrderString", nativeQuery = true)
	List<ProductCombinations> findBySupplierIdWithoutPagination(@Param("supplierId") String supplierId ,@Param("sortOrderString") String sortOrderString);

	
	@Query(value = "select * from product_combinations where status=:status ", nativeQuery = true)
	List<ProductCombinations> findByStatus( @Param("status") Integer status, Pageable page);

	@Query(value = "select * from product_combinations where status=:status :sortOrderString", nativeQuery = true)
	List<ProductCombinations> findByStatusWithOutPagination( @Param("status") Integer status,@Param("sortOrderString")  String sortOrderString);

	
	@Transactional
	@Modifying
	@Query(value = "update product_combinations set display_price = ?1 where id =?2" , nativeQuery=true)
	int updateDisplayPriceInProductCombination (float display_price, Long Id);

	@Query(value = "SELECT * FROM kb_catalog_inventory.product_combinations pc inner join kb_catalog_inventory.supplier sp where pc.supplier_id = sp.id and pc.status =:status and sp.supplier_name like %:supplierName%", nativeQuery = true)
	List<ProductCombinations> findByStatusAndSupplierName(@Param("supplierName") String supplierName, @Param("status") Integer status, Pageable page);

	@Query(value = "SELECT * FROM kb_catalog_inventory.product_combinations pc inner join kb_catalog_inventory.supplier sp where pc.supplier_id = sp.id and sp.supplier_name like %:supplierName%", nativeQuery = true)
	List<ProductCombinations> findBySupplierName(@Param("supplierName") String supplierName ,Pageable page);

	@Query(value = "SELECT pc.* FROM kb_catalog_inventory.product_combinations pc ", nativeQuery = true)
	List<ProductCombinations> findAllWithOutPagination(Pageable sort);

	@Query(value = "SELECT pc.* FROM kb_catalog_inventory.product_combinations pc where status = :status  :sortOrderString", nativeQuery = true)
	List<ProductCombinations> findAllByStatusWithOutPagination(@Param("status") Integer status,@Param("sortOrderString") String sortOrderString);

	@Query(value = "SELECT pc.* FROM kb_catalog_inventory.product_combinations pc ,kb_catalog_inventory.supplier sp where pc.supplier_id = sp.id and upper(sp.supplier_name) = upper(:supplierName)  :sortOrderString", nativeQuery = true)
	List<ProductCombinations> findBySupplierNameWithOutPagination(@Param("supplierName") String supplierName,@Param("sortOrderString") String sortOrderString);

	@Query(value = "SELECT pc.* FROM kb_catalog_inventory.product_combinations pc , kb_catalog_inventory.supplier sp where pc.supplier_id = sp.id and pc.status =:status and upper(sp.supplier_name) = upper(:supplierName)  :sortOrderString", nativeQuery = true)
	List<ProductCombinations> findByStatusAndSupplierNameWithOutPagination(@Param("supplierName") String supplierName, @Param("status") Integer status,@Param("sortOrderString") String sortOrderString);

	
	@Query(value = "select * from product_combinations", nativeQuery = true)
	List<ProductCombinations> getAllProducts(Pageable page);
	
	@Query(value = "select * from product_combinations :sortOrderString", nativeQuery = true)
	List<ProductCombinations> getAllProductsWithoutPagination(String sortOrderString);

	@Query(value = "select unique_identifier from product_combinations pc where supplier_id =:supplierId and is_active =:isActive and status not in (7)", nativeQuery = true)
	List<String> findBySupplierIdAndIsActiveAndProductStatusNotInNative(@Param("supplierId") Long supplierId, int isActive, Pageable pageable);
	@Query(value = "select unique_identifier from product_combinations pc where supplier_id =:supplierId and available_stock>0 and status not in (7)", nativeQuery = true)
	List<String> findBySupplierIdInStockNative(@Param("supplierId") Long supplierId, Pageable pageable);

	@Query(value = "select unique_identifier from product_combinations pc where supplier_id =:supplierId and available_stock=0 and status not in (7)", nativeQuery = true)
	List<String> findBySupplierIdOutOfStockNative(@Param("supplierId") Long supplierId, Pageable pageable);

	ProductCombinations findByUniqueIdentifier(String uuid);

	List<ProductCombinations> findBySkuAndSupplier(String sku,Supplier supp);
	@Query(value = "select hsn , unique_identifier,sku from product_combinations pc  where unique_identifier in (:uuids)",nativeQuery = true)
	List<Object[]>	findHsnOfProducts(@Param("uuids") List<String> uuids);

}