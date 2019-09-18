package io.myrecipes.api.repository;

import io.myrecipes.api.domain.Material;
import io.myrecipes.api.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
}
