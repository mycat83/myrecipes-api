package io.myrecipes.api.repository;

import io.myrecipes.api.domain.RecipeStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStepEntity, Integer> {
}
