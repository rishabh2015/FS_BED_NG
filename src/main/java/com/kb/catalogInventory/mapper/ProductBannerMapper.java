package com.kb.catalogInventory.mapper;

import com.kb.catalogInventory.datatable.ProductBanner;
import com.kb.catalogInventory.model.ProductBannerBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ProductBannerMapper {


 public ProductBanner map(ProductBannerBO bo){
      ProductBanner dto=new ProductBanner();
      dto.setId(bo.getId());
      dto.setActive(bo.isActive());
      dto.setDescription(bo.getDescription());
      dto.setBannerStage(bo.getBannerStage());
      dto.setBannerUrl(bo.getBannerUrl());
      dto.setHeading(bo.getHeading());
      dto.setImageURL(bo.getImageURL());
      dto.setSearchKeyword(bo.getSearchKeyword());
      dto.setSortOrder(bo.getSortOrder());
      dto.setShortTitle(bo.getShortTitle());
      dto.setIsCategoryBanner(bo.getIsCategoryBanner());
      return  dto;
  }

   public ProductBannerBO map(ProductBanner dto){

        String bannerCallBackUrl = "";
        if(dto.getBannerUrl().startsWith("/categories") || dto.getBannerUrl().startsWith("categories")) {
             bannerCallBackUrl = "/home/products/"+dto.getBannerUrl();
        }else {
             bannerCallBackUrl = dto.getBannerUrl();
        }


        ProductBannerBO bo=new ProductBannerBO();
        bo.setCreatedOn(dto.getCreatedOn());
        bo.setUpdatedOn(dto.getUpdatedOn());
        bo.setId(dto.getId());
        bo.setActive(dto.isActive());
        bo.setDescription(dto.getDescription());
        bo.setBannerStage(dto.getBannerStage());
        bo.setBannerUrl(dto.getBannerUrl());
        bo.setHeading(dto.getHeading());
        bo.setImageURL(dto.getImageURL());
        bo.setSearchKeyword(dto.getSearchKeyword());
        bo.setSortOrder(dto.getSortOrder());
        bo.setShortTitle(dto.getShortTitle());
        bo.setIsCategoryBanner(dto.getIsCategoryBanner());
        bo.setBannerCallBackUrl(bannerCallBackUrl);

        return  bo;
    }

}
