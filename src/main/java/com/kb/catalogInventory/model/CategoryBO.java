package com.kb.catalogInventory.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kb.catalogInventory.datatable.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer”, “handler"})
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBO implements	Comparable<CategoryBO>,Serializable{
	private Long id;

	private String categoryName;

	private String categoryIcon;

	private Category parent;
     
	@Builder.Default
	private Set<Category> childrenCategories=new TreeSet<>();

	private String categoryStage;

	List<String> parentImageList;

	@Override
	public int compareTo(CategoryBO o) {
		// TODO Auto-generated method stub
		return this.id.compareTo(o.getId());
	}

}
