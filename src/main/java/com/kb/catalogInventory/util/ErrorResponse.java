package com.kb.catalogInventory.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private long timestamp;
    private String status;
    private String message;
    private String error;
}
