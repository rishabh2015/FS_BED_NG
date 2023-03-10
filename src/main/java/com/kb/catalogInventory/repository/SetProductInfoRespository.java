package com.kb.catalogInventory.repository;

import com.kb.catalogInventory.datatable.SetProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetProductInfoRespository extends JpaRepository<SetProductInfo,Long> {
    List<SetProductInfo> findByProductcombinationidentifier(String productcombinationidentifier);
    SetProductInfo findByColorAndSizeAndProductcombinationidentifier(String color,String size,String Productcombinationidentifier);
}
