package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.ProductView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductViewForAdminRepo extends JpaRepository<ProductView, Long> {
    @Query(value = "select pc_uuid from product_view pv where pc_uuid = :pcuuid", nativeQuery = true)
    List<String> findByPcUuid(@Param("pcuuid") String pcuuid, Pageable pageable);


    @Query(value = "select pc_uuid from product_view pv where item_name like %:itemName% and status not in (7)",nativeQuery = true)
   List<String> findByItemNameLike(@Param("itemName") String itemName, Pageable pageable);


    @Query(value = "select pc_uuid from product_view pv where sku =:sku and status not in (7)",nativeQuery = true)
    List<String> findBySku(@Param("sku") String sku, Pageable pageable);

    @Query(value = "select pc_uuid from product_view pv ",nativeQuery = true)
    List<String> findAllProducts( Pageable pageable);

    @Query(value = "select pc_uuid from product_view where min_order_quantity*5 > available_stock and available_stock >0",nativeQuery = true)
    List<String> findAllProductsWithLowStock( Pageable pageable);
    @Query(value = "select pc_uuid from product_view where available_stock =0",nativeQuery = true)
    List<String> findAllProductsWithZeroStock( Pageable pageable);

    @Query(value = "select pc_uuid from product_view where status =:status and min_order_quantity*5 > available_stock and available_stock >0",nativeQuery = true)
    List<String> findByStatusIdWithLowStock(@Param("status") Integer status, Pageable pageable);

    @Query(value = "select pc_uuid from product_view where status =:status and available_stock =0",nativeQuery = true)
    List<String> findByStatusIdWithZeroStock(@Param("status") Integer status, Pageable pageable);

    @Query(value = "select count(pc_id) from product_view pv ",nativeQuery = true)
   Integer countAllProducts();

    @Query(value = "select count(pc_id) from product_view where min_order_quantity*5 > available_stock and available_stock >0 ",nativeQuery = true)
    Integer countAllProductsWithLowStock();

    @Query(value = "select count(pc_id) from product_view where available_stock =0",nativeQuery = true)
    Integer countAllProductsWithZeroStock();

    @Query(value = "select pc_uuid from product_view pv where status =:status ",nativeQuery = true)
    List<String> findByStatusId(@Param("status") Integer status, Pageable pageable);

    @Query(value = "select count(pc_id) from product_view pv where status =:status ",nativeQuery = true)
    Integer countByStatusId(@Param("status") Integer status);

    @Query(value = "select count(pc_id) from product_view where status =:status and min_order_quantity*5 > available_stock and available_stock >0 ",nativeQuery = true)
    Integer countByStatusIdWithLowStock(@Param("status") Integer status);

    @Query(value = "select count(pc_id) from product_view where status =:status and available_stock =0 ",nativeQuery = true)
    Integer countByStatusIdWithZeroStock(@Param("status") Integer status);

    @Query(value = "select pc_uuid from product_view where s_name like %:supplierName%",nativeQuery = true)
    List<String> findBySupplierName(@Param("supplierName") String supplierName , Pageable pageable );
    @Query(value = "select pc_uuid from product_view pv where s_name like %:supplierName% and available_stock =0",nativeQuery = true)
    List<String> findBySupplierNameWithZeroStock(@Param("supplierName") String supplierName , Pageable pageable );


    @Query(value = "select pc_uuid from product_view where s_name like %:supplierName% and min_order_quantity*5 > available_stock and available_stock >0",nativeQuery = true)
    List<String> findBySupplierNameWithLowStock(@Param("supplierName") String supplierName , Pageable pageable );

    @Query(value = "select count(pc_id) from product_view where s_name like %:supplierName% ",nativeQuery = true)
    Integer countBySupplierName(@Param("supplierName") String supplierName);

    @Query(value = "select count(pc_id) from product_view where s_name like %:supplierName% and min_order_quantity*5 > available_stock and available_stock >0 ",nativeQuery = true)
    Integer countBySupplierNameWithLowStock(@Param("supplierName") String supplierName);

    @Query(value = "select count(pc_id) from product_view where s_name like %:supplierName% and status= :statusId and min_order_quantity*5 > available_stock and available_stock >0 ",nativeQuery = true)
    Integer countBySupplierNameAndStatusWithLowStock(@Param("supplierName") String supplierName, @Param("statusId") Integer statusId);

    @Query(value = "select count(pc_id) from product_view where s_name like %:supplierName% and available_stock =0 ",nativeQuery = true)
    Integer countBySupplierNameWithZeroStock(@Param("supplierName") String supplierName);

    @Query(value = "select pc_uuid from product_view where s_name like %:supplierName% and status= :statusId",nativeQuery = true)
    List<String> findBySupplierNameAndStatusId(@Param("supplierName") String supplierName,@Param("statusId") Integer statusId , Pageable pageable );

    @Query(value = "select pc_uuid from product_view where s_name like %:supplierName% and status =:statusId and min_order_quantity*5 > available_stock and available_stock >0",nativeQuery = true)
    List<String> findBySupplierNameAndStatusIdWithLowStock(@Param("supplierName") String supplierName,@Param("statusId") Integer statusId , Pageable pageable );

 @Query(value = "select pc_uuid from product_view where s_name like %:supplierName% and status= :statusId and available_stock =0",nativeQuery = true)
    List<String> findBySupplierNameAndStatusIdWithZeroStock(@Param("supplierName") String supplierName,@Param("statusId") Integer statusId , Pageable pageable );

    @Query(value = "select count(pc_id) from product_view pv where s_name like %:supplierName% and status= :statusId ",nativeQuery = true)
    Integer countBySupplierNameAndStatusId(@Param("supplierName") String supplierName,@Param("statusId") Integer statusId);

    @Query(value = "select count(pc_id) from product_view pv where s_name like %:supplierName% and status= :statusId and available_stock =0 ",nativeQuery = true)
    Integer countBySupplierNameAndStatusIdWithZeroStock(@Param("supplierName") String supplierName,@Param("statusId") Integer statusId);

    @Query(value = "select pc_uuid from product_view pv where s_id = :supplierId  and status not in (7)",nativeQuery = true)
    List<String> findBySupplierId(@Param("supplierId") String supplierId, Pageable pageable );
    @Query(value = "select pc_uuid from product_view where s_id =:supplierId and min_order_quantity*5 > available_stock and available_stock >0 ",nativeQuery = true)
    List<String> findBySupplierIdWithLowStock(@Param("supplierId") String supplierId, Pageable pageable );

    @Query(value = "select pc_uuid from product_view where s_id =:supplierId and available_stock =0 ",nativeQuery = true)
    List<String> findBySupplierIdWithZeroStock(@Param("supplierId") String supplierId, Pageable pageable );

    @Query(value = "select count(pc_id) from product_view pv where s_id =:supplierId ",nativeQuery = true)
    Integer countBySupplierId(@Param("supplierId") String supplierId );

    @Query(value = "select pc_uuid from product_view pv where s_id =:supplierId and status=:statusId ",nativeQuery = true)
    List<String> findBySupplierIdAndStatusId(@Param("supplierId") String supplierId,@Param("statusId") Integer statusId, Pageable pageable );
    @Query(value = "select pc_uuid from product_view pv where s_id =:supplierId and status=:statusId and min_order_quantity*5 > available_stock and available_stock >0  ",nativeQuery = true)
    List<String> findBySupplierIdAndStatusIdWithLowStock(@Param("supplierId") String supplierId,@Param("statusId") Integer statusId, Pageable pageable );

    @Query(value = "select pc_uuid from product_view where s_id =:supplierId and status=:statusId and available_stock =0 ",nativeQuery = true)
    List<String> findBySupplierIdAndStatusIdWithZeroStock(@Param("supplierId") String supplierId,@Param("statusId") Integer statusId, Pageable pageable );

    @Query(value = "select count(pc_id) from product_view pv where s_id =:supplierId and status=:statusId ",nativeQuery = true)
    Integer countBySupplierIdAndStatusId(@Param("supplierId") String supplierId, @Param("statusId") Integer statusId );

    @Query(value = "select count(pc_id) from product_view pv where s_id =:supplierId and status=:statusId and min_order_quantity*5 > available_stock and available_stock >0  ",nativeQuery = true)
    Integer countBySupplierIdAndStatusIdWithLowStock(@Param("supplierId") String supplierId, @Param("statusId") Integer statusId );

    @Query(value = "select count(pc_id) from product_view where s_id =:supplierId and min_order_quantity*5 > available_stock and available_stock >0  ",nativeQuery = true)
    Integer countBySupplierIdWithLowStock(@Param("supplierId") String supplierId );

    @Query(value = "select count(pc_id) from product_view where s_id =:supplierId and available_stock =0  ",nativeQuery = true)
    Integer countBySupplierIdWithZeroStock(@Param("supplierId") String supplierId );

    @Query(value = "select count(pc_id) from product_view where s_id =:supplierId and status=:statusId and available_stock =0  ",nativeQuery = true)
    Integer countBySupplierIdAndStatusIdWithZeroStock(@Param("supplierId") String supplierId, @Param("statusId") Integer statusId );










}
