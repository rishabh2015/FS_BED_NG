package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.QuotesDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotesRepository extends JpaRepository<QuotesDto, Integer> {

    @Query(value = "select * from quotes order by rand() limit 1", nativeQuery = true)
    QuotesDto getRandomQuote();

}
