package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.TagDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagDto, Integer> {
}
