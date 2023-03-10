package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.Supplier;

import java.util.Collection;
import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{
	
	Supplier  findBySupplierName(String supplierName);
	Supplier  findByEmail(String email);

	List<Supplier>  findBySupplierNameContainingIgnoreCase(String supplierName);

	Supplier findByIdAndIsActive(long supplierId,boolean isActive);

	@Query(value = "select s from Supplier s where s.id in ?1" )
	List<Supplier> findAllByIdList(List<Long> ids);

	@Query("select s from Supplier s where (:firstName is null or s.firstName = :firstName)")
	List<Supplier> searchByFirstName(@Param("firstName") String firstName);

	@Query("select s from Supplier s where (:lastName is null or s.lastName = :lastName)")
	List<Supplier> searchByLastName(@Param("lastName") String lastName);

	@Query("select s from Supplier s where (:email is null or s.email = :email)")
	List<Supplier> searchByEmail(@Param("email") String email);

	@Query("select s from Supplier s where (:phone is null or s.phone = :phone)")
	List<Supplier> searchByPhone(@Param("phone") String phone);

	@Query("SELECT s FROM Supplier s WHERE s.groupString  LIKE (%:groupString%)")
	List<Supplier> findByGroupStringLike(String groupString);

	@Query(value = "select s.id, s.supplier_email from kb_catalog_inventory.supplier s where s.id in (:supplierIds)",nativeQuery = true)
	List<Object []> getSupplierEmailToIdMap(@Param("supplierIds") List<Long> supplierIds);

	@Query(value = "select s.id, s.supplier_email from supplier s where is_active =1",nativeQuery = true)
	List<Object []> getAllSupplierEmailToIdMap();

	@Query(value = "select supplier_email from supplier where id =:supplierId",nativeQuery = true)
	List<String> getEmailIdOfSupplier(@Param("supplierId")Long supplierId);


}
