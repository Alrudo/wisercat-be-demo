package com.wisercat.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "filter")
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
            fetch = FetchType.LAZY)
    private List<CriterionEntity> criteria = new ArrayList<>();
    @Column(nullable = false)
    private int selection;
}
