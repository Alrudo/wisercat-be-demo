package com.wisercat.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="filter")
@NoArgsConstructor
@AllArgsConstructor
public class FilterEntity {
    @Id
    @SequenceGenerator(
            name = "filter_id_sequence",
            sequenceName = "filter_id_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "filter_id_sequence")
    private Long id;
    @Column(
            unique = true,
            nullable = false)
    private String name;
    @OneToMany(
            mappedBy = "filter",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CriteriaEntity> criterias = new ArrayList<>();
    @Column(nullable = false)
    private int selection;
}
