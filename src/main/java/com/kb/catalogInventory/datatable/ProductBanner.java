package com.kb.catalogInventory.datatable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="product_banner")
@Data
public class ProductBanner {

	@Id
	@GeneratedValue(strategy=  GenerationType.AUTO)
	private Long id;

	@Column(name="image_url")
	private String imageURL;

	@Column(name="is_active")
	private boolean isActive;

	@Column(name="search_keyWord")
	private String searchKeyword;

	@Column(name="heading")
	private String heading;

	@Column(name="short_title")
	private String shortTitle;

	@Column(name="description")
	private String description;

	@Column(name="sort_order")
	private Long sortOrder;

	@Column(name="banner_stage")
	private String bannerStage;


	@Column(name="is_category_banner")
	private Boolean isCategoryBanner;
	
	@Column(name="banner_url")
	private String bannerUrl;
	@Column(name="created_on")
	private Date createdOn;
	@Column(name="updated_on")
	private Date updatedOn;
	
	
	@PrePersist
	public void prePersist() {
		createdOn=new Date();
		updatedOn=new Date();
	}
	@PreUpdate
	public void preUpdate() {
		updatedOn=new Date();
	}
}
