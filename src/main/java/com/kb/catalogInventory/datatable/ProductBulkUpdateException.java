package com.kb.catalogInventory.datatable;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="product_bulk_update_exception")
@Getter
@Setter
@NoArgsConstructor
public class ProductBulkUpdateException {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String UUUID;
	
	private String exception;
	
	private Date insertedOn;
	
	private Boolean isUpdated;
	
	private String exceptionStage;
	
	private String remark;
	
	private Boolean fromScheduler;
	
	private String schedulerName;
	
	private Boolean fromApi;
	
	private String apiName;
	
	
}
