package com.kb.catalogInventory.model;

import lombok.Data;

@Data
public class csvData {
    private String phone_string;

    @Override
    public String toString() {
        return "csvData{" +
                "phone_string='" + phone_string + '\'' +
                '}';
    }
}
