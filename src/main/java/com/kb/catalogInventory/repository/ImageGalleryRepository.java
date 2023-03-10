package com.kb.catalogInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.catalogInventory.datatable.ImageGallery;
@Repository
public interface ImageGalleryRepository extends JpaRepository<ImageGallery, Long>{

}
