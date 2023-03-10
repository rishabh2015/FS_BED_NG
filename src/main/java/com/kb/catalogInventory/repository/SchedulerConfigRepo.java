package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kb.catalogInventory.datatable.SchedulerConfig;

public interface SchedulerConfigRepo extends JpaRepository<SchedulerConfig,Long>{

	SchedulerConfig findBySchedulerName(String schedulerName);
	
	SchedulerConfig findBySchedulerNameAndIsRunAndIsProcessing(String schedulerName,boolean isRun,boolean isProcessing);
	
	SchedulerConfig findBySchedulerNameAndIsRun(String schedulerName,boolean isRun);
}
