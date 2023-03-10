package com.kb.catalogInventory.repository;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.ProductCombinationsArchive;
@Repository
@Transactional
public interface ProductCombinationsArchiveRepository extends JpaRepository<ProductCombinationsArchive, Long> {

	@Query(value = "Select pc.unique_identifier, pc.is_active, pc.available_stock from product_combinations_Archive pc where   pc.updated_on > NOW() - INTERVAL :dayAgo day", nativeQuery = true)
	List<Object[]> findInactiveProductByUpdatedDate(@Param("dayAgo") int dayAgo);

}