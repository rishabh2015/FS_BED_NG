package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedDataResponse {

	private Integer totalDataCount = Integer.valueOf(0);

	private Object pagedData;

	private Integer dataCount = Integer.valueOf(0);

	private Integer startingIndex = Integer.valueOf(1);

	private Integer lastIndex = Integer.valueOf(1);
	
	private Integer totalPages = Integer.valueOf(1);

}
