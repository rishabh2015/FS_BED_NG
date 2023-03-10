package com.kb.catalogInventory.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.ProductStock;
@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

	List<ProductStock> findByProductCombinationsAndUnitPriceAndIsActive(ProductCombinations pc, Float unitprice,
			boolean isActive);

	@Transactional
	@Modifying
	@Query("update ProductStock ps set ps.totalStock=:newStock,ps.oldStock=:oldStock,ps.totalStock=:newStock,ps.stockChangeComment=:comment, ps.updatedOn=:updatedOn where ps.id=:stockId")
	int updateAvailableProductStock(@Param("updatedOn") Date updatedOn, @Param("stockId") Long stockId,
			@Param("newStock") Integer newStock, @Param("oldStock") Integer oldStock, @Param("comment") String comment);

}
