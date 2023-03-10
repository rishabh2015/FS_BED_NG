package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.DynamicCollectionAttributeMaster;

public interface DynamicCollectionAttributeMasterRepo extends JpaRepository<DynamicCollectionAttributeMaster, Long> {

}
