package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.BrandModelCategory;
import com.kb.catalogInventory.datatable.Product;
import com.kb.catalogInventory.datatable.Supplier;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	Product findByProductName(String productNameStr);
	
	Product findByProductString(String productStr);
	
	Product findByProductStringAndProductNameAndBrandModelCategoryAndSupplierAndProductSku(String productStr, String productName,
			BrandModelCategory brandModelCategory, Supplier supplier,String productSku);

	Product findByUUID(String UUID);
	
	List<Product> findBySupplier(Supplier supplier);
	
	List<Product> findBySupplierIn(List<Supplier> supplierList);
	
	@Query("Select p from Product p where p.supplier.id=:supplierId And (p.productName like %:productName% or p.productString like %:productString%)")
	List<Product> findBySupplierIdAndProductNameLikeAndProductStringLike(@Param("supplierId") long supplierId,@Param("productName") String productName,
			@Param("productString") String productString);
	@Query("select p.id from Product p where p.supplier.id = :supplierId")
	List<Long> findAllProductIdBySupplier(@Param("supplierId")long supplierId);


	@Query(value = "select s.supplier_phone from kb_catalog_inventory.supplier s inner join kb_catalog_inventory.product p where s.id = p.supplier_id and p.uuid = :uuid", nativeQuery = true)
	String getSupplierPhoneNumber(@Param("uuid") String uuid);

	long countBysupplierId(long supplierId);
	
	List<Product> findByBrandModelCategoryIn(List<BrandModelCategory> categories);
	
	List<Product> findByProductSkuAndSupplier(String productSku,Supplier supplier);
	
	List<Product> findByProductSkuIn(List<String> productSku);
	
	Product findByProductSku(String productSku);

	Product findByProductSkuAndSupplierAndBrandModelCategory(String productSku,Supplier supplier,BrandModelCategory bmc);
	
}
