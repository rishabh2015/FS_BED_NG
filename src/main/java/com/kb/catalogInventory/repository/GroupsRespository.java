package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.Groups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRespository extends JpaRepository<Groups,Long> {

    @Query(value = "select * from groups grps where grps.is_active =1",nativeQuery = true)
    List<Groups> findByAndIsActive();

  Page<Groups> findByTitleIsContainingAndIsActive(String name, Boolean isActive, Pageable pageable);







}
