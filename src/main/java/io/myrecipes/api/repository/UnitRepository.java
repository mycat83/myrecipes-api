package io.myrecipes.api.repository;

import io.myrecipes.api.domain.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {
}
