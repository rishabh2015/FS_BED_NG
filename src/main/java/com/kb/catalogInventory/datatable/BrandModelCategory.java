package com.kb.catalogInventory.datatable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="brand_model_category")
public class BrandModelCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "name")
	private String name;
	
	
	
	
	public BrandModelCategory(String name, String categoryName, Category category, BrandModels brandModels) {
		super();
		this.name = name;
		this.categoryName = categoryName;
		this.category = category;
		this.brandModels = brandModels;
	}

	public BrandModelCategory() {
	}

	@Column(name = "category_name")
	private String categoryName;
	
	
	
	@JoinColumn(name = "category_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Category category;
	
	
	@JoinColumn(name = "brand_model_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private BrandModels brandModels;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

		public BrandModels getBrandModels() {
		return brandModels;
	}

	public void setBrandModels(BrandModels brandModels) {
		this.brandModels = brandModels;
	}
}
