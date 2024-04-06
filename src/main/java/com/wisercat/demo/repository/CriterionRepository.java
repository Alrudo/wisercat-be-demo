package com.wisercat.demo.repository;

import com.wisercat.demo.entity.CriterionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriterionRepository extends JpaRepository<CriterionEntity, Long> {
}
