package com.kb.catalogInventory.datatable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name="product_country_rule")
@Getter
@Setter
@NoArgsConstructor
public class ProductCountryRule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String zone;
	
	@Column(name="white_listed_countries")
	private String whiteListedCountries;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="admin_id")
	private Long adminId;
	
	@Column(name="last_updated")
	private Date lastUpdated;
	
	@Column(name="display_to_all")
	private Boolean displayToAll;
	
	@Column(name="is_international")
	private Boolean isInternational;
	
	@Column(name="is_deafault")
	private Boolean isDefault;

}
