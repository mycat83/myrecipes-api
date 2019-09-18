package io.myrecipes.api.repository;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.domain.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {
}
