package io.myrecipes.api.repository;

import io.myrecipes.api.domain.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {
    Optional<UnitEntity> findByName(String name);
}
