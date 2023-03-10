package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestApiResponse {
    private Long timestamp;
    private String status;
    private String message;
    private Object data;
}
