package com.wisercat.demo.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "filter_criteria")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AdditionCriteriaEntity.class, name = "addition"),
        @JsonSubTypes.Type(value = TitleCriteriaEntity.class, name = "title"),
        @JsonSubTypes.Type(value = DateCriteriaEntity.class, name = "date")
})
public abstract class CriteriaEntity {
    @Id
    @SequenceGenerator(
            name = "criteria_id_sequence",
            sequenceName = "criteria_id_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "criteria_id_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "filter_name")
    private FilterEntity filter;
    private String param;
}
