package io.myrecipes.api.repository;

import io.myrecipes.api.domain.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Integer> {
}
