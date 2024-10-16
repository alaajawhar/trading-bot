package com.amdose.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Getter
@Setter
@Entity
@Table(name = "SYMBOL")
public class SymbolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ADDED_BY")
    private Long addedBy;

    @CreationTimestamp
    @Column(name = "ADDED_DATE")
    private Date addedDate;

    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;

    @UpdateTimestamp
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;
}
