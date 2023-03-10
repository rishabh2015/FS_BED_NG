package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.PageTypeStatus;

public interface PageTypeStatusRepository extends JpaRepository<PageTypeStatus, Long> {
	
	PageTypeStatus findByName(String statusName);

}
