package com.kb.catalogInventory.datatable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_tags")
public class TagDto {
    @Id
    private Integer id;
    @Column(name = "tag_name")
    private String tagName;
}
