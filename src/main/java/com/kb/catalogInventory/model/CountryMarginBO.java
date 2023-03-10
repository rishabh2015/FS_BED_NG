package com.kb.catalogInventory.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryMarginBO {

	private String countryCode;
	
	private List<MarginBO> marginList;
}
