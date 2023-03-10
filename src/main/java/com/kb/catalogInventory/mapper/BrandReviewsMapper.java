package com.kb.catalogInventory.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kb.catalogInventory.constant.AppConstant;
import com.kb.catalogInventory.datatable.BrandReviews;
import com.kb.catalogInventory.model.BrandReviewsBO;
import com.kb.catalogInventory.repository.BrandReviewsRepository;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class BrandReviewsMapper {
	@Autowired 
	private BrandReviewsRepository brandReviewsRepository;
	
	public BrandReviews convertBoToEntity(BrandReviewsBO brandReviewsBO) {
		BrandReviews brandReviews=new BrandReviews();
		if(brandReviewsBO.getId()!=0) {
			Optional<BrandReviews> brandReviewsOpt=brandReviewsRepository.findById(brandReviewsBO.getId());
			if(!brandReviewsOpt.isPresent()) {
				throw new RuntimeException("Invalid Brand Review Id");
			}
			brandReviews=brandReviewsOpt.get();
		}
		convertBoToEntity(brandReviewsBO,brandReviews);
		return brandReviews;
	}

	private BrandReviews convertBoToEntity(BrandReviewsBO brandReviewsBO, BrandReviews brandReviews) {
		brandReviews.setId(brandReviewsBO.getId());
		brandReviews.setName(brandReviewsBO.getName());
		brandReviews.setComment(brandReviewsBO.getComment());
		brandReviews.setLocation(brandReviewsBO.getLocation());
		brandReviews.setBrands(brandReviewsBO.getBrands());
		brandReviews.setReviewDate(brandReviewsBO.getId()!=0?brandReviewsBO.getReviewDate():new Date());
		brandReviews.setReviewRating(brandReviewsBO.getReviewRating());
		brandReviews.setTitle(brandReviewsBO.getTitle());
		brandReviews.setIsActive( brandReviewsBO.getId()!=0?brandReviews.getIsActive():true);
		String reviewString="";
		if (ObjectUtils.isNotEmpty(brandReviewsBO.getReviewImages())) {
			 reviewString = brandReviewsBO.getReviewImages().stream().map(Object::toString).collect(Collectors.joining(","));
		}
		brandReviews.setReviewImages(reviewString);
		return brandReviews;
	}
	public BrandReviewsBO convertEntityToBo(BrandReviews brandReviews) {
		return convertEntityToBo(new BrandReviewsBO(), brandReviews);
	}

	public BrandReviewsBO convertEntityToBo(BrandReviewsBO brandReviewsBO, BrandReviews brandReviews) {
		brandReviewsBO.setId(brandReviews.getId());
		brandReviewsBO.setName(brandReviews.getName());
		brandReviewsBO.setComment(brandReviews.getComment());
		brandReviewsBO.setLocation(brandReviews.getLocation());
		brandReviewsBO.setBrands(brandReviews.getBrands());
		brandReviewsBO.setReviewDate(brandReviews.getReviewDate());
		brandReviewsBO.setReviewRating(brandReviews.getReviewRating());
		brandReviewsBO.setTitle(brandReviews.getTitle());
		List<String> reviews = new ArrayList<String>();
		if (StringUtils.isNotBlank(brandReviews.getReviewImages())) {
			reviews = Arrays.asList(brandReviews.getReviewImages().split(","));
			reviews.replaceAll(r -> r = AppConstant.IMAGE_URL + r);
		}
		brandReviewsBO.setReviewImages(ObjectUtils.isNotEmpty(reviews) ? (reviews) : null);
		return brandReviewsBO;
	}
}
