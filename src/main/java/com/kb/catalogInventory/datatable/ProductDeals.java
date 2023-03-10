package com.kb.catalogInventory.datatable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_deals")
public class ProductDeals {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="price_variation_percent")
	private Float priceVariationPercent;
	
	@Column(name="deals_id")
	private Long dealsId;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPriceVariationPercent() {
		return priceVariationPercent;
	}

	public void setPriceVariationPercent(Float priceVariationPercent) {
		this.priceVariationPercent = priceVariationPercent;
	}

	public Long getDealsId() {
		return dealsId;
	}

	public void setDealsId(Long dealsId) {
		this.dealsId = dealsId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	

}
