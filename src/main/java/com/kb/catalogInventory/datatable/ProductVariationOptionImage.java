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
@Table(name="product_variation_option_image")
public class ProductVariationOptionImage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@JoinColumn(name = "product_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Product product;
	
	@JoinColumn(name = "image_gallery_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private ImageGallery imageGallery;
	
	public ProductVariationOptionImage(Product product, ImageGallery imageGallery,
			ProductVariationOptionValue productVariationOptionValue, boolean isFeatured) {
		super();
		this.product = product;
		this.imageGallery = imageGallery;
		this.productVariationOptionValue = productVariationOptionValue;
		this.isFeatured = isFeatured;
	}

	public ProductVariationOptionImage() {
		// TODO Auto-generated constructor stub
	}
	
	@JoinColumn(name = "product_variation_option_value_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private ProductVariationOptionValue productVariationOptionValue;
	
	@Column(name = "is_featured")
	private boolean isFeatured=false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ImageGallery getImageGallery() {
		return imageGallery;
	}

	public void setImageGallery(ImageGallery imageGallery) {
		this.imageGallery = imageGallery;
	}

	public ProductVariationOptionValue getProductVariationOptionValue() {
		return productVariationOptionValue;
	}

	public void setProductVariationOptionValue(ProductVariationOptionValue productVariationOptionValue) {
		this.productVariationOptionValue = productVariationOptionValue;
	}

	public boolean isVariationOptionName() {
		return isFeatured;
	}

	public void setVariationOptionName(boolean variationOptionName) {
		this.isFeatured = variationOptionName;
	}
}
