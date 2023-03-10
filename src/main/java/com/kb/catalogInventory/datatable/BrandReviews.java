package com.kb.catalogInventory.datatable;

import java.util.Date;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="brand_reviews")
@Getter
@Setter
@NoArgsConstructor
public class BrandReviews {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name="location" )
	private String location;
	
	@Column(name = "review_date")
	private Date reviewDate;

	@Column(name="review_rating" )
	private Float reviewRating;
	
	
	@Column(name="review_Images" ,length = 2000 )
	private String reviewImages;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "is_active", columnDefinition="tinyint(1) default 1")
	private Boolean isActive;
	
	
	@JoinColumn(name = "brand_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Brands brands;
	

	

}
