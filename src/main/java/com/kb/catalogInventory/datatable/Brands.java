package com.kb.catalogInventory.datatable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="brands")
@Getter
@Setter
@NoArgsConstructor
public class Brands {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "thumbnails")
	private String thumbnails;

	
	//New Attribute
	@Column(name = "logo")
	private String brandLogo;
	
	@Column(name = "cover_pic")
	private String brandCoverPic;
	
	@Column(name = "based_in")
	private String basedIn;
	
	@Column(name = "ships_from")
	private String shipsFrom;
	
	@Column(name = "one_liner")
	private String oneLiner;
	
	@Column(name = "tags")
	private String tags;
	
	@Column(name = "sort_story")
	private String shortStory;
	
	@Column(name = "long_desc")
	private String longDescription;
	
	@Column(name = "best_selling_sku")
	private String bestSellingSku;
	
	@Column(name = "Added_on")
	private Date addedOn;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name = "average_rating")
	private Float avgRating;
	
	@Column(name = "admin_Id")
	private String adminId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "is_featured")
	private Boolean isFeatured;
	
	@Column(name = "sort_order")
	private Long sortOrder;
	
	@JoinColumn(name = "supplier_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Supplier supplier;

	@Column(name = "same_manufturer_as")
	private String SameManufacturerAs;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "brands")
	private List<BrandReviews> brandReviews=new ArrayList<>();
}
