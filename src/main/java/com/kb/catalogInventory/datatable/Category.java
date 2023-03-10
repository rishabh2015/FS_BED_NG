package com.kb.catalogInventory.datatable;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="category")
@ToString
public class Category implements Comparable<Category>,Serializable{

	public Category(String categoryName, String categoryIcon, Category parent) {
		super();
		this.categoryName = categoryName;
		this.categoryIcon = categoryIcon;
		this.parent = parent;
	}

	public Category() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "category_icon")
	private String categoryIcon;
	
	@Column(name="is_navigation" )
	private boolean isNavigation;
	
	@Column(name="category_stage" )
	private Integer categoryStage;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Category parent;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "parent")
	private Set<Category> childrenCategories;
	
	//@JsonIgnore  Commenting out this so that UI will not need to make another rest call for getting id by category name which causing multiple response for the same type. 
 	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryIcon() {
		return categoryIcon;
	}

	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}

	
	   @JsonIgnore
	public Set<Category> getChildrenCategories() {
		return childrenCategories;
	}

	public void setChildrenCategories(Set<Category> childrenCategories) {
		this.childrenCategories = childrenCategories;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public boolean isNavigation() {
		return isNavigation;
	}

	public void setNavigation(boolean isNavigation) {
		this.isNavigation = isNavigation;
	}

	public String getCategoryStage() {
		return "stage"+categoryStage+"Category";
	}

	public void setCategoryStage(Integer categoryStage) {
		this.categoryStage = categoryStage;
	}
	@JsonIgnore
	public Integer getCategoryStageIntValue() {
		return categoryStage;
	}

	@Override
	public int compareTo(Category o) {
		// TODO Auto-generated method stub
		return this.id.compareTo(o.getId());
	}

	

	
	
}
