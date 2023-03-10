package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupsBo {
    private Long id;
    private String title;
    private String phoneString;
    private String description;
    private Date creationDate;
    private Long agentId;
    private Boolean isActive;
    private Integer userCount;


}
