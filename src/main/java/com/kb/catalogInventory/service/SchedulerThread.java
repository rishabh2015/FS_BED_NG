package com.kb.catalogInventory.service;

import com.kb.catalogInventory.scheduler.UpdateCollectionScheduler;

public class SchedulerThread implements Runnable {

	@Override
	public void run() {
		UpdateCollectionScheduler ucs = new UpdateCollectionScheduler();
		ucs.updateProductCombination();
	}

}
