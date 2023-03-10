package com.kb.catalogInventory.datatable;

import java.util.HashSet;
import java.util.Set;

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
@Table(name="brand_models")
@Getter
@Setter
@NoArgsConstructor
public class BrandModels {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "name")
	private String name;

	@JoinColumn(name = "brand_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Brands brands;
	
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "brandModels")
	private Set<BrandModelCategory> brandModelCategories=new HashSet<>();

	public BrandModels(String name, Brands brands) {
		super();
		this.name = name;
		this.brands = brands;
	}
	
}
