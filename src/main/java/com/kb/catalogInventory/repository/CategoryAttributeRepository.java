package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.CategoryAttribute;
import com.kb.catalogInventory.datatable.CategoryAttributeOptions;
import com.kb.catalogInventory.datatable.CategoryVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, Integer> {

    Optional<CategoryAttribute> findByCategoryId(Integer categoryId);

    List<CategoryAttribute> findByAttributeName(String attributeName);

    @Query(value = "select * from category_attribute where category_id = :categoryId" , nativeQuery = true)
    List<CategoryAttribute> findByCategory(@Param("categoryId") Integer categoryId);

    @Query(value = "select * from category_attribute where category_id = :categoryId" , nativeQuery = true)
    List<CategoryAttribute> findByCategoryIdList(@Param("categoryId") Integer categoryId);

    @Query(value = "select * from category_attribute where category_id = :categoryId and attribute_name =:attributename" , nativeQuery = true)
    CategoryAttribute findByCategoryAndName(@Param("categoryId") Integer categoryId, @Param("attributename") String attributename);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM category_attribute where category_id = :categoryId", nativeQuery = true)
    int deleteAttributeAfterOptions(@Param("categoryId") Integer categoryId);

    @Query(value = "select * from category_attribute" , nativeQuery = true)
    List<CategoryAttribute> findByCategoryName();

}
