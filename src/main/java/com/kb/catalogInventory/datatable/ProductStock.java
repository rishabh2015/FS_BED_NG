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

@Entity
@Table(name="product_stock")
public class ProductStock {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@JoinColumn(name = "product_combination_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private ProductCombinations productCombinations;
	
	@Column(name = "total_stock")
	private Integer totalStock;
	
	@Column(name = "isActive")
	private Boolean isActive;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "inserted_on")
	private Date insertedOn;
	
	@Column(name = "stock_change_comment")
	private String stockChangeComment;
	
	@Column(name = "old_stock")
	private Integer oldStock;
	
	public ProductStock() {
		// TODO Auto-generated constructor stub
	}
	
	public ProductStock(ProductCombinations productCombinations, Integer totalStock, Float unitPrice,
			Float totalPrice) {
		super();
		this.productCombinations = productCombinations;
		this.totalStock = totalStock;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
		this.isActive=true;
		this.stockChangeComment="New Stock Added";
		this.insertedOn= new Date();
		this.oldStock=totalStock;
		this.updatedOn=new Date();
	}

	@Column(name = "unit_price")
	private Float unitPrice;
	
	@Column(name = "total_price")
	private Float totalPrice;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ProductCombinations getProductCombinations() {
		return productCombinations;
	}

	public void setProductCombinations(ProductCombinations productCombinations) {
		this.productCombinations = productCombinations;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public Float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getStockChangeComment() {
		return stockChangeComment;
	}

	public void setStockChangeComment(String stockChangeComment) {
		this.stockChangeComment = stockChangeComment;
	}

	public Date getInsertedOn() {
		return insertedOn;
	}

	public void setInsertedOn(Date insertedOn) {
		this.insertedOn = insertedOn;
	}

	public Integer getOldStock() {
		return oldStock;
	}

	public void setOldStock(Integer oldStock) {
		this.oldStock = oldStock;
	}
}
