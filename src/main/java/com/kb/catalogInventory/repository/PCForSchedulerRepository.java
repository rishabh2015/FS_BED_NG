package com.kb.catalogInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.ProductCombinations;

@Repository
public interface PCForSchedulerRepository extends CrudRepository<ProductCombinations, Long>{
	@Query(value="select pc.unique_identifier from product_combinations pc where pc.updated_on > NOW() -  INTERVAL :hoursAgo  hour",nativeQuery = true)
	List<String> findRowsUpdatedInGivenHour(@Param("hoursAgo") int hoursAgo);


	@Query(value="select pc.pc_uuid,pc.lowest_cat_name from product_view pc  where pc_uuid in (:uuids)",nativeQuery = true)
	List<Object[]> findDataOfPc(@Param("uuids") List<String> uuids);


	@Query(value="Select pc.unique_identifier, pc.is_active, pc.available_stock from product_combinations pc where pc.is_active=0 and pc.status in (1,3,5,7) and pc.updated_on > NOW() -  INTERVAL :hoursAgo  hour and pc.status not in (7)",nativeQuery = true)
	List<Object[]> findInactiveProductByUpdatedDate(@Param("hoursAgo") int hoursAgo);
	

}
