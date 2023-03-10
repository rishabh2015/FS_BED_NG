package com.kb.catalogInventory.model;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kb.catalogInventory.datatable.Brands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandReviewsBO {
	
	private long id;
	private String name;
	private String comment;
	private String title;
	private String location;
	@JsonFormat(pattern ="MMMM d, y")
	private Date reviewDate;
	private Float reviewRating;
	private List<String> reviewImages;
	@JsonIgnore
	private Brands brands;
	private Boolean isActive=Boolean.TRUE;

}
