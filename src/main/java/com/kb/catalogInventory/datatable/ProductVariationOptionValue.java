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
@Table(name="product_variation_option_value")
public class ProductVariationOptionValue {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "product_option_name")
	private String variationOptionName;
	
	public ProductVariationOptionValue(String variationOptionName, Product product, VariationOptions variationOptions) {
		super();
		this.variationOptionName = variationOptionName;
		this.product = product;
		this.variationOptions = variationOptions;
	}

	
	public ProductVariationOptionValue() {
		// TODO Auto-generated constructor stub
	}
	@JoinColumn(name = "product_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Product product;
	
	@JoinColumn(name = "variation_option_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private VariationOptions variationOptions;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVariationOptionName() {
		return variationOptionName;
	}

	public void setVariationOptionName(String variationOptionName) {
		this.variationOptionName = variationOptionName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public VariationOptions getVariationOptions() {
		return variationOptions;
	}

	public void setVariationOptions(VariationOptions variationOptions) {
		this.variationOptions = variationOptions;
	}
}
