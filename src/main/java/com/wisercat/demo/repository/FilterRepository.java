package com.wisercat.demo.repository;

import com.wisercat.demo.entity.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<FilterEntity, Long> {
}
