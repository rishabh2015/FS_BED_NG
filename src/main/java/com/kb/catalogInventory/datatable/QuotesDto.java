package com.kb.catalogInventory.datatable;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="quotes")
public class QuotesDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="quote_body")
    private String quoteBody;
    @Column(name="quote_by")
    private String quoteBy;
}
