package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.CategoryAttribute;
import com.kb.catalogInventory.datatable.CategoryAttributeOptions;
import com.kb.catalogInventory.datatable.CategoryVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryAttributeOptionsRepository  extends JpaRepository<CategoryAttributeOptions, Integer> {

    @Query(value = "select * from category_attribute_options where attribute_id = :attributeIdList", nativeQuery = true)
    List<CategoryAttributeOptions> findOptionsByAttribute(@Param("attributeIdList") Integer attributeIdList);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM category_attribute_options where id = :optionId", nativeQuery = true)
    int deleteOptionsByAttributeId(@Param("optionId") Integer optionId);

    @Query(value = "select * from category_attribute_options where attribute_id in( :attributeIdList)", nativeQuery = true)
    List<CategoryAttributeOptions> findOptionsByAttributeIds(@Param("attributeIdList") List<Integer> attributeIdList);

    @Query(value = "select * from category_attribute_options where attribute_id = :attributeId and name =:attributename" , nativeQuery = true)
    CategoryAttributeOptions findByAttributeIdAndName(@Param("attributeId") Integer attributeId, @Param("attributename") String attributename);

    @Transactional
    @Modifying
    @Query(value = "update category_attribute_options set value =:value where attribute_id = :attributeId " , nativeQuery = true)
    int updateValueByAttributeId(@Param("value") String value,@Param("attributeId") Integer attributeId);
}
