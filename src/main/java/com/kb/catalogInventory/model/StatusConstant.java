package com.kb.catalogInventory.model;

public interface StatusConstant {
	
	static final Long Active = Long.valueOf(0L); 
	
	static final Long Inactive = Long.valueOf(1L);
	
	static final Long QC_Pending = Long.valueOf(2L);
	
	static final Long InactiveBySupplier = Long.valueOf(3L);
	
	static final Long QC_Done = Long.valueOf(4L);
	
	static final Long QC_RejectedByAdmin = Long.valueOf(5L);
	
	static final Long Deleted_Product = Long.valueOf(7L);
	
	static final Long Recently_Inserted = Long.valueOf(8L);

	static final Long All_Product = Long.valueOf(10L);

}
