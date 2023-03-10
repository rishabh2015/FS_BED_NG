package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.ProductView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductView, Long> {

    ProductView findByPcUuid(String pcUUID);

    List<ProductView> findBySupplierIdAndIsActiveSuppTrue(Long supplierId, Pageable pageable);

    long countBySupplierId(Long supplierId);

    List<ProductView> findBySupplierIdAndIsActiveSuppTrueAndIsActiveTrue(Long supplierId, Pageable pageable);

    long count();

    List<ProductView> findByProductUuidAndPcUuidNot(String productUUID, String pcUUID);

    List<ProductView> findByProductUuid(String productUUID);

    List<ProductView> findByStatusIdNotIn(List<Long> statusIdList, Pageable pageable);

    Long countByStatusIdNotIn(List<Long> statusIdList);

    List<ProductView> findByBrandIdAndStatusIdAndAvailableStockGreaterThanEqual(Long brandId, Long statusId, Integer stock, Pageable pageable);

    List<ProductView> findBySkuInAndStatusIdAndAvailableStockGreaterThanEqual(List<String> skuId, Long statusId, Integer stock, Pageable pageable);

    Integer countBySkuInAndStatusId(List<String> skuId, Long statusId);

    long countByBrandIdAndStatusIdAndAvailableStockGreaterThanEqual(long brandId, Long statusId, Integer stock);

    long countByBrandIdAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThanEqual(long brandId, Long statusId, Integer stock);

    long countByBrandIdAndStatusIdAndPcrInterTrueAndAvailableStockGreaterThanEqual(long brandId, Long statusId, Integer stock);

    long countByBrandIdAndStatusIdAndPcrWlcInAndAvailableStockGreaterThanEqual(long brandId, Long statusId, List<String> countryCodes, Integer stock);

    long countByBrandModelCategoryId(long brandModelCategoryId);


    long countByLowestCategoryAndStatusId(Long lowestLevelCategoryId, Long statusId);


    List<ProductView> findByLowestCategoryAndStatusIdAndPcrDaTrueAndAvailableStockGreaterThan(long lowestLevelCategoryId, Long statusId, Integer stockQuantity);

    List<ProductView> findByLowestCategoryAndStatusIdAndPcrInterTrueAndAvailableStockGreaterThan(long lowestLevelCategoryId, Long statusId, Integer stockQuantity);

    List<ProductView> findByLowestCategoryAndStatusIdAndPcrWlcInAndAvailableStockGreaterThan(long lowestLevelCategoryId, Long statusId, List<String> countryCodes, Integer stockQuantity);

    @Query("select count(pv) from ProductView pv where pv.collectionIds like %:collectionId% and pv.status =:status and pv.pcrDa=true")
    long countByCollectionIdsAndStatusNameAndPcrDa(@Param("collectionId") String collectionId, @Param("status") String status);

    @Query("select count(pv) from ProductView pv where pv.collectionIds like %:collectionId% and pv.status =:status and pv.pcrInter=true")
    long countByCollectionIdsAndStatusNameAndPcrInterTrue(@Param("collectionId") String collectionId, @Param("status") String status);

    @Query("select count(pv) from ProductView pv where pv.collectionIds like %:collectionId% and pv.status =:status and pv.pcrWlc like %:countryCode%")
    long countByCollectionIdsAndStatusNameAndPcrWlc(@Param("collectionId") String collectionId, @Param("status") String status, @Param("countryCode") String countryCode);

    @Query("select count(pv) from ProductView pv where pv.collectionIds like %:collectionId% and pv.availableStock >0")
    long countByCollectionIdsAndAvailableStockGreaterThan(@Param("collectionId") String collectionId);
    
    @Query("select pv from ProductView pv where pv.collectionIds like %:collectionId% and pv.status =:status")
    List<ProductView> findByCollectionIdAndStatus(@Param("collectionId") String collectionId, @Param("status") String status);

    List<ProductView> findBySkuInAndStatusId(List<String> skuId, Long statusId, Pageable pageable);

    List<ProductView> findByProductSkuInAndStatusId(List<String> productskuId, Long statusId, Pageable pageable);

    List<ProductView> findByPcUuidIn(List<String> pcUUIDs);

    List<ProductView> findByLowestCategoryNameIn(List<String> lowestCategoryName);

    @Query(value = "select b_id from product_view pv where pc_uuid =:UUID", nativeQuery = true)
    Long getBrandOfProduct(@Param("UUID") String UUID);
    @Query(value = "select pc_uuid from product_view pv where pv.status not in (7)", nativeQuery = true)
    List<String> findNonDeletedUniqueUuuids( Pageable pageable);
    @Query(value = "select count(pc_id) from product_view pv where pv.status not in (7)", nativeQuery = true)
    Long countOfNonDeletedProducts();

}
