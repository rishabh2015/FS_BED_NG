package com.kb.catalogInventory.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponse {
    private long timestamp;
    private String status;
    private String message;
    private Object data;
}
