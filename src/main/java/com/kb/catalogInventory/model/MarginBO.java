package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MarginBO  implements Comparable<Object>{

	private String productId;
	
	private Long supplierId=Long.valueOf(0);
	
	private String categoryName;
	
	private Boolean isDefault=Boolean.valueOf(false);
	
	private Double margin=Double.valueOf(0);
	
	private Double internationalMargin=Double.valueOf(0);

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		MarginBO mb=(MarginBO) o;
		return (this.hashCode()==mb.hashCode())?0:1;
	}
	
}
