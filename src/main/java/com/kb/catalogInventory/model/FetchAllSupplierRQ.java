package com.kb.catalogInventory.model;

import java.util.List;

public class FetchAllSupplierRQ {
    List<Long> supplierProfileIdList;

    public List<Long> getSupplierProfileIdList() {
        return supplierProfileIdList;
    }

    public void setSupplierProfileIdList(List<Long> supplierProfileIdList) {
        this.supplierProfileIdList = supplierProfileIdList;
    }
}
