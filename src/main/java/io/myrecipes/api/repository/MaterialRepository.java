package io.myrecipes.api.repository;

import io.myrecipes.api.domain.Material;
import io.myrecipes.api.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
}
