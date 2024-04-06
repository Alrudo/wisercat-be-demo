package com.wisercat.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Builder
@Table(name = "criterion")
@NoArgsConstructor
@AllArgsConstructor
public class CriterionEntity {
    @Id
    @SequenceGenerator(
            name = "criterion_id_sequence",
            sequenceName = "criterion_id_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "criterion_id_sequence")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_id")
    private FilterEntity filter;
    private String type;
    private String param;
    private String value;
}
