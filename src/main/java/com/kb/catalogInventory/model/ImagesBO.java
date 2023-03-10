package com.kb.catalogInventory.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ImagesBO implements Serializable{
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String path;
	private String type;

	public ImagesBO(String path, String type) {
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
