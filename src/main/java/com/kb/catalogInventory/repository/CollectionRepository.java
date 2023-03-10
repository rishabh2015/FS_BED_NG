package com.kb.catalogInventory.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kb.catalogInventory.datatable.Collection;
import com.kb.catalogInventory.datatable.ProductCountryRule;

@Repository
@Transactional
public interface CollectionRepository extends JpaRepository<Collection,Long> {
	
	List<Collection> findByStatusIn(List<Integer> status,Pageable pageable);
	
	List<Collection> findByStatusAndIsBannerAndIsFeaturedOrderByAddedOnDesc(Integer status,Boolean isBanner,Boolean isFeatured);
	
	Integer countByStatusIn(List<Integer> status);
	
	List<Collection> findByStatusAndPageTypeIdAndIsFeatured(Integer status,Integer pageTypeId,Pageable pageable,Boolean isFeatured);
	
	List<Collection> findByStatusAndPageTypeIdAndIsFeatured(Integer status,Integer pageTypeId,Boolean isFeatured);
	
	Integer countByStatusAndPageTypeId(Integer status,Integer pageTypeId);
	
	Collection findByTitle(String title);
	
	@Modifying
	@Query("update Collection c set c.shortDescription=:shortDescription,c.longDescription=:longDescription,c.productCountryRule=:productCountryRule,"
			+ "c.whiteListedCountries=:whiteListedCountries,c.blackListedCountries=:blackListedCountries,c.pageTypeId=:pageTypeId,c.heroBanner=:heroBanner,"
			+ "c.roundThumbnail=:roundThumbnail,c.squareThumbnail=:squareThumbnail,c.productSkus=:productSkus,c.updatedOn=:updatedOn,"
			+ "c.type=:type,c.status=:status,c.title=:title,c.isFeatured=:isFeatured,c.isBanner=:isBanner,c.attributes=:attributes,c.condition=:condition where c.id=:id")
	int updateCollection(@Param("shortDescription") String shortDescription, @Param("longDescription") String longDescription,
		@Param("productCountryRule") ProductCountryRule productCountryRule, @Param("whiteListedCountries") String whiteListedCountries, @Param("blackListedCountries") String blackListedCountries,
			@Param("pageTypeId") Integer pageTypeId, @Param("heroBanner") String heroBanner, @Param("roundThumbnail") String roundThumbnail, @Param("squareThumbnail") String squareThumbnail, @Param("productSkus") String productSkus,
			@Param("updatedOn") Date updatedOn, @Param("type") String type, @Param("status") Integer status,@Param("title") String title,
			@Param("isFeatured") Boolean isFeatured,@Param("isBanner") Boolean isBanner,@Param("attributes") String attributes,@Param("condition") String conditions,@Param("id") Long id);

	Collection findByIsFeatured(Boolean isFeatured);
	
	@Modifying
	@Query("update Collection c set c.isFeatured=false where c.id=:id")
	int updateFeaturedFalse(@Param("id") Long id);

	@Modifying
	@Query(value = "delete from collection where id = :id", nativeQuery = true)
	int deleteCollection(@Param("id") Long id);
	
	List<Collection> findByTypeAndStatus(String type,Integer status);
}
