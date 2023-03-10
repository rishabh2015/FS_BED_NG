package com.kb.catalogInventory.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
public class CategoryAttributeBo {
        private Integer id;
        private Date updatedOn;
        private Date createdOn;
        private String attributeName;
        private Integer categoryId;
}

