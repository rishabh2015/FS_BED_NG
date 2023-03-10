package com.kb.catalogInventory.datatable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="image_gallery")
public class ImageGallery {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "small_image")
	private String smallImage;
	
		
	public ImageGallery(String smallImage, String mediumImage, String largeImage, String image_1, String image_2,
			String image_3, String image_4, String image_5, String image_6) {
		super();
		this.smallImage = smallImage;
		this.mediumImage = mediumImage;
		this.largeImage = largeImage;
		this.image_1 = image_1;
		this.image_2 = image_2;
		this.image_3 = image_3;
		this.image_4 = image_4;
		this.image_5 = image_5;
		this.image_6 = image_6;
	}


	public String getImage_1() {
		return image_1;
	}


	public void setImage_1(String image_1) {
		this.image_1 = image_1;
	}


	public String getImage_2() {
		return image_2;
	}


	public void setImage_2(String image_2) {
		this.image_2 = image_2;
	}


	public String getImage_3() {
		return image_3;
	}


	public void setImage_3(String image_3) {
		this.image_3 = image_3;
	}


	public String getImage_4() {
		return image_4;
	}


	public void setImage_4(String image_4) {
		this.image_4 = image_4;
	}


	public String getImage_5() {
		return image_5;
	}


	public void setImage_5(String image_5) {
		this.image_5 = image_5;
	}


	public String getImage_6() {
		return image_6;
	}


	public void setImage_6(String image_6) {
		this.image_6 = image_6;
	}


	public ImageGallery() {
		// TODO Auto-generated constructor stub
	}
	@Column(name = "medium_image")
	private String mediumImage;
	
	@Column(name = "large_image")
	private String largeImage;
	
	@Column(name = "image_1")
	private String image_1;
	
	@Column(name = "image_2")
	private String image_2;
	
	@Column(name = "image_3")
	private String image_3;
	
	@Column(name = "image_4")
	private String image_4;
	
	@Column(name = "image_5")
	private String image_5;
	
	@Column(name = "image_6")
	private String image_6;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getMediumImage() {
		return mediumImage;
	}

	public void setMediumImage(String mediumImage) {
		this.mediumImage = mediumImage;
	}

	public String getLargeImage() {
		return largeImage;
	}

	public void setLargeImage(String largeImage) {
		this.largeImage = largeImage;
	}
}
