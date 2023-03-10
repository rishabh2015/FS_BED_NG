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
@Table(name="variation_options")
public class VariationOptions {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "variation_option_name")
	private String variationOptionName;
	
	public VariationOptions(String variationOptionName, Variation variation) {
		super();
		this.variationOptionName = variationOptionName;
		this.variation = variation;
	}
	
	public VariationOptions() {
		// TODO Auto-generated constructor stub
	}

	@JoinColumn(name = "variation_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Variation variation;

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

	public Variation getVariation() {
		return variation;
	}

	public void setVariation(Variation variation) {
		this.variation = variation;
	}
}
