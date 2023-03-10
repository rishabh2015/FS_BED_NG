package com.kb.catalogInventory.datatable;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="groups")
@Data
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "phone_String")
    private String phoneString;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "agent_id")
    private Long agentId;
    @Column(name = "is_active",columnDefinition ="default 1")
    private Boolean isActive;

}
