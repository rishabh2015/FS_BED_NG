package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendUpdateStatusInWhatsappRQ {
    private String customerMobileNumber;
    private String templateName;
}
